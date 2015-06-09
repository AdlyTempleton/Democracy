package pixlepix.democracy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pixlepix.democracy.data.Ammendment;

import java.util.List;

/**
 * Created by localmacaccount on 5/25/15.
 */
public class ItemAmmendment extends Item {

    public ItemAmmendment() {
        super();
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Ammendment.potentialAmendments.get(stack.getItemDamage()).name;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        super.addInformation(stack, player, list, b);
        Ammendment ammendment = Ammendment.potentialAmendments.get(stack.getItemDamage());
        if (ammendment.stage != null) {
            list.add("Popular in: " + ammendment.stage.name);
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List l) {
        for (int i = 0; i < Ammendment.potentialAmendments.size(); i++) {
            l.add(new ItemStack(this, 1, i));
        }
    }
}
