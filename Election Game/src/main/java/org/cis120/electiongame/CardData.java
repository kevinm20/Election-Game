package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

/*
 * This brutal class creates all the presidents, policies, and elections.
 */
//meggy
public class CardData {
    private static boolean memes = false;
    private static boolean expanded = false;
    private static boolean standard = false;
    
    // For custom deck
    private static double minRateRem = 0.0;
    private static String[] tagsRem = null;
    private static boolean remembered = false;
    
    public static void clearRemembered() {
    	remembered = false;
    }

    // PRESIDENTS
    static President calvinCoolidge = new President(
            "Calvin Coolidge", "Northeast", 7, 6, 8, 5, "Libertarian", "Tax Cuts", 
            new String[]{"President", "Progressive Era"},
            "<center><b>Calvin Coolidge</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1923-1929<br><br>"
	                + "Vice President: 1921-1923<br><br>"
	                + "Governor: 1919-1921<br><br>"
	                + "Lt. Governor: 1916-1919<br><br>"
	                + "Other: 1907-1915<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1920 (won VP), 1924 (won)"
    );
    static President thomasJefferson = new President(
            "Thomas Jefferson", "South", 7, 7, 7, 10, "Libertarian", "States' Rights", 
            new String[]{"President", "Founding Era"},
            "<center><b>Thomas Jefferson</b></center><br><br>"
		            + "<b>Political Experience:</b><br><br>"
		            + "President: 1801-1809<br><br>"
		            + "Vice President: 1797-1801<br><br>"
		            + "Secretary of State: 1790-1793<br><br>"
		            + "Diplomat: 1785-1789<br><br>"
		            + "Governor: 1779-1781<br><br>"
		            + "<b>Presidential Runs:</b><br><br>"
		            + "1796 (lost), 1800 (won), 1804 (won)"
    );
    static President henryClay = new President(
            "Henry Clay", "South", 8, 2, 7, 7, "Conservative", "Internal Improvements",
            new String[]{"Jacksonian Era"},
            "<center><b>Henry Clay</b></center><br><br>"
		            + "<b>Political Experience:</b><br><br>"
		            + "Senator: 1806-1807, 1810-1811, 1831-1842, 1849-1852<br><br>"
		            + "Sec. of State: 1825-1829<br><br>"
		            + "House: 1811-1821, 1823-1825<br><br>"
		            + "<br><br>"
		            + "<b>Presidential Runs:</b><br><br>"
		            + "1824 (lost), 1832 (lost), 1840 (lost primary), 1844 (lost), 1848 (lost primary)"
    );
    static President alexanderHamilton = new President(
            "Alexander Hamilton", "Northeast", 4, 1, 10, 8, "Conservative", "Centralization",
            new String[]{"Founding Era"},
            "<center><b>Alexander Hamilton</b></center><br><br>"
                    + "<b>Political Experience:</b><br><br>"
                    + "Sec. of Treasury: 1789-1795<br><br>"
                    + "Sr. Officer of Army: 1799-1800<br><br>"
                    + "Other: 1782-1789<br><br>"
                    + "<br><br>"
                    + "<br><br>"
                    + "<b>Presidential Runs:</b><br><br>"
                    + "None"
    );
    static President lyndonBJohnson = new President(
            "Lyndon B. Johnson", "South", 10, 2, 7, 6, "Progressive", "Social Programs",
            new String[]{"President", "Civil Rights Era"},
            "<center><b>Lyndon B. Johnson</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1963-1969<br><br>"
	                + "Vice President: 1961-1963<br><br>"
	                + "Senator: 1949-1961<br><br>"
	                + "Sen. Maj. Lead.: 1955-1956, 1957-1961<br><br>"
	                + "House: 1937-1949<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1960 (won VP), 1964 (won), 1968 (lost primary)"
    );
    static President donaldTrump = new President(
            "Donald Trump", "Northeast", 1, 7, 2, 8, "Populist", "Nationalism",
            new String[]{"President", "Trump Era"},
            "<center><b>Donald Trump</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 2017-2021<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "2016 (won), 2020 (lost), 2024 (won)"
    );
    static President ulyssesSGrant = new President(
            "Ulysses S. Grant", "Midwest", 1, 8, 4, 9, "Conservative", "Civil Rights",
            new String[]{"President", "Civil War Era"},
            "<center><b>Ulysses S. Grant</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1869-1877<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1868 (won), 1872 (won)"
    );
    static President woodrowWilson = new President(
            "Woodrow Wilson", "Northeast", 2, 4, 8, 5, "Progressive", "Centralization",
            new String[]{"President", "Progressive Era"},
            "<center><b>Woodrow Wilson</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1913-1921<br><br>"
	                + "Governor: 1911-1913<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1912 (won), 1916 (won)"
    );
    static President millardFillmore = new President(
            "Millard Fillmore", "Northeast", 4, 2, 6, 4, "Conservative", "Closed Borders",
            new String[]{"President", "Civil War Era"},
            "<center><b>Millard Fillmore</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1850-1853<br><br>"
	                + "Vice President: 1849-1850<br><br>"
	                + "NY Comptroller: 1848-1849<br><br>"
	                + "House: 1833-1835, 1837-1843<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1848 (won VP), 1852 (lost primary), 1856 (lost)"
    );
    static President robertATaft = new President(
            "Robert A. Taft", "Midwest", 7, 3, 6, 3, "Libertarian", "Isolationism",
            new String[]{"New Deal Era"},
            "<center><b>Robert A. Taft</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "Senator: 1939-1953<br><br>"
	                + "OH Leg.: 1921-1933<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1940 (lost), 1948 (lost), 1952 (lost)"
    );
    static President billClinton = new President(
            "Bill Clinton", "South", 6, 7, 6, 5, "Progressive", "Free Trade",
            new String[]{"President", "Reagan Era"},
            "<center><b>Bill Clinton</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1993-2001<br><br>"
	                + "Governor: 1979-1981, 1983-1992<br><br>"
	                + "AR AG: 1977-1979<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1992 (won), 1996 (won)"
            
    );
    static President georgeWashington = new President(
            "George Washington", "South", 3, 10, 6, 10, "Conservative", "Nationalism",
            new String[]{"President", "Founding Era"},
            "<center><b>George Washington</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1789-1797<br><br>"
	                + "Cont. Cong.: 1774-1775<br><br>"
	                + "VA Leg.: 1758-1775<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1788 (won), 1792 (won)"
    );
    static President jamesAGarfield = new President(
            "James A. Garfield", "Midwest", 4, 5, 6, 3, "Libertarian", "Civil Rights",
            new String[]{"President", "Reconstruction Era"},
            "<center><b>James A. Garfield</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1881<br><br>"
	                + "House: 1863-1880<br><br>"
	                + "OH Leg.: 1860-1861<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1880 (won)"
    );
    static President jamesMonroe = new President(
            "James Monroe", "South", 8, 8, 6, 6, "Libertarian", "Nationalism",
            new String[]{"President", "Founding Era"},
            "<center><b>James Monroe</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1817-1825<br><br>"
	                + "Sec. of State: 1811-1817<br><br>"
	                + "Governor: 1779-1802, 1811<br><br>"
	                + "Senator: 1790-1794<br><br>"
	                + "Diplomat: 1794-1796, 1803-1807<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1808 (lost primary), 1812 (won), 1816 (won)"
    );
    static President williamMcKinley = new President(
            "William McKinley", "Midwest", 6, 5, 8, 6, "Conservative", "Tariffs",
            new String[]{"President", "Reconstruction Era"},
            "<center><b>William McKinley</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1897-1901<br><br>"
	                + "Governor: 1892-1896<br><br>"
	                + "House: 1877-1884, 1885-1891<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1896 (won), 1900 (won)"
    );
    static President andrewJackson = new President(
            "Andrew Jackson", "South", 2, 10, 3, 9, "Populist", "Anti-Central Bank",
            new String[]{"President", "Jacksonian Era"},
            "<center><b>Andrew Jackson</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1829-1837<br><br>"
	                + "Senator: 1797-1798, 1823-1825<br><br>"
	                + "Other: 1798-1804, 1821<br><br>"
	                + "House: 1796-1797<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1824 (lost), 1828 (won), 1832 (won)"
    );
    static President williamJenningsBryan = new President(
            "William Jennings Bryan", "West", 2, 9, 3, 5, "Populist", "Inflation",
            new String[]{"Reconstruction Era"},
            "<center><b>William Jennings Bryan</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "Sec. of State: 1913-1915<br><br>"
	                + "House: 1891-1895<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1896 (lost), 1900 (lost), 1908 (lost)"
    );
    static President ronaldReagan = new President(
            "Ronald Reagan", "West", 4, 10, 5, 8, "Conservative", "Tax Cuts",
            new String[]{"President", "Reagan Era"},
            "<center><b>Ronald Reagan</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1981-1989<br><br>"
	                + "Governor: 1967-1975<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1976 (lost primary), 1980 (won), 1984 (won)"
    );
    static President groverCleveland = new President(
            "Grover Cleveland", "Northeast", 3, 4, 7, 4, "Libertarian", "Free Trade",
            new String[]{"President", "Reconstruction Era"},
            "<center><b>Grover Cleveland</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1885-1889, 1893-1897<br><br>"
	                + "Governor: 1883-1885<br><br>"
	                + "Mayor: 1882<br><br>"
	                + "Sheriff: 1871-1873<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1884 (won), 1888 (lost), 1892 (won)"
    );
    static President richardNixon = new President(
            "Richard Nixon", "West", 8, 7, 5, 8, "Conservative", "Law and Order",
            new String[]{"President", "Civil Rights Era"},
            "<center><b>Richard Nixon</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 1969-1974<br><br>"
	                + "Vice President: 1953-1961<br><br>"
	                + "Senator: 1950-1953<br><br>"
	                + "House: 1947-1950<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "1952 (won VP), 1956 (won VP), 1960 (lost), 1968 (won), 1972 (won)"
    );
    static President warrenGHarding = new President(
            "Warren G. Harding", "Midwest", 3, 8, 5, 2, "Libertarian", "Limited Government",
            new String[]{"President", "Progressive Era"}
    );
    static President theodoreRoosevelt = new President(
            "Theodore Roosevelt", "Northeast", 4, 8, 9, 6, "Progressive", "Regulation",
            new String[]{"President", "Progressive Era"}
    );
    static President franklinDRoosevelt = new President(
            "Franklin D. Roosevelt", "Northeast", 5, 9, 9, 6, "Progressive", "Social Programs",
            new String[]{"President", "New Deal Era"}
    );
    static President johnAdams = new President(
            "John Adams", "Northeast", 10, 2, 9, 8, "Conservative", "Social Hierarchy",
            new String[]{"President", "Founding Era"}
    );
    static President johnCCalhoun = new President(
            "John C. Calhoun", "South", 9, 2, 7, 6, "Libertarian", "States' Rights",
            new String[]{"Jacksonian Era"}
    );
    static President jamesMadison = new President(
            "James Madison", "South", 7, 4, 10, 8, "Libertarian", "Constructionism",
            new String[]{"President", "Founding Era"}
    );
    static President johnQuincyAdams = new President(
            "John Quincy Adams", "Northeast", 7, 2, 8, 7, "Conservative", "Civil Rights",
            new String[]{"President", "Jacksonian Era"}
    );
    static President abrahamLincoln = new President(
            "Abraham Lincoln", "Midwest", 3, 9, 10, 7, "Conservative", "Civil Rights",
            new String[]{"President", "Civil War Era"}
    );
    static President williamHenryHarrison = new President(
            "William Henry Harrison", "Midwest", 3, 6, 1, 8, "Conservative", "Central Bank",
            new String[]{"President", "Jacksonian Era"}
    );
    static President martinVanBuren = new President(
            "Martin Van Buren", "Northeast", 7, 4, 4, 5, "Libertarian", "Laissez-Faire",
            new String[]{"President", "Jacksonian Era"}
    );
    static President barackObama = new President(
            "Barack Obama", "Midwest", 3, 8, 6, 2, "Progressive", "Social Liberalism",
            new String[]{"President", "Modern Era"}
    );
    static President jimmyCarter = new President(
            "Jimmy Carter", "South", 3, 6, 4, 1, "Progressive", "Anti-Establishment",
            new String[]{"President", "Civil Rights Era"}
    );
    static President johnTyler = new President(
            "John Tyler", "South", 7, 2, 4, 4, "Libertarian", "Constructionism",
            new String[]{"President", "Jacksonian Era"}
    );
    static President jamesKPolk = new President(
            "James K. Polk", "South", 6, 8, 5, 2, "Populist", "Imperialism",
            new String[]{"President", "Jacksonian Era"}
    );
    static President zacharyTaylor = new President(
            "Zachary Taylor", "Northeast", 1, 5, 1, 7, "Conservative", "Nationalism",
            new String[]{"President", "Jacksonian Era"}
    );
    static President georgeWBush = new President(
            "George W. Bush", "South", 3, 6, 3, 7, "Conservative", "Imperialism",
            new String[]{"President", "Modern Era"}
    );
    static President alGore = new President(
            "Al Gore", "South", 8, 3, 6, 5, "Progressive", "Social Liberalism",
            new String[]{"Modern Era"}
    );
    static President johnFKennedy = new President(
            "John F. Kennedy", "Northeast", 5, 9, 6, 7, "Progressive", "Imperialism",
            new String[]{"President", "Civil Rights Era"}
    );
    static President georgeHWBush = new President(
            "George H. W. Bush", "South", 7, 3, 5, 6, "Conservative", "Imperialism",
            new String[]{"President", "Reagan Era"}
    );
    static President geraldFord = new President(
            "Gerald Ford", "Midwest", 8, 3, 3, 3, "Conservative", "Establishment",
            new String[]{"President", "Civil Rights Era"}
    );
    static President williamHowardTaft = new President(
            "William Howard Taft", "Midwest", 2, 3, 8, 4, "Conservative", "Free Trade",
            new String[]{"President", "Progressive Era"}
    );
    static President herbertHoover = new President(
            "Herbert Hoover", "West", 2, 5, 3, 4, "Progressive", "Tariffs",
            new String[]{"President", "Progressive Era"}
    );
    static President harrySTruman = new President(
            "Harry S. Truman", "South", 7, 4, 3, 4, "Progressive", "Imperialism",
            new String[]{"President", "New Deal Era"}
    );
    static President dwightEisenhower = new President(
            "Dwight Eisenhower", "West", 1, 8, 5, 10, "Conservative", "Internal Improvements"
            ,
            new String[]{"President", "New Deal Era"}
    );
    static President benjaminHarrison = new President(
            "Benjamin Harrison", "Midwest", 3, 5, 4, 3, "Conservative", "Tariffs",
            new String[]{"President", "Reconstruction Era"}
    );
    static President chesterAArthur = new President(
            "Chester A. Arthur", "Northeast", 3, 3, 6, 3, "Conservative", "Closed Borders",
            new String[]{"President", "Reconstruction Era"}
    );
    static President rutherfordBHayes = new President(
            "Rutherford B. Hayes", "Midwest", 4, 6, 4, 2, "Conservative", "Gold Standard",
            new String[]{"President", "Reconstruction Era"}
    );
    static President andrewJohnson = new President(
            "Andrew Johnson", "South", 6, 3, 4, 3, "Populist", "Racism",
            new String[]{"President", "Civil War Era"}
    );
    static President jamesBuchanan = new President(
            "James Buchanan", "Northeast", 6, 4, 3, 3, "Populist", "Laissez-Faire",
            new String[]{"President", "Civil War Era"}
    );
    static President franklinPierce = new President(
            "Franklin Pierce", "Northeast", 4, 5, 4, 2, "Populist", "States' Rights",
            new String[]{"President", "Civil War Era"}
    );
    static President georgeWallace = new President(
            "George Wallace", "South", 5, 5, 1, 4, "Populist", "Racism",
            new String[]{"Civil Rights Era"}
    );
    static President barryGoldwater = new President(
            "Barry Goldwater", "West", 5, 3, 4, 4, "Libertarian", "Tax Cuts",
            new String[]{"Civil Rights Era"}
    );
    static President joeBiden = new President(
            "Joe Biden", "Northeast", 10, 4, 2, 6, "Progressive", "Social Programs",
            new String[]{"President", "Trump Era"}
    );
    // Meme cards
    static President millFill = new President(
            "Millard Fillmore (Ascended)", "Northeast", 999, 999, 999, 999, "Conservative",
            "Traditional Morality",
            new String[]{"Meme Card"}
    );
    static President jebBush = new President(
            "Jeb Bush!", "South", 3, -5, 2, 4, "Conservative", "Establishment",
            new String[]{"Meme Card"}
    );
    static President dababy = new President(
            "DaBaby", "South", 0, 10, 0, 10, "Progressive", "Civil Liberties",
            new String[]{"Meme Card"}
    );
    static President emperorpalpatine = new President(
            "Emperor Palpatine", "Northeast", 7, 6, 3, 10, "Conservative", "Centralization",
            new String[]{"Meme Card"}
    );
    static President odysseus = new President(
            "Odysseus", "South", 2, 5, 8, 9, "Conservative", "Imperialism",
            new String[]{"Meme Card"}
    );
    static President napoleon = new President(
            "Napoleon Bonaparte", "Northeast", 2, 7, 4, 10, "Conservative", "Imperialism",
            new String[]{"Meme Card"}
    );
    static President libRight = new President(
            "LibRight Chad", "Northeast", 4, 6, 6, 8, "Libertarian", "Tax Cuts",
            new String[]{"Meme Card"}
    );
    static President authRight = new President(
            "AuthRight Chad", "South", 8, 6, 4, 6, "Conservative", "Traditional Morality",
            new String[]{"Meme Card"}
    );
    static President libLeft = new President(
            "LibLeft Chad", "West", 4, 8, 6, 6, "Progressive", "Social Liberalism",
            new String[]{"Meme Card"}
    );
    static President authLeft = new President(
            "AuthLeft Chad", "Northeast", 6, 4, 8, 6, "Populist", "Social Programs",
            new String[]{"Meme Card"}
    );
    static President churchill = new President(
            "Winston Churchill", "Northeast", 6, 10, 4, 5, "Conservative", "Imperialism",
            new String[]{"Meme Card"}
    );
    static President monke = new President(
            "Monke", "West", 1, 10, 1, 1, "Conservative", "Traditional Morality",
            new String[]{"Meme Card"}
    );
    static President impostor = new President(
            "impostor", "West", 3, 10, 10, 0, "Populist", "Imperialism",
            new String[]{"Meme Card"}
    );
    static President comrademccain = new President(
            "Comrade McCain", "West", 8, 2, 4, 6, "Progressive", "Communism",
            new String[]{"Meme Card"}
    );
    static President michaelDukakis = new President(
            "Michael Dukakis", "Northeast", 4, 2, 4, 4, "Progressive", "Civil Liberties",
            new String[]{"Meme Card"}
    );
    static President evilGeorgeWallace = new President(
            "Evil George Wallace", "South", 6, 5, 1, 4, "Progressive", "Civil Rights",
            new String[]{"Meme Card"}
    );
    static President postMalone = new President(
            "Post Malone", "South", 0, 10, 0, 10, "Libertarian", "Constructionism",
            new String[]{"Meme Card"}
    );
    static President johnMcCain = new President(
            "John McCain", "West", 7, 2, 3, 5, "Conservative", "Imperialism",
            new String[]{"Modern Era"}
    );
    static President ambroseBurnside = new President(
            "Ambrose Burnside", "Northeast", 3, 5, 1, 3, "Conservative", "Imperialism",
            new String[]{"Civil Rights Era"}
    );
    static President ronDeSantis = new President(
            "Ron DeSantis", "South", 4, 2, 7, 5, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President hillaryClinton = new President(
            "Hillary Clinton", "Northeast", 4, 1, 4, 6, "Progressive", "Social Liberalism",
            new String[]{"Modern Era"}
    );
    static President mittRomney = new President(
            "Mitt Romney", "Northeast", 3, 3, 5, 4, "Conservative", "Tax Cuts",
            new String[]{"Modern Era"}
    );
    static President garyJohnson = new President(
            "Gary Johnson", "West", 3, 1, 2, 1, "Libertarian", "Civil Liberties",
            new String[]{"Modern Era"}
    );
    static President rossPerot = new President(
            "Ross Perot", "South", 1, 3, 6, 4, "Populist", "Tariffs",
            new String[]{"Reagan Era"}
    );
    static President robertLaFollette = new President(
            "Robert La Follette", "Midwest", 5, 3, 5, 5, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President charlesLindbergh = new President(
            "Charles Lindbergh", "Midwest", 1, 5, 2, 8, "Populist", "Isolationism",
            new String[]{"New Deal Era"}
    );
    static President williamHSeward = new President(
            "William H. Seward", "Northeast", 6, 3, 6, 6, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President josephMcCarthy = new President(
            "Joseph McCarthy", "Midwest", 3, 3, 1, 7, "Conservative", "Traditional Morality",
            new String[]{"New Deal Era"}
    );
    static President stromThurmond = new President(
            "Strom Thurmond", "South", 9, 1, 3, 3, "Populist", "Racism",
            new String[]{"Civil Rights Era"}
    );
    static President frederickDouglass = new President(
            "Frederick Douglass", "Northest", 1, 9, 6, 7, "Progressive", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President eugeneMcCarthy = new President(
            "Eugene McCarthy", "Midwest", 7, 3, 4, 2, "Progressive", "Isolationism",
            new String[]{"Civil Rights Era"}
    );
    static President hubertHumphrey = new President(
            "Hubert Humphrey", "Midwest", 9, 3, 7, 6, "Progressive", "Civil Rights",
            new String[]{"Civil Rights Era"}
    );
    static President douglasMacArthur = new President(
            "Douglas MacArthur", "South", 3, 6, 2, 9, "Conservative", "Imperialism",
            new String[]{"New Deal Era"}
    );
    static President kanyeWest = new President(
            "Kanye West", "Midwest", 1, 7, 2, 10, "Conservative", "Traditional Morality",
            new String[]{"Meme Card"}
    );
    static President johnDRockefeller = new President(
            "John D. Rockefeller", "Northeast", 1, 1, 5, 9, "Conservative", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President dickCheney = new President(
            "Dick Cheney", "West", 6, 1, 2, 4, "Conservative", "Imperialism",
            new String[]{"Modern Era"}
    );
    static President alSmith = new President(
            "Al Smith", "Northeast", 4, 2, 5, 3, "Progressive", "Civil Liberties",
            new String[]{"Progressive Era"}
    );
    static President patrickHenry = new President(
            "Patrick Henry", "South", 3, 8, 2, 6, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President patBuchanan = new President(
            "Pat Buchanan", "Northeast", 1, 4, 6, 2, "Conservative", "Traditional Morality",
            new String[]{"Reagan Era"}
    );
    static President thomasPaine = new President(
            "Thomas Paine", "Northeast", 1, 7, 5, 6, "Populist", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President marcoRubio = new President(
            "Marco Rubio", "South", 3, 2, 5, 2, "Conservative", "Tax Cuts",
            new String[]{"Modern Era"}
    );
    static President bernieSanders = new President(
            "Bernie Sanders", "Northeast", 6, 4, 3, 4, "Progressive", "Social Programs",
            new String[]{"Modern Era"}
    );
    static President ronPaul = new President(
            "Ron Paul", "South", 5, 5, 5, 1, "Libertarian", "Anti-Central Bank",
            new String[]{"Modern Era"}
    );
    static President johnMuir = new President(
            "John Muir", "West", 1, 4, 1, 5, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President tedCruz = new President(
            "Ted Cruz", "South", 4, 2, 6, 4, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President georgeMcClellan = new President(
            "George McClellan", "Northeast", 1, 5, 2, 8, "Conservative", "Tax Cuts",
            new String[]{"Civil War Era"}
    );
    static President stephenADouglas = new President(
            "Stephen A. Douglas", "Midwest", 6, 3, 9, 7, "Populist", "States' Rights",
            new String[]{"Civil War Era"}
    );
    static President thaddeusStevens = new President(
            "Thaddeus Stevens", "Northeast", 3, 4, 4, 5, "Progressive", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President johnCFremont = new President(
            "John C. Fremont", "West", 2, 4, 5, 3, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President horaceGreeley = new President(
            "Horace Greeley", "Northeast", 1, 3, 2, 6, "Progressive", "Social Liberalism",
            new String[]{"Reconstruction Era"}
    );
    static President robertELee = new President(
            "Robert E. Lee", "South", 1, 8, 1, 9, "Populist", "States' Rights",
            new String[]{"Civil War Era"}
    );
    static President nelsonRockefeller = new President(
            "Nelson Rockefeller", "Northeast", 4, 3, 4, 3, "Progressive", "Internal Improvements",
            new String[]{"Civil Rights Era"}
    );
    static President thomasDewey = new President(
            "Thomas Dewey", "Northeast", 5, 3, 3, 6, "Conservative", "Law and Order",
            new String[]{"New Deal Era"}
    );
    static President ralphNader = new President(
            "Ralph Nader", "Northeast", 1, 2, 2, 1, "Progressive", "Social Liberalism",
            new String[]{"Modern Era"}
    );
    static President georgeMcGovern = new President(
            "George McGovern", "West", 5, 2, 3, 2, "Progressive", "Social Programs",
            new String[]{"Civil Rights Era"}
    );
    static President bobbyKennedy = new President(
            "Bobby Kennedy", "Northeast", 3, 7, 4, 8, "Progressive", "Isolationism",
            new String[]{"Civil Rights Era"}
    );
    static President georgePatton = new President(
            "George Patton", "West", 1, 9, 1, 7, "Conservative", "Imperialism",
            new String[]{"New Deal Era"}
    );
    static President warrenG = new President(
            "Warren G.", "Midwest", 6, 9, 9, 4, "Conservative", "Limited Government",
            new String[]{"Meme Card"}
    );
    static President shirleyChisholm = new President(
            "Shirley Chisholm", "Northest", 2, 5, 3, 4, "Progressive", "Social Liberalism",
            new String[]{"Civil Rights Era"}
    );
    static President bettyFord = new President(
            "Betty Ford", "Midwest", 1, 6, 1, 6, "Progressive", "Social Liberalism",
            new String[]{"Civil Rights Era"}
    );
    static President sarahPalin = new President(
            "Sarah Palin", "West", 2, 4, 3, 2, "Conservative", "Anti-Establishment",
            new String[]{"Modern Era"}
    );
    static President jackKemp = new President(
            "Jack Kemp", "Northeast", 5, 1, 5, 1, "Libertarian", "Tax Cuts",
            new String[]{"Reagan Era"}
    );
    static President howardDean = new President(
            "Howard Dean", "Northeast", 4, 5, 3, 2, "Progressive", "Isolationism",
            new String[]{"Modern Era"}
    );
    static President rudyGiuliani = new President(
            "Rudy Giuliani", "Northeast", 2, 6, 2, 7, "Conservative", "Law and Order",
            new String[]{"Modern Era"}
    );
    static President martinLutherKingJr = new President(
            "Martin Luther King Jr.", "South", 1, 9, 5, 9, "Progressive", "Civil Rights",
            new String[]{"Civil Rights Era"}
    );
    static President danQuayle = new President(
            "Dan Quayle", "Midwest", 5, 1, 2, 2, "Conservative", "Traditional Morality",
            new String[]{"Reagan Era"}
    );
    static President hueyLong = new President(
            "Huey Long", "South", 4, 7, 6, 3, "Populist", "Social Programs",
            new String[]{"New Deal Era"}
    );
    static President colinPowell = new President(
            "Colin Powell", "South", 5, 4, 3, 4, "Conservative", "Imperialism",
            new String[]{"Modern Era"}
    );
    static President susanBAnthony = new President(
            "Susan B. Anthony", "Northeast", 1, 7, 2, 6, "Progressive", "Social Liberalism",
            new String[]{"Civil War Era"}
    );
    static President walterMondale = new President(
            "Walter Mondale", "Midwest", 6, 2, 2, 2, "Progressive", "Social Liberalism",
            new String[]{"Reagan Era"}
    );
    static President spiroAgnew = new President(
            "Spiro Agnew", "Northeast", 5, 3, 3, 2, "Conservative", "Traditional Morality",
            new String[]{"Civil Rights Era"}
    );
    static President alfLandon = new President(
            "Alf Landon", "West", 3, 1, 3, 1, "Conservative", "Tax Cuts",
            new String[]{"New Deal Era"}
    );
    static President wendellWillkie = new President(
            "Wendell Willkie", "Midwest", 1, 7, 2, 3, "Conservative", "Imperialism",
            new String[]{"New Deal Era"}
    );
    static President johnNanceGarner = new President(
            "John Nance Garner", "South", 7, 2, 5, 2, "Populist", "Limited Government",
            new String[]{"New Deal Era"}
    );
    static President henryFord = new President(
            "Henry Ford", "Midwest", 1, 1, 1, 8, "Progressive", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President danielWebster = new President(
            "Daniel Webster", "Northeast", 8, 6, 3, 6, "Conservative", "Nationalism",
            new String[]{"Jacksonian Era"}
    );
    static President winfieldScott = new President(
            "Winfield Scott", "South", 1, 5, 2, 5, "Conservative", "Nationalism",
            new String[]{"Civil War Era"}
    );
    static President johnCBreckinridge = new President(
            "John C. Breckinridge", "South", 4, 5, 3, 4, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President johnBell = new President(
            "John Bell", "South", 6, 3, 3, 3, "Conservative", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President charlesSumner = new President(
            "Charles Sumner", "Northeast", 5, 3, 3, 7, "Progressive", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President johnBrown = new President(
            "John Brown", "Northeast", 1, 1, 1, 9, "Progressive", "Civil Liberties",
            new String[]{"Civil War Era"}
    );
    static President andrewJackson24 = new President(
            "Andrew Jackson '24", "South", 2, 8, 2, 7, "Populist", "Egalitarianism",
            new String[]{"Generational"}
    );
    static President andrewJackson32 = new President(
            "Andrew Jackson '32", "South", 5, 10, 3, 9, "Populist", "Anti-Central Bank",
            new String[]{"Generational"}
    );
    static President andrewJackson36 = new President(
            "Andrew Jackson '36", "South", 7, 8, 6, 10, "Populist", "Anti-Central Bank",
            new String[]{"Generational"}
    );
    static President theodoreRoosevelt98 = new President(
            "Teddy Roosevelt '98", "Northeast", 2, 7, 3, 5, "Progressive", "Regulation",
            new String[]{"Generational"}
    );
    static President theodoreRoosevelt04 = new President(
            "Teddy Roosevelt '04", "Northeast", 5, 7, 7, 8, "Progressive", "Regulation",
            new String[]{"Generational"}
    );
    static President theodoreRoosevelt12 = new President(
            "Teddy Roosevelt '12", "Northeast", 6, 8, 9, 10, "Progressive", "Regulation",
            new String[]{"Generational"}
    );
    static President franklinDRoosevelt32 = new President(
            "Franklin Roosevelt '32", "Northeast", 2, 8, 5, 4, "Progressive", "Social Programs",
            new String[]{"Generational"}
    );
    static President franklinDRoosevelt40 = new President(
            "Franklin Roosevelt '40", "Northeast", 7, 9, 8, 8, "Progressive", "Social Programs",
            new String[]{"Generational"}
    );
    static President franklinDRoosevelt44 = new President(
            "Franklin Roosevelt '44", "Northeast", 9, 8, 9, 10, "Progressive", "Imperialism",
            new String[]{"Generational"}
    );
    static President abrahamLincoln58 = new President(
            "Abraham Lincoln '58", "Midwest", 2, 6, 8, 5, "Conservative", "Civil Rights",
            new String[]{"Generational"}
    );
    static President abrahamLincoln60 = new President(
            "Abraham Lincoln '60", "Midwest", 3, 9, 10, 7, "Conservative", "Civil Rights",
            new String[]{"Generational"}
    );
    static President abrahamLincoln65 = new President(
            "Abraham Lincoln '65", "Midwest", 8, 9, 10, 10, "Conservative", "Civil Rights",
            new String[]{"Generational"}
    );
    static President calvinCoolidge20 = new President(
            "Calvin Coolidge '20", "Northeast", 4, 4, 5, 2, "Libertarian", "Tax Cuts",
            new String[]{"Generational"}
    );
    static President calvinCoolidge23 = new President(
            "Calvin Coolidge '23", "Northeast", 6, 5, 7, 5, "Libertarian", "Tax Cuts",
            new String[]{"Generational"}
    );
    static President calvinCoolidge28 = new President(
            "Calvin Coolidge '28", "Northeast", 9, 6, 9, 7, "Libertarian", "Tax Cuts",
            new String[]{"Generational"}
    );
    static President richardNixon60 = new President(
            "Richard Nixon '60", "West", 8, 5, 5, 7, "Conservative", "Imperialism",
            new String[]{"Generational"}
    );
    static President richardNixon68 = new President(
            "Richard Nixon '68", "West", 8, 7, 5, 8, "Conservative", "Law and Order",
            new String[]{"Generational"}
    );
    static President richardNixon84 = new President(
            "Richard Nixon '84", "West", 9, 5, 8, 10, "Conservative", "Imperialism",
            new String[]{"Generational"}
    );
    static President ronaldReagan76 = new President(
            "Ronald Reagan '76", "West", 4, 6, 3, 7, "Conservative", "Traditional Morality",
            new String[]{"Generational"}
    );
    static President ronaldReagan80 = new President(
            "Ronald Reagan '80", "West", 4, 10, 5, 8, "Conservative", "Tax Cuts",
            new String[]{"Generational"}
    );
    static President ronaldReagan88 = new President(
            "Ronald Reagan '88", "West", 8, 9, 7, 10, "Conservative", "Imperialism",
            new String[]{"Generational"}
    );
    static President alGore88 = new President(
            "Al Gore '88", "South", 4, 2, 3, 4, "Progressive", "Traditional Morality",
            new String[]{"Generational"}
    );
    static President alGore00 = new President(
            "Al Gore '00", "South", 8, 3, 6, 5, "Progressive", "Social Liberalism",
            new String[]{"Generational"}
    );
    static President joeBiden96 = new President(
            "Joe Biden '96", "Northeast", 7, 2, 5, 2, "Progressive", "Social Programs",
            new String[]{"Generational"}
    );
    static President joeBiden20 = new President(
            "Joe Biden '20", "Northeast", 10, 2, 3, 6, "Progressive", "Social Programs",
            new String[]{"Generational"}
    );
    static President ulyssesSGrant66 = new President(
            "Ulysses S. Grant '66", "Midwest", 1, 6, 2, 9, "Conservative", "Civil Rights",
            new String[]{"Generational"}
    );
    static President ulyssesSGrant72 = new President(
            "Ulysses S. Grant '72", "Midwest", 5, 8, 5, 10, "Conservative", "Civil Rights",
            new String[]{"Generational"}
    );
    static President ulyssesSGrant76 = new President(
            "Ulysses S. Grant '76", "Midwest", 6, 8, 7, 10, "Conservative", "Civil Rights",
            new String[]{"Generational"}
    );
    static President mikePence = new President(
            "Mike Pence", "Midwest", 6, 2, 3, 4, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President jebBushh = new President(
            "Jeb Bush", "South", 3, 1, 2, 4, "Conservative", "Establishment",
            new String[]{"Modern Era"}
    );
    static President joeLiberal = new President(
            "Joe Liberal", "Northeast", 10, 1, 9, 5, "Progressive", "Social Liberalism",
            new String[]{"Meme Card"}
    );
    static President geraldMander = new President(
            "Gerald Mander", "South", 10, 1, 9, 5, "Conservative", "Tax Cuts",
            new String[]{"Meme Card"}
    );
    static President margaretChaseSmith = new President(
    		"Margaret Chase Smith", "Northeast", 4, 3, 3, 3, "Conservative", "Imperialism",
            new String[]{"New Deal Era"}
    );
    static President bobDole = new President(
    		"Bob Dole", "West", 7, 3, 4, 4, "Conservative", "Tax Cuts",
            new String[]{"Reagan Era"}
    );
    static President eugeneDebs = new President(
    		"Eugene Debs", "Midwest", 1, 4, 3, 4, "Progressive", "Class Unity",
            new String[]{"Progressive Era"}
    );
    static President garyHart = new President(
    		"Gary Hart", "West", 5, 4, 2, 1, "Progressive", "Anti-Establishment",
            new String[]{"Reagan Era"}
    );
    
    static President jerryBrown = new President(
    		"Jerry Brown", "West", 3, 4, 1, 2, "Populist", "Anti-Establishment",
            new String[]{"Reagan Era"}
    );
    
    static President newtGingrich = new President(
    		"Newt Gingrich", "South", 6, 2, 5, 5, "Conservative", "Traditional Morality",
            new String[]{"Reagan Era"}
    );
    static President paulTsongas = new President(
    		"Paul Tsongas", "Northeast", 5, 2, 5, 2, "Progressive", "Deregulation",
            new String[]{"Reagan Era"}
    );
    static President marioCuomo = new President(
    		"Mario Cuomo", "Northeast", 5, 6, 3, 5, "Progressive", "Social Programs",
            new String[]{"Reagan Era"}
    );
    static President jesseJackson = new President(
    		"Jesse Jackson", "South", 1, 6, 3, 5, "Progressive", "Social Liberalism",
            new String[]{"Civil Rights Era"}
    );
    static President pramilaJayapal = new President(
    		"Pramila Jayapal", "West", 2, 1, 2, 1, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President staceyAbrams = new President(
    		"Stacey Abrams", "South", 1, 4, 2, 3, "Progressive", "Civil Rights",
            new String[]{"Trump Era"}
    );
    static President sherrodBrown = new President(
    		"Sherrod Brown", "Midwest", 6, 2, 3, 1, "Progressive", "Tariffs",
            new String[]{"Trump Era"}
    );
    
    static President randPaul = new President(
    		"Rand Paul", "South", 4, 2, 6, 2, "Libertarian", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    
    static President dwightEisenhower48 = new President(
    		"Dwight Eisenhower '48", "West", 1, 7, 1, 10, "Conservative", "Imperialism",
            new String[]{"Generational"}
    );
    
    static President dwightEisenhower52 = new President(
    		"Dwight Eisenhower '52", "West", 1, 8, 4, 10, "Conservative", "Imperialism",
            new String[]{"Generational"}
    );
    
    static President dwightEisenhower60 = new President(
    		"Dwight Eisenhower '60", "West", 7, 9, 5, 10, "Conservative", "Internal Improvements",
            new String[]{"Generational"}
    );
    
    static President lyndonbjohnson60 = new President(
    		"Lyndon B. Johnson '60", "South", 7, 2, 6, 3, "Progressive", "Social Programs",
            new String[]{"Generational"}
    );
    
    static President lyndonbjohnson64 = new President(
    		"Lyndon B. Johnson '64", "South", 10, 2, 7, 6, "Progressive", "Social Programs",
            new String[]{"Generational"}
    );
    
    static President johnfkennedy56 = new President(
    		"John F. Kennedy '56", "Northeast", 3, 7, 5, 5, "Progressive", "Imperialism",
            new String[]{"Generational"}
    );
    
    static President johnfkennedy60 = new President(
    		"John F. Kennedy '60", "Northeast", 5, 9, 6, 7, "Progressive", "Imperialism",
            new String[]{"Generational"}
    );
  
    static President johnfkennedy63 = new President(
    		"John F. Kennedy '63", "Northeast", 8, 9, 6, 10, "Progressive", "Imperialism",
            new String[]{"Generational"}
    );
    
    static President williamMcKinley90 = new President(
            "William McKinley '90", "Midwest", 4, 3, 7, 4, "Conservative", "Tariffs",
            new String[]{"Generational"}
    );
    static President williamMcKinley96 = new President(
            "William McKinley '96", "Midwest", 7, 5, 7, 6, "Conservative", "Tariffs",
            new String[]{"Generational"}
    );
    static President williamMcKinley01 = new President(
            "William McKinley '01", "Midwest", 10, 5, 8, 8, "Conservative", "Imperialism",
            new String[]{"Generational"}
    );
    static President jamesMadison88 = new President(
            "James Madison '88", "South", 3, 2, 10, 3, "Conservative", "Constructionism",
            new String[]{"Generational"}
    );
    static President jamesMadison08 = new President(
            "James Madison '08", "South", 7, 4, 10, 8, "Libertarian", "Constructionism",
            new String[]{"Generational"}
    );
    static President jamesMadison16 = new President(
            "James Madison '16", "South", 10, 5, 10, 10, "Conservative", "Internal Improvements",
            new String[]{"Generational"}
    );
    static President johnAdams76 = new President(
            "John Adams '76", "Northeast", 5, 2, 6, 5, "Conservative", "Law and Order",
            new String[]{"Generational"}
    );
    static President johnAdams96 = new President(
            "John Adams '96", "Northeast", 10, 3, 7, 8, "Conservative", "Social Hierarchy",
            new String[]{"Generational"}
    );
    static President johnAdams00 = new President(
            "John Adams '00", "Northeast", 10, 4, 10, 10, "Conservative", "Social Hierarchy",
            new String[]{"Generational"}
    );
    static President thomasJefferson96 = new President(
            "Thomas Jefferson '96", "South", 3, 5, 6, 10, "Libertarian", "States' Rights",
            new String[]{"Generational"}
    );
    static President thomasJefferson00 = new President(
            "Thomas Jefferson '00", "South", 7, 7, 7, 10, "Libertarian", "Egalitarianism",
            new String[]{"Generational"}
    );
    static President thomasJefferson08 = new President(
            "Thomas Jefferson '08", "South", 10, 8, 10, 10, "Libertarian", "States' Rights",
            new String[]{"Generational"}
    );
    static President georgeWashington83 = new President(
            "George Washington '83", "South", 1, 8, 3, 10, "Libertarian", "Nationalism",
            new String[]{"Generational"}
    );
    static President georgeWashington89 = new President(
            "George Washington '89", "South", 3, 10, 6, 10, "Libertarian", "Nationalism",
            new String[]{"Generational"}
    );
    static President georgeWashington97 = new President(
            "George Washington '97", "South", 10, 10, 10, 10, "Conservative", "Centralization",
            new String[]{"Generational"}
    );
    // I think quotes are already filtered out of the name for image file. This line breaks the code
    static President honestLizCheney = new President(
            "Honest Liz Cheney", "West", 3, 9, 10, 7, "Conservative", "Constructionism",
            new String[]{"Meme Card"}
    );
    static President williamEMiller = new President(
            "William E. Miller", "Northeast", 5, 2, 2, 1, "Conservative", "Traditional Morality",
            new String[]{"Civil Rights Era"}
    );
    static President benjaminFranklin = new President(
            "Benjamin Franklin", "Northeast", 8, 4, 9, 8, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President frankKnox = new President(
            "Frank Knox", "Northeast", 1, 1, 1, 2, "Conservative", "Imperialism",
            new String[]{"New Deal Era"}
    );
    static President johnJay = new President(
            "John Jay", "Northeast", 7, 2, 9, 5, "Conservative", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President eleanorRoosevelt = new President(
            "Eleanor Roosevelt", "Northeast", 2, 7, 2, 8, "Progressive", "Civil Rights",
            new String[]{"New Deal Era"}
    );
    static President williamFBuckley = new President(
            "William F. Buckley", "Northeast", 1, 2, 7, 5, "Conservative", "Traditional Morality",
            new String[]{"Civil Rights Era"}
    );
    static President williamLloydGarrison = new President(
            "William Lloyd Garrison", "Northeast", 1, 6, 1, 5, "Progressive", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President georgeClinton = new President(
            "George Clinton", "Northeast", 9, 5, 5, 6, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President jpMorgan = new President(
            "J.P. Morgan", "Northeast", 1, 2, 6, 9, "Conservative", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President rufusKing = new President(
            "Rufus King", "Northeast", 4, 3, 6, 4, "Conservative", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President joeTRobinson = new President(
            "Joe T. Robinson", "South", 6, 1, 2, 2, "Progressive", "Social Programs",
            new String[]{"New Deal Era"}
    );
    static President williamScranton = new President(
            "William Scranton", "Northeast", 3, 2, 3, 3, "Conservative", "Social Liberalism",
            new String[]{"Civil Rights Era"}
    );
    static President georgeRomney = new President(
            "George Romney", "Midwest", 4, 2, 5, 4, "Conservative", "Civil Rights",
            new String[]{"Civil Rights Era"}
    );
    static President elbridgeGerry = new President(
            "Elbridge Gerry", "Northeast", 4, 3, 5, 4, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President henryCabotLodgeJr = new President(
            "Henry Cabot Lodge Jr.", "Northeast", 6, 2, 5, 5, "Conservative", "Imperialism",
            new String[]{"Civil Rights Era"}
    );
    static President charlesCPinckney = new President(
            "Charles C. Pinckney", "South", 2, 3, 4, 8, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President adlaiStevensonII = new President(
            "Adlai Stevenson II", "Midwest", 2, 5, 2, 2, "Progressive", "Law and Order",
            new String[]{"New Deal Era"}
    );
    static President henryWilson = new President(
            "Henry Wilson", "Northeast", 7, 2, 5, 3, "Progressive", "Civil Rights",
            new String[]{"Civil Rights Era"}
    );
    static President ronJohnson = new President(
            "Ron Johnson", "Midwest", 6, 2, 3, 2, "Conservative", "Deregulation",
            new String[]{"Trump Era"}
    );
    static President johnMarshall = new President(
            "John Marshall", "Northeast", 6, 3, 6, 6, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President thomasPinckney = new President(
            "Thomas Pinckney", "South", 4, 3, 1, 8, "Conservative", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President charlesPinckney = new President(
            "Charles Pinckney", "South", 5, 1, 3, 8, "Conservative", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President henryLeeIII = new President(
            "Henry Lee III", "South", 3, 3, 1, 4, "Conservative", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President jamesDuane = new President(
            "James Duane", "Northeast", 3, 1, 3, 3, "Conservative", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President georgeCabot = new President(
            "George Cabot", "Northeast", 3, 3, 5, 3, "Conservative", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President timothyPickering = new President(
            "Timothy Pickering", "Northeast", 7, 3, 4, 4, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President henryKnox = new President(
            "Henry Knox", "Northeast", 3, 4, 1, 7, "Conservative", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President philipSchuyler = new President(
            "Philip Schuyler", "Northeast", 3, 4, 2, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President calebStrong = new President(
            "Caleb Strong", "Northeast", 7, 4, 2, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President johnLowellJr = new President(
            "John Lowell Jr.", "Northeast", 1, 3, 4, 1, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President johnEagerHoward = new President(
            "John Eager Howard", "Northeast", 4, 3, 1, 3, "Conservative", "Civil Liberties",
            new String[]{"Founding Era"}
    );
    static President edmundRandolph = new President(
            "Edmund Randolph", "South", 5, 4, 4, 2, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President jonathanDayton = new President(
            "Jonathan Dayton", "Northeast", 6, 1, 2, 2, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President williamLivingston = new President(
            "William Livingston", "Northeast", 5, 5, 3, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President samuelHuntington = new President(
            "Samuel Huntington", "Northeast", 8, 5, 1, 4, "Conservative", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President richardVarick = new President(
            "Richard Varick", "Northeast", 4, 2, 7, 3, "Conservative", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President noahWebster = new President(
            "Noah Webster", "Northeast", 2, 1, 7, 5, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President increaseSumner = new President(
            "Increase Sumner", "Northeast", 4, 3, 4, 2, "Conservative", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President oliverWolcottJr = new President(
            "Oliver Wolcott Jr.", "Northeast", 5, 1, 5, 2, "Conservative", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President gouverneurMorris = new President(
            "Gouverneur Morris", "Northeast", 2, 4, 6, 2, "Conservative", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President josiahQuincyIII = new President(
            "Josiah Quincy III", "Northeast", 2, 1, 3, 1, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President jamesWilson = new President(
            "James Wilson", "Northeast", 4, 3, 6, 3, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President oliverEllsworth = new President(
            "Oliver Ellsworth", "Northeast", 5, 5, 4, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President jamesMcHenry = new President(
            "James McHenry", "Northeast", 2, 3, 3, 2, "Conservative", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President johnBrooks = new President(
            "John Brooks", "Northeast", 2, 6, 1, 2, "Conservative", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President christopherGore = new President(
            "Christopher Gore", "Northeast", 4, 1, 2, 1, "Conservative", "Internal Improvements",
            new String[]{"Founding Era"}
    );
    static President thomasWilling = new President(
            "Thomas Willing", "Northeast", 1, 1, 3, 2, "Conservative", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President williamPaterson = new President(
            "William Paterson", "Northeast", 5, 2, 5, 3, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President kimkardashian = new President(
            "Kim Kardashian", "West", 1, 7, 1, 8, "Conserative", "Civil Liberties",
            new String[]{"Meme Card"}
    );
    static President lolalago = new President(
            "Lola Lago", "Spain", 10, 10, 10, 10, "Populist", "Law and Order",
            new String[]{"Meme Card"}
    );
    static President charlesWFairbanks = new President(
            "Charles W. Fairbanks", "Midwest", 5, 4, 4, 3, "Conservative", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President altonBParker = new President(
            "Alton B. Parker", "Northeast", 2, 4, 4, 2, "Libertarian", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President jamesSSherman = new President(
            "James S. Sherman", "Northeast", 5, 5, 2, 2, "Conservative", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President adlaiStevensonI = new President(
            "Adlai Stevenson I", "Midwest", 6, 3, 3, 4, "Populist", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President henryGDavis = new President(
            "Henry G. Davis", "Midwest", 3, 3, 1, 2, "Conservative", "Internal Improvements",
            new String[]{"Progressive Era"}
    );
    static President johnWKern = new President(
            "John W. Kern", "Midwest", 3, 3, 4, 2, "Progressive", "Central Bank",
            new String[]{"Progressive Era"}
    );
    static President williamAWheeler = new President(
            "William A. Wheeler", "Northeast", 4, 5, 1, 1, "Conservative", "Limited Government",
            new String[]{"Reconstruction Era"}
    );
    static President samuelJTilden = new President(
            "Samuel J. Tilden", "Northeast", 2, 5, 5, 5, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President jamesGBlaine = new President(
            "James G. Blaine", "Northeast", 7, 5, 2, 5, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President thomasAHendricks = new President(
            "Thomas A. Hendricks", "Midwest", 6, 3, 4, 4, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President winfieldScottHancock = new President(
            "Winfield Scott Hancock", "Northeast", 1, 6, 2, 7, "Libertarian", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President williamHaydenEnglish = new President(
            "William Hayden English", "Midwest", 3, 4, 2, 2, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President johnHancock = new President(
            "John Hancock", "Northeast", 7, 4, 5, 8, "Conservative", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President danielDTompkins = new President(
            "Daniel D. Tompkins", "Northeast", 8, 2, 4, 5, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President samuelAdams = new President(
            "Samuel Adams", "Northeast", 3, 8, 3, 7, "Libertarian", "Civil Liberties",
            new String[]{"Founding Era"}
    );
    static President abigailAdams = new President(
            "Abigail Adams", "Northeast", 1, 4, 7, 5, "Conservative", "Social Liberalism",
            new String[]{"Founding Era"}
    );
    static President williamCarroll = new President(
            "William Carroll", "South", 5, 4, 3, 2, "Conservative", "Internal Improvements",
            new String[]{"Founding Era"}
    );
    static President aaronBurr = new President(
            "Aaron Burr", "Northeast", 6, 3, 4, 6, "Libertarian", "Establishment",
            new String[]{"Founding Era"}
    );
    static President johnTylerSr = new President(
            "John Tyler Sr.", "South", 3, 1, 2, 1, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President dewittClinton = new President(
            "DeWitt Clinton", "Northeast", 4, 5, 4, 6, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President melanctonSmith = new President(
            "Melancton Smith", "Northeast", 1, 4, 5, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President davidRittenhouse = new President(
            "David Rittenhouse", "Northeast", 1, 1, 2, 5, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President jamesFenner = new President(
            "James Fenner", "Northeast", 5, 1, 1, 1, "Libertarian", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President israelSmith = new President(
            "Israel Smith", "Northeast", 4, 1, 2, 1, "Libertarian", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President richardHenryLee = new President(
            "Richard Henry Lee", "South", 6, 2, 5, 4, "Libertarian", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President johnRandolph = new President(
            "John Randolph", "South", 5, 6, 3, 4, "Libertarian", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President jaredIrwin = new President(
            "Jared Irwin", "South", 3, 2, 1, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Founding Era"}
    );
    static President georgeMason = new President(
            "George Mason", "South", 2, 4, 7, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President williamCrawford = new President(
            "William Crawford", "South", 5, 4, 6, 3, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President robertYates = new President(
            "Robert Yates", "Northeast", 1, 4, 5, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President georgeTroup = new President(
            "George Troup", "South", 5, 3, 1, 2, "Populist", "Racism",
            new String[]{"Founding Era"}
    );
    static President lutherMartin = new President(
            "Luther Martin", "Northeast", 2, 2, 1, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President charlesScott = new President(
            "Charles Scott", "South", 2, 2, 1, 4, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President mercyOtisWarren = new President(
            "Mercy Otis Warren", "Northeast", 1, 3, 7, 1, "Libertarian", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President albertGallatin = new President(
            "Albert Gallatin", "Northeast", 6, 1, 9, 4, "Libertarian", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President nathanielMacon = new President(
            "Nathaniel Macon", "South", 8, 3, 5, 3, "Libertarian", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President johnBreckinridge = new President(
            "John Breckinridge", "South", 3, 3, 8, 3, "Libertarian", "Civil Liberties",
            new String[]{"Founding Era"}
    );
    static President johnLangdon = new President(
            "John Langdon", "Northeast", 6, 3, 4, 3, "Libertarian", "Centralization",
            new String[]{"Founding Era"}
    );
    static President jamesWarren = new President(
            "James Warren", "Northeast", 3, 1, 4, 2, "Libertarian", "Civil Liberties",
            new String[]{"Founding Era"}
    );
    static President danielShays = new President(
            "Daniel Shays", "Northeast", 1, 4, 1, 7, "Populist", "Tax Cuts",
            new String[]{"Founding Era"}
    );
    static President arthurFenner = new President(
            "Arthur Fenner", "Northeast", 6, 2, 4, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President jamesSullivan = new President(
            "James Sullivan", "Northeast", 2, 3, 4, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President morganLewis = new President(
            "Morgan Lewis", "Northeast", 3, 3, 4, 3, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President williamBlount = new President(
            "William Blount", "South", 3, 2, 2, 4, "Libertarian", "Deregulation",
            new String[]{"Founding Era"}
    );
    static President johnTaylor = new President(
            "John Taylor", "South", 4, 2, 7, 3, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President josephBradleyVarnum = new President(
            "Joseph Bradley Varnum", "Northeast", 7, 1, 4, 3, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President peterPorter = new President(
            "Peter Porter", "Northeast", 3, 3, 1, 2, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President jamesJackson = new President(
            "James Jackson", "South", 5, 1, 4, 1, "Libertarian", "Anti-Central Bank",
            new String[]{"Founding Era"}
    );
    static President johnLansingJr = new President(
            "John Lansing Jr.", "Northeast", 3, 2, 3, 4, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President philipBBarbour = new President(
            "Philip B. Barbour", "South", 6, 3, 4, 4, "Populist", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President langdonCheves = new President(
            "Langdon Cheves", "South", 4, 4, 3, 5, "Libertarian", "Free Trade",
            new String[]{"Founding Era"}
    );
    static President simonSnyder = new President(
            "Simon Snyder", "Northeast", 4, 3, 2, 2, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President thomasMcKean = new President(
            "Thomas McKean", "Northeast", 4, 2, 4, 2, "Libertarian", "Establishment",
            new String[]{"Founding Era"}
    );
    static President jamesBarbour = new President(
            "James Barbour", "South", 6, 1, 5, 2, "Conservative", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President jamesGarrard = new President(
            "James Garrard", "South", 3, 2, 3, 1, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President nicholasGilman = new President(
            "Nicholas Gilman", "Northeast", 5, 1, 2, 1, "Conservative", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President joshHawley = new President(
            "Josh Hawley", "South", 3, 4, 4, 3, "Populist", "Regulation",
            new String[]{"Trump Era"}
    );
    static President maryPeltola = new President(
            "Mary Peltola", "West", 2, 2, 2, 1, "Progressive", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President leeZeldin = new President(
            "Lee Zeldin", "Northeast", 3, 4, 5, 2, "Conservative", "Law and Order",
            new String[]{"Trump Era"}
    );
    static President glennYoungkin = new President(
            "Glenn Youngkin", "South", 2, 6, 6, 3, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President gretchenWhitmer = new President(
            "Gretchen Whitmer", "Midwest", 4, 5, 5, 3, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    
    static President kamalaHarris = new President(
            "Kamala Harris", "West", 7, 2, 3, 6, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    
    static President rickSantorum = new President(
            "Rick Santorum", "Midwest", 5, 2, 3, 3, "Conservative", "Traditional Morality",
            new String[]{"Modern Era"}
    );
    /** no parentheses? :(
    static President charliecristrep = new President(
            "Charlie Crist", "South", 4, 2, 2, 4, "Conservative", "Traditional Morality"
    );
    
    static President charliecristdem = new President(
            "Charlie Crist", "South", 4, 2, 2, 4, "Progressive", "Social Liberalism"
    );
    
    static President charliecristind = new President(
            "Charlie Crist", "South", 4, 2, 2, 4, "Conservative", "Anti-Establishment"
    );
    static President charliecristwhig = new President(
            "Charlie Crist", "South", 4, 2, 2, 4, "Conservative", "Internal Improvements"
    );
    static President charliecristdemrep = new President(
            "Charlie Crist", "South", 4, 2, 2, 4, "Libertarian", "Constructionism"
    );
    static President charliecristfed = new President(
            "Charlie Crist", "South", 4, 2, 2, 4, "Conservative", "Central Bank"
    );
    **/
    
    static President schuylerColfax = new President(
            "Schuyler Colfax", "Northeast", 8, 3, 4, 4, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President benjaminWade = new President(
            "Benjamin Wade", "Midwest", 6, 2, 6, 5, "Progressive", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President reubenFenton = new President(
            "Reuben Fenton", "Northeast", 7, 1, 3, 2, "Conservative", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President horatioSeymour = new President(
            "Horatio Seymour", "Northeast", 3, 5, 4, 5, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President francisPrestonBlairJr = new President(
            "Francis Preston Blair Jr.", "Northeast", 3, 4, 2, 3, "Libertarian", "Racism",
            new String[]{"Civil War Era"}
    );
    static President georgeHPendleton = new President(
            "George H. Pendleton", "Midwest", 5, 4, 4, 3, "Populist", "Isolationism",
            new String[]{"Civil War Era"}
    );
    static President andrewGreggCurtin = new President(
            "Andrew Gregg Curtin", "Northeast", 5, 4, 1, 2, "Conservative", "Imperialism",
            new String[]{"Civil War Era"}
    );
    static President jamesSpeed = new President(
            "James Speed", "South", 2, 3, 5, 2, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President jamesHarlan = new President(
            "James Harlan", "Northeast", 6, 2, 1, 1, "Conservative", "Traditional Morality",
            new String[]{"Civil War Era"}
    );
    static President jamesRoodDoolittle = new President(
            "James Rood Doolittle", "Midwest", 5, 2, 2, 1, "Midwest", "States' Rights",
            new String[]{"Civil War Era"}
    );
    static President joelParker = new President(
            "Joel Parker", "Midwest", 4, 4, 1, 3, "Populist", "Civil Liberties",
            new String[]{"Civil War Era"}
    );
    static President reverdyJohnson = new President(
            "Reverdy Johnson", "Northeast", 4, 2, 2, 1, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President pierceButler = new President(
            "Pierce Butler", "South", 4, 3, 1, 1, "Libertarian", "Racism",
            new String[]{"Founding Era"}
    );
    static President hughWilliamson = new President(
            "Hugh Williamson", "South", 2, 3, 1, 2, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President theodoreSedgwick = new President(
            "Theodore Sedgwick", "Northeast", 6, 4, 2, 4, "Conservative", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President johnFrancisMercer = new President(
            "John Francis Mercer", "South", 3, 1, 3, 1, "Libertarian", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President johnDickinson = new President(
            "John Dickinson", "Northeast", 4, 5, 3, 6, "Conservative", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President ninianEdwards = new President(
            "Ninian Edwards", "Midwest", 6, 1, 3, 2, "Conservative", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President fisherAmes = new President(
            "Fisher Ames", "Northeast", 3, 6, 3, 3, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President rogerGriswold = new President(
            "Roger Griswold", "Northeast", 3, 1, 1, 4, "Conservative", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President josephDennie = new President(
            "Joseph Dennie", "Northeast", 1, 4, 2, 3, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President robertRLivingston = new President(
            "Robert R. Livingston", "Northeast", 4, 4, 3, 4, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President jamesTallmadgeJr = new President(
            "James Tallmadge Jr.", "Northeast", 2, 1, 2, 2, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President frederickMuhlenberg = new President(
            "Frederick Muhlenberg", "Northeast", 5, 3, 4, 3, "Libertarian", "Civil Liberties",
            new String[]{"Founding Era"}
    );
    static President benjaminLincoln = new President(
            "Benjamin Lincoln", "Northeast", 1, 2, 1, 4, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President samuelJohnston = new President(
            "Samuel Johnston", "South", 3, 1, 2, 1, "Conservative", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President jamesABayard = new President(
            "James A. Bayard", "Northeast", 4, 6, 1, 3, "Conservative", "Establishment",
            new String[]{"Founding Era"}
    );
    static President philipFreneau = new President(
            "Philip Freneau", "Northeast", 1, 5, 2, 5, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President williamMaclay = new President(
            "William Maclay", "Northeast", 2, 4, 1, 2, "Libertarian", "Anti-Establishment",
            new String[]{"Founding Era"}
    );
    static President jonathanJennings = new President(
            "Jonathan Jennings", "Midwest", 5, 3, 2, 2, "Conservative", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President rogerSherman = new President(
            "Roger Sherman", "Northeast", 5, 3, 5, 4, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President theophilusParsons = new President(
            "Theophilus Parsons", "Northeast", 2, 2, 2, 1, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President francisDana = new President(
            "Francis Dana", "Northeast", 3, 2, 2, 1, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President samuelLMitchill = new President(
            "Samuel L. Mitchill", "Northeast", 4, 1, 5, 1, "Libertarian", "Internal Improvements",
            new String[]{"Founding Era"}
    );
    static President henryDearborn = new President(
            "Henry Dearborn", "Northeast", 3, 4, 1, 2, "Libertarian", "Racism",
            new String[]{"Founding Era"}
    );
    static President williamBranchGiles = new President(
            "William Branch Giles", "South", 6, 2, 4, 3, "Libertarian", "Laissez-Faire",
            new String[]{"Founding Era"}
    );
    static President israelThorndike = new President(
            "Israel Thorndike", "Northeast", 2, 2, 1, 1, "Conservative", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President jamesIredell = new President(
            "James Iredell", "South", 3, 1, 4, 2, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President samuelChase = new President(
            "Samuel Chase", "Northeast", 4, 2, 1, 4, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President matthewLyon = new President(
            "Matthew Lyon", "Northeast", 3, 1, 1, 5, "Libertarian", "Civil Liberties",
            new String[]{"Founding Era"}
    );
    static President johnWTaylor = new President(
            "John W. Taylor", "Northeast", 6, 2, 2, 2, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President williamGrayson = new President(
            "William Grayson", "South", 2, 2, 1, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President johnRutledge = new President(
            "John Rutledge", "South", 5, 2, 1, 4, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President robertMorris = new President(
            "Robert Morris", "Northeast", 3, 2, 7, 3, "Conservative", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President jonathanJackson = new President(
            "Jonathan Jackson", "Northeast", 2, 3, 1, 1, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President benjaminHawkins = new President(
            "Benjamin Hawkins", "South", 3, 4, 2, 1, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President williamCCClaiborne = new President(
            "William C. C. Claiborne", "South", 5, 2, 1, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President henryTazewell = new President(
            "Henry Tazewell", "South", 3, 2, 1, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President jamesHillhouse = new President(
            "James Hillhouse", "Northeast", 6, 1, 1, 1, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President williamRichardsonDavie = new President(
            "William Richardson Davie", "South", 2, 2, 3, 2, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President jamesLloyd = new President(
            "James Lloyd", "Northeast", 4, 3, 1, 1, "Conservative", "Free Trade",
            new String[]{"Founding Era"}
    );
    static President peterMuhlenberg = new President(
            "Peter Muhlenberg", "Northeast", 3, 2, 1, 2, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President johnMilledge = new President(
            "John Milledge", "South", 3, 3, 2, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Founding Era"}
    );
    static President nathanWilliams = new President(
            "Nathan Williams", "Northeast", 2, 1, 1, 1, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President thomasMifflin = new President(
            "Thomas Mifflin", "Northeast", 6, 2, 2, 3, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President nathanDane = new President(
            "Nathan Dane", "Northeast", 3, 1, 4, 1, "Conservative", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President benjaminGoodhue = new President(
            "Benjamin Goodhue", "Northeast", 4, 2, 1, 4, "Conservative", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President abrahamBaldwin = new President(
            "Abraham Baldwin", "South", 6, 1, 3, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President williamFew = new President(
            "William Few", "South", 2, 6, 2, 2, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President benjaminFranklinBache = new President(
            "Benjamin Franklin Bache", "Northeast", 1, 4, 1, 4, "Libertarian", "Anti-Establishment",
            new String[]{"Founding Era"}
    );
    static President josephHemphill = new President(
            "Joseph Hemphill", "Northeast", 4, 2, 1, 1, "Populist", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President aaronOgden = new President(
            "Aaron Ogden", "Northeast", 3, 2, 1, 5, "Conservative", "Deregulation",
            new String[]{"Founding Era"}
    );
    static President williamShepard = new President(
            "William Shepard", "Northeast", 2, 3, 1, 2, "Conservative", "Law and Order",
            new String[]{"Founding Era"}
    );
    static President johnChandler = new President(
            "John Chandler", "Northeast", 3, 1, 3, 1, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President philipVanCortland = new President(
            "Philip Van Cortland", "Northeast", 3, 3, 1, 1, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President edwardTiffin = new President(
            "Edward Tiffin", "Midwest", 4, 3, 6, 1, "Libertarian", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President theodoreDwight = new President(
            "Theodore Dwight", "Northeast", 2, 2, 1, 2, "Conservative", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President jamesRoss = new President(
            "James Ross", "Northeast", 3, 1, 1, 2, "Conservative", "Establishment",
            new String[]{"Founding Era"}
    );
    static President samuelWhite = new President(
            "Samuel White", "Northeast", 3, 2, 1, 1, "Conservative", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President meriwetherLewis = new President(
            "Meriwether Lewis", "South", 2, 4, 1, 7, "Libertarian", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President davidStone = new President(
            "David Stone", "South", 4, 3, 3, 1, "Libertarian", "Social Liberalism",
            new String[]{"Founding Era"}
    );
    static President williamWyattBibb = new President(
            "William Wyatt Bibb", "South", 5, 2, 2, 2, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President johnWitherspoon = new President(
            "John Witherspoon", "Northeast", 1, 2, 3, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President edwardRutledge = new President(
            "Edward Rutledge", "South", 2, 2, 2, 3, "Conservative", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President johnFenno = new President(
            "John Fenno", "Northeast", 1, 3, 1, 3, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President georgeClintonJr = new President(
            "George Clinton Jr.", "Northeast", 2, 1, 1, 5, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President thomasSumter = new President(
            "Thomas Sumter", "South", 5, 3, 1, 2, "Libertarian", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President josephAnderson = new President(
            "Joseph Anderson", "South", 7, 1, 3, 1, "Libertarian", "Open Borders",
            new String[]{"Founding Era"}
    );
    static President stephenVanRensselaer = new President(
            "Stephen Van Rensselaer", "Northeast", 3, 1, 3, 4, "Conservative", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President robertGoodloeHarper = new President(
            "Robert Goodloe Harper", "Northeast", 3, 2, 2, 3, "Conservative", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President danielOfStThomas = new President(
            "Daniel of St. T. Jenifer", "Northeast", 5, 2, 3, 1, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President wadeHamptonI = new President(
            "Wade Hampton I", "South", 2, 1, 1, 3, "Libertarian", "Racism",
            new String[]{"Founding Era"}
    );
    static President williamFindley = new President(
            "William Findley", "Northeast", 8, 3, 1, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President georgeLogan = new President(
            "George Logan", "Northeast", 2, 2, 1, 3, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President alexanderMartin = new President(
            "Alexander Martin", "Northeast", 6, 3, 1, 3, "Conservative", "Closed Borders",
            new String[]{"Founding Era"}
    );
    static President georgeMathews = new President(
            "George Mathews", "South", 2, 1, 1, 2, "Conservative", "Establishment",
            new String[]{"Founding Era"}
    );
    static President georgeWythe = new President(
            "George Wythe", "South", 2, 4, 3, 1, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President leviLincolnSr = new President(
            "Levi Lincoln Sr.", "Northeast", 4, 2, 3, 2, "Libertarian", "Establishment",
            new String[]{"Founding Era"}
    );
    static President josephDesha = new President(
            "Joseph Desha", "South", 4, 4, 1, 3, "Populist", "Social Programs",
            new String[]{"Founding Era"}
    );
    static President samuelDexter = new President(
            "Samuel Dexter", "Northeast", 3, 2, 1, 1, "Libertarian", "Traditional Morality",
            new String[]{"Founding Era"}
    );
    static President williamSamuelJohnson = new President(
            "William Samuel Johnson", "Northeast", 2, 4, 3, 1, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President stephenHigginson = new President(
            "Stephen Higginson", "South", 1, 2, 1, 1, "Conservative", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President jaredIngersoll = new President(
            "Jared Ingersoll", "Northeast", 3, 1, 6, 2, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President samuelSmith = new President(
            "Samuel Smith", "Northeast", 6, 5, 1, 3, "Libertarian", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President jamesTurner = new President(
            "James Turner", "South", 4, 1, 4, 1, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President lafayette = new President(
            "Lafayette", "Northeast", 1, 7, 4, 9, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President harrisonGrayOtis = new President(
            "Harrison Gray Otis", "Northeast", 4, 2, 4, 4, "Conservative", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President jamesGunn = new President(
            "James Gunn", "South", 4, 1, 2, 2, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President williamBingham = new President(
            "William Bingham", "Northeast", 3, 1, 2, 3, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President williamDuane = new President(
            "William Duane", "Northeast", 1, 3, 1, 2, "Populist", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President williamPinkney = new President(
            "William Pinkney", "Northeast", 3, 1, 3, 1, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President thomasRandolphJr = new President(
            "Thomas Randolph Jr.", "South", 3, 2, 2, 3, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President asaHutchinson = new President(
            "Asa Hutchinson", "South", 6, 1, 1, 1, "Conservative", "Law and Order",
            new String[]{"Trump Era"}
    );
    static President dougMastriano = new President(
            "Doug Mastriano", "Northeast", 1, 1, 1, 1, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President paulRyan = new President(
            "Paul Ryan", "Midwest", 4, 2, 6, 4, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President elizabethWarren = new President(
            "Elizabeth Warren", "Northeast", 4, 1, 7, 3, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President catherineCortezMasto = new President(
            "Catherine Cortez Masto", "West", 4, 2, 3, 1, "Progressive", "Open Borders",
            new String[]{"Trump Era"}
    );
    static President andrewCuomo = new President(
            "Andrew Cuomo", "Northeast", 3, 5, 1, 6, "Progressive", "Regulation",
            new String[]{"Trump Era"}
    );
    static President williamCushing = new President(
            "William Cushing", "Northeast", 5, 1, 5, 2, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President johnBlairJr = new President(
            "John Blair Jr.", "South", 3, 2, 6, 2, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President paulHamilton = new President(
            "Paul Hamilton", "South", 2, 1, 3, 1, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President philipReed = new President(
            "Philip Reed", "Northeast", 4, 1, 2, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President johnArmstrongJr = new President(
            "John Armstrong Jr.", "Northeast", 3, 1, 1, 1, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President williamLowndes = new President(
            "William Lowndes", "South", 3, 3, 5, 2, "Libertarian", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President bushrodWashington = new President(
            "Bushrod Washington", "South", 5, 2, 3, 3, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President thomasFitzsimons = new President(
            "Thomas Fitzsimons", "Northeast", 2, 2, 2, 1, "Conservative", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President jonathanTrumbullJr = new President(
            "Jonathan Trumbull Jr.", "Northeast", 6, 3, 1, 4, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President williamEustis = new President(
            "William Eustis", "Northeast", 4, 1, 1, 2, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President williamJones = new President(
            "William Jones", "Northeast", 2, 1, 2, 1, "Libertarian", "Central Bank",
            new String[]{"Founding Era"}
    );
    static President matthewClay = new President(
            "Matthew Clay", "South", 5, 2, 1, 1, "Libertarian", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President thomasJohnson = new President(
            "Thomas Johnson", "Northeast", 2, 1, 4, 2, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President alfredMoore = new President(
            "Alfred Moore", "South", 3, 1, 1, 1, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President isaacShelby = new President(
            "Isaac Shelby", "South", 3, 5, 1, 2, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President stephenRBradley = new President(
            "Stephen R. Bradley", "Northeast", 5, 2, 3, 2, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President johnSevier = new President(
            "John Sevier", "South", 5, 2, 3, 1, "Libertarian", "Imperialism",
            new String[]{"Founding Era"}
    );
    static President michaelLeib = new President(
            "Michael Leib", "Northeast", 4, 2, 1, 1, "Libertarian", "Deregulation",
            new String[]{"Founding Era"}
    );
    static President mehmetOz = new President(
            "Mehmet Oz", "Northeast", 1, 1, 1, 4, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President thomasMassie = new President(
            "Thomas Massie", "South", 3, 2, 4, 1, "Libertarian", "Constructionism",
            new String[]{"Trump Era"}
    );
    static President katieBritt = new President(
            "Katie Britt", "South", 2, 4, 2, 1, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President chuckSchumer = new President(
            "Chuck Schumer", "Northeast", 8, 1, 5, 4, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President kathyHochul = new President(
            "Kathy Hochul", "Northeast", 4, 1, 2, 2, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President alexandriaOcasioCortez = new President(
            "Alexandria Ocasio-Cortez", "Northeast", 2, 4, 1, 5, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President tuckerCarlson = new President(
            "Tucker Carlson", "Northeast", 1, 6, 1, 6, "Populist", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President marjorieTaylorGreene = new President(
            "Marjorie Taylor Greene", "South", 2, 2, 1, 4, "Conservative", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President timScott = new President(
            "Tim Scott", "South", 4, 4, 4, 3, "Conservative", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President peteButtigieg = new President(
            "Pete Buttigieg", "Midwest", 2, 3, 5, 3, "Progressive", "Internal Improvements",
            new String[]{"Trump Era"}
    );
    static President tulsiGabbard = new President(
            "Tulsi Gabbard", "West", 2, 5, 2, 3, "Populist", "Isolationism",
            new String[]{"Trump Era"}
    );
    static President joeManchin = new President(
            "Joe Manchin", "Midwest", 7, 2, 1, 3, "Populist", "Deregulation",
            new String[]{"Trump Era"}
    );
    static President brianKemp = new President(
            "Brian Kemp", "South", 4, 4, 3, 4, "Conservative", "Establishment",
            new String[]{"Trump Era"}
    );
    static President brettFavre = new President(
            "Brett Favre", "South", 1, 1, 1, 5, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President elonMusk = new President(
            "Elon Musk", "South", 1, 2, 6, 8, "Libertarian", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President gavinNewsom = new President(
            "Gavin Newsom", "West", 3, 6, 3, 4, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President leBronJames = new President(
            "LeBron James", "Midwest", 1, 2, 1, 9, "Progressive", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President raphaelWarnock = new President(
            "Raphael Warnock", "South", 2, 6, 2, 3, "Progressive", "Egalitarianism",
            new String[]{"Trump Era"}
    );   
    static President seanPatrickMaloney = new President(
            "Sean Patrick Maloney", "Northeast", 3, 1, 1, 1, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President johnFetterman = new President(
            "John Fetterman", "Midwest", 2, 3, 1, 2, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President joshShapiro = new President(
            "Josh Shapiro", "Midwest", 2, 4, 3, 2, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President mikeLawler = new President(
            "Mike Lawler", "Northeast", 2, 3, 2, 1, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President georgeSantos = new President(
            "George Santos", "Northeast", 2, 1, 1, 3, "Conservative", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President susanCollins = new President(
            "Susan Collins", "Northeast", 7, 2, 2, 2, "Conservative", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President cherylJohnson = new President(
            "Cheryl Johnson", "Northeast", 10, 10, 1, 1, "Progressive", "Law and Order",
            new String[]{"Meme Card"}
    );
    static President mikeLee = new President(
            "Mike Lee", "West", 4, 1, 5, 2, "Libertarian", "Limited Government",
            new String[]{"Trump Era"}
    );
    static President jdVance = new President(
            "J.D. Vance", "West", 4, 2, 8, 4, "Populist", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President kevinMcCarthy = new President(
            "Kevin McCarthy", "West", 8, 1, 4, 4, "Conservative", "Establishment",
            new String[]{"Trump Era"}
    );
    static President nancyPelosi = new President(
            "Nancy Pelosi", "West", 9, 1, 4, 5, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President philMurphy = new President(
            "Phil Murphy", "Northeast", 3, 1, 3, 2, "Progressive", "Regulation",
            new String[]{"Trump Era"}
    );
    static President dickDurbin = new President(
            "Dick Durbin", "Midwest", 8, 1, 3, 2, "Progressive", "Open Borders",
            new String[]{"Trump Era"}
    );
    static President alexanderConteeHanson = new President(
            "Alexander Contee Hanson", "Northeast", 2, 2, 1, 1, "Conservative", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President charlesFMercer = new President(
            "Charles F. Mercer", "South", 6, 3, 2, 2, "Conservative", "Internal Improvements",
            new String[]{"Founding Era"}
    );
    static President johnScott = new President(
            "John Scott", "South", 3, 1, 1, 1, "Conservative", "Internal Improvements",
            new String[]{"Founding Era"}
    );
    static President jesseBThomas = new President(
            "Jesse B. Thomas", "Midwest", 3, 1, 5, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President alexanderJDallas = new President(
            "Alexander J. Dallas", "Northeast", 2, 1, 6, 3, "Conservative", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President williamWirt = new President(
            "William Wirt", "South", 4, 5, 2, 2, "Conservative", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President richardRush = new President(
            "Richard Rush", "Northeast", 2, 4, 5, 3, "Libertarian", "Imperialism",
            new String[]{"Jacksonian Era"}
    );
    static President johnAndrewShulze = new President(
            "John Andrew Shulze", "Northeast", 3, 1, 4, 2, "Libertarian", "Internal Improvements",
            new String[]{"Jacksonian Era"}
    );
    static President nathanSanford = new President(
            "Nathan Sanford", "Northeast", 4, 3, 3, 2, "Conservative", "Internal Improvements",
            new String[]{"Jacksonian Era"}
    );
    static President francisPrestonBlair = new President(
            "Francis Preston Blair", "South", 1, 6, 2, 6, "Populist", "Egalitarianism",
            new String[]{"Jacksonian Era"}
    );
    static President jamesBrown = new President(
            "James Brown", "South", 4, 2, 1, 2, "Libertarian", "Racism",
            new String[]{"Jacksonian Era"}
    );
    static President samuelLSouthard = new President(
            "Samuel L. Southard", "Northeast", 5, 2, 4, 2, "Conservative", "Imperialism",
            new String[]{"Jacksonian Era"}
    );
    static President smithThompson = new President(
            "Smith Thompson", "Northeast", 5, 1, 4, 3, "Conservative", "Civil Rights",
            new String[]{"Jacksonian Era"}
    );
    static President solomonSouthwick = new President(
            "Solomon Southwick", "Northeast", 2, 4, 1, 2, "Conservative", "Anti-Establishment",
            new String[]{"Jacksonian Era"}
    );
    static President williamAPalmer = new President(
            "William A. Palmer", "Northeast", 4, 4, 1, 3, "Conservative", "Anti-Establishment",
            new String[]{"Jacksonian Era"}
    );
    static President thomasHartBenton = new President(
            "Thomas Hart Benton", "South", 7, 5, 3, 4, "Libertarian", "Imperialism",
            new String[]{"Jacksonian Era"}
    );
    static President georgeWolf = new President(
            "George Wolf", "Northeast", 5, 3, 3, 2, "Populist", "Social Programs",
            new String[]{"Jacksonian Era"}
    );
    static President andrewStevenson = new President(
            "Andrew Stevenson", "South", 6, 1, 1, 3, "Populist", "Anti-Establishment",
            new String[]{"Jacksonian Era"}
    );
    static President johnSergeant = new President(
            "John Sergeant", "Northeast", 3, 4, 4, 3, "Conservative", "Civil Rights",
            new String[]{"Jacksonian Era"}
    );
    static President johnMcLean = new President(
            "John McLean", "Midwest", 5, 4, 5, 4, "Conservative", "Civil Rights",
            new String[]{"Jacksonian Era"}
    );
    static President rufusChoate = new President(
            "Rufus Choate", "Northeast", 2, 7, 2, 2, "Conservative", "Centralization",
            new String[]{"Jacksonian Era"}
    );
    static President richardMJohnson = new President(
            "Richard M. Johnson", "South", 8, 1, 3, 4, "Populist", "Social Programs",
            new String[]{"Jacksonian Era"}
    );
    static President williamCabellRives = new President(
            "William Cabell Rives", "South", 5, 3, 3, 1, "Populist", "States' Rights",
            new String[]{"Jacksonian Era"}
    );
    static President johnMBerrien = new President(
            "John M. Berrien", "South", 4, 2, 3, 1, "Populist", "Racism",
            new String[]{"Jacksonian Era"}
    );
    static President francisGranger = new President(
            "Francis Granger", "Northeast", 2, 4, 3, 4, "Conservative", "Limited Government",
            new String[]{"Jacksonian Era"}
    );
    static President hughLWhite = new President(
            "Hugh L. White", "South", 5, 3, 6, 4, "Libertarian", "Constructionism",
            new String[]{"Jacksonian Era"}
    );
    static President willieMagnum = new President(
            "Willie Magnum", "South", 5, 3, 4, 2, "Libertarian", "Free Trade",
            new String[]{"Jacksonian Era"}
    );
    static President williamSmith = new President(
            "William Smith", "South", 3, 2, 2, 3, "Populist", "States' Rights",
            new String[]{"Jacksonian Era"}
    );
    static President felixGrundy = new President(
            "Felix Grundy", "South", 4, 2, 2, 2, "Populist", "Imperialism",
            new String[]{"Jacksonian Era"}
    );
    static President williamLegett = new President(
            "William Leggett", "Northeast", 1, 4, 1, 3, "Populist", "Laissez-Faire",
            new String[]{"Jacksonian Era"}
    );
    static President robertCWinthrop = new President(
            "Robert C. Winthrop", "Northeast", 3, 2, 2, 2, "Libertarian", "Traditional Morality",
            new String[]{"Jacksonian Era"}
    );
    static President abbotLawrence = new President(
            "Abbot Lawrence", "Northeast", 2, 5, 1, 3, "Conservative", "Internal Improvements",
            new String[]{"Jacksonian Era"}
    );
    static President johnJCrittenden = new President(
            "John J. Crittenden", "South", 8, 5, 3, 4, "Conservative", "Nationalism",
            new String[]{"Jacksonian Era"}
    );
    static President georgeMcDuffie = new President(
            "George McDuffie", "South", 5, 4, 1, 2, "Populist", "Free Trade",
            new String[]{"Jacksonian Era"}
    );
    static President littletonWTazewell = new President(
            "Littleton W. Tazewell", "South", 4, 2, 2, 1, "Populist", "Racism",
            new String[]{"Jacksonian Era"}
    );
    static President johnCatron = new President(
            "John Catron", "South", 5, 1, 4, 2, "Populist", "Anti-Central Bank",
            new String[]{"Civil War Era"}
    );
    static President kyrstenSinema = new President(
            "Kyrsten Sinema", "West", 4, 1, 2, 3, "Progressive", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President chuckGrassley = new President(
            "Chuck Grassley", "Midwest", 9, 2, 2, 2, "Conservative", "Internal Improvements",
            new String[]{"Trump Era"}
    );
    static President eliseStefanik = new President(
            "Elise Stefanik", "Northeast", 3, 2, 3, 2, "Conservative", "Establishment",
            new String[]{"Trump Era"}
    );
    static President jaredGolden = new President(
            "Jared Golden", "Northeast", 2, 2, 3, 1, "Populist", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President anthonyDevolder = new President(
            "Anthony Devolder", "Northeast", 10, 10, 10, 10, "Conservative", "Nationalism",
            new String[]{"Meme Card"}
    );
    static President scottWalker = new President(
            "Scott Walker", "Midwest", 4, 2, 5, 2, "Conservative", "Deregulation",
            new String[]{"Modern Era"}
    );
    static President johnPKennedy = new President(
            "John P. Kennedy", "Northeast", 2, 4, 1, 2, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President charlesFrancisAdamsSr = new President(
            "Charles Francis Adams Sr.", "Northeast", 2, 1, 4, 3, "Conservative", "Isolationism",
            new String[]{"Civil War Era"}
    );
    static President benjaminRobbinsCurtis = new President(
            "Benjamin Robbins Curtis", "Northeast", 3, 3, 3, 3, "Conservative", "Constructionism",
            new String[]{"Civil War Era"}
    );
    static President robertYHayne = new President(
            "Robert Y. Hayne", "South", 3, 4, 5, 4, "Populist", "Free Trade",
            new String[]{"Jacksonian Era"}
    );
    static President amosKendall = new President(
            "Amos Kendall", "South", 1, 4, 4, 4, "Populist", "Anti-Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President rogerBTaney = new President(
            "Roger B. Taney", "Northeast", 7, 1, 5, 6, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President mikePompeo = new President(
            "Mike Pompeo", "West", 3, 2, 5, 2, "Conservative", "Imperialism",
            new String[]{"Trump Era"}
    );
    static President jaredKushner = new President(
            "Jared Kushner", "Northeast", 1, 1, 5, 4, "Conservative", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President mattGaetz = new President(
            "Matt Gaetz", "South", 2, 2, 1, 3, "Libertarian", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President amyKlobuchar = new President(
            "Amy Klobuchar", "Midwest", 6, 2, 4, 2, "Progressive", "Regulation",
            new String[]{"Trump Era"}
    );
    static President ilhanOmar = new President(
            "Ilhan Omar", "Midwest", 2, 2, 1, 3, "Progressive", "Isolationism",
            new String[]{"Trump Era"}
    );
    static President janetYellen = new President(
            "Janet Yellen", "Northeast", 2, 1, 3, 2, "Progressive", "Inflation",
            new String[]{"Trump Era"}
    );
    static President theodoreFreling = new President(
            "Theodore Frelinghuysen", "Northeast", 3, 4, 3, 2, "Conservative", "Traditional Morality",
            new String[]{"Jacksonian Era"}
    );
    static President johnDavis = new President(
            "John Davis", "Northeast", 6, 4, 1, 1, "Conservative", "Tariffs",
            new String[]{"Jacksonian Era"}
    );
    static President johnMClayton = new President(
            "John M. Clayton", "Northeast", 4, 2, 3, 1, "Conservative", "Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President georgeMDallas = new President(
            "George M. Dallas", "Northeast", 4, 3, 3, 4, "Populist", "Free Trade",
            new String[]{"Jacksonian Era"}
    );
    static President silasWright = new President(
            "Silas Wright", "South", 5, 5, 3, 2, "Populist", "Anti-Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President johnFairfield = new President(
            "John Fairfield", "Northeast", 5, 1, 2, 3, "Populist", "Anti-Establishment",
            new String[]{"Jacksonian Era"}
    );
    static President georgeEvans = new President(
            "George Evans", "Northeast", 4, 1, 3, 1, "Conservative", "Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President williamCPreston = new President(
            "William C. Preston", "South", 3, 1, 2, 1, "Populist", "Racism",
            new String[]{"Jacksonian Era"}
    );
    static President stephenDMiller = new President(
            "Stephen D. Miller", "South", 2, 2, 1, 1, "Populist", "Free Trade",
            new String[]{"Jacksonian Era"}
    );
    static President gulianGVerplanck = new President(
            "Gulian G. Verplanck", "Northeast", 2, 1, 3, 1, "Conservative", "Free Trade",
            new String[]{"Jacksonian Era"}
    );
    static President louisMcLane = new President(
            "Louis McLane", "Northeast", 5, 4, 5, 1, "Conservative", "Anti-Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President georgePoindexter = new President(
            "George Poindexter", "South", 4, 1, 3, 2, "Populist", "Social Programs",
            new String[]{"Jacksonian Era"}
    );
    static President thomasEwing = new President(
            "Thomas Ewing", "Midwest", 3, 1, 2, 1, "Conservative", "Establishment",
            new String[]{"Jacksonian Era"}
    );
    static President williamWEllsworth = new President(
            "William W. Ellsworth", "Northeast", 3, 2, 2, 3, "Conservative", "Centralization",
            new String[]{"Jacksonian Era"}
    );
    static President benjaminWLeigh = new President(
            "Benjamin W. Leigh", "South", 2, 1, 1, 1, "Conservative", "Racism",
            new String[]{"Jacksonian Era"}
    );
    static President lewisCass = new President(
            "Lewis Cass", "Midwest", 6, 2, 6, 3, "Populist", "States' Rights",
            new String[]{"Jacksonian Era"}
    );
    static President leviWoodbury = new President(
            "Levi Woodbury", "Northeast", 6, 2, 5, 3, "Libertarian", "Constructionism",
            new String[]{"Jacksonian Era"}
    );
    static President williamOButler = new President(
            "William O. Butler", "South", 2, 3, 2, 3, "Populist", "Imperialism",
            new String[]{"Jacksonian Era"}
    );
    static President ninaTurner = new President(
            "Nina Turner", "Midwest", 1, 1, 1, 1, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President jaredPolis = new President(
            "Jared Polis", "West", 4, 5, 3, 2, "Libertarian", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President chrisSununu = new President(
            "Chris Sununu", "Northeast", 4, 4, 4, 2, "Libertarian", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President philScott = new President(
            "Phil Scott", "Northeast", 4, 3, 3, 2, "Progressive", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President charlieBaker = new President(
            "Charlie Baker", "Northeast", 4, 2, 4, 2, "Progressive", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President andyBeshear = new President(
            "Andy Beshear", "South", 3, 3, 2, 2, "Progressive", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President johnBelEdwards = new President(
            "John Bel Edwards", "South", 5, 2, 3, 2, "Populist", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President lauraKelly = new President(
            "Laura Kelly", "West", 3, 2, 4, 1, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President nikkiHaley = new President(
            "Nikki Haley", "South", 3, 5, 4, 2, "Conservative", "Nationalism",
            new String[]{"Trump Era"}
    );
    static President coreyStapleton = new President(
            "Corey Stapleton", "West", 1, 1, 1, 1, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President kariLake = new President(
            "Kari Lake", "West", 1, 3, 1, 3, "Conservative", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President markKelly = new President(
            "Mark Kelly", "West", 2, 4, 1, 3, "Progressive", "Implied Powers",
            new String[]{"Trump Era"}
    );
    static President katieHobbs = new President(
            "Katie Hobbs", "West", 3, 1, 4, 2, "Progressive", "Establishment",
            new String[]{"Trump Era"}
    );
    static President michelleObama = new President(
            "Michelle Obama", "Midwest", 1, 6, 1, 7, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President williamMarbury = new President(
            "William Marbury", "Northeast", 1, 1, 1, 4, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President richardStockton = new President(
            "Richard Stockton", "Northeast", 3, 2, 2, 2, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President williamLSmith = new President(
            "William L. Smith", "South", 3, 2, 4, 3, "Conservative", "Racism",
            new String[]{"Founding Era"}
    );
    static President williamClark = new President(
            "William Clark", "South", 3, 2, 1, 5, "Libertarian", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President davidHolmes = new President(
            "David Holmes", "South", 5, 3, 1, 1, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President caeserAugustusRodney = new President(
            "Caeser Augustus Rodney", "Northeast", 2, 3, 3, 3, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President gaylordGriswold = new President(
            "Gaylord Griswold", "Northeast", 2, 1, 2, 1, "Conservative", "Establishment",
            new String[]{"Founding Era"}
    );
    static President johnCottonSmith = new President(
            "John Cotton Smith", "Northeast", 4, 1, 1, 1, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President sethHastings = new President(
            "Seth Hastings", "Northeast", 2, 2, 1, 1, "Conservative", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President johnClopton = new President(
            "John Clopton", "South", 5, 2, 3, 1, "Libertarian", "Anti-Central Bank",
            new String[]{"Founding Era"}
    );
    static President johnSwanwick = new President(
            "John Swanwick", "Northeast", 2, 1, 3, 2, "Libertarian", "Free Trade",
            new String[]{"Founding Era"}
    );
    static President robertSmith = new President(
            "Robert Smith", "Northeast", 2, 2, 1, 2, "Libertarian", "Establishment",
            new String[]{"Founding Era"}
    );
    static President vivekRamaswamy = new President(
            "Vivek Ramaswamy", "Midwest", 1, 4, 4, 2, "Libertarian", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President jamesBuckley = new President(
            "James Buckley", "Northeast", 3, 2, 5, 3, "Conservative", "Traditional Morality",
            new String[]{"Civil Rights Era"}
    );
    static President johnRBrinkley = new President(
            "John R. Brinkley", "West", 1, 3, 1, 1, "Populist", "Internal Improvements",
            new String[]{"New Deal Era"}
    );
    static President marianneWilliamson = new President(
            "Marianne Williamson", "West", 1, 3, 1, 2, "Progressive", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President rufus = new President(
            "Rufus", "South", 1, 1, 10, 1, "Conservative", "Racism",
            new String[]{"Meme Card"}
    );
    static President phyllisSchlafy = new President(
            "Phyllis Schlafy", "South", 1, 4, 4, 4, "Conservative", "Traditional Morality",
            new String[]{"Civil Rights Era"}
    );
    static President larryHogan = new President(
            "Larry Hogan", "Northeast", 4, 4, 3, 1, "Conservative", "Establishment",
            new String[]{"Trump Era"}
    ); 
    static President blancheLincoln = new President(
            "Blanche Lincoln", "South", 4, 2, 2, 1, "Populist", "Internal Improvements",
            new String[]{"Modern Era"}
    );
    static President kristiNoem = new President(
            "Kristi Noem", "West", 5, 4, 2, 2, "Conservative", "Deregulation",
            new String[]{"Trump Era"}
    );
    static President lizCheney = new President(
            "Liz Cheney", "West", 2, 2, 2, 3, "Conservative", "Establishment",
            new String[]{"Trump Era"}
    );
    static President kimReynolds = new President(
            "Kim Reynolds", "Midwest", 4, 4, 4, 1, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President andrewYang = new President(
            "Andrew Yang", "Northeast", 1, 2, 5, 2, "Populist", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President betoORourke = new President(
            "Beto O'Rourke", "South", 2, 5, 2, 3, "Progressive", "Implied Powers",
            new String[]{"Trump Era"}
    );
    static President bennieThompson = new President(
            "Bennie Thompson", "South", 7, 1, 2, 2, "Progressive", "Establishment",
            new String[]{"Trump Era"}
    );
    static President gregAbbott = new President(
            "Greg Abbott", "South", 5, 3, 4, 3, "Conservative", "Closed Borders",
            new String[]{"Trump Era"}
    );
    static President blakeMasters = new President(
            "Blake Masters", "West", 1, 1, 4, 1, "Libertarian", "Closed Borders",
            new String[]{"Trump Era"}
    );
    static President tomCotton = new President(
            "Tom Cotton", "Midwest", 5, 3, 4, 2, "Conservative", "Imperialism",
            new String[]{"Trump Era"}
    );
    static President wesMoore = new President(
            "Wes Moore", "Northeast", 2, 2, 3, 1, "Progressive", "Regulation",
            new String[]{"Trump Era"}
    );
    static President jonOssoff = new President(
            "Jon Ossoff", "South", 2, 5, 4, 2, "Progressive", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President kirstenGillibrand = new President(
            "Kirsten Gillibrand", "Northeast", 5, 2, 2, 1, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President kellyAyotte = new President(
            "Kelly Ayotte", "Northeast", 3, 3, 3, 1, "Conservative", "Imperialism",
            new String[]{"Modern Era"}
    );
    static President nathanielPTallmadge = new President(
            "Nathaniel P. Tallmadge", "Midwest", 4, 3, 1, 2, "Conservative", "Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President nicholasBiddle = new President(
            "Nicholas Biddle", "Northeast", 2, 1, 5, 5, "Conservative", "Central Bank",
            new String[]{"Jacksonian Era"}
    );
    static President johnGPalfrey = new President(
            "John G. Palfrey", "Northeast", 2, 3, 1, 1, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President johnBranch = new President(
            "John Branch", "South", 2, 1, 2, 1, "Populist", "Imperialism",
            new String[]{"Jacksonian Era"}
    );
    static President jamesHHammond = new President(
            "James H. Hammond", "South", 3, 2, 2, 3, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President williamLMarcy = new President(
            "William L. Marcy", "Northeast", 7, 2, 3, 3, "Populist", "Imperialism",
            new String[]{"Civil War Era"}
    );
    static President thomasCorwin = new President(
            "Thomas Corwin", "Midwest", 5, 6, 2, 3, "Conservative", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President hannibalHamlin = new President(
            "Hannibal Hamlin", "Northeast", 9, 2, 3, 3, "Libertarian", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President edwardEverett = new President(
            "Edward Everett", "Northeast", 4, 6, 2, 1, "Conservative", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President jeffersonDavis = new President(
            "Jefferson Davis", "South", 4, 5, 5, 7, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President robertBRhett = new President(
            "Robert B. Rhett", "South", 3, 5, 3, 5, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President josephLane = new President(
            "Joseph Lane", "South", 2, 3, 3, 1, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President salmonPChase = new President(
            "Salmon P. Chase", "Midwest", 7, 1, 6, 6, "Libertarian", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President simonCameron = new President(
            "Simon Cameron", "Northeast", 5, 2, 6, 2, "Libertarian", "Internal Improvements",
            new String[]{"Civil War Era"}
    );
    static President williamTSherman = new President(
            "William T. Sherman", "Northeast", 1, 6, 1, 8, "Conservative", "Imperialism",
            new String[]{"Civil War Era"}
    );
    static President stonewallJackson = new President(
            "Stonewall Jackson", "South", 1, 6, 1, 8, "Populist", "Imperialism",
            new String[]{"Civil War Era"}
    );
    static President nathanBedfordForrest = new President(
            "Nathan Bedford Forrest", "South", 1, 5, 1, 5, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President johnAdamsDix = new President(
            "John Adams Dix", "Northeast", 2, 2, 1, 2, "Conservative", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President edwardBates = new President(
            "Edward Bates", "South", 2, 3, 4, 1, "Conservative", "Law and Order",
            new String[]{"Civil War Era"}
    );
    static President thurlowWeed = new President(
            "Thurlow Weed", "Northeast", 1, 5, 2, 5, "Conservative", "Establishment",
            new String[]{"Civil War Era"}
    );
    static President cassiusMClay = new President(
            "Cassius M. Clay", "South", 2, 4, 3, 1, "Conservative", "Imperialism",
            new String[]{"Civil War Era"}
    );
    static President williamLowndesYancy = new President(
            "William Lowndes Yancy", "South", 2, 6, 2, 3, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President jamesGuthrie = new President(
            "James Guthrie", "South", 2, 2, 7, 1, "Conservative", "Internal Improvements",
            new String[]{"Civil War Era"}
    );
    static President robertMTHunter = new President(
            "Robert M.T. Hunter", "South", 7, 1, 3, 3, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President alDAmato = new President(
            "Al D'Amato", "Northeast", 5, 2, 4, 1, "Conservative", "Internal Improvements",
            new String[]{"Reagan Era"}
    );
    static President jesseHelms = new President(
            "Jesse Helms", "South", 7, 3, 4, 3, "Conservative", "Traditional Morality",
            new String[]{"Reagan Era"}
    );
    static President robertHMichel = new President(
            "Robert H. Michel", "Midwest", 7, 2, 1, 1, "Conservative", "Establishment",
            new String[]{"Reagan Era"}
    );
    static President robertByrd = new President(
            "Robert Byrd", "Midwest", 9, 2, 5, 5, "Populist", "Internal Improvements",
            new String[]{"Reagan Era"}
    );
    static President johnCStennis = new President(
            "John C. Stennis", "South", 8, 2, 2, 2, "Populist", "Imperialism",
            new String[]{"Reagan Era"}
    );
    static President tipONeill = new President(
            "Tip O'Neill", "Northeast", 7, 4, 2, 5, "Progressive", "Social Programs",
            new String[]{"Reagan Era"}
    );
    static President marshaBlackburn = new President(
            "Marsha Blackburn", "South", 6, 3, 2, 2, "Conservative", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President larryElder = new President(
            "Larry Elder", "West", 1, 4, 2, 2, "Conservative", "Tax Cuts",
            new String[]{"Trump Era"}
    );
    static President dougBurgum = new President(
            "Doug Burgum", "West", 3, 1, 2, 1, "Conservative", "Regulation",
            new String[]{"Trump Era"}
    );
    static President garyPeters = new President(
            "Gary Peters", "Midwest", 6, 1, 2, 1, "Progressive", "Establishment",
            new String[]{"Trump Era"}
    );
    static President royCooper = new President(
            "Roy Cooper", "South", 5, 4, 4, 2, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President maggieHassan = new President(
            "Maggie Hassan", "Northeast", 5, 2, 3, 1, "Progressive", "Social Liberalism",
            new String[]{"Trump Era"}
    );
    static President johnKennedy = new President(
            "John Kennedy", "South", 3, 5, 2, 2, "Conservative", "Law and Order",
            new String[]{"Trump Era"}
    );
    static President lindseyGraham = new President(
            "Lindsey Graham", "South", 6, 1, 3, 3, "Conservative", "Law and Order",
            new String[]{"Trump Era"}
    );
    static President donBolduc = new President(
            "Don Bolduc", "Northeast", 1, 2, 1, 1, "Conservative", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President robertFKennedyJr = new President(
            "Robert F. Kennedy Jr.", "Northeast", 1, 4, 3, 6, "Populist", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President jayInslee = new President(
            "Jay Inslee", "West", 7, 2, 1, 2, "Progressive", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President maxwellFrost = new President(
            "Maxwell Frost", "South", 2, 2, 1, 1, "Progressive", "Class Unity",
            new String[]{"Trump Era"}
    );
    static President markHanna = new President(
            "Mark Hanna", "Midwest", 3, 6, 1, 5, "Conservative", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President harrySNew = new President(
            "Harry S. New", "Midwest", 4, 3, 1, 1, "Progressive", "Civil Liberties",
            new String[]{"Progressive Era"}
    );
    static President philanderCKnox = new President(
            "Philander C. Knox", "Northeast", 5, 4, 3, 3, "Conservative", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President williamRandolphHearst = new President(
            "William Randolph Hearst", "Northeast", 2, 7, 1, 6, "Progressive", "Class Unity",
            new String[]{"Progressive Era"}
    );
    static President francisCockrell = new President(
            "Francis Cockrell", "South", 7, 3, 1, 1, "Populist", "Inflation",
            new String[]{"Progressive Era"}
    );
    static President georgebMcClellanJr = new President(
            "George B. McClellan Jr.", "Northeast", 3, 1, 1, 3, "Progressive", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President charlesEvansHughes = new President(
            "Charles Evans Hughes", "Northeast", 7, 4, 5, 5, "Progressive", "Tax Cuts",
            new String[]{"Progressive Era"}
    );
    static President hiramJohnson = new President(
            "Hiram Johnson", "West", 6, 4, 3, 4, "Progressive", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President nelsonwAldrich = new President(
            "Nelson W. Aldrich", "Northeast", 7, 1, 7, 5, "Conservative", "Tariffs",
            new String[]{"Progressive Era"}
    );
    static President edithWilson = new President(
            "Edith Wilson", "Northeast", 1, 4, 6, 3, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President thomasRMarshall = new President(
            "Thomas R. Marshall", "Midwest", 7, 6, 1, 3, "Progressive", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President champClark = new President(
            "Champ Clark", "South", 8, 3, 4, 5, "Progressive", "Anti-Central Bank",
            new String[]{"Progressive Era"}
    );
    static President elihuRoot = new President(
            "Elihu Root", "Northeast", 6, 1, 7, 2, "Conservative", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President nicholasMButler = new President(
            "Nicholas M. Butler", "Northeast", 1, 4, 1, 4, "Conservative", "Racism",
            new String[]{"Progressive Era"}
    );
    static President albertBCummins = new President(
            "Albert B. Cummins", "Northeast", 5, 4, 4, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President judsonHarmon = new President(
            "Judson Harmon", "Midwest", 3, 4, 2, 1, "Libertarian", "Social Hierarchy",
            new String[]{"Progressive Era"}
    );
    static President oscarUnderwood = new President(
            "Oscar Underwood", "South", 8, 2, 5, 5, "Progressive", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President johnAlbertJohnson = new President(
            "John Albert Johnson", "Midwest", 2, 4, 1, 1, "Progressive", "Internal Improvements",
            new String[]{"Progressive Era"}
    );
    static President henryCabotLodge = new President(
            "Henry Cabot Lodge", "Northeast", 7, 2, 6, 5, "Conservative", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President josephGurneyCannon = new President(
            "Joseph Gurney Cannon", "Midwest", 9, 1, 6, 4, "Conservative", "Limited Government",
            new String[]{"Progressive Era"}
    );
    static President curtisGuildJr = new President(
            "Curtis Guild Jr.", "Northeast", 2, 2, 4, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President charlesWBryan = new President(
            "Charles W. Bryan", "West", 3, 2, 1, 4, "Populist", "Inflation",
            new String[]{"Progressive Era"}
    );
    static President edwardCWall = new President(
            "Edward C. Wall", "Midwest", 1, 2, 1, 1, "Libertarian", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President robertLansing = new President(
            "Robert Lansing", "Northeast", 4, 2, 5, 2, "Libertarian", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President andrewMellon = new President(
            "Andrew Mellon", "Northeast", 3, 1, 8, 5, "Libertarian", "Tax Cuts",
            new String[]{"Progressive Era"}
    );
    static President orvilleHPlatt = new President(
            "Orville H. Platt", "Northeast", 5, 2, 4, 3, "Conservative", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President williamBAllison = new President(
            "William B. Allison", "Northeast", 7, 3, 4, 4, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President johnCoitSpooner = new President(
            "John Coit Spooner", "Midwest", 5, 4, 3, 3, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamAllen = new President(
            "William Allen", "Midwest", 4, 3, 2, 2, "Populist", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President jamesBroadhead = new President(
            "James Broadhead", "South", 2, 1, 1, 2, "Populist", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President johnKelly = new President(
            "John Kelly", "Northeast", 2, 5, 1, 3, "Libertarian", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President johnSherman = new President(
            "John Sherman", "Midwest", 8, 1, 6, 4, "Conservative", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President benjaminBristow = new President(
            "Benjamin Bristow", "South", 2, 5, 1, 3, "Conservative", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President williamTweed = new President(
            "William Tweed", "Northeast", 2, 6, 1, 5, "Populist", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President lukePBlackburn = new President(
            "Luke P. Blackburn", "South", 2, 2, 1, 3, "Progressive", "Civil Liberties",
            new String[]{"Reconstruction Era"}
    );
    static President williamGaston = new President(
            "William Gaston", "Northeast", 2, 3, 1, 1, "Populist", "Civil Liberties",
            new String[]{"Reconstruction Era"}
    );
    static President roscoeconkling = new President(
            "Roscoe Conkling", "Northeast", 6, 4, 2, 5, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williammevarts = new President(
            "William M. Evarts", "Northeast", 4, 4, 2, 3, "Conservative", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President charlesdevens = new President(
            "Charles Devens", "Northeast", 2, 2, 1, 1, "Conservative", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President michaelckerr = new President(
            "Michael C. Kerr", "Northeast", 4, 1, 3, 2, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President johnwstevenson = new President(
            "John W. Stevenson", "South", 5, 2, 6, 2, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President luciusqclamar = new President(
            "Lucius Q. C. Lamar", "South", 5, 4, 2, 4, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President jamesburrillangell = new President(
            "James Burrill Angell", "Midwest", 1, 1, 2, 1, "Progressive", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President stephenbpackard = new President(
            "Stephen B. Packard", "South", 2, 1, 1, 2, "Conservative", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President hermanlhumphrey = new President(
            "Herman L. Humphrey", "Midwest", 2, 1, 2, 1, "Libertarian", "Limited Government",
            new String[]{"Reconstruction Era"}
    );
    static President richardpbland = new President(
            "Richard P. Bland", "South", 6, 4, 3, 4, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President wadehamptoniii = new President(
            "Wade Hampton III", "South", 5, 4, 1, 4, "Libertarian", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President williamkimmel = new President(
            "William Kimmel", "Northeast", 2, 1, 2, 1, "Populist", "Limited Government",
            new String[]{"Reconstruction Era"}
    );
    static President elihuwashburne = new President(
            "Elihu Washburne", "Midwest", 4, 3, 1, 2, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President georgefedmunds = new President(
            "George F. Edmunds", "Northeast", 7, 2, 4, 4, "Conservative", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President marshalljewell = new President(
            "Marshall Jewell", "Northeast", 3, 3, 2, 1, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President thomasfbayard = new President(
            "Thomas F. Bayard", "Northeast", 7, 2, 4, 5, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President samueljrandall = new President(
            "Samuel J. Randall", "Northeast", 7, 1, 5, 4, "Populist", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President henrybpayne = new President(
            "Henry B. Payne", "Midwest", 3, 2, 4, 2, "Libertarian", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President alonzobcornell = new President(
            "Alonzo B. Cornell", "Northeast", 2, 1, 2, 3, "Progressive", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President henrywblair = new President(
            "Henry W. Blair", "Northeast", 5, 2, 4, 2, "Conservative", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President frederickwseward = new President(
            "Frederick W. Seward", "Northeast", 1, 1, 2, 3, "Conservative", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President henrymmathews = new President(
            "Henry M. Mathews", "Midwest", 2, 5, 1, 2, "Libertarian", "Active Executive",
            new String[]{"Reconstruction Era"}
    );
    static President williamrallsmorrison = new President(
            "William Ralls Morrison", "Midwest", 5, 1, 3, 1, "Populist", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President stephenjfield = new President(
            "Stephen J. Field", "West", 5, 1, 6, 2, "Libertarian", "Open Borders",
            new String[]{"Reconstruction Era"}
    );
    static President williamwindom = new President(
            "William Windom", "Midwest", 4, 1, 3, 1, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President roberttoddlincoln = new President(
            "Robert Todd Lincoln", "Midwest", 2, 3, 2, 6, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President thomaslemueljames = new President(
            "Thomas Lemuel James", "Northeast", 2, 1, 3, 1, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President alcibiadesdeblanc = new President(
            "Alcibiades DeBlanc", "South", 1, 3, 1, 1, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President ishamgharris = new President(
            "Isham G. Harris", "South", 7, 2, 3, 3, "Libertarian", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President jamesmsmith = new President(
            "James M. Smith", "South", 3, 1, 3, 1, "Populist", "Tax Cuts",
            new String[]{"Reconstruction Era"}
    );
    static President fredericktfrelinghuysen = new President(
            "Frederick T. Frelinghuysen", "Northeast", 6, 1, 3, 3, "Progressive", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President charlesjfolger = new President(
            "Charles J. Folger", "Northeast", 2, 1, 3, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President benjaminhbrewster = new President(
            "Benjamin H. Brewster", "Northeast", 2, 2, 1, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President johnmstone = new President(
            "John M. Stone", "South", 5, 1, 4, 1, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President georgeshouston = new President(
            "George S. Houston", "South", 4, 3, 1, 2, "Populist", "Active Executive",
            new String[]{"Reconstruction Era"}
    );
    static President zebulonvance = new President(
            "Zebulon Vance", "South", 6, 4, 2, 4, "Populist", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President johnfhartranft = new President(
            "John F. Hartranft", "Northeast", 3, 3, 2, 2, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President oliverpmorton = new President(
            "Oliver P. Morton", "Midwest", 5, 1, 7, 4, "Progressive", "Active Executive",
            new String[]{"Reconstruction Era"}
    );
    static President charlesjguiteau = new President(
            "Charles J. Guiteau", "Midwest", 1, 1, 1, 4, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President davidmkey = new President(
            "David M. Key", "South", 3, 1, 2, 1, "Populist", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President luciusrobinson = new President(
            "Lucius Robinson", "Northeast", 2, 2, 2, 2, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President jamesaenglish = new President(
            "James A. English", "Northeast", 3, 1, 3, 2, "Libertarian", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President thomaswferry = new President(
            "Thomas W. Ferry", "Midwest", 4, 2, 4, 2, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President henrybanthony = new President(
            "Henry B. Anthony", "Northeast", 6, 3, 2, 2, "Conservative", "Social Hierarchy",
            new String[]{"Reconstruction Era"}
    );
    static President josephrhawley = new President(
            "Joseph R. Hawley", "Northeast", 5, 2, 3, 3, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamawallace = new President(
            "William A. Wallace", "Midwest", 3, 1, 2, 1, "Populist", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President hiesterclymber = new President(
            "Hiester Clymber", "Northeast", 2, 1, 2, 1, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President jcsblackburn = new President(
            "J. C. S. Blackburn", "South", 6, 4, 1, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President williamhrobertson = new President(
            "William H. Robertson", "Northeast", 2, 1, 1, 2, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President thomascplatt = new President(
            "Thomas C. Platt", "Northeast", 4, 5, 1, 3, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamdkelley = new President(
            "William D. Kelley", "Northeast", 5, 3, 3, 2, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President johngcarlisle = new President(
            "John G. Carlisle", "South", 7, 1, 5, 4, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President georgewgeddes = new President(
            "George W. Geddes", "Midwest", 3, 1, 1, 1, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President williamrosecrans = new President(
            "William Rosecrans", "West", 2, 4, 1, 3, "Populist", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President johnfranklinmiller = new President(
            "John Franklin Miller", "West", 2, 1, 4, 2, "Conservative", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President horacefpage = new President(
            "Horace F. Page", "West", 3, 1, 5, 1, "Conservative", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President samueljkirkwood = new President(
            "Samuel J. Kirkwood", "Midwest", 4, 1, 2, 1, "Conservative", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President danielwvoorhees = new President(
            "Daniel W. Voorhees", "Midwest", 5, 5, 1, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President jamesgfair = new President(
            "James G. Fair", "West", 2, 2, 1, 1, "Libertarian", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President johnrmcpherson = new President(
            "John R. McPherson", "Northeast", 5, 1, 3, 1, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President eugenehale = new President(
            "Eugene Hale", "Northeast", 7, 1, 3, 2, "Libertarian", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President williamechandler = new President(
            "William E. Chandler", "Northeast", 5, 3, 2, 1, "Progressive", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President georgewmccrary = new President(
            "George W. McCrary", "Midwest", 3, 2, 3, 1, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williammahone = new President(
            "William Mahone", "South", 2, 4, 4, 2, "Progressive", "Egalitarianism",
            new String[]{"Reconstruction Era"}
    );
    static President harrisonhriddleberger = new President(
            "Harrison H. Riddleberger", "South", 3, 1, 4, 2, "Populist", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President williamecameron = new President(
            "William E. Cameron", "South", 2, 3, 2, 1, "Progressive", "Egalitarianism",
            new String[]{"Reconstruction Era"}
    );
    static President stanleymatthews = new President(
            "Stanley Matthews", "Midwest", 4, 1, 5, 2, "Progressive", "Open Borders",
            new String[]{"Reconstruction Era"}
    );
    static President johndavislong = new President(
            "John Davis Long", "Northeast", 3, 4, 1, 1, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President georgefhoar = new President(
            "George F. Hoar", "Northeast", 6, 3, 3, 3, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President josephemcdonald = new President(
            "Joseph E. McDonald", "Midwest", 3, 4, 4, 1, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President roswellpflower = new President(
            "Roswell P. Flower", "Northeast", 3, 3, 2, 1, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President williamfvilas = new President(
            "William F. Vilas", "Midwest", 3, 3, 4, 3, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President richardfpettigrew = new President(
            "Richard F. Pettigrew", "West", 3, 2, 4, 1, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President jwarrenkeifer = new President(
            "J. Warren Keifer", "Midwest", 4, 3, 2, 2, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President leonidaschoulk = new President(
            "Leonidas C. Houlk", "South", 3, 3, 1, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President georgehoadly = new President(
            "George Hoadly", "Midwest", 2, 1, 1, 2, "Populist", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President danielmanning = new President(
            "Daniel Manning", "Northeast", 2, 2, 5, 3, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President thomasfgrady = new President(
            "Thomas F. Grady", "Northeast", 1, 5, 1, 1, "Northeast", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President jdonaldcameron = new President(
            "J. Donald Cameron", "Northeast", 6, 1, 2, 3, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President benjaminfmarsh = new President(
            "Benjamin F. Marsh", "Midwest", 4, 1, 1, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamoconnellbradley = new President(
            "William O'Connell Bradley", "South", 3, 6, 1, 2, "Conservative", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President allengthurman = new President(
            "Allen G. Thurman", "Midwest", 5, 3, 4, 5, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President henrywslocum = new President(
            "Henry W. Slocum", "Northeast", 2, 2, 1, 1, "Conservative", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President johntmorgan = new President(
            "John T. Morgan", "South", 5, 3, 2, 3, "Populist", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President johnrthomas = new President(
            "John R. Thomas", "Midwest", 3, 1, 2, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President warnermiller = new President(
            "Warner Miller", "Northeast", 3, 2, 2, 2, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamwhunt = new President(
            "William W. Hunt", "South", 2, 1, 1, 1, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President clarksonnottpotter = new President(
            "Clarkson Nott Potter", "Northeast", 3, 2, 1, 1, "Populist", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President charlesehooker = new President(
            "Charles E. Hooker", "South", 3, 1, 3, 1, "Libertarian", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President jproctorknott = new President(
            "J. Proctor Knott", "South", 4, 4, 1, 3, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President thomasjbrady = new President(
            "Thomas J. Brady", "Midwest", 1, 1, 1, 2, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President stephenwdorsey = new President(
            "Stephen W. Dorsey", "South", 3, 1, 1, 2, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President timothyohowe = new President(
            "Timothy O'Howe", "Midwest", 5, 1, 3, 2, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President jameshslater = new President(
            "James H. Slater", "West", 3, 1, 1, 1, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President samuelbmaxey = new President(
            "Samuel B. Maxey", "South", 4, 2, 1, 1, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President charleswjones = new President(
            "Charles W. Jones", "South", 4, 1, 1, 2, "Populist", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President jamesfwilson = new President(
            "James F. Wilson", "Midwest", 5, 1, 4, 2, "Conservative", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President nathanielphill = new President(
            "Nathaniel P. Hill", "West", 3, 2, 1, 1, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President thomaswpalmer = new President(
            "Thomas W. Palmer", "Midwest", 3, 2, 2, 2, "Progressive", "Social Liberalism",
            new String[]{"Reconstruction Era"}
    );
    static President randalllgibson = new President(
            "Randall L. Gibson", "South", 4, 2, 1, 1, "Populist", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President jamestfarley = new President(
            "James T. Farley", "West", 3, 2, 1, 2, "Populist", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President jamesbbeck = new President(
            "James B. Beck", "South", 6, 3, 2, 2, "Libertarian", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President dwightmsabin = new President(
            "Dwight M. Sabin", "Midwest", 3, 1, 4, 1, "Populist", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President williampfrye = new President(
            "William P. Frye", "Northeast", 7, 2, 4, 3, "Conservative", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President prestonbplumb = new President(
            "Preston B. Plumb", "Midwest", 4, 2, 2, 1, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President wilkinsoncall = new President(
            "Wilkinson Call", "South", 5, 3, 1, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President josephebrown = new President(
            "Joseph E. Brown", "South", 6, 1, 4, 2, "Libertarian", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President alfredhcolquitt = new President(
            "Alfred H. Colquitt", "South", 5, 3, 1, 2, "Populist", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President johnalogan = new President(
            "John A. Logan", "Midwest", 3, 4, 1, 2, "Conservative", "Nationalism",
            new String[]{"Reconstruction Era"}
    );
    static President powellclayton = new President(
            "Powell Clayton", "South", 4, 2, 4, 1, "Conservative", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President johnrlynch = new President(
            "John R. Lynch", "South", 2, 3, 3, 2, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President arthurpgorman = new President(
            "Arthur P. Gorman", "Northeast", 6, 2, 5, 3, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President richardbhubbard = new President(
            "Richard B. Hubbard", "South", 2, 3, 1, 1, "Populist", "Limited Government",
            new String[]{"Reconstruction Era"}
    );
    static President danielllockwood = new President(
            "Daniel L. Lockwood", "Northeast", 2, 2, 2, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President johnbhenderson = new President(
            "John B. Henderson", "South", 3, 2, 3, 2, "Conservative", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President charlesfoster = new President(
            "Charles Foster", "Midwest", 4, 2, 4, 1, "Conservative", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President thomasnast = new President(
            "Thomas Nast", "Northeast", 1, 5, 1, 4, "Progressive", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President charlessfairchild = new President(
            "Charles S. Fairchild", "Northeast", 2, 1, 4, 1, "Libertarian", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President williamcendicott = new President(
            "William C. Endicott", "South", 2, 2, 2, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President augustushgarland = new President(
            "Augustus H. Garland", "South", 4, 1, 5, 1, "Populist", "Constructionism",
            new String[]{"Reconstruction Era"}
    );
    static President henryldawes = new President(
            "Henry L. Dawes", "Northeast", 6, 2, 3, 3, "Conservative", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President williamrmoore = new President(
            "William R. Moore", "South", 2, 1, 2, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President henrymteller = new President(
            "Henry M. Teller", "West", 6, 2, 4, 3, "Progressive", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President rogerqmills = new President(
            "Roger Q. Mills", "South", 6, 1, 6, 3, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President williamlscott = new President(
            "William L. Scott", "Northeast", 2, 3, 3, 2, "Libertarian", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President donaldmdickinson = new President(
            "Donald M. Dickinson", "Midwest", 2, 1, 1, 1, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President shelbymcullom = new President(
            "Shelby M. Cullom", "Northeast", 8, 1, 3, 2, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President johnjingalls = new President(
            "John J. Ingalls", "West", 5, 2, 3, 2, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President knutenelson = new President(
            "Knute Nelson", "Midwest", 6, 3, 2, 1, "Libertarian", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President williamcwhitney = new President(
            "William C. Whitney", "Northeast", 2, 4, 1, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President normanjaycoleman = new President(
            "Norman Jay Coleman", "South", 1, 1, 1, 1, "Populist", "Active Executive",
            new String[]{"Reconstruction Era"}
    );
    static President williamhhatch = new President(
            "William H. Hatch", "South", 3, 1, 2, 2, "Populist", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President jonathanchace = new President(
            "Jonathan Chace", "Northeast", 3, 1, 2, 1, "Conservative", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President personcolbycheney = new President(
            "Person Colby Cheney", "Northeast", 3, 2, 1, 1, "Conservative", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President johnimitchell = new President(
            "John I. Mitchell", "Northeast", 3, 1, 2, 1, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President johnrandolphtucker = new President(
            "John Randolph Tucker", "South", 3, 1, 5, 2, "Populist", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President richardcoke = new President(
            "Richard Coke", "South", 4, 3, 1, 1, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President carlschurz = new President(
            "Carl Schurz", "South", 3, 4, 3, 4, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President georgehsharpe = new President(
            "George H. Sharpe", "Northeast", 1, 3, 1, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President waynemacveagh = new President(
            "Wayne MacVeagh", "Northeast", 1, 1, 3, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President benjaminfjonas = new President(
            "Benjamin F. Jonas", "South", 2, 1, 2, 2, "Populist", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President jameszgeorge = new President(
            "James Z. George", "South", 4, 3, 3, 2, "Populist", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President jamesblackgroome = new President(
            "James Black Groome", "Northeast", 4, 1, 1, 1, "Populist", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President russellaalger = new President(
            "Russell A. Alger", "Midwest", 3, 6, 1, 2, "Conservative", "Nationalism",
            new String[]{"Reconstruction Era"}
    );
    static President chaunceydepew = new President(
            "Chauncey Depew", "Northeast", 3, 2, 3, 3, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President isaacpgray = new President(
            "Isaac P. Gray", "Midwest", 3, 1, 4, 2, "Conservative", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President johncblack = new President(
            "John C. Black", "Midwest", 2, 2, 1, 1, "Populist", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President stephenmwhite = new President(
            "Stephen M. White", "West", 3, 1, 2, 2, "Populist", "Internal Improvements",
            new String[]{"Reconstruction Era"}
    );
    static President levipmorton = new President(
            "Levi P. Morton", "Northeast", 6, 1, 4, 3, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamwphelps = new President(
            "William W. Phelps", "Northeast", 2, 2, 3, 1, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President jeremiahmrusk = new President(
            "Jeremiah M. Rusk", "Midwest", 5, 1, 2, 2, "Conservative", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President walterqgresham = new President(
            "Walter Q. Gresham", "Midwest", 2, 4, 3, 2, "Populist", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President silaswoodson = new President(
            "Silas Woodson", "South", 2, 2, 1, 1, "Populist", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President prestonleslie = new President(
            "Preston Leslie", "West", 3, 3, 2, 1, "Libertarian", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President redfieldproctor = new President(
            "Redfield Proctor", "Northeast", 5, 1, 4, 1, "Conservative", "Active Executive",
            new String[]{"Reconstruction Era"}
    );
    static President johnwanamaker = new President(
            "John Wanamaker", "Northeast", 2, 3, 1, 1, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President williamhhmiller = new President(
            "William H. H. Miller", "Northeast", 2, 2, 4, 1, "Conservative", "Implied Powers",
            new String[]{"Reconstruction Era"}
    );
    static President johnbgordon = new President(
            "John B. Gordon", "South", 4, 3, 1, 2, "Libertarian", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President charlesoconor = new President(
            "Charles O'Conor", "Northeast", 1, 2, 4, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President thomasccatchings = new President(
            "Thomas C. Catchings", "South", 3, 1, 3, 1, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President williamemason = new President(
            "William E. Mason", "Midwest", 3, 2, 2, 1, "Progressive", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President charlesfmanderson = new President(
            "Charles F. Manderson", "Midwest", 4, 1, 3, 1, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President thomasbracketreed = new President(
            "Thomas Bracket Reed", "Northeast", 6, 5, 2, 4, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President rufusblodgett = new President(
            "Rufus Blodgett", "Northeast", 2, 1, 3, 1, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President hughsmiththompson = new President(
            "Hugh Smith Thompson", "South", 2, 1, 2, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williamsholman = new President(
            "William S. Holman", "Midwest", 5, 1, 5, 2, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President matthewquay = new President(
            "Matthew Quay", "Northeast", 3, 5, 1, 4, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President markhdunnell = new President(
            "Mark H. Dunnell", "Midwest", 3, 1, 1, 1, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President frankhiscock = new President(
            "Frank Hiscock", "Northeast", 4, 1, 1, 1, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President johnhreagan = new President(
            "John H. Reagan", "South", 3, 1, 3, 1, "Populist", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President robertepattison = new President(
            "Robert E. Pattison", "Northeast", 3, 5, 2, 2, "Libertarian", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President charlesfcrisp = new President(
            "Charles F. Crisp", "South", 5, 1, 4, 2, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President petercooper = new President(
            "Peter Cooper", "Northeast", 1, 3, 4, 2, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President samuelfcary = new President(
            "Samuel F. Cary", "Midwest", 2, 1, 3, 3, "Populist", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President barzillaivchambers = new President(
            "Barzillai V. Chambers", "South", 1, 2, 4, 1, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President benjaminbutler = new President(
            "Benjamin Butler", "Northeast", 4, 4, 1, 3, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President absolommwest = new President(
            "Absolom M. West", "South", 1, 1, 1, 1, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President thomasewingjr = new President(
            "Thomas Ewing Jr.", "Midwest", 2, 1, 3, 2, "Progressive", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President edwardhgillette = new President(
            "Edward H. Gillette", "Midwest", 2, 2, 1, 1, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President hendrickbwright = new President(
            "Hendrick B. Wright", "Midwest", 3, 1, 2, 1, "Populist", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President thompsonhmurch = new President(
            "Thompson H. Murch", "Northeast", 2, 1, 2, 1, "Populist", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President lumanhamlinweller = new President(
            "Luman Hamlin Weller", "Midwest", 2, 1, 1, 2, "Populist", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President newtonbooth = new President(
            "Newton Booth", "West", 4, 2, 1, 1, "Populist", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President ignatiusldonnelly = new President(
            "Ignatius L. Donnelly", "Midwest", 2, 2, 1, 3, "Populist", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President whitelawreid = new President(
            "Whitelaw Reid", "Northeast", 1, 4, 1, 3, "Conservative", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President benjaminftracy = new President(
            "Benjamin F. Tracy", "Northeast", 2, 2, 5, 1, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President edwardowolcott = new President(
            "Edward O. Wolcott", "West", 3, 2, 1, 1, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President davidbhill = new President(
            "David B. Hill", "Northeast", 4, 4, 1, 3, "Populist", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President horaceboies = new President(
            "Horace Boies", "Midwest", 2, 4, 1, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President calvinsbrice = new President(
            "Calvin S. Brice", "Midwest", 2, 4, 1, 1, "Libertarian", "Tariff",
            new String[]{"Reconstruction Era"}
    );
    static President thomashcarter = new President(
            "Thomas H. Carter", "West", 3, 1, 1, 1, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President thomasjhenderson = new President(
            "Thomas J. Henderson", "Midwest", 4, 2, 2, 1, "Conservative", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President johnwfoster = new President(
            "John W. Foster", "Midwest", 3, 1, 3, 1, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President leonabbett = new President(
            "Leon Abbett", "Northeast", 3, 4, 1, 1, "Populist", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President edwardmurphyjr = new President(
            "Edward Murphy Jr.", "Northeast", 2, 1, 3, 1, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President johnlmitchell = new President(
            "John L. Mitchell", "Midwest", 3, 1, 5, 1, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President freddubois = new President(
            "Fred Dubois", "West", 3, 4, 2, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President frankjcannon = new President(
            "Frank J. Cannon", "West", 2, 2, 2, 1, "Progressive", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President willissweet = new President(
            "Willis Sweet", "West", 2, 3, 3, 2, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President richardolney = new President(
            "Richard Olney", "Northeast", 3, 2, 5, 2, "Libertarian", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President danielslamont = new President(
            "Daniel S. Lamont", "Northeast", 2, 2, 4, 1, "Libertarian", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President williamlwilson = new President(
            "William L. Wilson", "Midwest", 3, 2, 5, 2, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President thomastodd = new President(
            "Thomas Todd", "South", 4, 1, 1, 1, "Libertarian", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President gabrielduvall = new President(
            "Gabriel Duvall", "Northeast", 4, 1, 1, 1, "Conservative", "Implied Powers",
            new String[]{"Jacksonian Era"}
    );
    static President williamjohnson = new President(
            "William Johnson", "South", 5, 3, 4, 3, "Libertarian", "Centralization",
            new String[]{"Founding Era"}
    );
    static President josephstory = new President(
            "Joseph Story", "Northeast", 5, 1, 7, 3, "Conservative", "Social Hierarchy",
            new String[]{"Jacksonian Era"}
    );
    static President henrybrockholstlivingston = new President(
            "Henry Brockholst Livingston", "Northeast", 4, 2, 2, 1, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President alfredthayermahan = new President(
            "Alfred Thayer Mahan", "Northeast", 1, 2, 6, 4, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President johnqabrackett = new President(
            "John Q. A. Brackett", "Northeast", 2, 4, 1, 1, "Progressive", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President edwardcurtissmith = new President(
            "Edward Curtis Smith", "Northeast", 3, 1, 2, 1, "Conservative", "Tax Cuts",
            new String[]{"Reconstruction Era"}
    );
    static President jsterlingmorton = new President(
            "J. Sterling Morton", "Midwest", 2, 2, 5, 1, "Libertarian", "Limited Government",
            new String[]{"Reconstruction Era"}
    );
    static President hilaryaherbert = new President(
            "Hilary A. Herbert", "South", 3, 2, 3, 1, "Libertarian", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President wilsonsbissell = new President(
            "Wilson S. Bissell", "Northeast", 2, 1, 1, 1, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President sanfordbdole = new President(
            "Sanford B. Dole", "West", 3, 4, 1, 3, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President johnrileytanner = new President(
            "John Riley Tanner", "Midwest", 3, 3, 5, 2, "Conservative", "Law and Order",
            new String[]{"Reconstruction Era"}
    );
    static President lorinathurston = new President(
            "Lorin A. Thurston", "West", 2, 4, 2, 2, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President jameshendersonblount = new President(
            "James Henderson Blount", "South", 4, 1, 4, 2, "Libertarian", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President johnpeteraltgeld = new President(
            "John Peter Altgeld", "Midwest", 3, 5, 3, 4, "Progressive", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President williambhornblower = new President(
            "William B. Hornblower", "Northeast", 1, 2, 2, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President cushmankdavis = new President(
            "Cushman K. Davis", "Midwest", 4, 1, 5, 2, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President josephwbabcock = new President(
            "Joseph W. Babcock", "Midwest", 3, 3, 5, 2, "Conservative", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President georgepullman = new President(
            "George Pullman", "Midwest", 1, 1, 1, 5, "Conservative", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President charlesjfaulkner = new President(
            "Charles J. Faulkner", "Midwest", 3, 3, 1, 1, "Libertarian", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President jamesgmacguire = new President(
            "James G. Macguire", "West", 2, 1, 3, 1, "Libertarian", "Civil Liberties",
            new String[]{"Reconstruction Era"}
    );
    static President mattwransom = new President(
            "Matt W. Ransom", "South", 6, 2, 2, 1, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President garrethobart = new President(
            "Garret Hobart", "Northeast", 3, 2, 6, 3, "Conservative", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President henryclayevans = new President(
            "Henry Clay Evans", "South", 2, 2, 4, 1, "Progressive", "Civil Rights",
            new String[]{"Reconstruction Era"}
    );
    static President jamesalbertgary = new President(
            "James Albert Gary", "Northeast", 2, 1, 1, 1, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President williamerussell = new President(
            "William E. Russell", "Northeast", 2, 4, 6, 3, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President johnrmclean = new President(
            "John R. McLean", "Midwest", 1, 4, 2, 3, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President claudematthews = new President(
            "Claude Matthews", "Midwest", 3, 3, 1, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President jamesawalker = new President(
            "James A. Walker", "South", 2, 2, 1, 2, "Libertarian", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President lymanjgage = new President(
            "Lyman J. Gage", "Northeast", 3, 1, 6, 2, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President johnwgriggs = new President(
            "John W. Griggs", "Northeast", 3, 4, 2, 1, "Progressive", "Egalitarianism",
            new String[]{"Reconstruction Era"}
    );
    static President arthursewall = new President(
            "Arthur Sewall", "Northeast", 1, 4, 2, 1, "Conservative", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President josephcsibley = new President(
            "Joseph C. Sibley", "Midwest", 2, 5, 1, 1, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President johnwdaniel = new President(
            "John W. Daniel", "South", 5, 3, 1, 1, "Populist", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President corneliusnewtonbliss = new President(
            "Cornelius Newton Bliss", "Northeast", 2, 1, 5, 2, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President nelsondingleyjr = new President(
            "Nelson Dingley Jr.", "Northeast", 5, 1, 6, 2, "Conservative", "Tariffs",
            new String[]{"Reconstruction Era"}
    );
    static President johnhay = new President(
            "John Hay", "Midwest", 4, 5, 3, 3, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President johnmpalmer = new President(
            "John M. Palmer", "Midwest", 4, 3, 4, 3, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President simonbolivarbuckner = new President(
            "Simon Bolivar Buckner", "South", 3, 4, 2, 3, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President georgehearst = new President(
            "George Hearst", "West", 2, 2, 1, 2, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President henryfbowers = new President(
            "Henry F. Bowers", "Northeast", 1, 4, 1, 3, "Conservative", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President nelsonamiles = new President(
            "Nelson A. Miles", "Northeast", 1, 4, 1, 4, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President charlesemorysmith = new President(
            "Charles Emory Smith", "Northeast", 2, 1, 3, 1, "Progressive", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President abramhewitt = new President(
            "Abram Hewitt", "Northeast", 3, 1, 4, 2, "Libertarian", "Tax Cuts",
            new String[]{"Reconstruction Era"}
    );
    static President edwardatkinson = new President(
            "Edward Atkinson", "Northeast", 1, 3, 5, 3, "Libertarian", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President charlesatowne = new President(
            "Charles A. Towne", "Midwest", 3, 2, 1, 1, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President leemantle = new President(
            "Lee Mantle", "Midwest", 2, 1, 1, 1, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President georgesboutwell = new President(
            "George S. Boutwell", "Northeast", 4, 3, 3, 3, "Libertarian", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President daniellindsayrussell = new President(
            "Daniel Lindsay Russell", "South", 3, 3, 1, 2, "Progressive", "Egalitarianism",
            new String[]{"Reconstruction Era"}
    );
    static President edwinlawrencegodkin = new President(
            "Edwin Lawrence Godkin", "Northeast", 1, 3, 1, 2, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President williamgrahamsummer = new President(
            "William Graham Summer", "Northeast", 1, 5, 2, 3, "Libertarian", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President johngwarwick = new President(
            "John G. Warwick", "Midwest", 2, 2, 2, 3, "Libertarian", "Free Trade",
            new String[]{"Reconstruction Era"}
    );
    static President andrewcarnegie = new President(
            "Andrew Carnegie", "Midwest", 1, 4, 5, 9, "Libertarian", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President henryadams = new President(
            "Henry Adams", "Northeast", 1, 1, 6, 3, "Conservative", "Social Hierarchy",
            new String[]{"Reconstruction Era"}
    );
    static President theodorerooseveltsr = new President(
            "Theodore Roosevelt Sr.", "Northeast", 1, 1, 2, 2, "Progressive", "Anti-Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President marktwain = new President(
            "Mark Twain", "South", 1, 9, 2, 8, "Progressive", "Isolationism",
            new String[]{"Reconstruction Era"}
    );
    static President spencertrask = new President(
            "Spencer Trask", "Northeast", 1, 2, 1, 2, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President franciskernan = new President(
            "Francis Kernan", "Northeast", 3, 1, 2, 2, "Libertarian", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President jonathanpdolliver = new President(
            "Jonathan P. Dolliver", "Midwest", 4, 4, 1, 3, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President timothylwoodruff = new President(
            "Timothy L. Woodruff", "Northeast", 2, 1, 1, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President josephbforaker = new President(
            "Joseph B. Foraker", "Midwest", 4, 6, 1, 3, "Conservative", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President georgedewey = new President(
            "George Dewey", "Northeast", 1, 4, 1, 5, "Libertarian", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President julianscarr = new President(
            "Julian S. Carr", "South", 1, 1, 1, 1, "Populist", "Racism",
            new String[]{"Progressive Era"}
    );
    static President johnwaltersmith = new President(
            "John Walter Smith", "Northeast", 5, 1, 2, 1, "Progressive", "Social Programs",
            new String[]{"Progressive Era"}
    );
    static President prescottfhall = new President(
            "Prescott F. Hall", "Northeast", 1, 2, 4, 2, "Conservative", "Closed Borders",
            new String[]{"Progressive Era"}
    );
    static President robertrhitt = new President(
            "Robert R. Hitt", "Midwest", 5, 2, 3, 2, "Progressive", "Open Borders",
            new String[]{"Progressive Era"}
    );
    static President serenoepayne = new President(
            "Sereno E. Payne", "Northeast", 6, 1, 5, 2, "Conservative", "Tariffs",
            new String[]{"Progressive Era"}
    );
    static President benjamintillman = new President(
            "Benjamin Tillman", "South", 4, 4, 3, 4, "Populist", "Racism",
            new String[]{"Progressive Era"}
    );
    static President francisgnewlands = new President(
            "Francis G. Newlands", "West", 4, 1, 5, 2, "Populist", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President josephwbailey = new President(
            "Joseph W. Bailey", "South", 5, 3, 1, 2, "Libertarian", "Inflation",
            new String[]{"Progressive Era"}
    );
    static President jimwilson = new President(
            "Jim Wilson", "Midwest", 4, 2, 3, 1, "Conservative", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President morganbulkeley = new President(
            "Morgan Bulkeley", "Northeast", 4, 3, 1, 3, "Conservative", "Limited Government",
            new String[]{"Progressive Era"}
    );
    static President albertjbeveridge = new President(
            "Albert J. Beveridge", "Midwest", 3, 5, 2, 3, "Progressive", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President jamesdrichardson = new President(
            "James D. Richardson", "South", 5, 2, 3, 1, "Populist", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President williamsulzer = new President(
            "William Sulzer", "Northeast", 3, 4, 1, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President moorfieldstorey = new President(
            "Moorfield Storey", "Northeast", 1, 4, 3, 2, "Progressive", "Civil Rights",
            new String[]{"Progressive Era"}
    );
    static President johnarchibaldcampbell = new President(
            "John Archibald Campbell", "South", 3, 2, 5, 2, "Populist", "Regulation",
            new String[]{"Jacksonian Era"}
    );
    static President nathanclifford = new President(
            "Nathan Clifford", "Northeast", 6, 1, 2, 2, "Libertarian", "States' Rights",
            new String[]{"Civil War Era"}
    );
    static President noahhaynesswayne = new President(
            "Noah Haynes Swayne", "Midwest", 4, 1, 1, 1, "Progressive", "Centralization",
            new String[]{"Civil War Era"}
    );
    static President samuelfreemanmiller = new President(
            "Samuel Freeman Miller", "Midwest", 5, 3, 3, 4, "Conservative", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President daviddavis = new President(
            "David Davis", "Midwest", 6, 4, 2, 3, "Libertarian", "Civil Liberties",
            new String[]{"Reconstruction Era"}
    );
    static President williamstrong = new President(
            "William Strong", "Midwest", 4, 1, 2, 1, "Libertarian", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President josephpbradley = new President(
            "Joseph P. Bradley", "Northeast", 4, 1, 1, 4, "Conservative", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President wardhunt = new President(
            "Ward Hunt", "Northeast", 3, 2, 1, 1, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President morrisonwaite = new President(
            "Morrison Waite", "Midwest", 4, 5, 1, 2, "Conservative", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President johnmarshallharlan = new President(
            "John Marshall Harlan", "South", 6, 3, 3, 4, "Progressive", "Implied Powers",
            new String[]{"Reconstruction Era"}
    );
    static President williamburnhamwoods = new President(
            "William Burnham Woods", "Midwest", 3, 1, 2, 1, "Conservative", "Centralization",
            new String[]{"Reconstruction Era"}
    );
    static President horacegray = new President(
            "Horace Gray", "Northeast", 4, 1, 3, 1, "Conservative", "Implied Powers",
            new String[]{"Reconstruction Era"}
    );
    static President samuelblatchford = new President(
            "Samuel Blatchford", "Northeast", 3, 1, 1, 1, "Conservative", "Implied Powers",
            new String[]{"Reconstruction Era"}
    );
    static President melvillefuller = new President(
            "Melville Fuller", "Midwest", 6, 4, 2, 4, "Libertarian", "Constructionism",
            new String[]{"Reconstruction Era"}
    );
    static President davidjbrewer = new President(
            "David J. Brewer", "West", 5, 1, 7, 3, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President henrybillingsbrown = new President(
            "Henry Billings Brown", "Midwest", 4, 2, 3, 2, "Conservative", "Racism",
            new String[]{"Reconstruction Era"}
    );
    static President georgesirasjr = new President(
            "George Siras Jr.", "Midwest", 3, 1, 2, 1, "Libertarian", "Tax Cuts",
            new String[]{"Reconstruction Era"}
    );
    static President philipsheridan = new President(
            "Philip Sheridan", "Midwest", 1, 3, 1, 6, "Conservative", "Imperialism",
            new String[]{"Reconstruction Era"}
    );
    static President deniskearney = new President(
            "Denis Kearney", "West", 1, 5, 1, 1, "Populist", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President howellejackson = new President(
            "Howell E. Jackson", "South", 3, 2, 1, 1, "Conservative", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President henrykyddouglas = new President(
            "Henry Kyd Douglas", "Northeast", 1, 3, 2, 1, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President josephmckenna = new President(
            "Joseph McKenna", "West", 7, 1, 2, 2, "Conservative", "Open Borders",
            new String[]{"Progressive Era"}
    );
    static President oliverwendellholmesjr = new President(
            "Oliver Wendell Holmes Jr.", "Northeast", 5, 4, 4, 4, "Progressive", "Civil Liberties",
            new String[]{"Progressive Era"}
    );
    static President charlesjbonaparte = new President(
            "Charles J. Bonaparte", "Northeast", 3, 1, 2, 4, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President edwarddouglasswhite = new President(
            "Edward Douglass White", "South", 7, 1, 5, 3, "Conservative", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President rufuspeckman = new President(
            "Rufus Peckman", "Northeast", 4, 1, 5, 2, "Libertarian", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President johnsharpwilliams = new President(
            "John Sharp Williams", "South", 6, 2, 2, 3, "Populist", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President williamphepburn = new President(
            "William P. Hepburn", "Midwest", 5, 2, 6, 3, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President williamrday = new President(
            "William R. Day", "Midwest", 5, 1, 3, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President jameshamiltonpeabody = new President(
            "James Hamilton Peabody", "West", 2, 3, 1, 2, "Conservative", "Law and Order",
            new String[]{"Progressive Era"}
    );
    static President alvaadams = new President(
            "Alva Adams", "West", 2, 4, 1, 1, "Populist", "Class Unity",
            new String[]{"Progressive Era"}
    );
    static President williamdjohnson = new President(
            "William D. Johnson", "Midwest", 2, 3, 1, 2, "Populist", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President murphyjfoster = new President(
            "Murphy J. Foster", "South", 5, 2, 2, 2, "Libertarian", "Racism",
            new String[]{"Progressive Era"}
    );
    static President johnflacey = new President(
            "John F. Lacey", "Midwest", 3, 1, 6, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President bookertwashington = new President(
            "Booker T. Washington", "South", 1, 7, 3, 6, "Conservative", "Social Programs",
            new String[]{"Progressive Era"}
    );
    static President edwardbvreeland = new President(
            "Edward B. Vreeland", "Northeast", 3, 1, 5, 2, "Conservative", "Central Bank",
            new String[]{"Progressive Era"}
    );
    static President samuelgompers = new President(
            "Samuel Gompers", "Northeast", 1, 5, 1, 4, "Populist", "Class Unity",
            new String[]{"Progressive Era"}
    );
    static President horaceharmonluton = new President(
            "Horace Harmon Luton", "South", 2, 1, 3, 1, "Progressive", "Civil Liberties",
            new String[]{"Progressive Era"}
    );
    static President jameskjones = new President(
            "James K. Jones", "South", 5, 3, 1, 1, "Populist", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President boiespenrose = new President(
            "Boies Penrose", "Northeast", 5, 4, 1, 2, "Conservative", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President williamhenrymoody = new President(
            "William Henry Moody", "Northeast", 3, 1, 4, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President georgebcortelyou = new President(
            "George B. Cortelyou", "Northeast", 3, 2, 1, 1, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President jameshay = new President(
            "James Hay", "South", 3, 1, 3, 1, "Libertarian", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President jamestlloyd = new President(
            "James T. Lloyd", "South", 4, 3, 2, 2, "Populist", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President napoleonbbroward = new President(
            "Napoleon B. Broward", "South", 3, 3, 1, 1, "Populist", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President samuelwpennypacker = new President(
            "Samuel W. Pennypacker", "Northeast", 3, 2, 2, 1, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President jamesrudolphgarfield = new President(
            "James Rudolph Garfield", "Midwest", 2, 1, 1, 2, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President charleswfdick = new President(
            "Charles W. F. Dick", "Midwest", 4, 1, 2, 2, "Conservative", "Centralization",
            new String[]{"Progressive Era"}
    );
    static President jamesrwilliams = new President(
            "James R. Williams", "Midwest", 3, 2, 1, 2, "Libertarian", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President georgeturner = new President(
            "George Turner", "South", 3, 1, 1, 1, "Populist", "Inflation",
            new String[]{"Progressive Era"}
    );
    static President williamaharris = new President(
            "William A. Harris", "West", 3, 1, 2, 1, "Populist", "Inflation",
            new String[]{"Progressive Era"}
    );
    static President henryclaypayne = new President(
            "Henry Clay Payne", "Midwest", 2, 3, 1, 1, "Conservative", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President johndalzell = new President(
            "John Dalzell", "Midwest", 4, 3, 2, 2, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President jamesatawney = new President(
            "James A. Tawney", "Midwest", 4, 1, 4, 1, "Conservative", "Tariffs",
            new String[]{"Progressive Era"}
    );
    static President johncmclaurin = new President(
            "John C. McLaurin", "South", 3, 1, 1, 2, "Populist", "Social Programs",
            new String[]{"Progressive Era"}
    );
    static President edwardcarmack = new President(
            "Edward Carmack", "South", 3, 2, 1, 2, "Populist", "Traditional Morality",
            new String[]{"Progressive Era"}
    );
    static President benjaminfshively = new President(
            "Benjamin F. Shively", "Midwest", 3, 2, 3, 2, "Populist", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President winfieldtdurbin = new President(
            "Winfield T. Durbin", "Midwest", 3, 3, 2, 2, "Progressive", "Civil Rights",
            new String[]{"Progressive Era"}
    );
    static President henrymcbride = new President(
            "Henry McBride", "West", 2, 1, 3, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President stephenbelkins = new President(
            "Stephen B. Elkins", "Midwest", 4, 1, 5, 3, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President luciusfcgarvin = new President(
            "Lucius F. C. Garvin", "Northeast", 2, 4, 3, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President jeffdavis = new President(
            "Jeff Davis", "South", 3, 5, 1, 2, "Populist", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President alexandermdockery = new President(
            "Alexander M. Dockery", "South", 4, 1, 5, 2, "Libertarian", "Tax Cuts",
            new String[]{"Progressive Era"}
    );
    
    static President jimwright = new President(
            "Jim Wright", "South", 7, 3, 2, 2, "Progressive", "Civil Rights",
            new String[]{"Reagan Era"}
    );
    
    static President howardbaker = new President(
            "Howard Baker", "South", 5, 3, 2, 3, "Conservative", "Establishment",
            new String[]{"Reagan Era"}
    );
    
    static President edkoch = new President(
            "Ed Koch", "Northeast", 3, 4, 1, 2, "Populist", "Law and Order",
            new String[]{"Reagan Era"}
    );
    
    static President jeanekirkpatrick = new President(
            "Jeane Kirkpatrick", "Northeast", 2, 2, 4, 2, "Conservative", "Imperialism",
            new String[]{"Reagan Era"}
    );
    
    static President donaldregan = new President(
            "Donald Regan", "Northeast", 2, 1, 1, 3, "Conservative", "Tax Cuts",
            new String[]{"Reagan Era"}
    );
    
    static President lawrenceeagleburger = new President(
            "Lawrence Eagleburger", "Midwest", 2, 2, 1, 2, "Conservative", "Imperialism",
            new String[]{"Reagan Era"}
    );
    
    static President edwinedwards = new President(
            "Edwin Edwards", "South", 4, 2, 1, 3, "Populist", "Social Programs",
            new String[]{"Reagan Era"}
    );
    
    static President jamesblack = new President(
            "James Black", "Northeast", 1, 3, 1, 2, "Progressive", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President johnstjohn = new President(
            "John St. John", "West", 3, 2, 2, 2, "Progressive", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President johnbidwell = new President(
            "John Bidwell", "West", 2, 3, 3, 1, "Progressive", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President alsonstreeter = new President(
            "Alson Streeter", "Midwest", 1, 2, 4, 1, "Progressive", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President jonathanblanchard = new President(
            "Jonathan Blanchard", "Midwest", 1, 3, 1, 1, "Conservative", "Traditional Morality",
            new String[]{"Reconstruction Era"}
    );
    static President simonwing = new President(
            "Simon Wing", "Northeast", 1, 1, 2, 1, "Progressive", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President jamesbweaver = new President(
            "James B. Weaver", "Midwest", 2, 4, 5, 3, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President thomasewatson = new President(
            "Thomas E. Watson", "South", 2, 4, 2, 2, "Populist", "Social Programs",
            new String[]{"Reconstruction Era"}
    );
    static President jamesgfield = new President(
            "James G. Field", "West", 1, 3, 3, 1, "Populist", "Egalitarianism",
            new String[]{"Reconstruction Era"}
    );
    static President maryelizabethlease = new President(
            "Mary Elizabeth Lease", "Northeast", 1, 5, 1, 2, "Progressive", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President leonidasjpolk = new President(
            "Leonidas J. Polk", "South", 1, 3, 4, 2, "Progressive", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President williamapeffer = new President(
            "William A. Peffer", "West", 3, 1, 2, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President omermkem = new President(
            "Omer M. Kem", "West", 2, 1, 2, 1, "Populist", "Social Liberalism",
            new String[]{"Reconstruction Era"}
    );
    static President jerrysimpson = new President(
            "Jerry Simpson", "West", 2, 2, 4, 3, "Populist", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President marionbutler = new President(
            "Marion Butler", "South", 3, 3, 1, 2, "Populist", "Inflation",
            new String[]{"Reconstruction Era"}
    );
    static President milfordmhoward = new President(
            "Milford M. Howard", "South", 2, 1, 1, 1, "Populist", "Active Executive",
            new String[]{"Reconstruction Era"}
    );
    static President jameshkyle = new President(
            "James H. Kyle", "West", 4, 2, 4, 1, "Progressive", "Class Unity",
            new String[]{"Reconstruction Era"}
    );
    static President johnpbuchanan = new President(
            "John P. Buchanan", "South", 2, 2, 1, 1, "Populist", "Regulation",
            new String[]{"Reconstruction Era"}
    );
    static President lmshaw = new President(
            "L. M. Shaw", "Midwest", 3, 1, 4, 2, "Conservative", "Central Bank",
            new String[]{"Progressive Era"}
    );
    static President juliuscburrows = new President(
            "Julius C. Burrows", "Midwest", 5, 3, 1, 1, "Conservative", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President franklinmurphy = new President(
            "Franklin Murphy", "Northeast", 2, 1, 3, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President georgegray = new President(
            "George Gray", "Northeast", 4, 2, 2, 3, "Libertarian", "Limited Government",
            new String[]{"Progressive Era"}
    );
    static President williamldouglas = new President(
            "William L. Douglas", "Northeast", 2, 3, 1, 1, "Populist", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President josephwfolk = new President(
            "Joseph W. Folk", "South", 2, 1, 2, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President henryclayfrick = new President(
            "Henry Clay Frick", "Midwest", 1, 3, 1, 6, "Conservative", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President lelandstanford = new President(
            "Leland Stanford", "West", 4, 2, 2, 6, "Populist", "Closed Borders",
            new String[]{"Reconstruction Era"}
    );
    static President collisphuntington = new President(
            "Collis P. Huntington", "West", 1, 1, 4, 5, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President corneliusvanderbilt = new President(
            "Cornelius Vanderbilt", "Northeast", 1, 3, 3, 8, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President jaygould = new President(
            "Jay Gould", "Northeast", 1, 1, 5, 8, "Libertarian", "Deregulation",
            new String[]{"Reconstruction Era"}
    );
    static President jamesjhill = new President(
            "James J. Hill", "Midwest", 1, 2, 5, 7, "Libertarian", "Laissez-Faire",
            new String[]{"Reconstruction Era"}
    );
    static President theodoreeburton = new President(
            "Theodore E. Burton", "Midwest", 5, 1, 5, 2, "Conservative", "Laissez-Faire",
            new String[]{"Progressive Era"}
    );
    static President charleswfulton = new President(
            "Charles W. Fulton", "West", 3, 1, 3, 2, "Conservative", "Laissez-Faire",
            new String[]{"Progressive Era"}
    );
    static President davidbhenderson = new President(
            "David B. Henderson", "Midwest", 5, 3, 3, 2, "Conservative", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President charlesnhaskell = new President(
            "Charles N. Haskell", "West", 2, 2, 4, 2, "Progressive", "Traditional Morality",
            new String[]{"Progressive Era"}
    );
    static President bentonmcmillan = new President(
            "Benton McMillan", "South", 4, 4, 1, 3, "Populist", "Social Programs",
            new String[]{"Progressive Era"}
    );
    static President johnssnook = new President(
            "John S. Snook", "Midwest", 2, 1, 2, 1, "Libertarian", "Laissez-Faire",
            new String[]{"Progressive Era"}
    );
    static President francishhitchcock = new President(
            "Francis H. Hitchcock", "Midwest", 2, 4, 1, 2, "Progressive", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President mahlonpitney = new President(
            "Mahlon Pitney", "Northeast", 4, 1, 6, 2, "Conservative", "Constructionism",
            new String[]{"Progressive Era"}
    );
    static President franklinmacveagh = new President(
            "Franklin MacVeagh", "Midwest", 2, 1, 3, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President augustusobacon = new President(
            "Augustus O. Bacon", "South", 4, 1, 3, 1, "Populist", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President edwinywebb = new President(
            "Edwin Y. Webb", "South", 3, 1, 4, 3, "Progressive", "Traditional Morality",
            new String[]{"Progressive Era"}
    );
    static President robertlhenry = new President(
            "Robert L. Henry", "South", 4, 1, 2, 1, "Progressive", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President georgevonlengerkemeyer = new President(
            "George von Lengerke Meyer", "Northeast", 2, 3, 1, 1, "Conservative", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President richardaballinger = new President(
            "Richard A. Ballinger", "West", 2, 1, 1, 4, "Conservative", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President charlesnagel = new President(
            "Charles Nagel", "South", 2, 2, 1, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President josephrlamar = new President(
            "Joseph R. Lamar", "South", 3, 1, 3, 2, "Libertarian", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President jacobdickinson = new President(
            "Jacob Dickinson", "South", 2, 1, 2, 1, "Progressive", "Open Borders",
            new String[]{"Progressive Era"}
    );
    static President tomljohnson = new President(
            "Tom L. Johnson", "Midwest", 2, 3, 5, 4, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President williampdillingham = new President(
            "William P. Dillingham", "Northeast", 5, 1, 5, 3, "Conservative", "Closed Borders",
            new String[]{"Progressive Era"}
    );
    static President winthropmcrane = new President(
            "Winthrop M. Crane", "Northeast", 4, 1, 4, 1, "Conservative", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President isaacstephenson = new President(
            "Isaac Stephenson", "Midwest", 3, 1, 2, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President jamesplclark = new President(
            "James P. Clark", "South", 5, 4, 1, 1, "Populist", "Racism",
            new String[]{"Progressive Era"}
    );
    static President robertfbroussard = new President(
            "Robert F. Broussard", "South", 4, 1, 1, 1, "Populist", "Internal Improvements",
            new String[]{"Progressive Era"}
    );
    static President shubertdentjr = new President(
            "S. Hubert Dent Jr.", "South", 2, 2, 1, 1, "Populist", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President josephlbristow = new President(
            "Joseph L. Bristow", "West", 3, 2, 4, 2, "Progressive", "Egalitarianism",
            new String[]{"Progressive Era"}
    );
    static President herbertshadley = new President(
            "Herbert S. Hadley", "South", 2, 5, 3, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President georgewperkins = new President(
            "George W. Perkins", "Northeast", 1, 1, 7, 4, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President johnburke = new President(
            "John Burke", "West", 3, 4, 3, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President georgeechamberlain = new President(
            "George E. Chamberlain", "West", 4, 1, 3, 1, "Populist", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President seabornroddenberry = new President(
            "Seaborn Roddenberry", "South", 2, 4, 2, 2, "Libertarian", "Racism",
            new String[]{"Progressive Era"}
    );
    static President moseseclapp = new President(
            "Moses E. Clapp", "Midwest", 3, 2, 3, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President jonathanbournejr = new President(
            "Jonathan Bourne Jr.", "West", 3, 4, 2, 3, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President williamspry = new President(
            "William Spry", "West", 4, 1, 3, 2, "Libertarian", "Social Liberalism",
            new String[]{"Progressive Era"}
    );
    static President josephptumulty = new President(
            "Joseph P. Tumulty", "Northeast", 1, 5, 1, 3, "Progressive", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President thomaswgregory = new President(
            "Thomas W. Gregory", "South", 2, 1, 5, 3, "Progressive", "Implied Powers",
            new String[]{"Progressive Era"}
    );
    static President williamcredfield = new President(
            "William C. Redfield", "Northeast", 3, 1, 2, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President williameglasscock = new President(
            "William E. Glasscock", "Midwest", 3, 1, 1, 1, "Progressive", "Law and Order",
            new String[]{"Progressive Era"}
    );
    static President josephmdixon = new President(
            "Joseph M. Dixon", "West", 4, 2, 1, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President williambmckinley = new President(
            "William B. McKinley", "Midwest", 4, 2, 1, 1, "Conservative", "Tariffs",
            new String[]{"Progressive Era"}
    );
    static President jamessmithjr = new President(
            "James Smith Jr.", "Northeast", 2, 3, 1, 1, "Populist", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President newtondbaker = new President(
            "Newton D. Baker", "Midwest", 3, 1, 5, 3, "Progressive", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President edwardmhouse = new President(
            "Edward M. House", "South", 1, 4, 1, 2, "Progressive", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President victormurdock = new President(
            "Victor Murdock", "West", 2, 2, 1, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President jamesrmann = new President(
            "James R. Mann", "Midwest", 6, 2, 5, 3, "Progressive", "Centralization",
            new String[]{"Progressive Era"}
    );
    static President johnjesch = new President(
            "John J. Esch", "Midwest", 4, 1, 2, 2, "Progressive", "Internal Improvements",
            new String[]{"Progressive Era"}
    );
    static President josephusdaniels = new President(
            "Josephus Daniels", "South", 3, 4, 2, 1, "Populist", "Racism",
            new String[]{"Progressive Era"}
    );
    static President claudekitchin = new President(
            "Claude Kitchin", "South", 5, 2, 5, 2, "Populist", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President jameskvardaman = new President(
            "James K. Vardaman", "South", 4, 4, 2, 4, "Populist", "Racism",
            new String[]{"Progressive Era"}
    );
    static President juliuskhan = new President(
            "Julius Khan", "West", 4, 1, 2, 1, "Conservative", "Imperialism",
            new String[]{"Progressive Era"}
    );
    static President francisemcgovern = new President(
            "Francis E. McGovern", "Midwest", 3, 1, 1, 2, "Conservative", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President walterrstubbs = new President(
            "Walter R. Stubbs", "West", 2, 1, 3, 1, "Progressive", "Traditional Morality",
            new String[]{"Progressive Era"}
    );
    static President georgebmharvey = new President(
            "George B. M. Harvey", "Northeast", 1, 3, 1, 1, "Libertarian", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President robertlowen = new President(
            "Robert L. Owen", "West", 5, 1, 6, 3, "Progressive", "Central Bank",
            new String[]{"Progressive Era"}
    );
    static President henrydclayton = new President(
            "Henry D. Clayton", "South", 3, 1, 4, 3, "Populist", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President andrewvolstead = new President(
            "Andrew Volstead", "Midwest", 3, 3, 3, 2, "Conservative", "Traditional Morality",
            new String[]{"Progressive Era"}
    );
    static President robertpbass = new President(
            "Robert P. Bass", "Northeast", 2, 1, 1, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President jacobhgallinger = new President(
            "Jacob H. Gallinger", "Northeast", 7, 1, 3, 2, "Conservative", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President jamesemartine = new President(
            "James E. Martine", "Northeast", 3, 1, 1, 1, "Progressive", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President paulwarbug = new President(
            "Paul Warbug", "Northeast", 1, 2, 6, 3, "Progressive", "Central Bank",
            new String[]{"Progressive Era"}
    );
    static President williamcadamson = new President(
            "William C. Adamson", "South", 4, 1, 3, 2, "Populist", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President francisewarren = new President(
            "Francis E. Warren", "West", 7, 2, 2, 1, "Conservative", "Internal Improvements",
            new String[]{"Progressive Era"}
    );
    static President williamskenyon = new President(
            "William S. Kenyon", "Midwest", 3, 3, 5, 2, "Progressive", "Centralization",
            new String[]{"Progressive Era"}
    );
    static President benwhooper = new President(
            "Ben W. Hooper", "South", 3, 3, 2, 2, "Progressive", "Traditional Morality",
            new String[]{"Progressive Era"}
    );
    static President albertsburleson = new President(
            "Albert S. Burleson", "South", 4, 1, 3, 4, "Populist", "Implied Powers",
            new String[]{"Progressive Era"}
    );
    static President eliasmammons = new President(
            "Elias M. Ammons", "West", 2, 1, 1, 3, "Libertarian", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President edwardkeating = new President(
            "Edward Keating", "West", 2, 3, 2, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President williamborah = new President(
            "William Borah", "West", 6, 3, 4, 4, "Populist", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President aslegronna = new President(
            "Asle Gronna", "West", 3, 2, 2, 2, "Progressive", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President lawrenceysherman = new President(
            "Lawrence Y. Sherman", "Midwest", 3, 3, 3, 2, "Conservative", "Nationalism",
            new String[]{"Progressive Era"}
    );
    static President jamesareed = new President(
            "James A. Reed", "South", 4, 2, 4, 1, "Libertarian", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President harrylane = new President(
            "Harry Lane", "West", 2, 4, 1, 2, "Progressive", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President williamjstone = new President(
            "William J. Stone", "South", 5, 1, 1, 1, "Populist", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President johnwweeks = new President(
            "John W. Weeks", "Northeast", 4, 4, 4, 2, "Conservative", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President mgbrumbaugh = new President(
            "M. G. Brumbaugh", "Northeast", 3, 3, 1, 1, "Libertarian", "Nationalism",
            new String[]{"Progressive Era"}
    );
    static President samuelwmccall = new President(
            "Samuel W. McCall", "Northeast", 5, 1, 4, 1, "Libertarian", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President roberteburke = new President(
            "Robert E. Burke", "South", 2, 1, 1, 1, "Libertarian", "Anti-Establishment",
            new String[]{"Progressive Era"}
    );
    static President williamajones = new President(
            "William A. Jones", "South", 5, 1, 3, 2, "Libertarian", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President franklinknightlane = new President(
            "Franklin Knight Lane", "West", 2, 1, 5, 1, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President joerogan = new President(
            "Joe Rogan", "South", 1, 6, 1, 7, "Populist", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President annapaulinaluna = new President(
            "Anna Paulina Luna", "South", 2, 3, 1, 1, "Populist", "Anti-Establishment",
            new String[]{"Trump Era"}
    );
    static President markcuban = new President(
            "Mark Cuban", "South", 1, 2, 5, 7, "Progressive", "Free Trade",
            new String[]{"Trump Era"}
    );
    static President joshstein = new President(
            "Josh Stein", "South", 2, 3, 4, 2, "Progressive", "Regulation",
            new String[]{"Trump Era"}
    );
    static President davemccormick = new President(
            "Dave McCormick", "Northeast", 2, 4, 6, 2, "Conservative", "Free Trade",
            new String[]{"Trump Era"}
    );
    static President brianfitzpatrick = new President(
            "Brian Fitzpatrick", "Northeast", 3, 2, 5, 1, "Conservative", "Social Programs",
            new String[]{"Trump Era"}
    );
    static President markrobinson = new President(
            "Mark Robinson", "South", 2, 1, 1, 2, "Populist", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President bobcaseyjr = new President(
            "Bob Casey Jr.", "Northeast", 6, 2, 4, 4, "Progressive", "Traditional Morality",
            new String[]{"Trump Era"}
    );
    static President timwalz = new President(
            "Tim Walz", "Midwest", 6, 5, 1, 4, "Progressive", "Civil Liberties",
            new String[]{"Trump Era"}
    );
    static President ericadams = new President(
            "Eric Adams", "Northeast", 2, 1, 1, 3, "Progressive", "Law and Order",
            new String[]{"Trump Era"}
    );
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  

    /************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     *********************** END OF PRESIDENTS ******************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************
     ************************************************************************************************************************/
    
    
    // ELECTIONS
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
            1840, "nam", "infl", "South", "Laissez-Faire vs. Internal Improvements",
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
            1800, "exp", "nam", "Northeast", "States' Rights vs. Centralization",
            "Constructionism vs. Implied Powers", "Social Hierarchy vs. Egalitarianism"
    );
    static Election e1796 = new Election(
            1796, "exp", "infl", "Northeast", "Imperialism vs. Isolationism",
            "Social Hierarchy vs. Egalitarianism", "Law and Order vs. Civil Liberties"
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

    static Policy toughfp1 = new Policy("Imperialism", "Progressive", "Conservative");
    static Policy toughfp2 = new Policy("Imperialism", "Progressive", "Conservative");
    static Policy toughfp3 = new Policy("Imperialism", "Progressive", "Conservative");
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
    static Policy goldstandard = new Policy("Gold Standard", "Conservative", "Libertarian", "Populist");
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
    static Policy openb = new Policy("Open Borders", "Progressive", "Libertarian", "Populist");
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
    static Policy pfreedom = new Policy("Civil Liberties", "Progressive", "Libertarian", "Populist");
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
    
    public static List<President> getOnePresident() {
    	List<President> presidents = new ArrayList<President>();
    	for (int i = 0; i < 25; i++) {
            presidents.add(barryGoldwater);
        }
    	return presidents;
    }
    
    /************************************************************************************************************************
     *********************** ADD PRESIDENTS FUNCTION ************************************************************************
     ************************************************************************************************************************/
    
    /***
     * Use this for getting/adding presidents to the game
     */
    public static List<President> getPresidents(String preset, String[] tags, double minRate) {
        List<President> presidents = new ArrayList<President>();
        List<President> nonpresidents = new ArrayList<President>();
        List<President> meme = new ArrayList<President>();
        presidents.add(calvinCoolidge);
        presidents.add(thomasJefferson);
        presidents.add(lyndonBJohnson);
        presidents.add(donaldTrump);
        presidents.add(ulyssesSGrant);
        presidents.add(woodrowWilson);
        presidents.add(millardFillmore);
        presidents.add(billClinton);
        presidents.add(georgeWashington);
        presidents.add(jamesAGarfield);
        presidents.add(jamesMonroe);
        presidents.add(williamMcKinley);
        presidents.add(andrewJackson);
        presidents.add(ronaldReagan);
        presidents.add(groverCleveland);
        presidents.add(richardNixon);
        presidents.add(warrenGHarding);
        presidents.add(theodoreRoosevelt);
        presidents.add(franklinDRoosevelt);
        presidents.add(johnAdams);        
        presidents.add(jamesMadison);
        presidents.add(johnQuincyAdams);
        presidents.add(abrahamLincoln);
        presidents.add(williamHenryHarrison);
        presidents.add(martinVanBuren);
        presidents.add(barackObama);
        presidents.add(jimmyCarter);
        presidents.add(johnTyler);
        presidents.add(jamesKPolk);
        presidents.add(zacharyTaylor);
        presidents.add(georgeWBush);
        presidents.add(johnFKennedy);
        presidents.add(georgeHWBush);
        presidents.add(geraldFord);
        presidents.add(williamHowardTaft);
        presidents.add(herbertHoover);
        presidents.add(harrySTruman);
        presidents.add(dwightEisenhower);
        presidents.add(benjaminHarrison);
        presidents.add(chesterAArthur);
        presidents.add(rutherfordBHayes);
        presidents.add(andrewJohnson);
        presidents.add(jamesBuchanan);
        presidents.add(franklinPierce);
        presidents.add(joeBiden);
        
        nonpresidents.add(johnCCalhoun);
        nonpresidents.add(henryClay);
        nonpresidents.add(alexanderHamilton);
        nonpresidents.add(robertATaft);
        nonpresidents.add(williamJenningsBryan);
        nonpresidents.add(alGore);
        nonpresidents.add(georgeWallace);
        nonpresidents.add(barryGoldwater);
        nonpresidents.add(johnMcCain);
        nonpresidents.add(michaelDukakis);
        nonpresidents.add(ronDeSantis);
        nonpresidents.add(hillaryClinton);
        nonpresidents.add(mittRomney);
        nonpresidents.add(garyJohnson);
        nonpresidents.add(rossPerot);
        nonpresidents.add(robertLaFollette);
        nonpresidents.add(charlesLindbergh);
        nonpresidents.add(williamHSeward);
        nonpresidents.add(josephMcCarthy);
        nonpresidents.add(stromThurmond);
        nonpresidents.add(frederickDouglass);
        nonpresidents.add(eugeneMcCarthy);
        nonpresidents.add(hubertHumphrey);
        nonpresidents.add(douglasMacArthur);
        nonpresidents.add(johnDRockefeller);
        nonpresidents.add(dickCheney);
        nonpresidents.add(alSmith);
        nonpresidents.add(patrickHenry);
        nonpresidents.add(patBuchanan);
        nonpresidents.add(thomasPaine);
        nonpresidents.add(marcoRubio);
        nonpresidents.add(bernieSanders);
        nonpresidents.add(ronPaul);
        nonpresidents.add(johnMuir);
        nonpresidents.add(georgeMcClellan);
        nonpresidents.add(stephenADouglas);
        nonpresidents.add(thaddeusStevens);
        nonpresidents.add(johnCFremont);
        nonpresidents.add(horaceGreeley);
        nonpresidents.add(robertELee);
        nonpresidents.add(nelsonRockefeller);
        nonpresidents.add(thomasDewey);
        nonpresidents.add(ralphNader);
        nonpresidents.add(georgeMcGovern);
        nonpresidents.add(bobbyKennedy);
        nonpresidents.add(georgePatton);
        nonpresidents.add(shirleyChisholm);
        nonpresidents.add(bettyFord);
        nonpresidents.add(sarahPalin);
        nonpresidents.add(jackKemp);
        nonpresidents.add(howardDean);
        nonpresidents.add(rudyGiuliani);
        nonpresidents.add(martinLutherKingJr);
        nonpresidents.add(danQuayle);
        nonpresidents.add(hueyLong);
        nonpresidents.add(colinPowell);
        nonpresidents.add(susanBAnthony);
        nonpresidents.add(walterMondale);
        nonpresidents.add(spiroAgnew);
        nonpresidents.add(alfLandon);
        nonpresidents.add(wendellWillkie);
        nonpresidents.add(johnNanceGarner);
        nonpresidents.add(henryFord);
        nonpresidents.add(charlesSumner);
        nonpresidents.add(johnBrown);
        nonpresidents.add(danielWebster);
        nonpresidents.add(johnBell);
        nonpresidents.add(winfieldScott);
        nonpresidents.add(johnCBreckinridge);
        nonpresidents.add(jebBushh);
        nonpresidents.add(mikePence);
        nonpresidents.add(margaretChaseSmith);   
        nonpresidents.add(bobDole);
        nonpresidents.add(eugeneDebs);
        nonpresidents.add(garyHart);
        nonpresidents.add(jerryBrown);
        nonpresidents.add(newtGingrich);
        nonpresidents.add(paulTsongas);
        nonpresidents.add(marioCuomo);
        nonpresidents.add(jesseJackson);
        nonpresidents.add(pramilaJayapal);
        nonpresidents.add(staceyAbrams);
        nonpresidents.add(sherrodBrown);
        nonpresidents.add(randPaul);
        nonpresidents.add(williamEMiller);
        nonpresidents.add(benjaminFranklin);
        nonpresidents.add(frankKnox);
        nonpresidents.add(johnJay);
        nonpresidents.add(eleanorRoosevelt);
        nonpresidents.add(williamFBuckley);
        nonpresidents.add(williamLloydGarrison);
        nonpresidents.add(georgeClinton);
        nonpresidents.add(jpMorgan);
        nonpresidents.add(rufusKing);
        nonpresidents.add(joeTRobinson);
        nonpresidents.add(williamScranton);
        nonpresidents.add(georgeRomney);
        nonpresidents.add(elbridgeGerry);
        nonpresidents.add(henryCabotLodgeJr);
        nonpresidents.add(charlesCPinckney);
        nonpresidents.add(adlaiStevensonII);
        nonpresidents.add(henryWilson);
        nonpresidents.add(ronJohnson);
        nonpresidents.add(johnMarshall);
        nonpresidents.add(thomasPinckney);
        nonpresidents.add(charlesPinckney);
        nonpresidents.add(henryLeeIII);
        nonpresidents.add(jamesDuane);
        nonpresidents.add(georgeCabot);
        nonpresidents.add(timothyPickering);
        nonpresidents.add(henryKnox);
        nonpresidents.add(philipSchuyler);
        nonpresidents.add(calebStrong);
        nonpresidents.add(johnLowellJr);
        nonpresidents.add(johnEagerHoward);
        nonpresidents.add(edmundRandolph);
        nonpresidents.add(jonathanDayton);
        nonpresidents.add(williamLivingston);
        nonpresidents.add(samuelHuntington);
        nonpresidents.add(richardVarick);
        nonpresidents.add(noahWebster);
        nonpresidents.add(increaseSumner);
        nonpresidents.add(oliverWolcottJr);
        nonpresidents.add(gouverneurMorris);
        nonpresidents.add(josiahQuincyIII);
        nonpresidents.add(jamesWilson);
        nonpresidents.add(oliverEllsworth);
        nonpresidents.add(jamesMcHenry);
        nonpresidents.add(johnBrooks);
        nonpresidents.add(christopherGore);
        nonpresidents.add(thomasWilling);
        nonpresidents.add(williamPaterson);
        nonpresidents.add(charlesWFairbanks);
        nonpresidents.add(altonBParker);
        nonpresidents.add(jamesSSherman);
        nonpresidents.add(adlaiStevensonI);
        nonpresidents.add(henryGDavis);
        nonpresidents.add(johnWKern);
        nonpresidents.add(williamAWheeler);
        nonpresidents.add(samuelJTilden);
        nonpresidents.add(jamesGBlaine);
        nonpresidents.add(thomasAHendricks);
        nonpresidents.add(winfieldScottHancock);
        nonpresidents.add(williamHaydenEnglish);
        nonpresidents.add(joshHawley);
        nonpresidents.add(maryPeltola); 
        nonpresidents.add(leeZeldin);
        nonpresidents.add(glennYoungkin);
        nonpresidents.add(gretchenWhitmer);
        nonpresidents.add(kamalaHarris);
        nonpresidents.add(johnHancock);
        nonpresidents.add(danielDTompkins);
        nonpresidents.add(samuelAdams);
        nonpresidents.add(abigailAdams);
        nonpresidents.add(williamCarroll);
        nonpresidents.add(aaronBurr);
        nonpresidents.add(johnTylerSr);
        nonpresidents.add(dewittClinton);
        nonpresidents.add(melanctonSmith);
        nonpresidents.add(davidRittenhouse);
        nonpresidents.add(jamesFenner);
        nonpresidents.add(israelSmith);
        nonpresidents.add(richardHenryLee);
        nonpresidents.add(johnRandolph);
        nonpresidents.add(jaredIrwin);
        nonpresidents.add(georgeMason);
        nonpresidents.add(williamCrawford);
        nonpresidents.add(robertYates);
        nonpresidents.add(georgeTroup);
        nonpresidents.add(lutherMartin);
        nonpresidents.add(charlesScott);
        nonpresidents.add(mercyOtisWarren);
        nonpresidents.add(albertGallatin);
        nonpresidents.add(nathanielMacon);
        nonpresidents.add(johnBreckinridge);
        nonpresidents.add(johnLangdon);
        nonpresidents.add(jamesWarren);
        nonpresidents.add(danielShays);
        nonpresidents.add(arthurFenner);
        nonpresidents.add(jamesSullivan);
        nonpresidents.add(morganLewis);
        nonpresidents.add(williamBlount);
        nonpresidents.add(johnTaylor);
        nonpresidents.add(josephBradleyVarnum);
        nonpresidents.add(peterPorter);
        nonpresidents.add(jamesJackson);
        nonpresidents.add(johnLansingJr);
        nonpresidents.add(philipBBarbour);
        nonpresidents.add(langdonCheves);
        nonpresidents.add(simonSnyder);
        nonpresidents.add(thomasMcKean);
        nonpresidents.add(jamesBarbour);
        nonpresidents.add(jamesGarrard);
        nonpresidents.add(nicholasGilman);
        nonpresidents.add(rickSantorum);
        nonpresidents.add(schuylerColfax);
        nonpresidents.add(benjaminWade);
        nonpresidents.add(reubenFenton);
        nonpresidents.add(horatioSeymour);
        nonpresidents.add(francisPrestonBlairJr);
        nonpresidents.add(georgeHPendleton);
        nonpresidents.add(andrewGreggCurtin);
        nonpresidents.add(jamesSpeed);
        nonpresidents.add(jamesHarlan);
        nonpresidents.add(jamesRoodDoolittle);
        nonpresidents.add(joelParker);
        nonpresidents.add(reverdyJohnson);
        nonpresidents.add(pierceButler);
        nonpresidents.add(hughWilliamson);
        nonpresidents.add(theodoreSedgwick);
        nonpresidents.add(johnFrancisMercer);
        nonpresidents.add(johnDickinson);
        nonpresidents.add(ninianEdwards);
        nonpresidents.add(fisherAmes);
        nonpresidents.add(rogerGriswold);
        nonpresidents.add(josephDennie);
        nonpresidents.add(robertRLivingston);
        nonpresidents.add(jamesTallmadgeJr);
        nonpresidents.add(frederickMuhlenberg);
        nonpresidents.add(ambroseBurnside);
        nonpresidents.add(benjaminLincoln);
        nonpresidents.add(samuelJohnston);
        nonpresidents.add(jamesABayard);
        nonpresidents.add(philipFreneau);
        nonpresidents.add(williamMaclay);
        nonpresidents.add(jonathanJennings);
        nonpresidents.add(rogerSherman);
        nonpresidents.add(theophilusParsons);
        nonpresidents.add(francisDana);
        nonpresidents.add(samuelLMitchill);
        nonpresidents.add(henryDearborn);
        nonpresidents.add(williamBranchGiles);
        nonpresidents.add(thomasMifflin);
        nonpresidents.add(nathanDane);
        nonpresidents.add(benjaminGoodhue);
        nonpresidents.add(abrahamBaldwin);
        nonpresidents.add(williamFew);
        nonpresidents.add(benjaminFranklinBache);
        nonpresidents.add(israelThorndike);
        nonpresidents.add(jamesIredell);
        nonpresidents.add(samuelChase);
        nonpresidents.add(matthewLyon);
        nonpresidents.add(johnWTaylor);
        nonpresidents.add(williamGrayson);
        nonpresidents.add(johnRutledge);
        nonpresidents.add(robertMorris);
        nonpresidents.add(jonathanJackson);
        nonpresidents.add(benjaminHawkins);
        nonpresidents.add(williamCCClaiborne);
        nonpresidents.add(henryTazewell);
        nonpresidents.add(jamesHillhouse);
        nonpresidents.add(williamRichardsonDavie);
        nonpresidents.add(jamesLloyd);
        nonpresidents.add(peterMuhlenberg);
        nonpresidents.add(johnMilledge);
        nonpresidents.add(nathanWilliams);
        nonpresidents.add(josephHemphill);
        nonpresidents.add(aaronOgden);
        nonpresidents.add(williamShepard);
        nonpresidents.add(johnChandler);
        nonpresidents.add(philipVanCortland);
        nonpresidents.add(edwardTiffin);
        nonpresidents.add(theodoreDwight);
        nonpresidents.add(jamesRoss);
        nonpresidents.add(samuelWhite);
        nonpresidents.add(meriwetherLewis);
        nonpresidents.add(davidStone);
        nonpresidents.add(williamWyattBibb);
        nonpresidents.add(johnWitherspoon);
        nonpresidents.add(edwardRutledge);
        nonpresidents.add(johnFenno);
        nonpresidents.add(georgeClintonJr);
        nonpresidents.add(thomasSumter);
        nonpresidents.add(josephAnderson);
        nonpresidents.add(stephenVanRensselaer);
        nonpresidents.add(robertGoodloeHarper);
        nonpresidents.add(danielOfStThomas);
        nonpresidents.add(wadeHamptonI);
        nonpresidents.add(williamFindley);
        nonpresidents.add(georgeLogan);
        nonpresidents.add(alexanderMartin);
        nonpresidents.add(georgeMathews);
        nonpresidents.add(georgeWythe);
        nonpresidents.add(leviLincolnSr);
        nonpresidents.add(josephDesha);
        nonpresidents.add(samuelDexter);
        nonpresidents.add(williamSamuelJohnson);
        nonpresidents.add(stephenHigginson);
        nonpresidents.add(jaredIngersoll);
        nonpresidents.add(samuelSmith);
        nonpresidents.add(jamesTurner);
        nonpresidents.add(lafayette);
        nonpresidents.add(harrisonGrayOtis);
        nonpresidents.add(jamesGunn);
        nonpresidents.add(williamBingham);
        nonpresidents.add(williamDuane);
        nonpresidents.add(williamPinkney);
        nonpresidents.add(thomasRandolphJr);
        nonpresidents.add(asaHutchinson);
        nonpresidents.add(dougMastriano);
        nonpresidents.add(paulRyan);
        nonpresidents.add(elizabethWarren);
        nonpresidents.add(catherineCortezMasto);
        nonpresidents.add(andrewCuomo);
        nonpresidents.add(williamCushing);
        nonpresidents.add(johnBlairJr);
        nonpresidents.add(paulHamilton);
        nonpresidents.add(philipReed);
        nonpresidents.add(johnArmstrongJr);
        nonpresidents.add(williamLowndes);
        nonpresidents.add(bushrodWashington);
        nonpresidents.add(thomasFitzsimons);
        nonpresidents.add(jonathanTrumbullJr);
        nonpresidents.add(williamEustis);
        nonpresidents.add(williamJones);
        nonpresidents.add(matthewClay);
        nonpresidents.add(thomasJohnson);
        nonpresidents.add(alfredMoore);
        nonpresidents.add(isaacShelby);
        nonpresidents.add(stephenRBradley);
        nonpresidents.add(johnSevier);
        nonpresidents.add(michaelLeib);
        nonpresidents.add(mehmetOz);
        nonpresidents.add(thomasMassie);
        nonpresidents.add(katieBritt);
        nonpresidents.add(chuckSchumer);
        nonpresidents.add(kathyHochul);
        nonpresidents.add(alexandriaOcasioCortez);
        nonpresidents.add(tuckerCarlson);
        nonpresidents.add(marjorieTaylorGreene);
        nonpresidents.add(timScott);
        nonpresidents.add(peteButtigieg);
        nonpresidents.add(tulsiGabbard);
        nonpresidents.add(joeManchin);
        nonpresidents.add(brianKemp);
        nonpresidents.add(brettFavre);
        nonpresidents.add(elonMusk);
        nonpresidents.add(gavinNewsom);
        nonpresidents.add(leBronJames);
        nonpresidents.add(raphaelWarnock);
        nonpresidents.add(seanPatrickMaloney); 
        nonpresidents.add(johnFetterman);
        nonpresidents.add(joshShapiro);
        nonpresidents.add(mikeLawler);
        nonpresidents.add(georgeSantos);
        nonpresidents.add(susanCollins);
        nonpresidents.add(mikeLee);
        nonpresidents.add(jdVance);
        nonpresidents.add(kevinMcCarthy);
        nonpresidents.add(nancyPelosi);
        nonpresidents.add(philMurphy);
        nonpresidents.add(dickDurbin);
        nonpresidents.add(alexanderConteeHanson);
        nonpresidents.add(charlesFMercer);
        nonpresidents.add(johnScott);
        nonpresidents.add(jesseBThomas);
        nonpresidents.add(alexanderJDallas);
        nonpresidents.add(williamWirt);
        nonpresidents.add(richardRush);
        nonpresidents.add(johnAndrewShulze);
        nonpresidents.add(nathanSanford);
        nonpresidents.add(francisPrestonBlair);
        nonpresidents.add(jamesBrown);
        nonpresidents.add(samuelLSouthard);
        nonpresidents.add(smithThompson);
        nonpresidents.add(solomonSouthwick);
        nonpresidents.add(williamAPalmer);
        nonpresidents.add(thomasHartBenton);
        nonpresidents.add(georgeWolf);
        nonpresidents.add(andrewStevenson);
        nonpresidents.add(johnSergeant);
        nonpresidents.add(johnMcLean);
        nonpresidents.add(rufusChoate);
        nonpresidents.add(richardMJohnson);
        nonpresidents.add(williamCabellRives);
        nonpresidents.add(johnMBerrien);
        nonpresidents.add(francisGranger);
        nonpresidents.add(hughLWhite);
        nonpresidents.add(willieMagnum);
        nonpresidents.add(williamSmith);
        nonpresidents.add(felixGrundy);
        nonpresidents.add(williamLegett);
        nonpresidents.add(robertCWinthrop);
        nonpresidents.add(abbotLawrence);
        nonpresidents.add(johnJCrittenden);
        nonpresidents.add(georgeMcDuffie);
        nonpresidents.add(littletonWTazewell);
        nonpresidents.add(johnCatron);
        nonpresidents.add(kyrstenSinema);
        nonpresidents.add(chuckGrassley);
        nonpresidents.add(eliseStefanik);
        nonpresidents.add(jaredGolden);
        nonpresidents.add(scottWalker);
        nonpresidents.add(johnPKennedy);
        nonpresidents.add(charlesFrancisAdamsSr);
        nonpresidents.add(benjaminRobbinsCurtis);
        nonpresidents.add(robertYHayne);
        nonpresidents.add(amosKendall);
        nonpresidents.add(rogerBTaney);
        nonpresidents.add(mikePompeo);
        nonpresidents.add(jaredKushner);
        nonpresidents.add(mattGaetz);
        nonpresidents.add(amyKlobuchar);
        nonpresidents.add(ilhanOmar);
        nonpresidents.add(janetYellen);
        nonpresidents.add(theodoreFreling);
        nonpresidents.add(johnDavis);
        nonpresidents.add(johnMClayton);
        nonpresidents.add(georgeMDallas);
        nonpresidents.add(silasWright);
        nonpresidents.add(johnFairfield);
        nonpresidents.add(georgeEvans);
        nonpresidents.add(williamCPreston);
        nonpresidents.add(stephenDMiller);
        nonpresidents.add(gulianGVerplanck);
        nonpresidents.add(louisMcLane);
        nonpresidents.add(georgePoindexter);
        nonpresidents.add(thomasEwing);
        nonpresidents.add(williamWEllsworth);
        nonpresidents.add(benjaminWLeigh);
        nonpresidents.add(lewisCass);
        nonpresidents.add(leviWoodbury);
        nonpresidents.add(williamOButler);
        nonpresidents.add(ninaTurner);
        nonpresidents.add(jaredPolis);
        nonpresidents.add(chrisSununu);
        nonpresidents.add(philScott);
        nonpresidents.add(charlieBaker);
        nonpresidents.add(andyBeshear);
        nonpresidents.add(johnBelEdwards);
        nonpresidents.add(lauraKelly);
        nonpresidents.add(nikkiHaley);
        nonpresidents.add(coreyStapleton);
        nonpresidents.add(kariLake);
        nonpresidents.add(markKelly);
        nonpresidents.add(katieHobbs);
        nonpresidents.add(michelleObama);
        nonpresidents.add(williamMarbury);
        nonpresidents.add(richardStockton);
        nonpresidents.add(williamLSmith);
        nonpresidents.add(williamClark);
        nonpresidents.add(davidHolmes);
        nonpresidents.add(caeserAugustusRodney);
        nonpresidents.add(gaylordGriswold);
        nonpresidents.add(johnCottonSmith);
        nonpresidents.add(sethHastings);
        nonpresidents.add(johnClopton);
        nonpresidents.add(johnSwanwick);
        nonpresidents.add(robertSmith);
        nonpresidents.add(vivekRamaswamy);
        nonpresidents.add(jamesBuckley);
        nonpresidents.add(johnRBrinkley);
        nonpresidents.add(marianneWilliamson);
        nonpresidents.add(phyllisSchlafy);
        nonpresidents.add(larryHogan); 
        nonpresidents.add(blancheLincoln);
        nonpresidents.add(kristiNoem);
        nonpresidents.add(lizCheney);
        nonpresidents.add(kimReynolds);
        nonpresidents.add(andrewYang);
        nonpresidents.add(betoORourke);
        nonpresidents.add(bennieThompson);
        nonpresidents.add(gregAbbott);
        nonpresidents.add(blakeMasters);
        nonpresidents.add(tomCotton);
        nonpresidents.add(wesMoore);
        nonpresidents.add(jonOssoff);
        nonpresidents.add(kirstenGillibrand);
        nonpresidents.add(kellyAyotte);
        nonpresidents.add(nathanielPTallmadge);
        nonpresidents.add(nicholasBiddle);
        nonpresidents.add(johnGPalfrey);
        nonpresidents.add(johnBranch);
        nonpresidents.add(jamesHHammond);
        nonpresidents.add(williamLMarcy);
        nonpresidents.add(thomasCorwin);
        nonpresidents.add(hannibalHamlin);
        nonpresidents.add(edwardEverett);
        nonpresidents.add(jeffersonDavis);
        nonpresidents.add(robertBRhett);
        nonpresidents.add(josephLane);
        nonpresidents.add(salmonPChase);
        nonpresidents.add(simonCameron);
        nonpresidents.add(williamTSherman);
        nonpresidents.add(stonewallJackson);
        nonpresidents.add(nathanBedfordForrest);
        nonpresidents.add(johnAdamsDix);
        nonpresidents.add(edwardBates);
        nonpresidents.add(thurlowWeed);
        nonpresidents.add(cassiusMClay);
        nonpresidents.add(williamLowndesYancy);
        nonpresidents.add(jamesGuthrie);
        nonpresidents.add(robertMTHunter);
        nonpresidents.add(alDAmato);
        nonpresidents.add(jesseHelms);
        nonpresidents.add(robertHMichel);
        nonpresidents.add(robertByrd);
        nonpresidents.add(johnCStennis);
        nonpresidents.add(tipONeill);
        nonpresidents.add(marshaBlackburn);
        nonpresidents.add(larryElder);
        nonpresidents.add(dougBurgum);
        nonpresidents.add(garyPeters);
        nonpresidents.add(royCooper);
        nonpresidents.add(maggieHassan);
        nonpresidents.add(johnKennedy);
        nonpresidents.add(lindseyGraham);
        nonpresidents.add(donBolduc);
        nonpresidents.add(robertFKennedyJr);
        nonpresidents.add(jayInslee);
        nonpresidents.add(maxwellFrost);
        nonpresidents.add(markHanna);
        nonpresidents.add(harrySNew);
        nonpresidents.add(philanderCKnox);
        nonpresidents.add(williamRandolphHearst);
        nonpresidents.add(francisCockrell);
        nonpresidents.add(georgebMcClellanJr);
        nonpresidents.add(charlesEvansHughes);
        nonpresidents.add(hiramJohnson);
        nonpresidents.add(nelsonwAldrich);
        nonpresidents.add(edithWilson);
        nonpresidents.add(thomasRMarshall);
        nonpresidents.add(champClark);
        nonpresidents.add(elihuRoot);
        nonpresidents.add(nicholasMButler);
        nonpresidents.add(albertBCummins);
        nonpresidents.add(judsonHarmon);
        nonpresidents.add(oscarUnderwood);
        nonpresidents.add(johnAlbertJohnson);
        nonpresidents.add(henryCabotLodge);
        nonpresidents.add(josephGurneyCannon);
        nonpresidents.add(curtisGuildJr);
        nonpresidents.add(charlesWBryan);
        nonpresidents.add(edwardCWall);
        nonpresidents.add(robertLansing);
        nonpresidents.add(andrewMellon);
        nonpresidents.add(orvilleHPlatt);
        nonpresidents.add(williamBAllison);
        nonpresidents.add(johnCoitSpooner);
        nonpresidents.add(williamAllen);
        nonpresidents.add(jamesBroadhead);
        nonpresidents.add(johnKelly);
        nonpresidents.add(johnSherman);
        nonpresidents.add(benjaminBristow);
        nonpresidents.add(williamTweed);
        nonpresidents.add(lukePBlackburn);
        nonpresidents.add(williamGaston);
        nonpresidents.add(roscoeconkling);
        nonpresidents.add(williammevarts);
        nonpresidents.add(charlesdevens);
        nonpresidents.add(michaelckerr);
        nonpresidents.add(johnwstevenson);
        nonpresidents.add(luciusqclamar);
        nonpresidents.add(jamesburrillangell);
        nonpresidents.add(stephenbpackard);
        nonpresidents.add(hermanlhumphrey);
        nonpresidents.add(richardpbland);
        nonpresidents.add(wadehamptoniii);
        nonpresidents.add(elihuwashburne);
        nonpresidents.add(georgefedmunds);
        nonpresidents.add(marshalljewell);
        nonpresidents.add(thomasfbayard);
        nonpresidents.add(samueljrandall);
        nonpresidents.add(henrybpayne);
        nonpresidents.add(alonzobcornell);
        nonpresidents.add(henrywblair);
        nonpresidents.add(frederickwseward);
        nonpresidents.add(henrymmathews);
        nonpresidents.add(williamrallsmorrison);
        nonpresidents.add(stephenjfield);
        nonpresidents.add(williamwindom);
        nonpresidents.add(roberttoddlincoln);
        nonpresidents.add(thomaslemueljames);
        nonpresidents.add(alcibiadesdeblanc);
        nonpresidents.add(ishamgharris);
        nonpresidents.add(jamesmsmith);
        nonpresidents.add(fredericktfrelinghuysen);
        nonpresidents.add(charlesjfolger);
        nonpresidents.add(benjaminhbrewster);
        nonpresidents.add(johnmstone);
        nonpresidents.add(georgeshouston);
        nonpresidents.add(zebulonvance);
        nonpresidents.add(johnfhartranft);
        nonpresidents.add(oliverpmorton);
        nonpresidents.add(charlesjguiteau);
        nonpresidents.add(davidmkey);
        nonpresidents.add(luciusrobinson);
        nonpresidents.add(jamesaenglish);
        nonpresidents.add(thomaswferry);
        nonpresidents.add(henrybanthony);
        nonpresidents.add(josephrhawley);
        nonpresidents.add(williamawallace);
        nonpresidents.add(hiesterclymber);
        nonpresidents.add(jcsblackburn);
        nonpresidents.add(williamhrobertson);
        nonpresidents.add(thomascplatt);
        nonpresidents.add(williamdkelley);
        nonpresidents.add(johngcarlisle);
        nonpresidents.add(georgewgeddes);
        nonpresidents.add(williamrosecrans);
        nonpresidents.add(johnfranklinmiller);
        nonpresidents.add(horacefpage);
        nonpresidents.add(samueljkirkwood);
        nonpresidents.add(danielwvoorhees);
        nonpresidents.add(jamesgfair);
        nonpresidents.add(johnrmcpherson);
        nonpresidents.add(eugenehale);
        nonpresidents.add(williamechandler);
        nonpresidents.add(georgewmccrary);
        nonpresidents.add(williammahone);
        nonpresidents.add(harrisonhriddleberger);
        nonpresidents.add(williamecameron);
        nonpresidents.add(stanleymatthews);
        nonpresidents.add(johndavislong);
        nonpresidents.add(georgefhoar);
        nonpresidents.add(josephemcdonald);
        nonpresidents.add(roswellpflower);
        nonpresidents.add(williamfvilas);
        nonpresidents.add(richardfpettigrew);
        nonpresidents.add(jwarrenkeifer);
        nonpresidents.add(leonidaschoulk);
        nonpresidents.add(georgehoadly);
        nonpresidents.add(danielmanning);
        nonpresidents.add(thomasfgrady);
        nonpresidents.add(jdonaldcameron);
        nonpresidents.add(benjaminfmarsh);
        nonpresidents.add(williamoconnellbradley);
        nonpresidents.add(allengthurman);
        nonpresidents.add(henrywslocum);
        nonpresidents.add(johntmorgan);
        nonpresidents.add(johnrthomas);
        nonpresidents.add(warnermiller);
        nonpresidents.add(williamwhunt);
        nonpresidents.add(clarksonnottpotter);
        nonpresidents.add(charlesehooker);
        nonpresidents.add(jproctorknott);
        nonpresidents.add(thomasjbrady);
        nonpresidents.add(stephenwdorsey);
        nonpresidents.add(timothyohowe);
        nonpresidents.add(jameshslater);
        nonpresidents.add(samuelbmaxey);
        nonpresidents.add(charleswjones);
        nonpresidents.add(jamesfwilson);
        nonpresidents.add(nathanielphill);
        nonpresidents.add(thomaswpalmer);
        nonpresidents.add(randalllgibson);
        nonpresidents.add(jamestfarley);
        nonpresidents.add(jamesbbeck);
        nonpresidents.add(dwightmsabin);
        nonpresidents.add(williampfrye);
        nonpresidents.add(prestonbplumb);
        nonpresidents.add(wilkinsoncall);
        nonpresidents.add(josephebrown);
        nonpresidents.add(alfredhcolquitt);
        nonpresidents.add(johnalogan);
        nonpresidents.add(powellclayton);
        nonpresidents.add(johnrlynch);
        nonpresidents.add(arthurpgorman);
        nonpresidents.add(richardbhubbard);
        nonpresidents.add(danielllockwood);
        nonpresidents.add(johnbhenderson);
        nonpresidents.add(charlesfoster);
        nonpresidents.add(thomasnast);
        nonpresidents.add(charlessfairchild);
        nonpresidents.add(williamcendicott);
        nonpresidents.add(augustushgarland);
        nonpresidents.add(henryldawes);
        nonpresidents.add(williamrmoore);
        nonpresidents.add(henrymteller);
        nonpresidents.add(rogerqmills);
        nonpresidents.add(williamlscott);
        nonpresidents.add(donaldmdickinson);
        nonpresidents.add(shelbymcullom);
        nonpresidents.add(johnjingalls);
        nonpresidents.add(knutenelson);
        nonpresidents.add(williamcwhitney);
        nonpresidents.add(normanjaycoleman);
        nonpresidents.add(williamhhatch);
        nonpresidents.add(jonathanchace);
        nonpresidents.add(personcolbycheney);
        nonpresidents.add(johnimitchell);
        nonpresidents.add(johnrandolphtucker);
        nonpresidents.add(richardcoke);
        nonpresidents.add(carlschurz);
        nonpresidents.add(georgehsharpe);
        nonpresidents.add(waynemacveagh);
        nonpresidents.add(benjaminfjonas);
        nonpresidents.add(jameszgeorge);
        nonpresidents.add(jamesblackgroome);
        nonpresidents.add(russellaalger);
        nonpresidents.add(chaunceydepew);
        nonpresidents.add(isaacpgray);
        nonpresidents.add(johncblack);
        nonpresidents.add(stephenmwhite);
        nonpresidents.add(levipmorton);
        nonpresidents.add(williamwphelps);
        nonpresidents.add(jeremiahmrusk);
        nonpresidents.add(walterqgresham);
        nonpresidents.add(silaswoodson);
        nonpresidents.add(prestonleslie);
        nonpresidents.add(redfieldproctor);
        nonpresidents.add(johnwanamaker);
        nonpresidents.add(williamhhmiller);
        nonpresidents.add(johnbgordon);
        nonpresidents.add(charlesoconor);
        nonpresidents.add(thomasccatchings);
        nonpresidents.add(williamemason);
        nonpresidents.add(charlesfmanderson);
        nonpresidents.add(thomasbracketreed);
        nonpresidents.add(rufusblodgett);
        nonpresidents.add(hughsmiththompson);
        nonpresidents.add(williamsholman);
        nonpresidents.add(petercooper);
        nonpresidents.add(samuelfcary);
        nonpresidents.add(barzillaivchambers);
        nonpresidents.add(benjaminbutler);
        nonpresidents.add(absolommwest);
        nonpresidents.add(thomasewingjr);
        nonpresidents.add(edwardhgillette);
        nonpresidents.add(hendrickbwright);
        nonpresidents.add(thompsonhmurch);
        nonpresidents.add(lumanhamlinweller);
        nonpresidents.add(newtonbooth);
        nonpresidents.add(ignatiusldonnelly);
        nonpresidents.add(whitelawreid);
        nonpresidents.add(benjaminftracy);
        nonpresidents.add(edwardowolcott);
        nonpresidents.add(davidbhill);
        nonpresidents.add(horaceboies);
        nonpresidents.add(calvinsbrice);
        nonpresidents.add(thomashcarter);
        nonpresidents.add(thomasjhenderson);
        nonpresidents.add(johnwfoster);
        nonpresidents.add(leonabbett);
        nonpresidents.add(edwardmurphyjr);
        nonpresidents.add(johnlmitchell);
        nonpresidents.add(freddubois);
        nonpresidents.add(frankjcannon);
        nonpresidents.add(willissweet);
        nonpresidents.add(richardolney);
        nonpresidents.add(danielslamont);
        nonpresidents.add(williamlwilson);
        nonpresidents.add(thomastodd);
        nonpresidents.add(gabrielduvall);
        nonpresidents.add(williamjohnson);
        nonpresidents.add(josephstory);
        nonpresidents.add(henrybrockholstlivingston);
        nonpresidents.add(alfredthayermahan);
        nonpresidents.add(johnqabrackett);
        nonpresidents.add(edwardcurtissmith);
        nonpresidents.add(jsterlingmorton);
        nonpresidents.add(hilaryaherbert);
        nonpresidents.add(wilsonsbissell);
        nonpresidents.add(sanfordbdole);
        nonpresidents.add(johnrileytanner);
        nonpresidents.add(lorinathurston);
        nonpresidents.add(jameshendersonblount);
        nonpresidents.add(johnpeteraltgeld);
        nonpresidents.add(williambhornblower);
        nonpresidents.add(cushmankdavis);
        nonpresidents.add(josephwbabcock);
        nonpresidents.add(georgepullman);
        nonpresidents.add(charlesjfaulkner);
        nonpresidents.add(jamesgmacguire);
        nonpresidents.add(mattwransom);
        nonpresidents.add(garrethobart);
        nonpresidents.add(henryclayevans);
        nonpresidents.add(jamesalbertgary);
        nonpresidents.add(williamerussell);
        nonpresidents.add(johnrmclean);
        nonpresidents.add(claudematthews);
        nonpresidents.add(jamesawalker);
        nonpresidents.add(lymanjgage);
        nonpresidents.add(johnwgriggs);
        nonpresidents.add(arthursewall);
        nonpresidents.add(josephcsibley);
        nonpresidents.add(johnwdaniel);
        nonpresidents.add(corneliusnewtonbliss);
        nonpresidents.add(nelsondingleyjr);
        nonpresidents.add(johnhay);
        nonpresidents.add(johnmpalmer);
        nonpresidents.add(simonbolivarbuckner);
        nonpresidents.add(georgehearst);
        nonpresidents.add(henryfbowers);
        nonpresidents.add(nelsonamiles);
        nonpresidents.add(charlesemorysmith);
        nonpresidents.add(abramhewitt);
        nonpresidents.add(edwardatkinson);
        nonpresidents.add(charlesatowne);
        nonpresidents.add(leemantle);
        nonpresidents.add(georgesboutwell);
        nonpresidents.add(daniellindsayrussell);
        nonpresidents.add(edwinlawrencegodkin);
        nonpresidents.add(williamgrahamsummer);
        nonpresidents.add(johngwarwick);
        nonpresidents.add(andrewcarnegie);
        nonpresidents.add(henryadams);
        nonpresidents.add(theodorerooseveltsr);
        nonpresidents.add(marktwain);
        nonpresidents.add(spencertrask);
        nonpresidents.add(franciskernan);
        nonpresidents.add(jonathanpdolliver);
        nonpresidents.add(timothylwoodruff);
        nonpresidents.add(josephbforaker);
        nonpresidents.add(georgedewey);
        nonpresidents.add(julianscarr);
        nonpresidents.add(johnwaltersmith);
        nonpresidents.add(prescottfhall);
        nonpresidents.add(robertrhitt);
        nonpresidents.add(serenoepayne);
        nonpresidents.add(benjamintillman);
        nonpresidents.add(francisgnewlands);
        nonpresidents.add(josephwbailey);
        nonpresidents.add(jimwilson);
        nonpresidents.add(morganbulkeley);
        nonpresidents.add(albertjbeveridge);
        nonpresidents.add(jamesdrichardson);
        nonpresidents.add(williamsulzer);
        nonpresidents.add(moorfieldstorey);
        nonpresidents.add(johnarchibaldcampbell);
        nonpresidents.add(nathanclifford);
        nonpresidents.add(noahhaynesswayne);
        nonpresidents.add(samuelfreemanmiller);
        nonpresidents.add(daviddavis);
        nonpresidents.add(williamstrong);
        nonpresidents.add(josephpbradley);
        nonpresidents.add(wardhunt);
        nonpresidents.add(morrisonwaite);
        nonpresidents.add(johnmarshallharlan);
        nonpresidents.add(williamburnhamwoods);
        nonpresidents.add(horacegray);
        nonpresidents.add(samuelblatchford);
        nonpresidents.add(melvillefuller);
        nonpresidents.add(davidjbrewer);
        nonpresidents.add(henrybillingsbrown);
        nonpresidents.add(georgesirasjr);
        nonpresidents.add(philipsheridan);
        nonpresidents.add(deniskearney);
        nonpresidents.add(howellejackson);
        nonpresidents.add(henrykyddouglas);
        nonpresidents.add(josephmckenna);
        nonpresidents.add(oliverwendellholmesjr);
        nonpresidents.add(charlesjbonaparte);
        nonpresidents.add(edwarddouglasswhite);
        nonpresidents.add(rufuspeckman);
        nonpresidents.add(johnsharpwilliams);
        nonpresidents.add(williamphepburn);
        nonpresidents.add(williamrday);
        nonpresidents.add(jameshamiltonpeabody);
        nonpresidents.add(alvaadams);
        nonpresidents.add(williamdjohnson);
        nonpresidents.add(murphyjfoster);
        nonpresidents.add(johnflacey);
        nonpresidents.add(bookertwashington);
        nonpresidents.add(edwardbvreeland);
        nonpresidents.add(samuelgompers);
        nonpresidents.add(horaceharmonluton);
        nonpresidents.add(jameskjones);
        nonpresidents.add(boiespenrose);
        nonpresidents.add(williamhenrymoody);
        nonpresidents.add(georgebcortelyou);
        nonpresidents.add(jameshay);
        nonpresidents.add(jamestlloyd);
        nonpresidents.add(napoleonbbroward);
        nonpresidents.add(samuelwpennypacker);
        nonpresidents.add(jamesrudolphgarfield);
        nonpresidents.add(charleswfdick);
        nonpresidents.add(jamesrwilliams);
        nonpresidents.add(georgeturner);
        nonpresidents.add(williamaharris);
        nonpresidents.add(henryclaypayne);
        nonpresidents.add(johndalzell);
        nonpresidents.add(jamesatawney);
        nonpresidents.add(johncmclaurin);
        nonpresidents.add(edwardcarmack);
        nonpresidents.add(benjaminfshively);
        nonpresidents.add(winfieldtdurbin);
        nonpresidents.add(henrymcbride);
        nonpresidents.add(stephenbelkins);
        nonpresidents.add(luciusfcgarvin);
        nonpresidents.add(jeffdavis);
        nonpresidents.add(alexandermdockery);
        nonpresidents.add(jimwright);
        nonpresidents.add(howardbaker);   
        nonpresidents.add(edkoch); 
        nonpresidents.add(jeanekirkpatrick);    
        nonpresidents.add(donaldregan);    
        nonpresidents.add(lawrenceeagleburger); 
        nonpresidents.add(edwinedwards);    
        nonpresidents.add(jamesblack);
        nonpresidents.add(johnstjohn);
        nonpresidents.add(johnbidwell);
        nonpresidents.add(alsonstreeter);
        nonpresidents.add(jonathanblanchard);
        nonpresidents.add(simonwing);
        nonpresidents.add(jamesbweaver);
        nonpresidents.add(thomasewatson);
        nonpresidents.add(jamesgfield);
        nonpresidents.add(maryelizabethlease);
        nonpresidents.add(leonidasjpolk);
        nonpresidents.add(williamapeffer);
        nonpresidents.add(omermkem);
        nonpresidents.add(jerrysimpson);
        nonpresidents.add(marionbutler);
        nonpresidents.add(milfordmhoward);
        nonpresidents.add(jameshkyle);
        nonpresidents.add(johnpbuchanan);
        nonpresidents.add(lmshaw);
        nonpresidents.add(juliuscburrows);
        nonpresidents.add(franklinmurphy);
        nonpresidents.add(georgegray);
        nonpresidents.add(williamldouglas);
        nonpresidents.add(josephwfolk);
        nonpresidents.add(henryclayfrick);
        nonpresidents.add(lelandstanford);
        nonpresidents.add(collisphuntington);
        nonpresidents.add(corneliusvanderbilt);
        nonpresidents.add(jaygould);
        nonpresidents.add(jamesjhill);
        nonpresidents.add(theodoreeburton);
        nonpresidents.add(charleswfulton);
        nonpresidents.add(davidbhenderson);
        nonpresidents.add(charlesnhaskell);
        nonpresidents.add(bentonmcmillan);
        nonpresidents.add(johnssnook);
        nonpresidents.add(francishhitchcock);
        nonpresidents.add(mahlonpitney);
        nonpresidents.add(franklinmacveagh);
        nonpresidents.add(augustusobacon);
        nonpresidents.add(edwinywebb);
        nonpresidents.add(robertlhenry);
        nonpresidents.add(georgevonlengerkemeyer);
        nonpresidents.add(richardaballinger);
        nonpresidents.add(charlesnagel);
        nonpresidents.add(josephrlamar);
        nonpresidents.add(jacobdickinson);
        nonpresidents.add(tomljohnson);
        nonpresidents.add(williampdillingham);
        nonpresidents.add(winthropmcrane);
        nonpresidents.add(isaacstephenson);
        nonpresidents.add(jamesplclark);
        nonpresidents.add(robertfbroussard);
        nonpresidents.add(shubertdentjr);
        nonpresidents.add(josephlbristow);
        nonpresidents.add(herbertshadley);
        nonpresidents.add(georgewperkins);
        nonpresidents.add(johnburke);
        nonpresidents.add(georgeechamberlain);
        nonpresidents.add(seabornroddenberry);
        nonpresidents.add(moseseclapp);
        nonpresidents.add(jonathanbournejr);
        nonpresidents.add(williamspry);
        nonpresidents.add(josephptumulty);
        nonpresidents.add(thomaswgregory);
        nonpresidents.add(williamcredfield);
        nonpresidents.add(williameglasscock);
        nonpresidents.add(josephmdixon);
        nonpresidents.add(williambmckinley);
        nonpresidents.add(jamessmithjr);
        nonpresidents.add(newtondbaker);
        nonpresidents.add(edwardmhouse);
        nonpresidents.add(victormurdock);
        nonpresidents.add(jamesrmann);
        nonpresidents.add(johnjesch);
        nonpresidents.add(josephusdaniels);
        nonpresidents.add(claudekitchin);
        nonpresidents.add(jameskvardaman);
        nonpresidents.add(juliuskhan);
        nonpresidents.add(francisemcgovern);
        nonpresidents.add(walterrstubbs);
        nonpresidents.add(georgebmharvey);
        nonpresidents.add(robertlowen);
        nonpresidents.add(henrydclayton);
        nonpresidents.add(andrewvolstead);
        nonpresidents.add(robertpbass);
        nonpresidents.add(jacobhgallinger);
        nonpresidents.add(jamesemartine);
        nonpresidents.add(paulwarbug);
        nonpresidents.add(williamcadamson);
        nonpresidents.add(francisewarren);
        nonpresidents.add(williamskenyon);
        nonpresidents.add(benwhooper);
        nonpresidents.add(albertsburleson);
        nonpresidents.add(eliasmammons);
        nonpresidents.add(edwardkeating);
        nonpresidents.add(williamborah);
        nonpresidents.add(aslegronna);
        nonpresidents.add(lawrenceysherman);
        nonpresidents.add(jamesareed);
        nonpresidents.add(harrylane);
        nonpresidents.add(williamjstone);
        nonpresidents.add(johnwweeks);
        nonpresidents.add(mgbrumbaugh);
        nonpresidents.add(samuelwmccall);
        nonpresidents.add(roberteburke);
        nonpresidents.add(williamajones);
        nonpresidents.add(franklinknightlane);
        nonpresidents.add(joerogan);
        nonpresidents.add(annapaulinaluna);
        nonpresidents.add(markcuban);
        nonpresidents.add(joshstein);
        nonpresidents.add(davemccormick);
        nonpresidents.add(brianfitzpatrick);
        nonpresidents.add(markrobinson);
        nonpresidents.add(bobcaseyjr);
        nonpresidents.add(timwalz);
        nonpresidents.add(ericadams);




        







        
        
        
        
        
        
        
        
        

        
   
        

        

        
        
        meme.add(millFill);
        meme.add(jebBush);
        meme.add(dababy);
        meme.add(emperorpalpatine);
        meme.add(odysseus);
        meme.add(napoleon);
        meme.add(libRight);
        meme.add(authRight);
        meme.add(authLeft);
        meme.add(libLeft);
        meme.add(churchill);
        meme.add(monke);
        meme.add(impostor);
        meme.add(comrademccain);
        meme.add(evilGeorgeWallace);
        meme.add(postMalone);
        for (int i = 0; i < 5; i++) {
            meme.add(michaelDukakis);
        }
        meme.add(warrenG);
        meme.add(geraldMander);
        meme.add(joeLiberal);
        meme.add(honestLizCheney);
        meme.add(kimkardashian);
        meme.add(lolalago);
        meme.add(kanyeWest);
        /** no parentheses? :(
        meme.add(charliecristrep);
        meme.add(charliecristind);
        meme.add(charliecristdem);
        meme.add(charliecristwhig);
        meme.add(charliecristdemrep);
        meme.add(charliecristfed);
        **/
        meme.add(cherylJohnson);
        meme.add(anthonyDevolder);
        meme.add(rufus); 
       


        
        
        
        
        
        // Generational
        if(preset.equals("generational")) {
            presidents.remove(andrewJackson);
            presidents.add(andrewJackson24);
            presidents.add(andrewJackson32);
            presidents.add(andrewJackson36);
            presidents.remove(theodoreRoosevelt);
            presidents.add(theodoreRoosevelt98);
            presidents.add(theodoreRoosevelt04);
            presidents.add(theodoreRoosevelt12);
            presidents.remove(franklinDRoosevelt);
            presidents.add(franklinDRoosevelt32);
            presidents.add(franklinDRoosevelt40);
            presidents.add(franklinDRoosevelt44);
            presidents.remove(abrahamLincoln);
            presidents.add(abrahamLincoln58);
            presidents.add(abrahamLincoln60);
            presidents.add(abrahamLincoln65);
            presidents.remove(calvinCoolidge);
            presidents.add(calvinCoolidge20);
            presidents.add(calvinCoolidge23);
            presidents.add(calvinCoolidge28);
            presidents.remove(richardNixon);
            presidents.add(richardNixon60);
            presidents.add(richardNixon68);
            presidents.add(richardNixon84);
            presidents.remove(ronaldReagan);
            presidents.add(ronaldReagan76);
            presidents.add(ronaldReagan80);
            presidents.add(ronaldReagan88);
            presidents.remove(alGore);
            presidents.add(alGore88);
            presidents.add(alGore00);
            presidents.remove(joeBiden);
            presidents.add(joeBiden96);
            presidents.add(joeBiden20);
            presidents.remove(ulyssesSGrant);
            presidents.add(ulyssesSGrant66);
            presidents.add(ulyssesSGrant72);
            presidents.add(ulyssesSGrant76);
            presidents.remove(dwightEisenhower);
            presidents.add(dwightEisenhower48);
            presidents.add(dwightEisenhower52);
            presidents.add(dwightEisenhower60);
            presidents.remove(lyndonBJohnson);
            presidents.add(lyndonbjohnson60);
            presidents.add(lyndonbjohnson64);
            presidents.remove(johnFKennedy);
            presidents.add(johnfkennedy56);
            presidents.add(johnfkennedy60);
            presidents.add(johnfkennedy63);
            presidents.remove(williamMcKinley);
            presidents.add(williamMcKinley90);
            presidents.add(williamMcKinley96);
            presidents.add(williamMcKinley01);
            presidents.remove(jamesMadison);
            presidents.add(jamesMadison88);
            presidents.add(jamesMadison08);
            presidents.add(jamesMadison16);
            presidents.remove(johnAdams);
            presidents.add(johnAdams76);
            presidents.add(johnAdams96);
            presidents.add(johnAdams00);
            presidents.remove(thomasJefferson);
            presidents.add(thomasJefferson96);
            presidents.add(thomasJefferson00);
            presidents.add(thomasJefferson08);
            presidents.remove(georgeWashington);
            presidents.add(georgeWashington83);
            presidents.add(georgeWashington89);
            presidents.add(georgeWashington97);
            
            presidents.addAll(nonpresidents);
            //System.out.println("GENERATIONAL CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        }
        
        if (preset.equals("expanded")) {
            //System.out.println("EXPANDED DECK");
        	Iterator<President> it = nonpresidents.iterator();
        	while (it.hasNext()) {
        	    President curr = it.next();
        	    boolean valid = true;
        	    if(curr.getWeightedAve()<4) {
        	        valid = false;
        	    }
        	    if(!valid) {
        	        it.remove();
        	    }
        	}
        	Collections.shuffle(nonpresidents);
            for (int i = 0; i < 54; i++) {
                presidents.add(nonpresidents.get(i));
            }
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("full")) {
            presidents.addAll(nonpresidents);
            //System.out.println("FULL CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("memes")) {
            presidents.addAll(nonpresidents);
            presidents.addAll(meme);
            //System.out.println("MEME CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("standard")) {
        	Iterator<President> it = nonpresidents.iterator();
        	while (it.hasNext()) {
        	    President curr = it.next();
        	    boolean valid = true;
        	    if(curr.getWeightedAve()<4) {
        	        valid = false;
        	    }
        	    if(!valid) {
        	        it.remove();
        	    }
        	}
            Collections.shuffle(nonpresidents);
            for (int i = 0; i < 6; i++) {
                presidents.add(nonpresidents.get(i));
            }
            //System.out.println("STANDARD CARD DECK");
            Collections.shuffle(presidents);
            /*
            for (President p : presidents) {
                System.out.println(p.toString());
            } 
           	*/
            return presidents;
        }
        
        if(preset.equals("custom")&&(!remembered)) {
        	List<President> custom = new ArrayList<President>();
        	
        	custom.addAll(presidents);
        	custom.addAll(nonpresidents);
        	custom.addAll(meme);
        	// Is there a more efficient way to do this?
        	Iterator<President> it = custom.iterator();
        	while (it.hasNext()) {
        	    President curr = it.next();
        	    boolean valid = false;
        	    for (String customTag: tags) {
        	        for (String presTag: curr.getTags()) {
        	            if(customTag.equals(presTag)) {
        	                valid=true;
        	            }
        	        }
        	    }
        	    if(curr.getWeightedAve()<minRate) {
        	        valid = false;
        	    }
        	    if(!valid) {
        	        it.remove();
        	    }
        	}
        	
        	// Fill in the minimum amount of cards. The actual amount for this might need to change.
        	if(custom.size()<20) {
        		for(int i=0; i<(20-custom.size()); i++) {
        			custom.add(presidents.get(i));
        		}
        		JOptionPane.showMessageDialog(null,
						"Error: Not enough cards for current filter. A few more cards have been added.", "Error",
						JOptionPane.ERROR_MESSAGE);
        	}
        	minRateRem = minRate;
        	tagsRem = tags;
        	remembered=true;
        	return custom;
        	
        } else if (preset.equals("custom")&&remembered) {
        	List<President> custom = new ArrayList<President>();
        	
        	custom.addAll(presidents);
        	custom.addAll(nonpresidents);
        	custom.addAll(meme);
        	// Is there a more efficient way to do this?
        	Iterator<President> it = custom.iterator();
        	while (it.hasNext()) {
        	    President curr = it.next();
        	    boolean valid = false;
        	    for (String customTag: tagsRem) {
        	        for (String presTag: curr.getTags()) {
        	            if(customTag.equals(presTag)) {
        	                valid=true;
        	            }
        	        }
        	    }
        	    if(curr.getWeightedAve()<minRateRem) {
        	        valid = false;
        	    }
        	    if(!valid) {
        	        it.remove();
        	    }
        	}
        	
        	// Fill in the minimum amount of cards. The actual amount for this might need to change.
        	if(custom.size()<20) {
        		for(int i=0; i<(20-custom.size()); i++) {
        			custom.add(presidents.get(i));
        		}
        		JOptionPane.showMessageDialog(null,
						"Error: Not enough cards for current filter. A few more cards have been added.", "Error",
						JOptionPane.ERROR_MESSAGE);
        	}
        	return custom;
        }
        
        Collections.shuffle(nonpresidents);
        for (int i = 0; i < 7; i++) {
            presidents.add(nonpresidents.get(i));
        }
        //System.out.println("STANDARD CARD DECK");
        return presidents;
    }
   
    
    // Old function  - this can be ignored or deleted
    public static List<President> getPresidents() {
        List<President> presidents = new ArrayList<President>();
        presidents.add(calvinCoolidge);
        presidents.add(thomasJefferson);
        presidents.add(lyndonBJohnson);
        presidents.add(donaldTrump);
        presidents.add(ulyssesSGrant);
        presidents.add(woodrowWilson);
        presidents.add(millardFillmore);
        presidents.add(billClinton);
        presidents.add(georgeWashington);
        presidents.add(jamesAGarfield);
        presidents.add(jamesMonroe);
        presidents.add(williamMcKinley);
        presidents.add(andrewJackson);
        presidents.add(ronaldReagan);
        presidents.add(groverCleveland);
        presidents.add(richardNixon);
        presidents.add(warrenGHarding);
        presidents.add(theodoreRoosevelt);
        presidents.add(franklinDRoosevelt);
        presidents.add(johnAdams);
        presidents.add(jamesMadison);
        presidents.add(johnQuincyAdams);
        presidents.add(abrahamLincoln);
        presidents.add(williamHenryHarrison);
        presidents.add(martinVanBuren);
        presidents.add(barackObama);
        presidents.add(jimmyCarter);
        presidents.add(johnTyler);
        presidents.add(jamesKPolk);
        presidents.add(zacharyTaylor);
        presidents.add(georgeWBush);
        presidents.add(johnFKennedy);
        presidents.add(georgeHWBush);
        presidents.add(geraldFord);
        presidents.add(williamHowardTaft);
        presidents.add(herbertHoover);
        presidents.add(harrySTruman);
        presidents.add(dwightEisenhower);
        presidents.add(benjaminHarrison);
        presidents.add(chesterAArthur);
        presidents.add(rutherfordBHayes);
        presidents.add(andrewJohnson);
        presidents.add(jamesBuchanan);
        presidents.add(franklinPierce);
        presidents.add(joeBiden);
        if (expanded) {
            presidents.add(johnCCalhoun);
            presidents.add(henryClay);
            presidents.add(alexanderHamilton);
            presidents.add(robertATaft);
            presidents.add(williamJenningsBryan);
            presidents.add(alGore);
            presidents.add(georgeWallace);
            presidents.add(barryGoldwater); 
            presidents.add(johnMcCain);  
            presidents.add(michaelDukakis);
            presidents.add(ronDeSantis);
            presidents.add(hillaryClinton);
            presidents.add(mittRomney);
            presidents.add(garyJohnson);
            presidents.add(rossPerot);
            presidents.add(robertLaFollette);
            presidents.add(charlesLindbergh);
            presidents.add(williamHSeward);
            presidents.add(josephMcCarthy);
            presidents.add(stromThurmond);
            presidents.add(frederickDouglass);
            presidents.add(eugeneMcCarthy);
            presidents.add(hubertHumphrey);
            presidents.add(douglasMacArthur);
            presidents.add(johnDRockefeller);
            presidents.add(dickCheney);
            presidents.add(alSmith);
            presidents.add(patrickHenry);
            presidents.add(patBuchanan);
            presidents.add(thomasPaine);
            presidents.add(marcoRubio);
            presidents.add(bernieSanders);
            presidents.add(kanyeWest);
            presidents.add(ronPaul);
            presidents.add(johnMuir);
            presidents.add(georgeMcClellan);
            presidents.add(stephenADouglas);
            presidents.add(thaddeusStevens);
            presidents.add(johnCFremont);
            presidents.add(horaceGreeley);
            presidents.add(robertELee);
            presidents.add(nelsonRockefeller);
            presidents.add(thomasDewey);
            presidents.add(ralphNader);
            presidents.add(georgeMcGovern);
            presidents.add(bobbyKennedy);
            presidents.add(georgePatton);
            presidents.add(shirleyChisholm);
            presidents.add(bettyFord);
            presidents.add(sarahPalin);
            presidents.add(jackKemp);
            presidents.add(howardDean);
            presidents.add(rudyGiuliani);
            presidents.add(martinLutherKingJr);
            presidents.add(danQuayle);
            presidents.add(hueyLong);
            presidents.add(colinPowell);
            presidents.add(susanBAnthony);
            presidents.add(walterMondale);
            presidents.add(spiroAgnew);
            
        }
        if (memes) {
            presidents.add(millFill);
            presidents.add(jebBush);
            presidents.add(emperorpalpatine);
            presidents.add(odysseus);
            presidents.add(napoleon);
            presidents.add(libRight);
            presidents.add(authRight);
            presidents.add(authLeft);
            presidents.add(libLeft);
            presidents.add(churchill);
            presidents.add(monke);
            presidents.add(impostor);
            presidents.add(comrademccain);            
            presidents.add(evilGeorgeWallace);
            presidents.add(postMalone);     
            presidents.add(ambroseBurnside);
            for (int i = 0; i<5; i++) {
                presidents.add(michaelDukakis);
            }
        }
        return presidents;
    }

    public static List<Election> getElections() {
        List<Election> elections = new ArrayList<Election>();
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
