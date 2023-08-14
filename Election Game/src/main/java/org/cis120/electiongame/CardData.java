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
            "Donald Trump", "Northeast", 1, 6, 1, 7, "Conservative", "Nationalism",
            new String[]{"President", "Modern Era"},
            "<center><b>Donald Trump</b></center><br><br>"
	                + "<b>Political Experience:</b><br><br>"
	                + "President: 2017-2021<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<br><br>"
	                + "<b>Presidential Runs:</b><br><br>"
	                + "2016 (won), 2020 (lost)"
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
            "William McKinley", "Midwest", 7, 5, 7, 6, "Conservative", "Tariffs",
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
            "William Jennings Bryan", "West", 2, 9, 6, 2, "Populist", "Inflation",
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
            "James K. Polk", "South", 6, 7, 4, 2, "Populist", "Tough Foreign Policy",
            new String[]{"President", "Jacksonian Era"}
    );
    static President zacharyTaylor = new President(
            "Zachary Taylor", "Northeast", 1, 5, 1, 7, "Conservative", "Nationalism",
            new String[]{"President", "Jacksonian Era"}
    );
    static President georgeWBush = new President(
            "George W. Bush", "South", 3, 6, 3, 7, "Conservative", "Tough Foreign Policy",
            new String[]{"President", "Modern Era"}
    );
    static President alGore = new President(
            "Al Gore", "South", 8, 3, 6, 5, "Progressive", "Social Liberalism",
            new String[]{"Modern Era"}
    );
    static President johnFKennedy = new President(
            "John F. Kennedy", "Northeast", 5, 9, 6, 7, "Progressive", "Tough Foreign Policy",
            new String[]{"President", "Civil Rights Era"}
    );
    static President georgeHWBush = new President(
            "George H. W. Bush", "South", 7, 3, 5, 6, "Conservative", "Tough Foreign Policy",
            new String[]{"President", "Reagan Era"}
    );
    static President geraldFord = new President(
            "Gerald Ford", "Midwest", 8, 3, 3, 3, "Conservative", "Establishment",
            new String[]{"President", "Civil Rights Era"}
    );
    static President williamHowardTaft = new President(
            "William Howard Taft", "Midwest", 2, 4, 7, 4, "Conservative", "Free Trade",
            new String[]{"President", "Progressive Era"}
    );
    static President herbertHoover = new President(
            "Herbert Hoover", "West", 2, 5, 3, 4, "Progressive", "Tariffs",
            new String[]{"President", "Progressive Era"}
    );
    static President harrySTruman = new President(
            "Harry S. Truman", "South", 7, 4, 3, 4, "Progressive", "Tough Foreign Policy",
            new String[]{"President", "New Deal Era"}
    );
    static President dwightEisenhower = new President(
            "Dwight Eisenhower", "West", 1, 8, 5, 10, "Conservative", "Internal Improvements"
            ,
            new String[]{"President", "New Deal Era"}
    );
    static President benjaminHarrison = new President(
            "Benjamin Harrison", "Midwest", 3, 4, 2, 3, "Conservative", "Tariffs",
            new String[]{"President", "Reconstruction Era"}
    );
    static President chesterAArthur = new President(
            "Chester A. Arthur", "Northeast", 3, 4, 5, 3, "Conservative", "Closed Borders",
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
            "Joe Biden", "Northeast", 10, 2, 3, 6, "Progressive", "Social Programs",
            new String[]{"President", "Present Era"}
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
            "DaBaby", "South", 0, 10, 0, 10, "Progressive", "Personal Freedom",
            new String[]{"Meme Card"}
    );
    static President emperorpalpatine = new President(
            "Emperor Palpatine", "Northeast", 7, 6, 3, 10, "Conservative", "Centralization",
            new String[]{"Meme Card"}
    );
    static President odysseus = new President(
            "Odysseus", "South", 2, 5, 8, 9, "Conservative", "Tough Foreign Policy",
            new String[]{"Meme Card"}
    );
    static President napoleon = new President(
            "Napoleon Bonaparte", "Northeast", 2, 7, 4, 10, "Conservative", "Tough Foreign Policy",
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
            "Winston Churchill", "Northeast", 6, 10, 4, 5, "Conservative", "Tough Foreign Policy",
            new String[]{"Meme Card"}
    );
    static President monke = new President(
            "Monke", "West", 1, 10, 1, 1, "Conservative", "Traditional Morality",
            new String[]{"Meme Card"}
    );
    static President impostor = new President(
            "impostor", "West", 3, 10, 10, 0, "Populist", "Tough Foreign Policy",
            new String[]{"Meme Card"}
    );
    static President comrademccain = new President(
            "Comrade McCain", "West", 8, 2, 4, 6, "Progressive", "Communism",
            new String[]{"Meme Card"}
    );
    static President michaelDukakis = new President(
            "Michael Dukakis", "Northeast", 4, 2, 4, 4, "Progressive", "Personal Freedom",
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
            "John McCain", "West", 7, 2, 3, 5, "Conservative", "Tough Foreign Policy",
            new String[]{"Modern Era"}
    );
    static President ambroseBurnside = new President(
            "Ambrose Burnside", "Northeast", 3, 5, 1, 3, "Conservative", "Tough Foreign Policy",
            new String[]{"Civil Rights Era"}
    );
    static President ronDeSantis = new President(
            "Ron DeSantis", "South", 4, 4, 8, 5, "Conservative", "Traditional Morality",
            new String[]{"Present Era"}
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
            "Gary Johnson", "West", 3, 1, 2, 1, "Libertarian", "Personal Freedom",
            new String[]{"Modern Era"}
    );
    static President rossPerot = new President(
            "Ross Perot", "South", 1, 3, 6, 4, "Populist", "Tariffs",
            new String[]{"Reagan Era"}
    );
    static President robertLaFollette = new President(
            "Robert La Follette", "Midwest", 6, 3, 5, 5, "Progressive", "Regulation",
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
            "Douglas MacArthur", "South", 3, 6, 2, 9, "Conservative", "Tough Foreign Policy",
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
            "Dick Cheney", "West", 6, 1, 2, 4, "Conservative", "Tough Foreign Policy",
            new String[]{"Modern Era"}
    );
    static President alSmith = new President(
            "Al Smith", "Northeast", 4, 2, 5, 3, "Progressive", "Personal Freedom",
            new String[]{"Progressive Era"}
    );
    static President patrickHenry = new President(
            "Patrick Henry", "South", 3, 8, 2, 6, "Libertarian", "Limited Government",
            new String[]{"Founding Era"}
    );
    static President patBuchanan = new President(
            "Pat Buchanan", "Northeast", 1, 2, 8, 2, "Conservative", "Isolationism",
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
            new String[]{"Present Era"}
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
            "George Patton", "West", 1, 9, 1, 7, "Conservative", "Tough Foreign Policy",
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
            "Colin Powell", "South", 5, 4, 3, 4, "Conservative", "Tough Foreign Policy",
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
            "Wendell Willkie", "Midwest", 1, 7, 2, 3, "Conservative", "Tough Foreign Policy",
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
            "Daniel Webster", "Northeast", 8, 6, 3, 6, "Conservative", "Centralization",
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
            "Charles Sumner", "Northeast", 5, 3, 3, 7, "Conservative", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President johnBrown = new President(
            "John Brown", "Northeast", 1, 1, 1, 9, "Progressive", "Personal Freedom",
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
            "Franklin Roosevelt '44", "Northeast", 9, 8, 9, 10, "Progressive", "Tough Foreign Policy",
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
            "Richard Nixon '60", "West", 8, 5, 5, 7, "Conservative", "Tough Foreign Policy",
            new String[]{"Generational"}
    );
    static President richardNixon68 = new President(
            "Richard Nixon '68", "West", 8, 7, 5, 8, "Conservative", "Law and Order",
            new String[]{"Generational"}
    );
    static President richardNixon84 = new President(
            "Richard Nixon '84", "West", 9, 5, 8, 10, "Conservative", "Tough Foreign Policy",
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
            "Ronald Reagan '88", "West", 8, 9, 7, 10, "Conservative", "Tough Foreign Policy",
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
            new String[]{"Present Era"}
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
    		"Margaret Chase Smith", "Northeast", 6, 4, 4, 4, "Conservative", "Tough Foreign Policy",
            new String[]{"New Deal Era"}
    );
    static President bobDole = new President(
    		"Bob Dole", "West", 7, 3, 4, 4, "Conservative", "Tax Cuts",
            new String[]{"Reagan Era"}
    );
    
    static President eugeneDebs = new President(
    		"Eugene Debs", "Midwest", 1, 3, 3, 3, "Progressive", "Class Unity",
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
            new String[]{"Present Era"}
    );
    static President staceyAbrams = new President(
    		"Stacey Abrams", "South", 1, 4, 2, 3, "Progressive", "Civil Rights",
            new String[]{"Present Era"}
    );
    static President sherrodBrown = new President(
    		"Sherrod Brown", "Midwest", 6, 2, 3, 1, "Progressive", "Tariffs",
            new String[]{"Present Era"}
    );
    
    static President randPaul = new President(
    		"Rand Paul", "South", 4, 2, 6, 2, "Libertarian", "Tax Cuts",
            new String[]{"Present Era"}
    );
    
    static President dwightEisenhower48 = new President(
    		"Dwight Eisenhower '48", "West", 1, 7, 1, 10, "Conservative", "Tough Foreign Policy",
            new String[]{"Generational"}
    );
    
    static President dwightEisenhower52 = new President(
    		"Dwight Eisenhower '52", "West", 1, 8, 4, 10, "Conservative", "Tough Foreign Policy",
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
    		"John F. Kennedy '56", "Northeast", 3, 7, 5, 5, "Progressive", "Tough Foreign Policy",
            new String[]{"Generational"}
    );
    
    static President johnfkennedy60 = new President(
    		"John F. Kennedy '60", "Northeast", 5, 9, 6, 7, "Progressive", "Tough Foreign Policy",
            new String[]{"Generational"}
    );
  
    static President johnfkennedy63 = new President(
    		"John F. Kennedy '63", "Northeast", 8, 9, 6, 10, "Progressive", "Tough Foreign Policy",
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
            "William McKinley '01", "Midwest", 10, 5, 8, 8, "Conservative", "Tough Foreign Policy",
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
            "Frank Knox", "Northeast", 1, 1, 1, 2, "Conservative", "Tough Foreign Policy",
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
            "J.P. Morgan", "Northeast", 1, 2, 4, 9, "Conservative", "Deregulation",
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
            "Henry Cabot Lodge Jr.", "Northeast", 6, 2, 5, 5, "Conservative", "Tough Foreign Policy",
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
            new String[]{"Present Era"}
    );
    static President johnMarshall = new President(
            "John Marshall", "Northeast", 5, 3, 6, 6, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President thomasPinckney = new President(
            "Thomas Pinckney", "South", 4, 3, 1, 8, "Conservative", "Tough Foreign Policy",
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
            "George Cabot", "Northeast", 3, 3, 5, 3, "Conservative", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President timothyPickering = new President(
            "Timothy Pickering", "Northeast", 7, 3, 4, 4, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President henryKnox = new President(
            "Henry Knox", "Northeast", 3, 4, 1, 7, "Conservative", "Tough Foreign Policy",
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
            "John Eager Howard", "Northeast", 4, 3, 1, 3, "Conservative", "Personal Freedom",
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
            "Oliver Wolcott Jr.", "Northeast", 5, 1, 2, 1, "Conservative", "Central Bank",
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
            "James Wilson", "Northeast", 2, 3, 6, 2, "Conservative", "Active Executive",
            new String[]{"Founding Era"}
    );
    static President oliverEllsworth = new President(
            "Oliver Ellsworth", "Northeast", 5, 5, 4, 3, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President jamesMcHenry = new President(
            "James McHenry", "Northeast", 2, 3, 3, 2, "Conservative", "Tough Foreign Policy",
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
            "William Paterson", "Northeast", 3, 2, 6, 3, "Conservative", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President kimkardashian = new President(
            "Kim Kardashian", "West", 1, 7, 1, 8, "Conserative", "Personal Freedom",
            new String[]{"Meme Card"}
    );
    static President lolalago = new President(
            "Lola Lago", "Spain", 10, 10, 10, 10, "Populist", "Law and Order",
            new String[]{"Meme Card"}
    );
    static President charlesWFairbanks = new President(
            "Charles W. Fairbanks", "Midwest", 5, 4, 3, 3, "Conservative", "Deregulation",
            new String[]{"Progressive Era"}
    );
    static President altonBParker = new President(
            "Alton B. Parker", "Northeast", 2, 4, 4, 2, "Libertarian", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President jamesSSherman = new President(
            "James S. Sherman", "Northeast", 5, 5, 2, 1, "Conservative", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President adlaiStevensonI = new President(
            "Adlai Stevenson I", "Midwest", 6, 3, 3, 4, "Populist", "Establishment",
            new String[]{"Progressive Era"}
    );
    static President henryGDavis = new President(
            "Henry G. Davis", "Midwest", 3, 3, 1, 2, "Conservative", "Subsidies",
            new String[]{"Progressive Era"}
    );
    static President johnWKern = new President(
            "John W. Kern", "Midwest", 3, 1, 3, 1, "Progressive", "Central Bank",
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
            "James G. Blaine", "Northeast", 7, 5, 2, 5, "Conservative", "Tough Foreign Policy",
            new String[]{"Reconstruction Era"}
    );
    static President thomasAHendricks = new President(
            "Thomas A. Hendricks", "Midwest", 6, 3, 5, 2, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President winfieldScottHancock = new President(
            "Winfield Scott Hancock", "Northeast", 1, 6, 2, 7, "Libertarian", "States' Rights",
            new String[]{"Reconstruction Era"}
    );
    static President williamHaydenEnglish = new President(
            "William Hayden English", "Midwest", 3, 2, 2, 1, "Libertarian", "Gold Standard",
            new String[]{"Reconstruction Era"}
    );
    static President johnHancock = new President(
            "John Hancock", "Northeast", 7, 4, 5, 8, "Conservative", "Nationalism",
            new String[]{"Founding Era"}
    );
    static President danielDTompkins = new President(
            "Daniel D. Tompkins", "Northeast", 8, 2, 4, 5, "Libertarian", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President samuelAdams = new President(
            "Samuel Adams", "Northeast", 3, 8, 3, 7, "Libertarian", "Personal Freedom",
            new String[]{"Founding Era"}
    );
    static President abigailAdams = new President(
            "Abigail Adams", "Northeast", 1, 5, 7, 6, "Conservative", "Social Liberalism",
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
            "Robert Yates", "Northeast", 2, 5, 5, 4, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President georgeTroup = new President(
            "George Troup", "South", 5, 1, 1, 2, "Populist", "Racism",
            new String[]{"Founding Era"}
    );
    static President lutherMartin = new President(
            "Luther Martin", "Northeast", 2, 2, 1, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President charlesScott = new President(
            "Charles Scott", "South", 2, 2, 1, 4, "Libertarian", "Tough Foreign Policy",
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
            "John Breckinridge", "South", 3, 3, 8, 3, "Libertarian", "Personal Freedom",
            new String[]{"Founding Era"}
    );
    static President johnLangdon = new President(
            "John Langdon", "Northeast", 7, 1, 4, 3, "Libertarian", "Centralization",
            new String[]{"Founding Era"}
    );
    static President jamesWarren = new President(
            "James Warren", "Northeast", 3, 1, 4, 2, "Libertarian", "Personal Freedom",
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
            "James Sullivan", "Northeast", 2, 1, 6, 1, "Libertarian", "Egalitarianism",
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
            "John Taylor", "South", 4, 1, 7, 2, "Libertarian", "States' Rights",
            new String[]{"Founding Era"}
    );
    static President josephBradleyVarnum = new President(
            "Joseph Bradley Varnum", "Northeast", 7, 1, 4, 3, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President peterPorter = new President(
            "Peter Porter", "Northeast", 3, 3, 1, 2, "Libertarian", "Tough Foreign Policy",
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
            "Philip B. Barbour", "South", 6, 3, 5, 5, "Libertarian", "Constructionism",
            new String[]{"Founding Era"}
    );
    static President langdonCheves = new President(
            "Langdon Cheves", "South", 4, 4, 3, 5, "Libertarian", "Free Trade",
            new String[]{"Founding Era"}
    );
    static President simonSnyder = new President(
            "Simon Snyder", "Northeast", 4, 3, 2, 1, "Libertarian", "Tough Foreign Policy",
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
            new String[]{"Present Era"}
    );
    static President maryPeltola = new President(
            "Mary Peltola", "West", 2, 2, 2, 1, "Progressive", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President leeZeldin = new President(
            "Lee Zeldin", "Northeast", 3, 5, 6, 3, "Conservative", "Law and Order",
            new String[]{"Present Era"}
    );
    static President glennYoungkin = new President(
            "Glenn Youngkin", "South", 2, 6, 6, 3, "Conservative", "Traditional Morality",
            new String[]{"Present Era"}
    );
    static President gretchenWhitmer = new President(
            "Gretchen Whitmer", "Midwest", 4, 5, 5, 3, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    
    static President kamalaHarris = new President(
            "Kamala Harris", "West", 7, 2, 3, 6, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
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
            "Charlie Crist", "South", 4, 2, 2, 4, "Conservative", "Subsidies"
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
            "Andrew Gregg Curtin", "Northeast", 5, 4, 1, 2, "Conservative", "Tough Foreign Policy",
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
            "Joel Parker", "Midwest", 4, 4, 1, 3, "Populist", "Personal Freedom",
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
            "Joseph Dennie", "Northeast", 1, 4, 1, 2, "Conservative", "Social Hierarchy",
            new String[]{"Founding Era"}
    );
    static President robertRLivingston = new President(
            "Robert R. Livingston", "Northeast", 4, 4, 3, 4, "Libertarian", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President jamesTallmadgeJr = new President(
            "James Tallmadge Jr.", "Northeast", 2, 1, 2, 1, "Libertarian", "Civil Rights",
            new String[]{"Founding Era"}
    );
    static President frederickMuhlenberg = new President(
            "Frederick Muhlenberg", "Northeast", 5, 3, 4, 3, "Libertarian", "Personal Freedom",
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
            "William Maclay", "Northeast", 2, 1, 2, 2, "Libertarian", "Anti-Establishment",
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
            "Samuel Chase", "Northeast", 3, 1, 1, 4, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President matthewLyon = new President(
            "Matthew Lyon", "Northeast", 3, 1, 1, 5, "Libertarian", "Personal Freedom",
            new String[]{"Founding Era"}
    );
    static President johnWTaylor = new President(
            "John W. Taylor", "Northeast", 6, 2, 1, 2, "Libertarian", "Civil Rights",
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
            "William Richardson Davie", "South", 2, 1, 1, 2, "Conservative", "Active Executive",
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
            "John Chandler", "Northeast", 3, 1, 3, 1, "Libertarian", "Tough Foreign Policy",
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
            "Robert Goodloe Harper", "Northeast", 3, 2, 2, 3, "Conservative", "Tough Foreign Policy",
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
            "Samuel Smith", "Northeast", 7, 5, 1, 2, "Libertarian", "Tariffs",
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
            new String[]{"Present Era"}
    );
    static President dougMastriano = new President(
            "Doug Mastriano", "Northeast", 1, 1, 1, 1, "Conservative", "Traditional Morality",
            new String[]{"Present Era"}
    );
    static President paulRyan = new President(
            "Paul Ryan", "Midwest", 4, 2, 6, 4, "Conservative", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President elizabethWarren = new President(
            "Elizabeth Warren", "Northeast", 4, 1, 7, 3, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President catherineCortezMasto = new President(
            "Catherine Cortez Masto", "West", 4, 2, 3, 1, "Progressive", "Open Borders",
            new String[]{"Present Era"}
    );
    static President andrewCuomo = new President(
            "Andrew Cuomo", "Northeast", 3, 5, 1, 6, "Progressive", "Regulation",
            new String[]{"Present Era"}
    );
    static President williamCushing = new President(
            "William Cushing", "Northeast", 3, 1, 3, 1, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President johnBlairJr = new President(
            "John Blair Jr.", "South", 2, 2, 6, 2, "Conservative", "Centralization",
            new String[]{"Founding Era"}
    );
    static President paulHamilton = new President(
            "Paul Hamilton", "South", 2, 1, 3, 1, "Libertarian", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President philipReed = new President(
            "Philip Reed", "Northeast", 4, 1, 2, 1, "Libertarian", "Egalitarianism",
            new String[]{"Founding Era"}
    );
    static President johnArmstrongJr = new President(
            "John Armstrong Jr.", "Northeast", 3, 1, 1, 1, "Libertarian", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President williamLowndes = new President(
            "William Lowndes", "South", 3, 3, 5, 2, "Libertarian", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President bushrodWashington = new President(
            "Bushrod Washington", "South", 4, 1, 3, 2, "Conservative", "Implied Powers",
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
            "Alfred Moore", "South", 2, 1, 2, 1, "Conservative", "Implied Powers",
            new String[]{"Founding Era"}
    );
    static President isaacShelby = new President(
            "Isaac Shelby", "South", 3, 5, 1, 2, "Libertarian", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President stephenRBradley = new President(
            "Stephen R. Bradley", "Northeast", 5, 2, 3, 2, "Libertarian", "Isolationism",
            new String[]{"Founding Era"}
    );
    static President johnSevier = new President(
            "John Sevier", "South", 5, 2, 3, 1, "Libertarian", "Tough Foreign Policy",
            new String[]{"Founding Era"}
    );
    static President michaelLeib = new President(
            "Michael Leib", "Northeast", 4, 2, 1, 1, "Libertarian", "Deregulation",
            new String[]{"Founding Era"}
    );
    static President mehmetOz = new President(
            "Mehmet Oz", "Northeast", 1, 1, 1, 4, "Conservative", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President thomasMassie = new President(
            "Thomas Massie", "South", 3, 2, 4, 1, "Libertarian", "Constructionism",
            new String[]{"Present Era"}
    );
    static President katieBritt = new President(
            "Katie Britt", "South", 2, 4, 2, 1, "Conservative", "Traditional Morality",
            new String[]{"Present Era"}
    );
    static President chuckSchumer = new President(
            "Chuck Schumer", "Northeast", 8, 1, 5, 4, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President kathyHochul = new President(
            "Kathy Hochul", "Northeast", 4, 1, 2, 2, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President alexandriaOcasioCortez = new President(
            "Alexandria Ocasio-Cortez", "Northeast", 2, 4, 1, 5, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President tuckerCarlson = new President(
            "Tucker Carlson", "Northeast", 1, 6, 1, 6, "Populist", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President marjorieTaylorGreene = new President(
            "Marjorie Taylor Greene", "South", 2, 2, 1, 4, "Conservative", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President timScott = new President(
            "Tim Scott", "South", 4, 4, 4, 3, "Conservative", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President peteButtigieg = new President(
            "Pete Buttigieg", "Midwest", 2, 3, 5, 3, "Progressive", "Internal Improvements",
            new String[]{"Present Era"}
    );
    static President tulsiGabbard = new President(
            "Tulsi Gabbard", "West", 3, 3, 2, 3, "Populist", "Isolationism",
            new String[]{"Present Era"}
    );
    static President joeManchin = new President(
            "Joe Manchin", "Midwest", 7, 2, 1, 3, "Populist", "Deregulation",
            new String[]{"Present Era"}
    );
    static President brianKemp = new President(
            "Brian Kemp", "South", 4, 4, 3, 4, "Conservative", "Establishment",
            new String[]{"Present Era"}
    );
    static President brettFavre = new President(
            "Brett Favre", "South", 1, 1, 1, 5, "Conservative", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President elonMusk = new President(
            "Elon Musk", "South", 1, 3, 3, 9, "Libertarian", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President gavinNewsom = new President(
            "Gavin Newsom", "West", 3, 6, 3, 4, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President leBronJames = new President(
            "LeBron James", "Midwest", 1, 2, 1, 9, "Progressive", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President raphaelWarnock = new President(
            "Raphael Warnock", "South", 2, 6, 2, 3, "Progressive", "Egalitarianism",
            new String[]{"Present Era"}
    );   
    static President seanPatrickMaloney = new President(
            "Sean Patrick Maloney", "Northeast", 3, 1, 1, 1, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President johnFetterman = new President(
            "John Fetterman", "Midwest", 2, 3, 1, 2, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President joshShapiro = new President(
            "Josh Shapiro", "Midwest", 2, 4, 3, 2, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President mikeLawler = new President(
            "Mike Lawler", "Northeast", 2, 3, 2, 1, "Conservative", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President georgeSantos = new President(
            "George Santos", "Northeast", 2, 1, 1, 3, "Conservative", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President susanCollins = new President(
            "Susan Collins", "Northeast", 7, 2, 2, 2, "Conservative", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President cherylJohnson = new President(
            "Cheryl Johnson", "Northeast", 10, 10, 1, 1, "Progressive", "Law and Order",
            new String[]{"Meme Card"}
    );
    static President mikeLee = new President(
            "Mike Lee", "West", 4, 1, 5, 2, "Libertarian", "Limited Government",
            new String[]{"Present Era"}
    );
    static President jdVance = new President(
            "J.D. Vance", "West", 4, 1, 5, 2, "Populist", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President kevinMcCarthy = new President(
            "Kevin McCarthy", "West", 8, 1, 4, 4, "Conservative", "Establishment",
            new String[]{"Present Era"}
    );
    static President nancyPelosi = new President(
            "Nancy Pelosi", "West", 9, 1, 4, 5, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President philMurphy = new President(
            "Phil Murphy", "Northeast", 3, 1, 3, 2, "Progressive", "Regulation",
            new String[]{"Present Era"}
    );
    static President dickDurbin = new President(
            "Dick Durbin", "Midwest", 8, 1, 3, 2, "Progressive", "Open Borders",
            new String[]{"Present Era"}
    );
    static President alexanderConteeHanson = new President(
            "Alexander Contee Hanson", "Northeast", 2, 2, 1, 1, "Conservative", "Tariffs",
            new String[]{"Founding Era"}
    );
    static President charlesFMercer = new President(
            "Charles F. Mercer", "South", 6, 3, 2, 2, "Conservative", "Subsidies",
            new String[]{"Founding Era"}
    );
    static President johnScott = new President(
            "John Scott", "South", 3, 1, 1, 1, "Conservative", "Subsidies",
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
            "Richard Rush", "Northeast", 2, 4, 5, 3, "Libertarian", "Tough Foreign Policy",
            new String[]{"Jacksonian Era"}
    );
    static President johnAndrewShulze = new President(
            "John Andrew Shulze", "Northeast", 3, 1, 4, 2, "Conservative", "Internal Improvements",
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
            "Samuel L. Southard", "Northeast", 5, 2, 4, 2, "Conservative", "Tough Foreign Policy",
            new String[]{"Jacksonian Era"}
    );
    static President smithThompson = new President(
            "Smith Thompson", "Northeast", 3, 1, 4, 3, "Conservative", "Civil Rights",
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
            "Thomas Hart Benton", "South", 7, 5, 3, 3, "Libertarian", "Tough Foreign Policy",
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
            "John McLean", "Midwest", 4, 4, 5, 4, "Conservative", "Civil Rights",
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
            "Felix Grundy", "South", 4, 2, 2, 2, "Populist", "Tough Foreign Policy",
            new String[]{"Jacksonian Era"}
    );
    static President williamLegett = new President(
            "William Legett", "Northeast", 1, 4, 1, 3, "Populist", "Laissez-Faire",
            new String[]{"Jacksonian Era"}
    );
    static President robertCWinthrop = new President(
            "Robert C. Winthrop", "Northeast", 3, 2, 2, 2, "Libertarian", "Traditional Morality",
            new String[]{"Jacksonian Era"}
    );
    static President abbotLawrence = new President(
            "Abbot Lawrence", "Northeast", 2, 5, 1, 3, "Conservative", "Subsidies",
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
            "John Catron", "South", 4, 1, 4, 2, "Populist", "Anti-Central Bank",
            new String[]{"Civil War Era"}
    );
    static President kyrstenSinema = new President(
            "Kyrsten Sinema", "West", 4, 1, 2, 3, "Progressive", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President chuckGrassley = new President(
            "Chuck Grassley", "Midwest", 9, 2, 2, 2, "Conservative", "Subsidies",
            new String[]{"Present Era"}
    );
    static President eliseStefanik = new President(
            "Elise Stefanik", "Northeast", 3, 2, 3, 2, "Conservative", "Establishment",
            new String[]{"Present Era"}
    );
    static President jaredGolden = new President(
            "Jared Golden", "Northeast", 2, 2, 3, 1, "Populist", "Personal Freedom",
            new String[]{"Present Era"}
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
            "Benjamin Robbins Curtis", "Northeast", 2, 1, 3, 2, "Conservative", "Constructionism",
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
            "Roger B. Taney", "Northeast", 5, 1, 5, 5, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President mikePompeo = new President(
            "Mike Pompeo", "West", 3, 2, 5, 2, "Conservative", "Tough Foreign Policy",
            new String[]{"Present Era"}
    );
    static President jaredKushner = new President(
            "Jared Kushner", "Northeast", 1, 1, 5, 4, "Conservative", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President mattGaetz = new President(
            "Matt Gaetz", "South", 2, 2, 1, 3, "Libertarian", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President amyKlobuchar = new President(
            "Amy Klobuchar", "Midwest", 6, 2, 4, 2, "Progressive", "Regulation",
            new String[]{"Present Era"}
    );
    static President ilhanOmar = new President(
            "Ilhan Omar", "Northeast", 2, 2, 1, 3, "Progressive", "Isolationism",
            new String[]{"Present Era"}
    );
    static President janetYellen = new President(
            "Janet Yellen", "Northeast", 2, 1, 3, 2, "Progressive", "Inflation",
            new String[]{"Present Era"}
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
            "William O. Butler", "South", 2, 3, 2, 3, "Populist", "Tough Foreign Policy",
            new String[]{"Jacksonian Era"}
    );
    static President ninaTurner = new President(
            "Nina Turner", "Midwest", 1, 1, 1, 1, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President jaredPolis = new President(
            "Jared Polis", "West", 4, 5, 3, 2, "Libertarian", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President chrisSununu = new President(
            "Chris Sununu", "Northeast", 4, 4, 4, 2, "Libertarian", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President philScott = new President(
            "Phil Scott", "Northeast", 4, 3, 3, 2, "Progressive", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President charlieBaker = new President(
            "Charlie Baker", "Northeast", 4, 2, 4, 2, "Progressive", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President andyBeshear = new President(
            "Andy Beshear", "South", 3, 3, 2, 2, "Progressive", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President johnBelEdwards = new President(
            "John Bel Edwards", "South", 5, 2, 3, 2, "Populist", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President lauraKelly = new President(
            "Laura Kelly", "West", 3, 2, 4, 1, "Progressive", "Social Programs",
            new String[]{"Present Era"}
    );
    static President nikkiHaley = new President(
            "Nikki Haley", "South", 3, 5, 4, 2, "Conservative", "Nationalism",
            new String[]{"Present Era"}
    );
    static President coreyStapleton = new President(
            "Corey Stapleton", "West", 1, 1, 1, 1, "Conservative", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President kariLake = new President(
            "Kari Lake", "West", 1, 3, 1, 3, "Conservative", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President markKelly = new President(
            "Mark Kelly", "West", 2, 4, 1, 3, "Progressive", "Implied Powers",
            new String[]{"Present Era"}
    );
    static President katieHobbs = new President(
            "Katie Hobbs", "West", 3, 1, 4, 2, "Progressive", "Establishment",
            new String[]{"Present Era"}
    );
    static President michelleObama = new President(
            "Michelle Obama", "Midwest", 1, 6, 1, 7, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
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
            "Vivek Ramaswamy", "Midwest", 1, 1, 2, 1, "Conservative", "Traditional Morality",
            new String[]{"Present Era"}
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
            new String[]{"Present Era"}
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
            new String[]{"Present Era"}
    ); 
    static President blancheLincoln = new President(
            "Blanche Lincoln", "South", 4, 2, 2, 1, "Populist", "Subsidies",
            new String[]{"Modern Era"}
    );
    static President kristiNoem = new President(
            "Kristi Noem", "West", 5, 4, 2, 2, "Conservative", "Deregulation",
            new String[]{"Present Era"}
    );
    static President lizCheney = new President(
            "Liz Cheney", "West", 2, 2, 2, 3, "Conservative", "Establishment",
            new String[]{"Present Era"}
    );
    static President kimReynolds = new President(
            "Kim Reynolds", "Midwest", 4, 4, 4, 1, "Conservative", "Tough Foreign Policy",
            new String[]{"Present Era"}
    );
    static President andrewYang = new President(
            "Andrew Yang", "Northeast", 1, 2, 5, 2, "Populist", "Social Programs",
            new String[]{"Present Era"}
    );
    static President betoORourke = new President(
            "Beto O'Rourke", "South", 2, 5, 2, 3, "Progressive", "Implied Powers",
            new String[]{"Present Era"}
    );
    static President bennieThompson = new President(
            "Bennie Thompson", "South", 7, 1, 2, 2, "Progressive", "Establishment",
            new String[]{"Present Era"}
    );
    static President gregAbbott = new President(
            "Greg Abbott", "South", 5, 3, 4, 3, "Conservative", "Closed Borders",
            new String[]{"Present Era"}
    );
    static President blakeMasters = new President(
            "Blake Masters", "West", 1, 1, 4, 1, "Libertarian", "Closed Borders",
            new String[]{"Present Era"}
    );
    static President tomCotton = new President(
            "Tom Cotton", "Midwest", 5, 3, 4, 2, "Conservative", "Tough Foreign Policy",
            new String[]{"Present Era"}
    );
    static President wesMoore = new President(
            "Wes Moore", "Northeast", 2, 2, 3, 1, "Progressive", "Regulation",
            new String[]{"Present Era"}
    );
    static President jonOssoff = new President(
            "Jon Ossoff", "South", 2, 5, 4, 2, "Progressive", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President kirstenGillibrand = new President(
            "Kirsten Gillibrand", "Northeast", 5, 2, 2, 1, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President kellyAyotte = new President(
            "Kelly Ayotte", "Northeast", 3, 3, 3, 1, "Conservative", "Tough Foreign Policy",
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
            "John Branch", "South", 2, 1, 2, 1, "Populist", "Tough Foreign Policy",
            new String[]{"Jacksonian Era"}
    );
    static President jamesHHammond = new President(
            "James H. Hammond", "South", 3, 2, 2, 3, "Populist", "Racism",
            new String[]{"Civil War Era"}
    );
    static President williamLMarcy = new President(
            "William L. Marcy", "Northeast", 7, 2, 3, 3, "Populist", "Tough Foreign Policy",
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
            "Salmon P. Chase", "Midwest", 7, 1, 6, 4, "Libertarian", "Civil Rights",
            new String[]{"Civil War Era"}
    );
    static President simonCameron = new President(
            "Simon Cameron", "Northeast", 5, 2, 6, 2, "Libertarian", "Subsidies",
            new String[]{"Civil War Era"}
    );
    static President williamTSherman = new President(
            "William T. Sherman", "Northeast", 1, 6, 1, 8, "Conservative", "Tough Foreign Policy",
            new String[]{"Civil War Era"}
    );
    static President stonewallJackson = new President(
            "Stonewall Jackson", "South", 1, 6, 1, 8, "Populist", "Tough Foreign Policy",
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
            "Cassius M. Clay", "South", 2, 4, 3, 1, "Conservative", "Tough Foreign Policy",
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
            "Al D'Amato", "Northeast", 5, 2, 4, 1, "Conservative", "Subsidies",
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
            "Robert Byrd", "Midwest", 9, 2, 5, 5, "Populist", "Subsidies",
            new String[]{"Reagan Era"}
    );
    static President johnCStennis = new President(
            "John C. Stennis", "South", 8, 2, 2, 2, "Populist", "Tough Foreign Policy",
            new String[]{"Reagan Era"}
    );
    static President tipONeill = new President(
            "Tip O'Neill", "Northeast", 7, 4, 2, 5, "Progressive", "Social Programs",
            new String[]{"Reagan Era"}
    );
    static President marshaBlackburn = new President(
            "Marsha Blackburn", "South", 6, 3, 2, 2, "Conservative", "Traditional Morality",
            new String[]{"Present Era"}
    );
    static President larryElder = new President(
            "Larry Elder", "West", 1, 4, 2, 2, "Conservative", "Tax Cuts",
            new String[]{"Present Era"}
    );
    static President dougBurgum = new President(
            "Doug Burgum", "West", 3, 1, 2, 1, "Conservative", "Regulation",
            new String[]{"Present Era"}
    );
    static President garyPeters = new President(
            "Gary Peters", "Midwest", 6, 1, 2, 1, "Progressive", "Establishment",
            new String[]{"Present Era"}
    );
    static President royCooper = new President(
            "Roy Cooper", "South", 5, 4, 4, 2, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President maggieHassan = new President(
            "Maggie Hassan", "Northeast", 5, 2, 3, 1, "Progressive", "Social Liberalism",
            new String[]{"Present Era"}
    );
    static President johnKennedy = new President(
            "John Kennedy", "South", 3, 5, 2, 2, "Conservative", "Law and Order",
            new String[]{"Present Era"}
    );
    static President lindseyGraham = new President(
            "Lindsey Graham", "South", 6, 1, 3, 3, "Conservative", "Law and Order",
            new String[]{"Present Era"}
    );
    static President donBolduc = new President(
            "Don Bolduc", "Northeast", 1, 2, 1, 1, "Conservative", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President robertFKennedyJr = new President(
            "Robert F. Kennedy Jr.", "Northeast", 1, 3, 2, 4, "Populist", "Anti-Establishment",
            new String[]{"Present Era"}
    );
    static President jayInslee = new President(
            "Jay Inslee", "West", 7, 2, 1, 2, "Progressive", "Personal Freedom",
            new String[]{"Present Era"}
    );
    static President maxwellFrost = new President(
            "Maxwell Frost", "South", 2, 2, 1, 1, "Progressive", "Class Unity",
            new String[]{"Present Era"}
    );
    static President markHanna = new President(
            "Mark Hanna", "Midwest", 4, 5, 2, 4, "Conservative", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President harrySNew = new President(
            "Harry S. New", "Midwest", 4, 2, 1, 1, "Progressive", "Personal Freedom",
            new String[]{"Progressive Era"}
    );
    static President philanderCKnox = new President(
            "Philander C. Knox", "Northeast", 5, 1, 3, 1, "Conservative", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President williamRandolphHearst = new President(
            "William Randolph Hearst", "Northeast", 2, 4, 1, 4, "Progressive", "Class Unity",
            new String[]{"Progressive Era"}
    );
    static President francisCockrell = new President(
            "Francis Cockrell", "South", 6, 1, 1, 1, "Populist", "Inflation",
            new String[]{"Progressive Era"}
    );
    static President georgebMcClellanJr = new President(
            "George B. McClellan Jr.", "Northeast", 3, 1, 1, 2, "Progressive", "Gold Standard",
            new String[]{"Progressive Era"}
    );
    static President charlesEvansHughes = new President(
            "Charles Evans Hughes", "Northeast", 7, 4, 5, 5, "Progressive", "Tax Cuts",
            new String[]{"Progressive Era"}
    );
    static President hiramJohnson = new President(
            "Hiram Johnson", "West", 6, 4, 3, 5, "Progressive", "Isolationism",
            new String[]{"Progressive Era"}
    );
    static President nelsonwAldrich = new President(
            "Nelson W. Aldrich", "Northeast", 8, 1, 5, 5, "Conservative", "Tariffs",
            new String[]{"Progressive Era"}
    );
    static President edithWilson = new President(
            "Edith Wilson", "Northeast", 1, 4, 6, 3, "Progressive", "Active Executive",
            new String[]{"Progressive Era"}
    );
    static President thomasRMarshall = new President(
            "Thomas R. Marshall", "Midwest", 7, 6, 1, 2, "Progressive", "Tough Foreign Policy",
            new String[]{"Progressive Era"}
    );
    static President champClark = new President(
            "Champ Clark", "South", 7, 3, 4, 3, "Progressive", "Anti-Central Bank",
            new String[]{"Progressive Era"}
    );
    static President elihuRoot = new President(
            "Elihu Root", "Northeast", 5, 1, 6, 2, "Conservative", "Tough Foreign Policy",
            new String[]{"Progressive Era"}
    );
    static President nicholasMButler = new President(
            "Nicholas M. Butler", "Northeast", 1, 3, 1, 3, "Conservative", "Racism",
            new String[]{"Progressive Era"}
    );
    static President albertBCummins = new President(
            "Albert B. Cummins", "Northeast", 5, 3, 2, 2, "Progressive", "Regulation",
            new String[]{"Progressive Era"}
    );
    static President judsonHarmon = new President(
            "Judson Harmon", "Midwest", 3, 2, 2, 1, "Libertarian", "Social Hierarchy",
            new String[]{"Progressive Era"}
    );
    static President oscarUnderwood = new President(
            "Oscar Underwood", "South", 7, 3, 5, 3, "Progressive", "Free Trade",
            new String[]{"Progressive Era"}
    );
    static President johnAlbertJohnson = new President(
            "John Albert Johnson", "Midwest", 2, 4, 1, 1, "Progressive", "Internal Improvements",
            new String[]{"Progressive Era"}
    );
    static President henryCabotLodge = new President(
            "Henry Cabot Lodge", "Northeast", 7, 2, 6, 5, "Conservative", "Tough Foreign Policy",
            new String[]{"Progressive Era"}
    );
    static President josephGurneyCannon = new President(
            "Joseph Gurney Cannon", "Midwest", 9, 1, 4, 4, "Conservative", "Limited Government",
            new String[]{"Progressive Era"}
    );
    static President curtisGuildJr = new President(
            "Curtis Guild Jr.", "Northeast", 2, 2, 2, 2, "Progressive", "Regulation",
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
            "Robert Lansing", "Northeast", 2, 2, 4, 1, "Libertarian", "Tough Foreign Policy",
            new String[]{"Progressive Era"}
    );
    static President andrewMellon = new President(
            "Andrew Mellon", "Northeast", 3, 1, 8, 6, "Libertarian", "Tough Foreign Policy",
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
            "John Kelly", "Northeast", 2, 4, 1, 3, "Libertarian", "Establishment",
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
            "William Tweed", "Northeast", 2, 6, 1, 5, "Libertarian", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President lukePBlackburn = new President(
            "Luke P. Blackburn", "South", 2, 2, 1, 3, "Progressive", "Personal Freedom",
            new String[]{"Reconstruction Era"}
    );
    static President williamGaston = new President(
            "William Gaston", "Northeast", 2, 3, 1, 1, "Populist", "Personal Freedom",
            new String[]{"Reconstruction Era"}
    );
    static President roscoeconkling = new President(
            "Roscoe Conkling", "Northeast", 6, 4, 2, 5, "Conservative", "Establishment",
            new String[]{"Reconstruction Era"}
    );
    static President williammevarts = new President(
            "William M. Evarts", "Northeast", 3, 4, 1, 2, "Conservative", "Law and Order",
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
            "Lucius Q. C. Lamar", "South", 6, 2, 2, 2, "Populist", "Racism",
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
            "Tax Cuts vs. Social Programs", "Law and Order vs. Personal Freedom"
    );
    static Election e2016 = new Election(
            2016, "nam", "infl", "Midwest", "Closed Borders vs. Open Borders",
            "Free Trade vs. Tariffs", "Establishment vs. Anti-Establishment"
    );
    static Election e2012 = new Election(
            2012, "infl", "pol", "South", "Tax Cuts vs. Social Programs",
            "Tough Foreign Policy vs. Isolationism", "Traditional Morality vs. Social Liberalism"
    );
    static Election e2008 = new Election(
            2008, "infl", "pol", "South", "Regulation vs. Deregulation",
            "Tough Foreign Policy vs. Isolationism", "Tax Cuts vs. Social Programs"
    );
    static Election e2004 = new Election(
            2004, "pol", "exp", "Midwest", "Tough Foreign Policy vs. Isolationism",
            "Law and Order vs. Personal Freedom", "Traditional Morality vs. Social Liberalism"
    );
    static Election e2000 = new Election(
            2000, "infl", "nam", "Midwest", "Traditional Morality vs. Social Liberalism",
            "Tax Cuts vs. Social Programs", "Tough Foreign Policy vs. Isolationism"
    );
    static Election e1996 = new Election(
            1996, "infl", "nam", "South", "Law and Order vs. Personal Freedom",
            "Tax Cuts vs. Social Programs", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1992 = new Election(
            1992, "exp", "pol", "South", "Tax Cuts vs. Social Programs",
            "Tough Foreign Policy vs. Isolationism", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1988 = new Election(
            1988, "exp", "infl", "West", "Law and Order vs. Personal Freedom",
            "Traditional Morality vs. Social Liberalism",
            "Tax Cuts vs. Social Programs"
    );

    static Election e1984 = new Election(
            1984, "infl", "pol", "Midwest", "Tax Cuts vs. Social Programs",
            "Tough Foreign Policy vs. Isolationism",
            "Traditional Morality vs. Social Liberalism"
    );
    static Election e1980 = new Election(
            1980, "nam", "infl", "South", "Tax Cuts vs. Social Programs",
            "Tough Foreign Policy vs. Isolationism", "Nationalism vs. Class Unity"
    );
    static Election e1976 = new Election(
            1976, "infl", "pol", "West", "Regulation vs. Deregulation",
            "Establishment vs. Anti-Establishment", "Law and Order vs. Personal Freedom"
    );
    static Election e1972 = new Election(
            1972, "infl", "nam", "Midwest", "Tough Foreign Policy vs. Isolationism",
            "Law and Order vs. Personal Freedom", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1968 = new Election(
            1968, "nam", "exp", "Midwest", "Traditional Morality vs. Social Liberalism",
            "Law and Order vs. Personal Freedom", "Tough Foreign Policy vs. Isolationism"
    );
    static Election e1964 = new Election(
            1964, "exp", "pol", "West", "Civil Rights vs. Racism", "Tax Cuts vs. Social Programs",
            "Tough Foreign Policy vs. Isolationism"
    );
    static Election e1960 = new Election(
            1960, "infl", "pol", "Midwest", "Tough Foreign Policy vs. Isolationism",
            "Civil Rights vs. Racism", "Tax Cuts vs. Social Programs"
    );
    static Election e1956 = new Election(
            1956, "nam", "infl", "South", "Civil Rights vs. Racism",
            "Tough Foreign Policy vs. Isolationism", "Laissez-Faire vs. Subsidies/Int. Imrp."
    );
    static Election e1952 = new Election(
            1952, "infl", "nam", "South", "Tough Foreign Policy vs. Isolationism",
            "Traditional Morality vs. Social Liberalism", "Tax Cuts vs. Social Programs"
    );
    static Election e1948 = new Election(
            1948, "infl", "nam", "Northeast", "Regulation vs. Deregulation",
            "Civil Rights vs. Racism", "Tax Cuts vs. Social Programs"
    );
    static Election e1944 = new Election(
            1944, "infl", "nam", "Midwest", "Tough Foreign Policy vs. Isolationism",
            "Tax Cuts vs. Social Programs", "Active Executive vs. Limited Government"
    );
    static Election e1940 = new Election(
            1940, "pol", "nam", "Midwest", "Regulation vs. Deregulation",
            "Tough Foreign Policy vs. Isolationism", "Active Executive vs. Limited Government"
    );
    static Election e1936 = new Election(
            1936, "pol", "infl", "Northeast", "Tax Cuts vs. Social Programs",
            "Active Executive vs. Limited Government", "Law and Order vs. Personal Freedom"
    );
    static Election e1932 = new Election(
            1932, "infl", "pol", "Northeast", "Tax Cuts vs. Social Programs",
            "Regulation vs. Deregulation", "Free Trade vs. Tariffs"
    );
    static Election e1928 = new Election(
            1928, "infl", "nam", "South", "Traditional Morality vs. Social Liberalism",
            "Civil Rights vs. Racism", "Regulation vs. Deregulation"
    );
    static Election e1924 = new Election(
            1924, "pol", "infl", "West", "Tax Cuts vs. Social Programs",
            "Closed Borders vs. Open Borders", "Law and Order vs. Personal Freedom"
    );
    static Election e1920 = new Election(
            1920, "infl", "pol", "South", "Active Executive vs. Limited Government",
            "Tough Foreign Policy vs. Isolationism", "Tax Cuts vs. Social Programs"
    );
    static Election e1916 = new Election(
            1916, "pol", "infl", "Northeast", "Tough Foreign Policy vs. Isolationism",
            "Regulation vs. Deregulation", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1912 = new Election(
            1912, "pol", "nam", "West", "Regulation vs. Deregulation",
            "Active Executive vs. Limited Government", "Free Trade vs. Tariffs"
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
            1900, "pol", "nam", "West", "Tough Foreign Policy vs. Isolationism",
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
            "Laissez-Faire vs. Subsidies/Int. Imrp.", "Traditional Morality vs. Social Liberalism"
    );
    static Election e1880 = new Election(
            1880, "exp", "infl", "South", "Civil Rights vs. Racism",
            "Closed Borders vs. Open Borders", "Free Trade vs. Tariffs"
    );
    static Election e1876 = new Election(
            1876, "infl", "pol", "South", "States' Rights vs. Centralization",
            "Civil Rights vs. Racism", "Laissez-Faire vs. Subsidies/Int. Imrp."
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
            1864, "pol", "infl", "Northeast", "Tough Foreign Policy vs. Isolationism",
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
            1848, "infl", "nam", "Midwest", "Nationalism vs. Class Unity", "Tough Foreign Policy vs. Isolationism",
            "Gold Standard vs. Inflation"
    );
    static Election e1844 = new Election(
            1844, "exp", "pol", "Northeast", "Tough Foreign Policy vs. Isolationism",
            "Civil Rights vs. Racism", "States' Rights vs. Centralization"
    );
    static Election e1840 = new Election(
            1840, "nam", "infl", "South", "Laissez-Faire vs. Subsidies/Int. Imrp.",
            "Active Executive vs. Limited Government", "Establishment vs. Anti-Establishment"
    );
    static Election e1836 = new Election(
            1836, "exp", "pol", "South", "Laissez-Faire vs. Subsidies/Int. Imrp.",
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
            1824, "infl", "nam", "Midwest", "Laissez-Faire vs. Subsidies/Int. Imrp.",
            "Social Hierarchy vs. Egalitarianism", "Central Bank vs. Anti-Central Bank"
    );
    static Election e1820 = new Election(
            1820, "nam", "exp", "Northeast", "States' Rights vs. Centralization",
            "Constructionism vs. Implied Powers", "Civil Rights vs. Racism"
    );
    static Election e1816 = new Election(
            1816, "nam", "exp", "Northeast", "Tough Foreign Policy vs. Isolationism",
            "Central Bank vs. Anti-Central Bank", "Free Trade vs. Tariffs"
    );
    static Election e1812 = new Election(
            1812, "pol", "nam", "Northeast", "Tough Foreign Policy vs. Isolationism",
            "States' Rights vs. Centralization", "Laissez-Faire vs. Subsidies/Int. Imrp."
    );
    static Election e1808 = new Election(
            1808, "pol", "infl", "Northeast", "Free Trade vs. Tariffs",
            "Constructionism vs. Implied Powers", "States' Rights vs. Centralization"
    );
    static Election e1804 = new Election(
            1804, "exp", "nam", "Northeast", "Constructionism vs. Implied Powers",
            "Free Trade vs. Tariffs", "Laissez-Faire vs. Subsidies/Int. Imrp."
    );
    static Election e1800 = new Election(
            1800, "exp", "nam", "Northeast", "States' Rights vs. Centralization",
            "Constructionism vs. Implied Powers", "Social Hierarchy vs. Egalitarianism"
    );
    static Election e1796 = new Election(
            1796, "pol", "infl", "Northeast", "Tough Foreign Policy vs. Isolationism",
            "Social Hierarchy vs. Egalitarianism", "Law and Order vs. Personal Freedom"
    );
    static Election e1792 = new Election(
            1792, "nam", "infl", "South", "Constructionism vs. Implied Powers",
            "Central Bank vs. Anti-Central Bank", "Laissez-Faire vs. Subsidies/Int. Imrp."
    );
    static Election e1788 = new Election(
            1788, "nam", "infl", "Northeast", "States' Rights vs. Centralization",
            "Social Hierarchy vs. Egalitarianism", "Constructionism vs. Implied Powers"
    );
    static Election albania2017 = new Election(
            2017, "nam", "infl", "South", "Establishment vs. Anti-Establishment",
            "Tough Foreign Policy vs. Isolationism", "Law and Order vs. Personal Freedom"
    );

    static Policy toughfp1 = new Policy("Tough Foreign Policy", "Progressive", "Conservative");
    static Policy toughfp2 = new Policy("Tough Foreign Policy", "Progressive", "Conservative");
    static Policy toughfp3 = new Policy("Tough Foreign Policy", "Progressive", "Conservative");
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
    static Policy sociallib1 = new Policy("Social Liberalism", "Progressive");
    static Policy sociallib2 = new Policy("Social Liberalism", "Progressive");
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
    static Policy intimprov = new Policy("Internal Improvements", "Conservative", "Progressive");
    static Policy subsidies = new Policy("Subsidies", "Conservative", "Progressive");
    static Policy laissez1 = new Policy("Laissez-Faire", "Libertarian", "Populist");
    static Policy laissez2 = new Policy("Laissez-Faire", "Libertarian", "Populist");
    static Policy openb = new Policy("Open Borders", "Progressive", "Libertarian");
    static Policy closedb = new Policy("Closed Borders", "Conservative", "Populist", "Libertarian");
    static Policy activeexec = new Policy("Active Executive", "Progressive", "Conservative", "Populist");
    static Policy limitedgov = new Policy(
            "Limited Government", "Conservative", "Libertarian", "Populist"
    );
    static Policy impliedp = new Policy("Implied Powers", "Progressive", "Conservative");
    static Policy construc = new Policy(
            "Constructionism", "Conservative", "Libertarian", "Populist"
    );
    static Policy laworder = new Policy("Law and Order", "Conservative", "Populist");
    static Policy pfreedom = new Policy("Personal Freedom", "Progressive", "Libertarian", "Populist");
    static Policy bank = new Policy("Central Bank", "Progressive", "Conservative");
    static Policy antibank = new Policy("Anti-Central Bank", "Libertarian", "Populist");
    static Policy nationalism1 = new Policy("Nationalism", "Conservative", "Libertarian");
    static Policy classunity = new Policy("Class Unity", "Progressive", "Populist");
    static Policy establishment = new Policy("Establishment", "Conservative", "Progressive");
    static Policy antiestablishment = new Policy(
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

    static String fp = "Tough Foreign Policy vs. Isolationism";
    static String tax = "Tax Cuts vs. Social Programs";
    static String morality = "Traditional Morality vs. Social Liberalism";
    static String trade = "Free Trade vs. Tariffs";
    static String sr = "States' Rights vs. Centralization";
    static String goldinfl = "Gold Standard vs. Inflation";
    static String civilracism = "Civil Rights vs. Racism";
    static String reg = "Regulation vs. Deregulation";
    static String society = "Social Hierarchy vs. Egalitarianism";
    static String cronyism = "Laissez-Faire vs. Subsidies/Int. Impr.";
    static String borders = "Closed Borders vs. Open Borders";
    static String exec = "Active Executive vs. Limited Government";
    static String consti = "Constructionism vs. Implied Powers";
    static String law = "Law and Order vs. Personal Freedom";
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
        policies.add(intimprov);
        policies.add(subsidies);
        policies.add(laissez1);
        policies.add(laissez2);
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
        policies.add(antiestablishment);
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
        
        if (preset.equals("presidentsonly")) {
            //System.out.println("PRESIDENTS ONLY CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("expanded")) {
            presidents.addAll(nonpresidents);
            //System.out.println("EXPANDED CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("memes")) {
            presidents.addAll(nonpresidents);
            presidents.addAll(meme);
            //System.out.println("MEME CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("standard")) {
            Collections.shuffle(nonpresidents);
            for (int i = 0; i < 7; i++) {
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
            presidents.add(dababy);
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
