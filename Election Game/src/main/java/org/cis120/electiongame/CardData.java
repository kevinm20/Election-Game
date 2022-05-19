package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * This brutal class creates all the presidents, policies, and elections.
 */
public class CardData {
    private static boolean memes = false;
    private static boolean expanded = false;
    private static boolean standard = false;

    static President calvinCoolidge = new President(
            "Calvin Coolidge", "Northeast", 7, 6, 8, 5, "Libertarian", "Tax Cuts"
    );
    static President thomasJefferson = new President(
            "Thomas Jefferson", "South", 7, 7, 7, 10, "Libertarian", "States' Rights"
    );
    static President henryClay = new President(
            "Henry Clay", "South", 9, 2, 8, 7, "Conservative", "Subsidies"
    );
    static President alexanderHamilton = new President(
            "Alexander Hamilton", "Northeast", 4, 1, 10, 8, "Conservative", "Centralization"
    );
    static President lyndonBJohnson = new President(
            "Lyndon B. Johnson", "South", 10, 2, 7, 6, "Progressive", "Social Programs"
    );
    static President donaldTrump = new President(
            "Donald Trump", "Northeast", 1, 6, 1, 7, "Conservative", "Nationalism"
    );
    static President ulyssesSGrant = new President(
            "Ulysses S. Grant", "Midwest", 1, 8, 4, 9, "Conservative", "Civil Rights"
    );
    static President woodrowWilson = new President(
            "Woodrow Wilson", "Northeast", 2, 4, 8, 5, "Progressive", "Centralization"
    );
    static President millardFillmore = new President(
            "Millard Fillmore", "Northeast", 4, 1, 5, 2, "Conservative", "Traditional Morality"
    );
    static President robertATaft = new President(
            "Robert A. Taft", "Midwest", 7, 3, 6, 3, "Libertarian", "Isolationism"
    );
    static President billClinton = new President(
            "Bill Clinton", "South", 6, 7, 6, 5, "Progressive", "Free Trade"
    );
    static President georgeWashington = new President(
            "George Washington", "South", 3, 10, 6, 10, "Libertarian", "Nationalism"
    );
    static President jamesAGarfield = new President(
            "James A. Garfield", "Midwest", 5, 5, 2, 3, "Conservative", "Limited Government"
    );
    static President jamesMonroe = new President(
            "James Monroe", "South", 8, 8, 6, 6, "Libertarian", "Nationalism"
    );
    static President williamMcKinley = new President(
            "William McKinley", "Midwest", 7, 5, 7, 6, "Conservative", "Tariffs"
    );
    static President andrewJackson = new President(
            "Andrew Jackson", "South", 2, 10, 3, 9, "Populist", "Anti-Central Bank"
    );
    static President williamJenningsBryan = new President(
            "William Jennings Bryan", "West", 2, 9, 6, 2, "Populist", "Inflation"
    );
    static President ronaldReagan = new President(
            "Ronald Reagan", "West", 4, 10, 5, 8, "Conservative", "Tax Cuts"
    );
    static President groverCleveland = new President(
            "Grover Cleveland", "Northeast", 3, 4, 7, 2, "Libertarian", "Free Trade"
    );
    static President richardNixon = new President(
            "Richard Nixon", "West", 8, 7, 5, 8, "Conservative", "Law and Order"
    );
    static President warrenGHarding = new President(
            "Warren G. Harding", "Midwest", 3, 6, 6, 1, "Conservative", "Limited Government"
    );
    static President theodoreRoosevelt = new President(
            "Theodore Roosevelt", "Northeast", 4, 8, 9, 6, "Progressive", "Regulation"
    );
    static President franklinDRoosevelt = new President(
            "Franklin D. Roosevelt", "Northeast", 5, 9, 9, 6, "Progressive", "Social Programs"
    );
    static President johnAdams = new President(
            "John Adams", "Northeast", 10, 3, 7, 8, "Conservative", "Social Hierarchy"
    );
    static President johnCCalhoun = new President(
            "John C. Calhoun", "South", 9, 2, 6, 5, "Libertarian", "States' Rights"
    );
    static President jamesMadison = new President(
            "James Madison", "South", 7, 4, 10, 8, "Libertarian", "Constructionism"
    );
    static President johnQuincyAdams = new President(
            "John Quincy Adams", "Northeast", 8, 3, 6, 5, "Conservative", "Civil Rights"
    );
    static President abrahamLincoln = new President(
            "Abraham Lincoln", "Midwest", 3, 9, 10, 7, "Conservative", "Civil Rights"
    );
    static President williamHenryHarrison = new President(
            "William Henry Harrison", "Midwest", 3, 6, 1, 7, "Conservative", "Central Bank"
    );
    static President martinVanBuren = new President(
            "Martin Van Buren", "Northeast", 8, 4, 5, 4, "Libertarian", "Laissez-Faire"
    );
    static President barackObama = new President(
            "Barack Obama", "Midwest", 3, 8, 6, 2, "Progressive", "Social Liberalism"
    );
    static President jimmyCarter = new President(
            "Jimmy Carter", "South", 3, 5, 4, 1, "Progressive", "Anti-Establishment"
    );
    static President johnTyler = new President(
            "John Tyler", "South", 6, 2, 2, 3, "Libertarian", "Constructionism"
    );
    static President jamesKPolk = new President(
            "James K. Polk", "South", 6, 6, 2, 1, "Populist", "Tough Foreign Policy"
    );
    static President zacharyTaylor = new President(
            "Zachary Taylor", "Northeast", 1, 3, 1, 6, "Conservative", "Nationalism"
    );
    static President georgeWBush = new President(
            "George W. Bush", "South", 3, 6, 3, 7, "Conservative", "Tough Foreign Policy"
    );
    static President alGore = new President(
            "Al Gore", "South", 8, 3, 6, 5, "Progressive", "Social Liberalism"
    );
    static President johnFKennedy = new President(
            "John F. Kennedy", "Northeast", 5, 9, 6, 7, "Progressive", "Tough Foreign Policy"
    );
    static President georgeHWBush = new President(
            "George H. W. Bush", "South", 7, 2, 4, 6, "Conservative", "Tough Foreign Policy"
    );
    static President geraldFord = new President(
            "Gerald Ford", "Midwest", 7, 2, 1, 3, "Conservative", "Establishment"
    );
    static President williamHowardTaft = new President(
            "William Howard Taft", "Midwest", 2, 3, 5, 4, "Conservative", "Free Trade"
    );
    static President herbertHoover = new President(
            "Herbert Hoover", "West", 2, 4, 5, 2, "Progressive", "Tariffs"
    );
    static President harrySTruman = new President(
            "Harry S. Truman", "South", 7, 4, 3, 4, "Progressive", "Tough Foreign Policy"
    );
    static President dwightEisenhower = new President(
            "Dwight Eisenhower", "West", 1, 8, 5, 10, "Conservative", "Internal Improvements"
    );
    static President benjaminHarrison = new President(
            "Benjamin Harrison", "Midwest", 3, 4, 2, 3, "Conservative", "Tariffs"
    );
    static President chesterAArthur = new President(
            "Chester A. Arthur", "Northeast", 3, 3, 2, 1, "Conservative", "Closed Borders"
    );
    static President rutherfordBHayes = new President(
            "Rutherford B. Hayes", "Midwest", 3, 2, 1, 3, "Conservative", "Civil Rights"
    );
    static President andrewJohnson = new President(
            "Andrew Johnson", "South", 6, 2, 3, 3, "Populist", "Racism"
    );
    static President jamesBuchanan = new President(
            "James Buchanan", "Northeast", 6, 3, 3, 3, "Populist", "Laissez-Faire"
    );
    static President franklinPierce = new President(
            "Franklin Pierce", "Northeast", 5, 5, 3, 1, "Populist", "Racism"
    );
    static President georgeWallace = new President(
            "George Wallace", "South", 5, 5, 1, 4, "Populist", "Racism"
    );
    static President barryGoldwater = new President(
            "Barry Goldwater", "West", 5, 3, 4, 4, "Libertarian", "Tax Cuts"
    );
    static President joeBiden = new President(
            "Joe Biden", "Northeast", 10, 2, 3, 6, "Progressive", "Social Programs"
    );
    // Meme cards
    static President millFill = new President(
            "Millard Fillmore (Ascended)", "Northeast", 999, 999, 999, 999, "Conservative",
            "Traditional Morality"
    );
    static President jebBush = new President(
            "Jeb Bush!", "South", 3, -5, 2, 4, "Conservative", "Establishment"
    );
    static President dababy = new President(
            "DaBaby", "South", 0, 10, 0, 10, "Progressive", "Personal Freedom"
    );
    static President emperorpalpatine = new President(
            "Emperor Palpatine", "Northeast", 7, 6, 3, 10, "Conservative", "Centralization"
    );
    static President odysseus = new President(
            "Odysseus", "South", 2, 5, 8, 9, "Conservative", "Tough Foreign Policy"
    );
    static President napoleon = new President(
            "Napoleon Bonaparte", "Northeast", 2, 7, 4, 10, "Conservative", "Tough Foreign Policy"
    );
    static President libRight = new President(
            "LibRight Chad", "Northeast", 4, 6, 6, 8, "Libertarian", "Tax Cuts"
    );
    static President authRight = new President(
            "AuthRight Chad", "South", 8, 6, 4, 6, "Conservative", "Traditional Morality"
    );
    static President libLeft = new President(
            "LibLeft Chad", "West", 4, 8, 6, 6, "Progressive", "Social Liberalism"
    );
    static President authLeft = new President(
            "AuthLeft Chad", "Northeast", 6, 4, 8, 6, "Populist", "Social Programs"
    );
    static President churchill = new President(
            "Winston Churchill", "Northeast", 6, 10, 4, 5, "Conservative", "Tough Foreign Policy"
    );
    static President monke = new President(
            "Monke", "West", 1, 10, 1, 1, "Conservative", "Traditional Morality"
    );
    static President impostor = new President(
            "impostor", "West", 3, 10, 10, 0, "Populist", "Tough Foreign Policy"
    );
    static President comrademccain = new President(
            "Comrade McCain", "West", 8, 2, 4, 6, "Progressive", "Communism"
    );
    static President michaelDukakis = new President(
            "Michael Dukakis", "Northeast", 5, 2, 5, 4, "Progressive", "Personal Freedom"
    );
    static President evilGeorgeWallace = new President(
            "Evil George Wallace", "South", 6, 5, 1, 4, "Progressive", "Civil Rights"
    );
    static President postMalone = new President(
            "Post Malone", "South", 0, 10, 0, 10, "Libertarian", "Constructionism"
    );
    static President johnMcCain = new President(
            "John McCain", "West", 7, 2, 3, 5, "Conservative", "Tough Foreign Policy"
    );
    static President ambroseBurnside = new President(
            "Ambrose Burnside", "Northeast", 3, 5, 1, 4, "Conservative", "Tough Foreign Policy"
    );
    static President ronDeSantis = new President(
            "Ron DeSantis", "South", 4, 6, 8, 5, "Conservative", "Deregulation"
    );
    static President hillaryClinton = new President(
            "Hillary Clinton", "Northeast", 4, 1, 4, 6, "Progressive", "Social Liberalism"
    );
    static President mittRomney = new President(
            "Mitt Romney", "Northeast", 3, 3, 5, 4, "Conservative", "Tax Cuts"
    );
    static President garyJohnson = new President(
            "Gary Johnson", "West", 3, 1, 2, 1, "Libertarian", "Tax Cuts"
    );
    static President rossPerot = new President(
            "Ross Perot", "South", 1, 3, 6, 4, "Populist", "Tariffs"
    );
    static President robertLaFollette = new President(
            "Robert La Follette", "Midwest", 9, 3, 5, 3, "Progressive", "Regulation"
    );
    static President charlesLindbergh = new President(
            "Charles Lindbergh", "Midwest", 1, 5, 2, 9, "Populist", "Isolationism"
    );
    static President williamHSeward = new President(
            "William H. Seward", "Northeast", 8, 1, 5, 6, "Conservative", "Civil Rights"
    );
    static President josephMcCarthy = new President(
            "Joseph McCarthy", "Midwest", 3, 3, 1, 7, "Conservative", "Traditional Morality"
    );
    static President stromThurmond = new President(
            "Strom Thurmond", "South", 9, 1, 3, 3, "Populist", "Racism"
    );
    static President frederickDouglass = new President(
            "Frederick Douglass", "Northest", 1, 9, 6, 7, "Progressive", "Civil Rights"
    );
    static President eugeneMcCarthy = new President(
            "Eugene McCarthy", "Midwest", 7, 3, 4, 2, "Progressive", "Isolationism"
    );
    static President hubertHumphrey = new President(
            "Hubert Humphrey", "Midwest", 9, 3, 7, 6, "Progressive", "Civil Rights"
    );
    static President douglasMacArthur = new President(
            "Douglas MacArthur", "South", 3, 6, 2, 9, "Conservative", "Tough Foreign Policy"
    );
    static President kanyeWest = new President(
            "Kanye West", "Midwest", 1, 7, 2, 10, "Conservative", "Traditional Morality"
    );
    static President johnDRockefeller = new President(
            "John D. Rockefeller", "Northeast", 1, 1, 5, 9, "Conservative", "Deregulation"
    );
    static President dickCheney = new President(
            "Dick Cheney", "West", 6, 1, 2, 4, "Conservative", "Tough Foreign Policy"
    );
    static President alSmith = new President(
            "Al Smith", "Northeast", 4, 2, 3, 3, "Progressive", "Personal Freedom"
    );
    static President patrickHenry = new President(
            "Patrick Henry", "South", 3, 8, 2, 6, "Libertarian", "Limited Government"
    );
    static President patBuchanan = new President(
            "Pat Buchanan", "Northeast", 1, 2, 8, 2, "Conservative", "Isolationism"
    );
    static President thomasPaine = new President(
            "Thomas Paine", "Northeast", 1, 7, 5, 6, "Progressive", "Egalitarianism"
    );
    static President marcoRubio = new President(
            "Marco Rubio", "South", 3, 2, 5, 2, "Conservative", "Tax Cuts"
    );
    static President bernieSanders = new President(
            "Bernie Sanders", "Northeast", 6, 4, 3, 4, "Progressive", "Social Programs"
    );
    static President ronPaul = new President(
            "Ron Paul", "South", 5, 5, 5, 1, "Libertarian", "Anti-Central Bank"
    );
    static President johnMuir = new President(
            "John Muir", "West", 1, 6, 1, 4, "Progressive", "Regulation"
    );
    static President tedCruz = new President(
            "Ted Cruz", "South", 4, 3, 6, 3, "Conservative", "Tax Cuts"
    );
    static President georgeMcClellan = new President(
            "George McClellan", "Northeast", 1, 5, 2, 8, "Conservative", "Tax Cuts"
    );
    static President stephenADouglas = new President(
            "Stephen A. Douglas", "Midwest", 6, 3, 9, 7, "Populist", "States' Rights"
    );
    static President thaddeusStevens = new President(
            "Thaddeus Stevens", "Northeast", 3, 4, 5, 6, "Conservative", "Civil Rights"
    );
    static President johnCFremont = new President(
            "John C. Fremont", "West", 3, 3, 3, 4, "Conservative", "Civil Rights"
    );
    static President horaceGreeley = new President(
            "Horace Greeley", "Northeast", 1, 3, 2, 6, "Progressive", "Social Liberalism"
    );
    static President robertELee = new President(
            "Robert E. Lee", "South", 1, 8, 1, 9, "Populist", "States' Rights"
    );
    static President nelsonRockefeller = new President(
            "Nelson Rockefeller", "Northeast", 4, 3, 4, 3, "Progressive", "Internal Improvements"
    );
    static President thomasDewey = new President(
            "Thomas Dewey", "Northeast", 5, 3, 3, 6, "Conservative", "Law and Order"
    );
    static President ralphNader = new President(
            "Ralph Nader", "Northeast", 1, 2, 2, 1, "Progressive", "Social Liberalism"
    );
    static President georgeMcGovern = new President(
            "George McGovern", "West", 5, 2, 3, 2, "Progressive", "Social Programs"
    );
    static President bobbyKennedy = new President(
            "Bobby Kennedy", "Northeast", 3, 7, 4, 8, "Progressive", "Isolationism"
    );
    static President georgePatton = new President(
            "George Patton", "West", 1, 9, 1, 7, "Conservative", "Tough Foreign Policy"
    );
    static President warrenG = new President(
            "Warren G.", "Midwest", 6, 9, 9, 4, "Conservative", "Limited Government"
    );
    static President shirleyChisholm = new President(
            "Shirley Chisholm", "Northest", 2, 5, 3, 4, "Progressive", "Social Liberalism"
    );
    static President bettyFord = new President(
            "Betty Ford", "Midwest", 1, 6, 1, 6, "Progressive", "Social Liberalism"
    );
    static President sarahPalin = new President(
            "Sarah Palin", "West", 2, 4, 3, 2, "Conservative", "Anti-Establishment"
    );
    static President jackKemp = new President(
            "Jack Kemp", "Northeast", 5, 1, 5, 1, "Conservative", "Tax Cuts"
    );
    static President howardDean = new President(
            "Howard Dean", "Northeast", 4, 5, 3, 2, "Progressive", "Isolationism"
    );
    static President rudyGiuliani = new President(
            "Rudy Giuliani", "Northeast", 2, 6, 2, 7, "Conservative", "Law and Order"
    );
    static President martinLutherKingJr = new President(
            "Martin Luther King Jr.", "South", 1, 9, 5, 9, "Progressive", "Civil Rights"
    );
    static President danQuayle = new President(
            "Dan Quayle", "Midwest", 5, 1, 2, 2, "Conservative", "Traditional Morality"
    );
    static President hueyLong = new President(
            "Huey Long", "South", 4, 7, 6, 3, "Populist", "Social Programs"
    );
    static President colinPowell = new President(
            "Colin Powell", "South", 5, 4, 3, 4, "Conservative", "Tough Foreign Policy"
    );
    static President susanBAnthony = new President(
            "Susan B. Anthony", "Northeast", 1, 7, 2, 6, "Progressive", "Social Liberalism"
    );
    static President walterMondale = new President(
            "Walter Mondale", "Midwest", 6, 2, 2, 2, "Progressive", "Social Liberalism"
    );
    static President spiroAgnew = new President(
            "Spiro Agnew", "Northeast", 5, 3, 3, 2, "Conservative", "Traditional Morality"
    );
    static President alfLandon = new President(
            "Alf Landon", "West", 3, 1, 3, 1, "Conservative", "Tax Cuts"
    );
    static President wendellWillkie = new President(
            "Wendell Willkie", "Midwest", 1, 7, 2, 3, "Conservative", "Tough Foreign Policy"
    );
    static President johnNanceGarner = new President(
            "John Nance Garner", "South", 7, 2, 5, 2, "Populist", "Limited Government"
    );
    static President henryFord = new President(
            "Henry Ford", "Midwest", 1, 1, 1, 8, "Progressive", "Isolationism"
    );
    static President danielWebster = new President(
            "Daniel Webster", "Northeast", 9, 2, 8, 4, "Conservative", "Centralization"
    );
    static President winfieldScott = new President(
            "Winfield Scott", "South", 1, 2, 1, 5, "Conservative", "Nationalism"
    );
    static President johnCBreckinridge = new President(
            "John C. Breckinridge", "South", 4, 4, 3, 3, "Populist", "Racism"
    );
    static President johnBell = new President(
            "John Bell", "South", 6, 2, 4, 1, "Conservative", "Centralization"
    );
    static President charlesSumner = new President(
            "Charles Sumner", "Northeast", 5, 1, 3, 7, "Conservative", "Civil Rights"
    );
    static President johnBrown = new President(
            "John Brown", "Northeast", 1, 5, 1, 9, "Progressive", "Civil Rights"
    );
    static President andrewJackson24 = new President(
            "Andrew Jackson '24", "South", 2, 8, 2, 7, "Populist", "Egalitarianism"
    );
    static President andrewJackson32 = new President(
            "Andrew Jackson '32", "South", 5, 10, 3, 9, "Populist", "Anti-Central Bank"
    );
    static President andrewJackson36 = new President(
            "Andrew Jackson '36", "South", 7, 8, 6, 10, "Populist", "Anti-Central Bank"
    );
    static President theodoreRoosevelt98 = new President(
            "Teddy Roosevelt '98", "Northeast", 2, 7, 3, 5, "Progressive", "Regulation"
    );
    static President theodoreRoosevelt04 = new President(
            "Teddy Roosevelt '04", "Northeast", 5, 7, 7, 8, "Progressive", "Regulation"
    );
    static President theodoreRoosevelt12 = new President(
            "Teddy Roosevelt '12", "Northeast", 6, 8, 9, 10, "Progressive", "Regulation"
    );
    static President franklinDRoosevelt32 = new President(
            "Franklin Roosevelt '32", "Northeast", 2, 8, 5, 4, "Progressive", "Social Programs"
    );
    static President franklinDRoosevelt40 = new President(
            "Franklin Roosevelt '40", "Northeast", 7, 9, 8, 8, "Progressive", "Social Programs"
    );
    static President franklinDRoosevelt44 = new President(
            "Franklin Roosevelt '44", "Northeast", 9, 8, 9, 10, "Progressive", "Tough Foreign Policy"
    );
    static President abrahamLincoln56 = new President(
            "Abraham Lincoln '56", "Midwest", 2, 6, 8, 5, "Conservative", "Civil Rights"
    );
    static President abrahamLincoln60 = new President(
            "Abraham Lincoln '60", "Midwest", 3, 9, 10, 7, "Conservative", "Civil Rights"
    );
    static President abrahamLincoln65 = new President(
            "Abraham Lincoln '65", "Midwest", 8, 9, 10, 10, "Conservative", "Civil Rights"
    );
    static President calvinCoolidge20 = new President(
            "Calvin Coolidge '20", "Northeast", 4, 4, 5, 2, "Libertarian", "Tax Cuts"
    );
    static President calvinCoolidge23 = new President(
            "Calvin Coolidge '23", "Northeast", 6, 5, 7, 5, "Libertarian", "Tax Cuts"
    );
    static President calvinCoolidge28 = new President(
            "Calvin Coolidge '28", "Northeast", 9, 6, 9, 7, "Libertarian", "Tax Cuts"
    );
    static President richardNixon60 = new President(
            "Richard Nixon '60", "West", 8, 5, 5, 7, "Conservative", "Tough Foreign Policy"
    );
    static President richardNixon68 = new President(
            "Richard Nixon '68", "West", 8, 7, 5, 8, "Conservative", "Law and Order"
    );
    static President richardNixon84 = new President(
            "Richard Nixon '84", "West", 9, 5, 8, 10, "Conservative", "Tough Foreign Policy"
    );
    static President ronaldReagan76 = new President(
            "Ronald Reagan '76", "West", 4, 6, 3, 7, "Conservative", "Traditional Morality"
    );
    static President ronaldReagan80 = new President(
            "Ronald Reagan '80", "West", 4, 10, 5, 8, "Conservative", "Tax Cuts"
    );
    static President ronaldReagan88 = new President(
            "Ronald Reagan '88", "West", 8, 9, 7, 10, "Conservative", "Tough Foreign Policy"
    );
    static President alGore88 = new President(
            "Al Gore '88", "South", 4, 2, 3, 4, "Progressive", "Traditional Morality"
    );
    static President alGore00 = new President(
            "Al Gore '00", "South", 8, 3, 6, 5, "Progressive", "Social Liberalism"
    );
    static President joeBiden96 = new President(
            "Joe Biden '96", "Northeast", 7, 2, 5, 2, "Progressive", "Social Programs"
    );
    static President joeBiden20 = new President(
            "Joe Biden '20", "Northeast", 10, 2, 3, 6, "Progressive", "Social Programs"
    );
    static President ulyssesSGrant66 = new President(
            "Ulysses S. Grant '66", "Midwest", 1, 6, 2, 9, "Conservative", "Civil Rights"
    );
    static President ulyssesSGrant72 = new President(
            "Ulysses S. Grant '72", "Midwest", 5, 8, 5, 10, "Conservative", "Civil Rights"
    );
    static President ulyssesSGrant76 = new President(
            "Ulysses S. Grant '76", "Midwest", 6, 8, 7, 10, "Conservative", "Civil Rights"
    );
    static President mikePence = new President(
            "Mike Pence", "Midwest", 6, 2, 3, 4, "Conservative", "Traditional Morality"
    );
    static President jebBushh = new President(
            "Jeb Bush", "South", 3, 1, 2, 4, "Conservative", "Establishment"
    );
    static President joeLiberal = new President(
            "Joe Liberal", "Northest", 10, 1, 9, 5, "Progressive", "Social Liberalism"
    );
    static President geraldMander = new President(
            "Gerald Mander", "South", 10, 1, 9, 5, "Conservative", "Tax Cuts"
    );
    
    
    
    
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
            "Tough Foreign Policy vs. Isolationism", "Nationalism"
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
            "States' Rights vs. Centralization", "Nationalism"
    );
    static Election e1848 = new Election(
            1848, "infl", "nam", "Midwest", "Nationalism", "Tough Foreign Policy vs. Isolationism",
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
    static Policy taxcuts1 = new Policy("Tax Cuts", "Conservative", "Libertarian", "Populist");
    static Policy taxcuts2 = new Policy("Tax Cuts", "Conservative", "Libertarian", "Populist");
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
    static Policy socialhierarchy = new Policy("Social Hierarchy", "Conservative");
    static Policy egalitarianism = new Policy(
            "Egalitarianism", "Progressive", "Populist", "Libertarian"
    );
    static Policy intimprov = new Policy("Internal Improvements", "Conservative", "Progressive");
    static Policy subsidies = new Policy("Subsidies", "Conservative", "Progressive");
    static Policy laissez1 = new Policy("Laissez-Faire", "Libertarian", "Populist");
    static Policy laissez2 = new Policy("Laissez-Faire", "Libertarian", "Populist");
    static Policy openb = new Policy("Open Borders", "Progressive", "Libertarian");
    static Policy closedb = new Policy("Closed Borders", "Conservative", "Populist");
    static Policy activeexec = new Policy("Active Executive", "Progressive");
    static Policy limitedgov = new Policy(
            "Limited Government", "Conservative", "Libertarian", "Populist"
    );
    static Policy impliedp = new Policy("Implied Powers", "Progressive", "Conservative");
    static Policy construc = new Policy(
            "Constructionism", "Conservative", "Libertarian", "Populist"
    );
    static Policy laworder = new Policy("Law and Order", "Conservative", "Populist");
    static Policy pfreedom = new Policy("Personal Freedom", "Progressive", "Libertarian");
    static Policy bank = new Policy("Central Bank", "Progressive", "Conservative");
    static Policy antibank = new Policy("Anti-Central Bank", "Libertarian", "Populist");
    static Policy nationalism1 = new Policy("Nationalism", "Conservative", "Libertarian");
    static Policy nationalism2 = new Policy("Nationalism", "Conservative", "Libertarian");
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
    static String nation = "Nationalism";
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
        policies.add(nationalism2);
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

    public static List<President> getPresidents(String preset, int pres, boolean presSort, int nonpres, 
            boolean nonPresSort, int memeCards, boolean memesSort) {
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
        nonpresidents.add(kanyeWest);
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
        meme.add(ambroseBurnside);
        for (int i = 0; i < 5; i++) {
            meme.add(michaelDukakis);
        }
        meme.add(warrenG);
        meme.add(geraldMander);
        meme.add(joeLiberal);
        
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
            presidents.add(abrahamLincoln56);
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
            presidents.addAll(nonpresidents);
            System.out.println("GENERATIONAL CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        }
        
        if (preset.equals("presidentsonly")) {
            System.out.println("PRESIDENTS ONLY CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("expanded")) {
            presidents.addAll(nonpresidents);
            System.out.println("EXPANDED CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("memes")) {
            presidents.addAll(nonpresidents);
            presidents.addAll(meme);
            System.out.println("MEME CARD DECK");
            Collections.shuffle(presidents);
            return presidents;
        } else if (preset.equals("standard")) {
            Collections.shuffle(nonpresidents);
            for (int i = 0; i < 7; i++) {
                presidents.add(nonpresidents.get(i));
            }
            System.out.println("STANDARD CARD DECK");
            Collections.shuffle(presidents);
            for (President p : presidents) {
                System.out.println(p.toString());
            }       
            return presidents;
        }
        
        if(preset.equals("custom")) {
            
        }
        
        Collections.shuffle(nonpresidents);
        for (int i = 0; i < 7; i++) {
            presidents.add(nonpresidents.get(i));
        }
        System.out.println("STANDARD CARD DECK");
        return presidents;
    }
   
    
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
