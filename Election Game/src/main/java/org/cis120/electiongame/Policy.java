package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

/*
 * Policies are another type of card.
 */

public class Policy extends Card {
    private String category;
    private List<String> ideologies = new ArrayList<String>();

    // I needed these 4 different constructors because each policy can have up to 4
    // ideologies
    public Policy(String name, String id1, String id2, String id3, String id4) {
        this(name, id1, id2, id3);
        this.ideologies.add(id4);
    }

    public Policy(String name, String id1, String id2, String id3) {
        this(name, id1, id2);
        this.ideologies.add(id3);
    }

    public Policy(String name, String id1, String id2) {
        this(name, id1);
        this.ideologies.add(id2);
    }

    public Policy(String name, String ideology1) {
        super.setName(name);
        this.category = setCategory(name);
        this.ideologies.add(ideology1);
    }

    // This method was not very fun...
    public String setCategory(String s) {
        switch (s) {
            case ("Tough Foreign Policy"):
                return "Tough Foreign Policy vs. Isolationism";
            case ("Isolationism"):
                return "Tough Foreign Policy vs. Isolationism";
            case ("Tax Cuts"):
                return "Tax Cuts vs. Social Programs";
            case ("Social Programs"):
                return "Tax Cuts vs. Social Programs";
            case ("Traditional Morality"):
                return "Traditional Morality vs. Social Liberalism";
            case ("Social Liberalism"):
                return "Traditional Morality vs. Social Liberalism";
            case ("Free Trade"):
                return "Free Trade vs. Tariffs";
            case ("Tariffs"):
                return "Free Trade vs. Tariffs";
            case ("States' Rights"):
                return "States' Rights vs. Centralization";
            case ("Centralization"):
                return "States' Rights vs. Centralization";
            case ("Gold Standard"):
                return "Gold Standard vs. Inflation";
            case ("Inflation"):
                return "Gold Standard vs. Inflation";
            case ("Civil Rights"):
                return "Civil Rights vs. Racism";
            case ("Racism"):
                return "Civil Rights vs. Racism";
            case ("Regulation"):
                return "Regulation vs. Deregulation";
            case ("Deregulation"):
                return "Regulation vs. Deregulation";
            case ("Social Hierarchy"):
                return "Social Hierarchy vs. Egalitarianism";
            case ("Egalitarianism"):
                return "Social Hierarchy vs. Egalitarianism";
            case ("Laissez-Faire"):
                return "Laissez-Faire vs. Strategic Investments";
            case ("Strategic Investments"):
                return "Laissez-Faire vs. Strategic Investments";
            case ("Closed Borders"):
                return "Closed Borders vs. Open Borders";
            case ("Open Borders"):
                return "Closed Borders vs. Open Borders";
            case ("Active Executive"):
                return "Active Executive vs. Limited Government";
            case ("Limited Government"):
                return "Active Executive vs. Limited Government";
            case ("Constructionism"):
                return "Constructionism vs. Implied Powers";
            case ("Implied Powers"):
                return "Constructionism vs. Implied Powers";
            case ("Law and Order"):
                return "Law and Order vs. Personal Freedom";
            case ("Personal Freedom"):
                return "Law and Order vs. Personal Freedom";
            case ("Central Bank"):
                return "Central Bank vs. Anti-Central Bank";
            case ("Anti-Central Bank"):
                return "Central Bank vs. Anti-Central Bank";
            case ("Nationalism"):
                return "Nationalism vs. Class Unity";
            case ("Class Unity"):
                return "Nationalism vs. Class Unity";
            case ("Establishment"):
                return "Establishment vs. Anti-Establishment";
            case ("Anti-Establishment"):
                return "Establishment vs. Anti-Establishment";
            default:
                return "None";
        }
    }

    public String getCategory() {
        return category;
    }

    // Tests if two policies are the same or opposite in a category
    public boolean sameCategory(Policy p) {
        return p != null && category.equals(p.getCategory());
    }

    public boolean checkIdeology(String idea) {
        return ideologies.contains(idea);
    }

    public String getImageURL() {
        String target = super.getName().replaceAll("\\s", "").replace(".", "");
        target = target.toLowerCase();
        return "files/" + target + ".PNG";
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
