package org.cis120.electiongame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JOptionPane;

/*
 * CardData v11.2.
 *
 * Candidate cards are now loaded from files/candidates.TSV instead of being
 * hardcoded as hundreds of static President fields. Policies and elections are
 * intentionally still defined here for now so the rest of the game can keep
 * using the same CardData backend without a wider refactor.
 */
public class CardData {
    private static boolean memes = false;
    private static boolean expanded = false;
    private static boolean standard = false;

    // For custom deck
    private static double minRateRem = 0.0;
    private static String[] tagsRem = null;
    private static boolean remembered = false;

    private static final String CANDIDATE_FILE = "files/candidates.TSV";
    private static final String CANDIDATE_FILE_JAR_PREFIX = "resources/files/candidates.TSV";
    private static List<President> cachedCandidates = null;
    private static String candidateLoadError = null;
    private static String candidateDataSource = null;

    private static final String[] GENERATIONAL_BASE_NAMES = new String[] {
            "Andrew Jackson",
            "Theodore Roosevelt",
            "Franklin D. Roosevelt",
            "Abraham Lincoln",
            "Calvin Coolidge",
            "Richard Nixon",
            "Ronald Reagan",
            "Al Gore",
            "Joe Biden",
            "Ulysses S. Grant",
            "Dwight Eisenhower",
            "Lyndon B. Johnson",
            "John F. Kennedy",
            "William McKinley",
            "James Madison",
            "John Adams",
            "Thomas Jefferson",
            "George Washington"
    };

    public static void clearRemembered() {
        remembered = false;
    }

    /************************************************************************************************************************
     *********************** CANDIDATE TSV LOADER **************************************************************************
     ************************************************************************************************************************/

    private static List<President> getCandidateCache() {
        if (cachedCandidates == null) {
            cachedCandidates = loadCandidatesFromTSV();
        }
        return cachedCandidates;
    }

    /**
     * Clears and reloads candidate data from candidates.TSV. This is mostly useful
     * for debugging/data cleanup while the game is open.
     */
    public static void reloadCandidateData() {
        cachedCandidates = null;
        candidateLoadError = null;
        candidateDataSource = null;
        getCandidateCache();
    }

    private static List<President> loadCandidatesFromTSV() {
        ArrayList<President> candidates = new ArrayList<President>();
        InputStream stream = openCandidateDataStream();

        if (stream == null) {
            candidateLoadError = "Could not find " + CANDIDATE_FILE
                    + " or " + CANDIDATE_FILE_JAR_PREFIX
                    + ". Put candidates.TSV inside files/ for dev mode or resources/files/ for JAR mode.";
            throw new IllegalStateException(candidateLoadError);
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String headerLine = reader.readLine();
            if (headerLine == null) {
                candidateLoadError = CANDIDATE_FILE + " is empty.";
                throw new IllegalStateException(candidateLoadError);
            }

            String[] headers = splitTSV(headerLine);
            int nameIdx = findColumn(headers, "Name");
            int partyIdx = findColumn(headers, "Party");
            int stateIdx = findColumn(headers, "State");
            int regionIdx = findColumn(headers, "Region");
            int eraIdx = findColumn(headers, "Era");
            int ideologyIdx = findColumn(headers, "Ideology");
            int marqueeIdx = findColumn(headers, "Marquee");
            int expIdx = findColumn(headers, "EXP");
            int inflIdx = findColumn(headers, "INFL");
            int polIdx = findColumn(headers, "POL");
            int namIdx = findColumn(headers, "NAM");
            int tagsIdx = findColumn(headers, "Tags");

            validateRequiredColumn(nameIdx, "Name");
            validateRequiredColumn(regionIdx, "Region");
            validateRequiredColumn(ideologyIdx, "Ideology");
            validateRequiredColumn(marqueeIdx, "Marquee");
            validateRequiredColumn(expIdx, "EXP");
            validateRequiredColumn(inflIdx, "INFL");
            validateRequiredColumn(polIdx, "POL");
            validateRequiredColumn(namIdx, "NAM");
            validateRequiredColumn(tagsIdx, "Tags");

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] cols = splitTSV(line);
                try {
                    String name = getColumn(cols, nameIdx);
                    if (name.length() == 0) {
                        continue;
                    }

                    String party = getColumn(cols, partyIdx);
                    String state = getColumn(cols, stateIdx);
                    String region = getColumn(cols, regionIdx);
                    String era = getColumn(cols, eraIdx);
                    String ideology = getColumn(cols, ideologyIdx);
                    String marquee = getColumn(cols, marqueeIdx);
                    int exp = parseIntColumn(getColumn(cols, expIdx), "EXP", lineNumber, name);
                    int infl = parseIntColumn(getColumn(cols, inflIdx), "INFL", lineNumber, name);
                    int pol = parseIntColumn(getColumn(cols, polIdx), "POL", lineNumber, name);
                    int nam = parseIntColumn(getColumn(cols, namIdx), "NAM", lineNumber, name);
                    String[] tags = parseTags(getColumn(cols, tagsIdx), era);

                    candidates.add(new President(
                            name, party, state, region, era, exp, infl, pol, nam,
                            ideology, marquee, tags
                    ));
                } catch (RuntimeException ex) {
                    candidateLoadError = "Error reading " + CANDIDATE_FILE + " line "
                            + lineNumber + ": " + ex.getMessage();
                    throw ex;
                }
            }
        } catch (IOException ex) {
            candidateLoadError = "Error reading " + CANDIDATE_FILE + ": " + ex.getMessage();
            throw new IllegalStateException(candidateLoadError, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                    // Ignore close failures.
                }
            }
        }

        if (candidates.isEmpty()) {
            candidateLoadError = "No candidates were loaded from " + CANDIDATE_FILE + ".";
            throw new IllegalStateException(candidateLoadError);
        }

        candidateLoadError = null;
        return candidates;
    }

    private static InputStream openCandidateDataStream() {
        /*
         * Candidate data is small and cached after the first load, so this smart
         * lookup only runs once per program session. It lets Eclipse/dev mode use
         * files/candidates.TSV while JAR exports can use resources/files/candidates.TSV
         * without manual edits inside this class.
         */
        String[] candidatePaths = new String[] {
                CANDIDATE_FILE,
                CANDIDATE_FILE_JAR_PREFIX
        };

        ClassLoader loader = CardData.class.getClassLoader();
        for (String path : candidatePaths) {
            InputStream stream = loader.getResourceAsStream(path);
            if (stream == null) {
                stream = CardData.class.getResourceAsStream("/" + path);
            }
            if (stream != null) {
                candidateDataSource = "classpath:" + path;
                return stream;
            }
        }

        // Eclipse/dev-mode filesystem fallback.
        for (String path : candidatePaths) {
            try {
                File file = new File(path);
                if (file.exists() && file.isFile()) {
                    candidateDataSource = "filesystem:" + file.getPath();
                    return new FileInputStream(file);
                }
            } catch (IOException ignored) {
                // Try the next path.
            }
        }

        candidateDataSource = null;
        return null;
    }

    private static String[] splitTSV(String line) {
        return line.split("\t", -1);
    }

    private static int findColumn(String[] headers, String target) {
        if (headers == null || target == null) {
            return -1;
        }
        for (int i = 0; i < headers.length; i++) {
            if (target.equalsIgnoreCase(clean(headers[i]))) {
                return i;
            }
        }
        return -1;
    }

    private static void validateRequiredColumn(int idx, String name) {
        if (idx < 0) {
            throw new IllegalStateException("Missing required column: " + name);
        }
    }

    private static String getColumn(String[] cols, int idx) {
        if (idx < 0 || cols == null || idx >= cols.length) {
            return "";
        }
        return clean(cols[idx]);
    }

    private static int parseIntColumn(String value, String column, int lineNumber, String name) {
        try {
            if (value == null || value.trim().isEmpty()) {
                throw new NumberFormatException("blank");
            }
            return (int) Math.round(Double.parseDouble(value.trim()));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Invalid " + column + " value for " + name + " on line "
                            + lineNumber + ": '" + value + "'"
            );
        }
    }

    private static String[] parseTags(String rawTags, String era) {
        ArrayList<String> tags = new ArrayList<String>();
        addTagIfMissing(tags, era);

        if (rawTags != null && !rawTags.trim().isEmpty()) {
            String[] pieces = rawTags.split("[,|]");
            for (String piece : pieces) {
                addTagIfMissing(tags, piece);
            }
        }

        return tags.toArray(new String[tags.size()]);
    }

    private static void addTagIfMissing(List<String> tags, String tag) {
        String cleaned = clean(tag);
        if (cleaned.length() == 0) {
            return;
        }
        for (String existing : tags) {
            if (normalize(existing).equals(normalize(cleaned))) {
                return;
            }
        }
        tags.add(cleaned);
    }

    private static String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("﻿", "").trim();
    }

    private static String normalize(String value) {
        return clean(value).toLowerCase(Locale.US);
    }

    private static String normalizeName(String value) {
        return normalize(value).replaceAll("[^a-z0-9]", "");
    }

    private static boolean hasName(President president, String name) {
        return president != null && normalize(president.toString()).equals(normalize(name));
    }

    private static boolean isMemeCard(President president) {
        return president != null && president.hasTag("Meme Card");
    }

    private static boolean isGenerationalCard(President president) {
        return president != null && president.hasTag("Generational");
    }

    private static boolean isBasePresident(President president) {
        return president != null && president.hasTag("President")
                && !isMemeCard(president) && !isGenerationalCard(president);
    }

    private static boolean isBaseNonPresident(President president) {
        return president != null && !president.hasTag("President")
                && !isMemeCard(president) && !isGenerationalCard(president);
    }

    private static boolean isReplacedByGenerationalVersion(President president) {
        if (president == null) {
            return false;
        }
        for (String name : GENERATIONAL_BASE_NAMES) {
            if (hasName(president, name)) {
                return true;
            }
        }
        return false;
    }

    private static List<President> getBasePresidents() {
        ArrayList<President> result = new ArrayList<President>();
        for (President president : getCandidateCache()) {
            if (isBasePresident(president)) {
                result.add(president);
            }
        }
        return result;
    }

    private static List<President> getBaseNonPresidents() {
        ArrayList<President> result = new ArrayList<President>();
        for (President president : getCandidateCache()) {
            if (isBaseNonPresident(president)) {
                result.add(president);
            }
        }
        return result;
    }

    private static List<President> getMemeCards() {
        ArrayList<President> result = new ArrayList<President>();
        for (President president : getCandidateCache()) {
            if (isMemeCard(president)) {
                result.add(president);
            }
        }
        return result;
    }

    private static List<President> getGenerationalCards() {
        ArrayList<President> result = new ArrayList<President>();
        for (President president : getCandidateCache()) {
            if (isGenerationalCard(president)) {
                result.add(president);
            }
        }
        return result;
    }

    private static President findCandidateByName(String name) {
        for (President president : getCandidateCache()) {
            if (hasName(president, name)) {
                return president;
            }
        }
        return null;
    }

    // ELECTIONS
    static Election e2024 = new Election(
            2024, "infl", "exp", "Midwest", "Closed Borders vs. Open Borders",
            "Tax Cuts vs. Social Programs", "Traditional Morality vs. Social Liberalism"
    );
    static Election e2020 = new Election(
            2020, "nam", "exp", "Midwest", "Regulation vs. Deregulation",
            "Tax Cuts vs. Social Programs", "Law and Order vs. Civil Liberties"
    );
    static Election e2016 = new Election(
            2016, "nam", "infl", "Midwest", "Closed Borders vs. Open Borders",
            "Free Trade vs. Tariffs", "Establishment vs. Anti-Establishment"
    );
    static Election e2012 = new Election(
            2012, "infl", "pol", "South", "Tax Cuts vs. Social Programs",
            "Imperialism vs. Isolationism", "Traditional Morality vs. Social Liberalism"
    );
    static Election e2008 = new Election(
            2008, "infl", "pol", "South", "Regulation vs. Deregulation",
            "Imperialism vs. Isolationism", "Establishment vs. Anti-Establishment"
    );
    static Election e2004 = new Election(
            2004, "pol", "exp", "Midwest", "Imperialism vs. Isolationism",
            "Law and Order vs. Civil Liberties", "Traditional Morality vs. Social Liberalism"
    );
    static Election e2000 = new Election(
            2000, "infl", "nam", "Midwest", "Traditional Morality vs. Social Liberalism",
            "Tax Cuts vs. Social Programs", "Imperialism vs. Isolationism"
    );
    static Election e1996 = new Election(
            1996, "infl", "nam", "South", "Law and Order vs. Civil Liberties",
            "Tax Cuts vs. Social Programs", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1992 = new Election(
            1992, "exp", "pol", "South", "Tax Cuts vs. Social Programs",
            "Establishment vs. Anti-Establishment", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1988 = new Election(
            1988, "exp", "infl", "West", "Law and Order vs. Civil Liberties",
            "Traditional Morality vs. Social Liberalism",
            "Tax Cuts vs. Social Programs"
    );
    static Election e1984 = new Election(
            1984, "infl", "pol", "Midwest", "Tax Cuts vs. Social Programs",
            "Imperialism vs. Isolationism",
            "Traditional Morality vs. Social Liberalism"
    );
    static Election e1980 = new Election(
            1980, "nam", "infl", "South", "Tax Cuts vs. Social Programs",
            "Imperialism vs. Isolationism", "Nationalism vs. Class Unity"
    );
    static Election e1976 = new Election(
            1976, "infl", "pol", "West", "Establishment vs. Anti-Establishment", "Regulation vs. Deregulation",
            "Central Bank vs. Anti-Central Bank"
    );
    static Election e1972 = new Election(
            1972, "infl", "nam", "Midwest", "Imperialism vs. Isolationism",
            "Law and Order vs. Civil Liberties", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1968 = new Election(
            1968, "nam", "exp", "Midwest", "Law and Order vs. Civil Liberties",
            "Establishment vs. Anti-Establishment", "Imperialism vs. Isolationism"
    );
    static Election e1964 = new Election(
            1964, "exp", "pol", "West", "Civil Rights vs. Racism", "Tax Cuts vs. Social Programs",
            "Closed Borders vs. Open Borders"
    );
    static Election e1960 = new Election(
            1960, "infl", "pol", "Midwest", "Imperialism vs. Isolationism",
            "Civil Rights vs. Racism", "Tax Cuts vs. Social Programs"
    );
    static Election e1956 = new Election(
            1956, "nam", "infl", "South", "Civil Rights vs. Racism",
            "Imperialism vs. Isolationism", "Laissez-Faire vs. Internal Improvements"
    );
    static Election e1952 = new Election(
            1952, "infl", "nam", "South", "Imperialism vs. Isolationism",
            "Traditional Morality vs. Social Liberalism", "Regulation vs. Deregulation"
    );
    static Election e1948 = new Election(
            1948, "infl", "nam", "Northeast", "Regulation vs. Deregulation",
            "Civil Rights vs. Racism", "Tax Cuts vs. Social Programs"
    );
    static Election e1944 = new Election(
            1944, "infl", "nam", "Midwest", "Imperialism vs. Isolationism",
            "Nationalism vs. Class Unity", "Active Executive vs. Limited Government"
    );
    static Election e1940 = new Election(
            1940, "pol", "nam", "Midwest", "Regulation vs. Deregulation",
            "Imperialism vs. Isolationism", "Active Executive vs. Limited Government"
    );
    static Election e1936 = new Election(
            1936, "infl", "exp", "Northeast", "Nationalism vs. Class Unity",
            "Active Executive vs. Limited Government", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1932 = new Election(
            1932, "infl", "pol", "Northeast", "Tax Cuts vs. Social Programs",
            "Regulation vs. Deregulation", "Free Trade vs. Tariffs"
    );
    static Election e1928 = new Election(
            1928, "infl", "nam", "South", "Traditional Morality vs. Social Liberalism",
            "Active Executive vs. Limited Government", "Regulation vs. Deregulation"
    );
    static Election e1924 = new Election(
            1924, "pol", "infl", "West", "Tax Cuts vs. Social Programs",
            "Closed Borders vs. Open Borders", "Law and Order vs. Civil Liberties"
    );
    static Election e1920 = new Election(
            1920, "infl", "pol", "South", "Active Executive vs. Limited Government",
            "Imperialism vs. Isolationism", "Tax Cuts vs. Social Programs"
    );
    static Election e1916 = new Election(
            1916, "pol", "infl", "Northeast", "Imperialism vs. Isolationism",
            "Regulation vs. Deregulation", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1912 = new Election(
            1912, "pol", "nam", "West", "Regulation vs. Deregulation",
            "Active Executive vs. Limited Government", "Establishment vs. Anti-Establishment"
    );
    static Election e1908 = new Election(
            1908, "pol", "exp", "West", "Regulation vs. Deregulation", "Free Trade vs. Tariffs",
            "Central Bank vs. Anti-Central Bank"
    );
    static Election e1904 = new Election(
            1904, "infl", "nam", "South", "Regulation vs. Deregulation", "Free Trade vs. Tariffs",
            "Gold Standard vs. Inflation"
    );
    static Election e1900 = new Election(
            1900, "pol", "nam", "West", "Imperialism vs. Isolationism",
            "Free Trade vs. Tariffs", "Gold Standard vs. Inflation"
    );
    static Election e1896 = new Election(
            1896, "infl", "pol", "West", "Gold Standard vs. Inflation",
            "Regulation vs. Deregulation", "Free Trade vs. Tariffs"
    );
    static Election e1892 = new Election(
            1892, "pol", "exp", "South", "Free Trade vs. Tariffs", "Civil Rights vs. Racism",
            "Gold Standard vs. Inflation"
    );
    
    static Election e1888 = new Election(
            1888, "pol", "infl", "Northeast", "Free Trade vs. Tariffs",
            "Gold Standard vs. Inflation", "Establishment vs. Anti-Establishment"
    );
     
    static Election e1884 = new Election(
            1884, "infl", "exp", "Northeast",
            "Establishment vs. Anti-Establishment",
            "Laissez-Faire vs. Internal Improvements", "Free Trade vs. Tariffs"
    );
    static Election e1880 = new Election(
            1880, "exp", "infl", "Northeast", "Establishment vs. Anti-Establishment",
            "Closed Borders vs. Open Borders", "Free Trade vs. Tariffs"
    );
    static Election e1876 = new Election(
            1876, "infl", "pol", "South", "States' Rights vs. Centralization",
            "Civil Rights vs. Racism", "Establishment vs. Anti-Establishment"
    );
    static Election e1872 = new Election(
            1872, "infl", "nam", "South", "Establishment vs. Anti-Establishment",
            "States' Rights vs. Centralization", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1868 = new Election(
            1868, "infl", "nam", "Midwest", "Civil Rights vs. Racism",
            "Gold Standard vs. Inflation", "States' Rights vs. Centralization"
    );
    static Election e1864 = new Election(
            1864, "pol", "infl", "Northeast", "Imperialism vs. Isolationism",
            "Civil Rights vs. Racism", "States' Rights vs. Centralization"
    );
    static Election e1860 = new Election(
            1860, "pol", "infl", "South", "Civil Rights vs. Racism",
            "States' Rights vs. Centralization", "States' Rights vs. Centralization"
    );
    static Election e1856 = new Election(
            1856, "pol", "exp", "Midwest", "States' Rights vs. Centralization",
            "Civil Rights vs. Racism", "Closed Borders vs. Open Borders"
    );
    static Election e1852 = new Election(
            1852, "infl", "exp", "Northeast", "Civil Rights vs. Racism",
            "States' Rights vs. Centralization", "Nationalism vs. Class Unity"
    );
    static Election e1848 = new Election(
            1848, "infl", "nam", "Midwest", "Nationalism vs. Class Unity", "Imperialism vs. Isolationism",
            "Gold Standard vs. Inflation"
    );
    static Election e1844 = new Election(
            1844, "exp", "pol", "Northeast", "Imperialism vs. Isolationism",
            "Civil Rights vs. Racism", "States' Rights vs. Centralization"
    );
    static Election e1840 = new Election(
            1840, "nam", "infl", "South", "Central Bank vs. Anti-Central Bank",
            "Active Executive vs. Limited Government", "Establishment vs. Anti-Establishment"
    );
    static Election e1836 = new Election(
            1836, "exp", "pol", "South", "Laissez-Faire vs. Internal Improvements",
            "States' Rights vs. Centralization", "Gold Standard vs. Inflation"
    );
    static Election e1832 = new Election(
            1832, "infl", "nam", "Northeast", "Central Bank vs. Anti-Central Bank",
            "Constructionism vs. Implied Powers", "Social Hierarchy vs. Egalitarianism"
    );
    static Election e1828 = new Election(
            1828, "nam", "infl", "Northeast", "Social Hierarchy vs. Egalitarianism",
            "Free Trade vs. Tariffs", "Establishment vs. Anti-Establishment"
    );
    static Election e1824 = new Election(
            1824, "infl", "nam", "Midwest", "Laissez-Faire vs. Internal Improvements",
            "Social Hierarchy vs. Egalitarianism", "Central Bank vs. Anti-Central Bank"
    );
    static Election e1820 = new Election(
            1820, "nam", "exp", "Northeast", "States' Rights vs. Centralization",
            "Constructionism vs. Implied Powers", "Civil Rights vs. Racism"
    );
    static Election e1816 = new Election(
            1816, "nam", "pol", "Northeast", "Nationalism vs. Class Unity",
            "Central Bank vs. Anti-Central Bank", "Free Trade vs. Tariffs"
    );
    static Election e1812 = new Election(
            1812, "pol", "nam", "Northeast", "Imperialism vs. Isolationism",
            "States' Rights vs. Centralization", "Laissez-Faire vs. Internal Improvements"
    );
    static Election e1808 = new Election(
            1808, "pol", "infl", "Northeast", "Free Trade vs. Tariffs",
            "Constructionism vs. Implied Powers", "States' Rights vs. Centralization"
    );
    static Election e1804 = new Election(
            1804, "pol", "nam", "Northeast", "Constructionism vs. Implied Powers",
            "Free Trade vs. Tariffs", "Laissez-Faire vs. Internal Improvements"
    );
    static Election e1800 = new Election(
            1800, "pol", "nam", "Northeast", "States' Rights vs. Centralization",
            "Constructionism vs. Implied Powers", "Social Hierarchy vs. Egalitarianism"
    );
    static Election e1796 = new Election(
            1796, "exp", "infl", "Northeast", "Social Hierarchy vs. Egalitarianism",
            "Imperialism vs. Isolationism", "Law and Order vs. Civil Liberties"
    );
    static Election e1792 = new Election(
            1792, "nam", "infl", "South", "Constructionism vs. Implied Powers",
            "Central Bank vs. Anti-Central Bank", "Laissez-Faire vs. Internal Improvements"
    );
    static Election e1788 = new Election(
            1788, "nam", "infl", "Northeast", "Constructionism vs. Implied Powers",
            "Social Hierarchy vs. Egalitarianism", "States' Rights vs. Centralization"
    );
    static Election albania2017 = new Election(
            2017, "nam", "infl", "South", "Establishment vs. Anti-Establishment",
            "Imperialism vs. Isolationism", "Law and Order vs. Civil Liberties"
    );

    static Policy toughfp1 = new Policy("Imperialism", "Progressive", "Populist", "Conservative");
    static Policy toughfp2 = new Policy("Imperialism", "Progressive", "Populist", "Conservative");
    static Policy toughfp3 = new Policy("Imperialism", "Progressive", "Populist", "Conservative");
    static Policy isolationism1 = new Policy(
            "Isolationism", "Progressive", "Libertarian", "Populist"
    );
    static Policy isolationism2 = new Policy(
            "Isolationism", "Progressive", "Libertarian", "Populist"
    );
    static Policy isolationism3 = new Policy(
            "Isolationism", "Progressive", "Libertarian", "Populist"
    );
    static Policy taxcuts1 = new Policy("Tax Cuts", "Conservative", "Libertarian");
    static Policy taxcuts2 = new Policy("Tax Cuts", "Conservative", "Libertarian");
    static Policy socialprogram1 = new Policy("Social Programs", "Progressive", "Populist");
    static Policy socialprogram2 = new Policy("Social Programs", "Progressive", "Populist");
    static Policy tradmorality1 = new Policy(
            "Traditional Morality", "Conservative", "Populist", "Libertarian"
    );
    static Policy tradmorality2 = new Policy(
            "Traditional Morality", "Conservative", "Populist", "Libertarian"
    );
    static Policy sociallib1 = new Policy("Social Liberalism", "Progressive", "Libertarian");
    static Policy sociallib2 = new Policy("Social Liberalism", "Progressive", "Libertarian");
    static Policy freetrade1 = new Policy("Free Trade", "Progressive", "Libertarian", "Populist");
    static Policy freetrade2 = new Policy("Free Trade", "Progressive", "Libertarian", "Populist");
    static Policy tariffs1 = new Policy("Tariffs", "Populist", "Conservative");
    static Policy tariffs2 = new Policy("Tariffs", "Populist", "Conservative");
    static Policy statesrights1 = new Policy(
            "States' Rights", "Conservative", "Libertarian", "Populist"
    );
    static Policy statesrights2 = new Policy(
            "States' Rights", "Conservative", "Libertarian", "Populist"
    );
    static Policy centralization1 = new Policy("Centralization", "Progressive", "Conservative");
    static Policy centralization2 = new Policy("Centralization", "Progressive", "Conservative");
    static Policy goldstandard = new Policy("Gold Standard", "Conservative", "Libertarian");
    static Policy inflation = new Policy("Inflation", "Progressive", "Populist");
    static Policy civilrights1 = new Policy(
            "Civil Rights", "Progressive", "Conservative", "Libertarian"
    );
    static Policy civilrights2 = new Policy(
            "Civil Rights", "Progressive", "Conservative", "Libertarian"
    );
    static Policy racism1 = new Policy("Racism", "Libertarian", "Populist");
    static Policy racism2 = new Policy("Racism", "Libertarian", "Populist");
    static Policy regulation1 = new Policy("Regulation", "Progressive", "Populist");
    static Policy regulation2 = new Policy("Regulation", "Progressive", "Populist");
    static Policy deregulation1 = new Policy("Deregulation", "Conservative", "Libertarian");
    static Policy deregulation2 = new Policy("Deregulation", "Conservative", "Libertarian");
    static Policy socialhierarchy = new Policy("Social Hierarchy", "Conservative", "Libertarian");
    static Policy egalitarianism = new Policy(
            "Egalitarianism", "Progressive", "Populist", "Libertarian"
    );
    static Policy strategicdev = new Policy("Internal Improvements", "Conservative", "Progressive");
    static Policy laissez1 = new Policy("Laissez-Faire", "Libertarian", "Populist", "Conservative");
    static Policy openb = new Policy("Open Borders", "Progressive", "Libertarian");
    static Policy closedb = new Policy("Closed Borders", "Conservative", "Populist");
    static Policy activeexec = new Policy("Active Executive", "Progressive", "Conservative", "Populist");
    static Policy limitedgov = new Policy(
            "Limited Government", "Conservative", "Libertarian"
    );
    static Policy impliedp = new Policy("Implied Powers", "Progressive", "Conservative");
    static Policy construc = new Policy(
            "Constructionism", "Conservative", "Libertarian", "Populist"
    );
    static Policy laworder = new Policy("Law and Order", "Conservative", "Populist");
    static Policy pfreedom = new Policy("Civil Liberties", "Progressive", "Libertarian");
    static Policy bank = new Policy("Central Bank", "Progressive", "Conservative");
    static Policy antibank = new Policy("Anti-Central Bank", "Libertarian", "Populist");
    static Policy nationalism1 = new Policy("Nationalism", "Conservative", "Libertarian");
    static Policy classunity = new Policy("Class Unity", "Progressive", "Populist");
    static Policy establishment = new Policy("Establishment", "Conservative", "Progressive");
    static Policy establishment2 = new Policy("Establishment", "Conservative", "Progressive");
    static Policy antiestablishment = new Policy(
            "Anti-Establishment", "Progressive", "Libertarian", "Populist"
    );
    static Policy antiestablishment2 = new Policy(
            "Anti-Establishment", "Progressive", "Libertarian", "Populist"
    );
    static Policy communism = new Policy(
            "Communism", "Progressive"
    );
    static Policy anarchy = new Policy(
            "Anarcho-Capitalism", "Libertarian"
    );
    static Policy theocracy = new Policy(
            "Theocracy", "Conservative"
    );
    static Policy riots = new Policy(
            "Riots", "Progressive"
    );

    static String fp = "Imperialism vs. Isolationism";
    static String tax = "Tax Cuts vs. Social Programs";
    static String morality = "Traditional Morality vs. Social Liberalism";
    static String trade = "Free Trade vs. Tariffs";
    static String sr = "States' Rights vs. Centralization";
    static String goldinfl = "Gold Standard vs. Inflation";
    static String civilracism = "Civil Rights vs. Racism";
    static String reg = "Regulation vs. Deregulation";
    static String society = "Social Hierarchy vs. Egalitarianism";
    static String cronyism = "Laissez-Faire vs. Internal Improvements";
    static String borders = "Closed Borders vs. Open Borders";
    static String exec = "Active Executive vs. Limited Government";
    static String consti = "Constructionism vs. Implied Powers";
    static String law = "Law and Order vs. Civil Liberties";
    static String banking = "Central Bank vs. Anti-Central Bank";
    static String nation = "Nationalism vs. Class Unity";
    static String estab = "Establishment vs. Anti-Establishment";

    public CardData() {

    }

    public static void flipMemes() {
        memes = !memes;
    }
    
    public static void flipExpanded() {
        expanded = !expanded;
    }
    public static void flipStandard() {
        standard = !standard;
    }

    public static ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add(fp);
        categories.add(tax);
        categories.add(morality);
        categories.add(trade);
        categories.add(sr);
        categories.add(goldinfl);
        categories.add(civilracism);
        categories.add(reg);
        categories.add(society);
        categories.add(cronyism);
        categories.add(borders);
        categories.add(exec);
        categories.add(consti);
        categories.add(law);
        categories.add(banking);
        categories.add(nation);
        categories.add(estab);
        return categories;
    }

    public static List<Policy> getPolicies() {
        List<Policy> policies = new ArrayList<Policy>();
        policies.add(toughfp1);
        policies.add(toughfp2);
        policies.add(toughfp3);
        policies.add(isolationism1);
        policies.add(isolationism2);
        policies.add(isolationism3);
        policies.add(taxcuts1);
        policies.add(taxcuts2);
        policies.add(socialprogram1);
        policies.add(socialprogram2);
        policies.add(tradmorality1);
        policies.add(tradmorality2);
        policies.add(sociallib1);
        policies.add(sociallib2);
        policies.add(tariffs1);
        policies.add(tariffs2);
        policies.add(freetrade1);
        policies.add(freetrade2);
        policies.add(statesrights1);
        policies.add(statesrights2);
        policies.add(centralization1);
        policies.add(centralization2);
        policies.add(goldstandard);
        policies.add(inflation);
        policies.add(civilrights1);
        policies.add(civilrights2);
        policies.add(racism1);
        policies.add(racism2);
        policies.add(regulation1);
        policies.add(regulation2);
        policies.add(deregulation1);
        policies.add(deregulation2);
        policies.add(socialhierarchy);
        policies.add(egalitarianism);
        policies.add(strategicdev);
        policies.add(laissez1);
        policies.add(openb);
        policies.add(closedb);
        policies.add(activeexec);
        policies.add(limitedgov);
        policies.add(impliedp);
        policies.add(construc);
        policies.add(laworder);
        policies.add(pfreedom);
        policies.add(bank);
        policies.add(antibank);
        policies.add(nationalism1);
        policies.add(classunity);
        policies.add(establishment);
        policies.add(establishment2);
        policies.add(antiestablishment);
        policies.add(antiestablishment2);
        if (memes) {
            policies.add(communism);
            policies.add(anarchy);
            policies.add(riots);
            policies.add(theocracy);
        }
        Collections.shuffle(policies);
        return policies;
    }
    

    /************************************************************************************************************************
     *********************** ADD PRESIDENTS FUNCTION ************************************************************************
     ************************************************************************************************************************/

    /**
     * Applies the requested minimum weighted-average rating to any finished
     * President pool. Campaign Mode relies on calls like
     * getPresidents("full", null, 3.0), so every preset should respect minRate.
     */
    private static List<President> filterPresidentsByMinRating(List<President> source, double minRate) {
        ArrayList<President> filtered = new ArrayList<President>();
        if (source == null) {
            return filtered;
        }

        for (President president : source) {
            if (president != null && president.getWeightedAve() >= minRate) {
                filtered.add(president);
            }
        }
        return filtered;
    }

    private static List<President> shuffleAndReturnFiltered(List<President> source, double minRate) {
        List<President> filtered = filterPresidentsByMinRating(source, minRate);
        Collections.shuffle(filtered);
        return filtered;
    }

    private static List<President> getCardsAtOrAbove(List<President> source, double minRate) {
        ArrayList<President> result = new ArrayList<President>();
        if (source == null) {
            return result;
        }
        for (President president : source) {
            if (president != null && president.getWeightedAve() >= minRate) {
                result.add(president);
            }
        }
        return result;
    }

    private static void addRandomCards(List<President> destination, List<President> source, int count) {
        if (destination == null || source == null || count <= 0) {
            return;
        }
        ArrayList<President> shuffled = new ArrayList<President>(source);
        Collections.shuffle(shuffled);
        int limit = Math.min(count, shuffled.size());
        for (int i = 0; i < limit; i++) {
            destination.add(shuffled.get(i));
        }
    }

    /**
     * Use this for getting/adding candidate cards to the game.
     *
     * Legacy public method preserved so ElectionGame, CampaignMode, packs,
     * tournaments, and old simulator files can continue using CardData as the
     * backend without being rewritten yet.
     */
    public static List<President> getPresidents(String preset, String[] tags, double minRate) {
        String selectedPreset = preset == null ? "standard" : preset.trim().toLowerCase(Locale.US);

        List<President> presidents = getBasePresidents();
        List<President> nonpresidents = getBaseNonPresidents();
        List<President> meme = getMemeCards();
        List<President> generational = getGenerationalCards();

        if (selectedPreset.equals("generational")) {
            ArrayList<President> pool = new ArrayList<President>();
            for (President president : presidents) {
                if (!isReplacedByGenerationalVersion(president)) {
                    pool.add(president);
                }
            }
            pool.addAll(generational);
            pool.addAll(nonpresidents);
            return shuffleAndReturnFiltered(pool, minRate);
        }

        if (selectedPreset.equals("expanded")) {
            ArrayList<President> pool = new ArrayList<President>(presidents);
            addRandomCards(pool, getCardsAtOrAbove(nonpresidents, 4.0), 54);
            return shuffleAndReturnFiltered(pool, minRate);
        } else if (selectedPreset.equals("full")) {
            ArrayList<President> pool = new ArrayList<President>(presidents);
            pool.addAll(nonpresidents);
            return shuffleAndReturnFiltered(pool, minRate);
        } else if (selectedPreset.equals("memes")) {
            ArrayList<President> pool = new ArrayList<President>(presidents);
            pool.addAll(nonpresidents);
            pool.addAll(meme);
            return shuffleAndReturnFiltered(pool, minRate);
        } else if (selectedPreset.equals("standard")) {
            ArrayList<President> pool = new ArrayList<President>(presidents);
            addRandomCards(pool, getCardsAtOrAbove(nonpresidents, 4.0), 6);
            return shuffleAndReturnFiltered(pool, minRate);
        }

        if (selectedPreset.equals("custom") && (!remembered)) {
            List<President> custom = buildCustomPresidentPool(tags, minRate, presidents, nonpresidents, meme);
            minRateRem = minRate;
            tagsRem = tags == null ? null : tags.clone();
            remembered = true;
            return custom;
        } else if (selectedPreset.equals("custom") && remembered) {
            return buildCustomPresidentPool(tagsRem, minRateRem, presidents, nonpresidents, meme);
        }

        // Legacy default branch: base presidents plus a small random splash of
        // non-presidents, then apply minRate and shuffle.
        ArrayList<President> pool = new ArrayList<President>(presidents);
        addRandomCards(pool, nonpresidents, 7);
        return shuffleAndReturnFiltered(pool, minRate);
    }

    private static List<President> buildCustomPresidentPool(
            String[] tags,
            double minRate,
            List<President> presidents,
            List<President> nonpresidents,
            List<President> meme
    ) {
        List<President> custom = new ArrayList<President>();
        custom.addAll(presidents);
        custom.addAll(nonpresidents);
        custom.addAll(meme);

        ArrayList<President> filtered = new ArrayList<President>();
        boolean hasTags = tags != null && tags.length > 0;

        for (President candidate : custom) {
            boolean valid = !hasTags || candidate.hasAnyTag(tags);
            if (candidate.getWeightedAve() < minRate) {
                valid = false;
            }
            if (valid) {
                filtered.add(candidate);
            }
        }

        // Fill in the minimum amount of cards. The actual amount for this might need to change.
        if (filtered.size() < 20) {
            int needed = 20 - filtered.size();
            for (int i = 0; i < needed && i < presidents.size(); i++) {
                filtered.add(presidents.get(i));
            }
            JOptionPane.showMessageDialog(
                    null,
                    "Error: Not enough cards for current filter. A few more cards have been added.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return filtered;
    }

    /**
     * Old no-argument function preserved for legacy ElectionGame flows.
     */
    public static List<President> getPresidents() {
        List<President> presidents = getBasePresidents();
        if (expanded || standard) {
            presidents.addAll(getBaseNonPresidents());
        }
        if (memes) {
            presidents.addAll(getMemeCards());
        }
        return presidents;
    }

    public static List<President> getOnePresident() {
        List<President> presidents = new ArrayList<President>();
        President barry = findCandidateByName("Barry Goldwater");
        if (barry == null) {
            List<President> fallback = getCandidateCache();
            if (!fallback.isEmpty()) {
                barry = fallback.get(0);
            }
        }
        for (int i = 0; i < 25 && barry != null; i++) {
            presidents.add(barry);
        }
        return presidents;
    }

    /************************************************************************************************************************
     *********************** CANDIDATE QUERY HELPERS ************************************************************************
     ************************************************************************************************************************/

    /**
     * Returns a shuffled copy of every loaded candidate card, including base,
     * meme, and generational cards. The cache itself is never exposed.
     */
    public static List<President> getAllCandidates() {
        ArrayList<President> result = new ArrayList<President>(getCandidateCache());
        Collections.shuffle(result);
        return result;
    }

    public static List<President> getCandidatesByTag(String tag) {
        ArrayList<President> result = new ArrayList<President>();
        for (President candidate : getCandidateCache()) {
            if (candidate.hasTag(tag)) {
                result.add(candidate);
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public static List<President> getCandidatesByEra(String era) {
        return getCandidatesByExactField("era", era);
    }

    public static List<President> getCandidatesByParty(String party) {
        return getCandidatesByExactField("party", party);
    }

    public static List<President> getCandidatesByState(String state) {
        return getCandidatesByExactField("state", state);
    }

    public static List<President> getCandidatesByRegion(String region) {
        return getCandidatesByExactField("region", region);
    }

    public static List<President> getCandidatesByIdeology(String ideology) {
        return getCandidatesByExactField("ideology", ideology);
    }

    private static List<President> getCandidatesByExactField(String field, String target) {
        ArrayList<President> result = new ArrayList<President>();
        String normalizedTarget = normalize(target);
        if (normalizedTarget.length() == 0) {
            return result;
        }

        for (President candidate : getCandidateCache()) {
            String value = "";
            if ("era".equals(field)) {
                value = candidate.getEra();
            } else if ("party".equals(field)) {
                value = candidate.getParty();
            } else if ("state".equals(field)) {
                value = candidate.getState();
            } else if ("region".equals(field)) {
                value = candidate.getRegion();
            } else if ("ideology".equals(field)) {
                value = candidate.getIdeology();
            }

            if (normalize(value).equals(normalizedTarget)) {
                result.add(candidate);
            }
        }
        Collections.shuffle(result);
        return result;
    }

    public static List<String> getAllTags() {
        ArrayList<String> tags = new ArrayList<String>();
        for (President candidate : getCandidateCache()) {
            String[] candidateTags = candidate.getTags();
            for (String tag : candidateTags) {
                addStringIfMissing(tags, tag);
            }
        }
        Collections.sort(tags);
        return tags;
    }

    public static List<String> getAllEras() {
        return getAllValuesForField("era");
    }

    public static List<String> getAllParties() {
        return getAllValuesForField("party");
    }

    public static List<String> getAllStates() {
        return getAllValuesForField("state");
    }

    public static List<String> getAllRegions() {
        return getAllValuesForField("region");
    }

    public static List<String> getAllIdeologies() {
        return getAllValuesForField("ideology");
    }

    private static List<String> getAllValuesForField(String field) {
        ArrayList<String> values = new ArrayList<String>();
        for (President candidate : getCandidateCache()) {
            String value = "";
            if ("era".equals(field)) {
                value = candidate.getEra();
            } else if ("party".equals(field)) {
                value = candidate.getParty();
            } else if ("state".equals(field)) {
                value = candidate.getState();
            } else if ("region".equals(field)) {
                value = candidate.getRegion();
            } else if ("ideology".equals(field)) {
                value = candidate.getIdeology();
            }
            addStringIfMissing(values, value);
        }
        Collections.sort(values);
        return values;
    }

    private static void addStringIfMissing(List<String> values, String value) {
        String cleaned = clean(value);
        if (cleaned.length() == 0) {
            return;
        }
        for (String existing : values) {
            if (normalize(existing).equals(normalize(cleaned))) {
                return;
            }
        }
        values.add(cleaned);
    }

    /************************************************************************************************************************
     *********************** CANDIDATE DATA AUDIT **************************************************************************
     ************************************************************************************************************************/

    public static String getCandidateDataAudit() {
        StringBuilder sb = new StringBuilder();
        List<President> all = getCandidateCache();
        sb.append("Candidate data file: ").append(CANDIDATE_FILE).append("\n");
        sb.append("Candidate data source: ").append(candidateDataSource == null ? "(not loaded)" : candidateDataSource).append("\n");
        sb.append("Loaded candidates: ").append(all.size()).append("\n");
        sb.append("Base presidents: ").append(getBasePresidents().size()).append("\n");
        sb.append("Base non-presidents: ").append(getBaseNonPresidents().size()).append("\n");
        sb.append("Generational cards: ").append(getGenerationalCards().size()).append("\n");
        sb.append("Meme cards: ").append(getMemeCards().size()).append("\n");

        appendCountByField(sb, "Regions", "region", all);
        appendCountByField(sb, "Eras", "era", all);
        appendCountByField(sb, "Ideologies", "ideology", all);
        appendDuplicateNames(sb, all);
        appendMissingCoreFields(sb, all);

        if (candidateLoadError != null) {
            sb.append("\nLast load error: ").append(candidateLoadError).append("\n");
        }
        return sb.toString();
    }

    public static void printCandidateDataAudit() {
        System.out.println(getCandidateDataAudit());
    }

    private static void appendCountByField(
            StringBuilder sb,
            String title,
            String field,
            List<President> candidates
    ) {
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<Integer> counts = new ArrayList<Integer>();

        for (President candidate : candidates) {
            String value;
            if ("region".equals(field)) {
                value = candidate.getRegion();
            } else if ("era".equals(field)) {
                value = candidate.getEra();
            } else if ("ideology".equals(field)) {
                value = candidate.getIdeology();
            } else {
                value = "";
            }

            value = value == null || value.trim().isEmpty() ? "(blank)" : value.trim();
            int idx = values.indexOf(value);
            if (idx < 0) {
                values.add(value);
                counts.add(1);
            } else {
                counts.set(idx, counts.get(idx) + 1);
            }
        }

        sb.append("\n").append(title).append(":\n");
        for (int i = 0; i < values.size(); i++) {
            sb.append("  ").append(values.get(i)).append(": ").append(counts.get(i)).append("\n");
        }
    }

    private static void appendDuplicateNames(StringBuilder sb, List<President> candidates) {
        Set<String> seen = new HashSet<String>();
        Set<String> dupes = new HashSet<String>();
        for (President candidate : candidates) {
            String key = normalizeName(candidate.toString());
            if (seen.contains(key)) {
                dupes.add(candidate.toString());
            }
            seen.add(key);
        }

        sb.append("\nDuplicate normalized names: ").append(dupes.size()).append("\n");
        for (String dupe : dupes) {
            sb.append("  ").append(dupe).append("\n");
        }
    }

    private static void appendMissingCoreFields(StringBuilder sb, List<President> candidates) {
        int missingRegion = 0;
        int missingIdeology = 0;
        int missingMarquee = 0;
        int missingTags = 0;

        for (President candidate : candidates) {
            if (candidate.getRegion() == null || candidate.getRegion().trim().isEmpty()) {
                missingRegion++;
            }
            if (candidate.getIdeology() == null || candidate.getIdeology().trim().isEmpty()) {
                missingIdeology++;
            }
            if (candidate.getMarquee() == null || candidate.getMarquee().trim().isEmpty()) {
                missingMarquee++;
            }
            if (candidate.getTags() == null || candidate.getTags().length == 0) {
                missingTags++;
            }
        }

        sb.append("\nMissing core fields:\n");
        sb.append("  Missing region: ").append(missingRegion).append("\n");
        sb.append("  Missing ideology: ").append(missingIdeology).append("\n");
        sb.append("  Missing marquee: ").append(missingMarquee).append("\n");
        sb.append("  Missing tags: ").append(missingTags).append("\n");
    }

    public static List<Election> getElections() {
        List<Election> elections = new ArrayList<Election>();
        elections.add(e2024);
        elections.add(e2020);
        elections.add(e2016);
        elections.add(e2012);
        elections.add(e2008);
        elections.add(e2004);
        elections.add(e2000);
        elections.add(e1996);
        elections.add(e1992);
        elections.add(e1988);
        elections.add(e1984);
        elections.add(e1980);
        elections.add(e1976);
        elections.add(e1972);
        elections.add(e1968);
        elections.add(e1964);
        elections.add(e1960);
        elections.add(e1956);
        elections.add(e1952);
        elections.add(e1948);
        elections.add(e1944);
        elections.add(e1940);
        elections.add(e1936);
        elections.add(e1932);
        elections.add(e1928);
        elections.add(e1924);
        elections.add(e1920);
        elections.add(e1916);
        elections.add(e1912);
        elections.add(e1908);
        elections.add(e1904);
        elections.add(e1900);
        elections.add(e1896);
        elections.add(e1892);
        elections.add(e1888);
        elections.add(e1884);
        elections.add(e1880);
        elections.add(e1876);
        elections.add(e1872);
        elections.add(e1868);
        elections.add(e1864);
        elections.add(e1860);
        elections.add(e1856);
        elections.add(e1852);
        elections.add(e1848);
        elections.add(e1844);
        elections.add(e1840);
        elections.add(e1836);
        elections.add(e1832);
        elections.add(e1828);
        elections.add(e1824);
        elections.add(e1820);
        elections.add(e1816);
        elections.add(e1812);
        elections.add(e1808);
        elections.add(e1804);
        elections.add(e1800);
        elections.add(e1796);
        elections.add(e1792);
        elections.add(e1788);
        if (memes) {
            elections.add(albania2017);
        }
        Collections.shuffle(elections);
        Collections.shuffle(elections);
        return elections;
    }
}
