package pixlepix.democracy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import pixlepix.democracy.entity.EntityCongressman;
import pixlepix.democracy.entity.RenderCongressman;

/**
 * Created by localmacaccount on 5/23/15.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(new OverlayRender());
        RenderingRegistry.registerEntityRenderingHandler(EntityCongressman.class, new RenderCongressman());
    }

    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
