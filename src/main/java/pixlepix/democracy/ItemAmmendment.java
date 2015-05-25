package pixlepix.democracy;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import pixlepix.democracy.data.Ammendment;

import java.util.List;

/**
 * Created by localmacaccount on 5/25/15.
 */
public class ItemAmmendment extends Item {
    IIcon iIcon;

    public ItemAmmendment() {
        super();
        setMaxStackSize(1);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Ammendment.potentialAmendments.get(stack.getItemDamage()).name;
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        iIcon = reg.registerIcon("book");
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {

        return iIcon;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }


    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List l) {
        for (int i = 0; i < Ammendment.potentialAmendments.size(); i++) {
            l.add(new ItemStack(this, 1, i));
        }
    }
}
