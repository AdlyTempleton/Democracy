package pixlepix.democracy.data;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by localmacaccount on 5/23/15.
 */
public class Ammendment {
    public static ArrayList<Ammendment> potentialAmendments = new ArrayList<Ammendment>();
    public static int nextId = 0;
    public String name;
    public ItemStack displayStack;
    public int id;

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
    }
}
