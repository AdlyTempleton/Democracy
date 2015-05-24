package pixlepix.democracy.data;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

/**
 * Created by localmacaccount on 5/23/15.
 */
public class Ammendment {
    public static ArrayList<Ammendment> potentialAmendments = new ArrayList<Ammendment>();
    
    public Ammendment(String s, ItemStack displayStack){

        name = s;
        this.displayStack = displayStack;
        potentialAmendments.add(this);
        id = nextId;
        nextId++;
    }
    
    public String name;
    public ItemStack displayStack;
    public static int nextId = 0;
    public int id;
    public static void init(){
        new Ammendment("Cookies", new ItemStack(Items.cookie));
        new Ammendment("Spooky Skeletons", new ItemStack(Items.bone));
    }
}
