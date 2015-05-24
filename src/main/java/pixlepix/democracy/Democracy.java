package pixlepix.democracy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraftforge.common.MinecraftForge;
import pixlepix.democracy.data.Ammendment;
import pixlepix.democracy.data.BillData;
import pixlepix.democracy.entity.EntityCongressman;

/**
 * Created by localmacaccount on 5/23/15.
 */
@Mod(modid=Democracy.MODID, name=Democracy.MODNAME, version=Democracy.MODVER)public class Democracy {
    public static final String MODID = "Democracy";
    public static final String MODNAME = "Democracy";
    public static final String MODVER = "1.0";

    @SidedProxy(clientSide="pixlepix.democracy.ClientProxy", serverSide="pixlepix.democracy.CommonProxy")
    public static CommonProxy proxy;
    
    @Mod.Instance(value = Democracy.MODID)
    public static Democracy instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        proxy.init();
        BillData.init();
        Ammendment.init();

        EntityRegistry.registerModEntity(EntityCongressman.class, "Congressman", 0 , this, 80, 1, true);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}