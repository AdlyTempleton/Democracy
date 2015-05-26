package pixlepix.democracy.data;

import java.util.ArrayList;

/**
 * Created by localmacaccount on 5/23/15.
 */
public class Ammendment {
    public static ArrayList<Ammendment> potentialAmendments = new ArrayList<Ammendment>();
    public static int nextId = 0;
    public EnumStage stage;
    public String name;
    public int id;

    public Ammendment(String s, EnumStage stage) {
        this(s);
        this.stage = stage;
    }
    
    public Ammendment(String s) {

        name = s;
        potentialAmendments.add(this);
        id = nextId;
        nextId++;
    }

    public static void init(){
        new Ammendment("Cookies");
        new Ammendment("Ice cream");
        new Ammendment("Root beer");
        new Ammendment("Pizza");
        new Ammendment("Brownies");
        new Ammendment("Popcorn");
        new Ammendment("Cheetos");
        new Ammendment("Potato Chips");
        new Ammendment("Cream soda");

        new Ammendment("Doritos", EnumStage.COMMITTEE);
        new Ammendment("Granola Bars", EnumStage.COMMITTEE);
        new Ammendment("Chips and Salsa", EnumStage.HOUSE);
        new Ammendment("Waffles", EnumStage.HOUSE);
        new Ammendment("Whipped cream", EnumStage.SENATE);
        new Ammendment("Nutella", EnumStage.SENATE);
    }

    public static ArrayList<Ammendment> getPreferredBy(EnumStage stage) {
        ArrayList<Ammendment> result = new ArrayList<Ammendment>();
        for (Ammendment ammendment : potentialAmendments) {
            if (ammendment.stage == stage) {
                result.add(ammendment);
            }
        }
        return result;
    }

    public static ArrayList<Ammendment> getHatedBy(EnumStage stage) {
        ArrayList<Ammendment> result = new ArrayList<Ammendment>();
        for (Ammendment ammendment : potentialAmendments) {
            if (ammendment.stage != null && ammendment.stage != stage) {
                result.add(ammendment);
            }
        }
        return result;
    }
}
