package pixlepix.democracy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import pixlepix.democracy.data.Ammendment;
import pixlepix.democracy.data.BillData;
import pixlepix.democracy.data.EnumStage;
import pixlepix.democracy.entity.EntityCongressman;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by localmacaccount on 5/24/15.
 * Borrowed from Auracascade 
 */
public class OverlayRender {
    //Borrowed from VazkiiRenderHelper in Botania
    public static void renderTooltip(int x, int y, List<String> tooltipData, int color, int color2) {
        boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        if (lighting)
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        if (!tooltipData.isEmpty()) {
            int var5 = 0;
            int var6;
            int var7;
            FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
            for (var6 = 0; var6 < tooltipData.size(); ++var6) {
                var7 = fontRendererObj.getStringWidth(tooltipData.get(var6));
                if (var7 > var5)
                    var5 = var7;
            }
            var6 = x + 12;
            var7 = y - 12;
            int var9 = 8;
            if (tooltipData.size() > 1)
                var9 += 2 + (tooltipData.size() - 1) * 10;
            float z = 300F;
            drawGradientRect(var6 - 3, var7 - 4, z, var6 + var5 + 3, var7 - 3, color2, color2);
            drawGradientRect(var6 - 3, var7 + var9 + 3, z, var6 + var5 + 3, var7 + var9 + 4, color2, color2);
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 + var9 + 3, color2, color2);
            drawGradientRect(var6 - 4, var7 - 3, z, var6 - 3, var7 + var9 + 3, color2, color2);
            drawGradientRect(var6 + var5 + 3, var7 - 3, z, var6 + var5 + 4, var7 + var9 + 3, color2, color2);
            int var12 = (color & 0xFFFFFF) >> 1 | color & -16777216;
            drawGradientRect(var6 - 3, var7 - 3 + 1, z, var6 - 3 + 1, var7 + var9 + 3 - 1, color, var12);
            drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, z, var6 + var5 + 3, var7 + var9 + 3 - 1, color, var12);
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 - 3 + 1, color, color);
            drawGradientRect(var6 - 3, var7 + var9 + 2, z, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            for (int var13 = 0; var13 < tooltipData.size(); ++var13) {
                String var14 = tooltipData.get(var13);
                fontRendererObj.drawStringWithShadow(var14, var6, var7, -1);
                if (var13 == 0)
                    var7 += 2;
                var7 += 10;
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        if (!lighting)
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
    
    public static void drawGradientRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
        float var7 = (par5 >> 24 & 255) / 255F;
        float var8 = (par5 >> 16 & 255) / 255F;
        float var9 = (par5 >> 8 & 255) / 255F;
        float var10 = (par5 & 255) / 255F;
        float var11 = (par6 >> 24 & 255) / 255F;
        float var12 = (par6 >> 16 & 255) / 255F;
        float var13 = (par6 >> 8 & 255) / 255F;
        float var14 = (par6 & 255) / 255F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.getInstance();
        var15.getWorldRenderer().startDrawingQuads();
        var15.getWorldRenderer().setColorRGBA_F(var8, var9, var10, var7);
        var15.getWorldRenderer().addVertex(par3, par2, z);
        var15.getWorldRenderer().addVertex(par1, par2, z);
        var15.getWorldRenderer().setColorRGBA_F(var12, var13, var14, var11);
        var15.getWorldRenderer().addVertex(par1, par4, z);
        var15.getWorldRenderer().addVertex(par3, par4, z);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @SubscribeEvent
    public void onScreenRenderEvent(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            World world = Democracy.proxy.getWorld();
            EntityPlayer player = Democracy.proxy.getPlayer();


            //Global overlay
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);


            int topX = (resolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth("hi"));

            int topY = (resolution.getScaledHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);

            int centerX = (resolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth("hi")) / 2;

            int centerY = (resolution.getScaledHeight() - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) / 2;


            ArrayList<String> global = new ArrayList<String>();

            global.add("Bill: Free food in school");
            global.add("Stage: " + BillData.bill.stage);
            global.add("Ammendments: ");
            for (Ammendment ammendment : BillData.bill.amendments) {
                global.add(ammendment.name);
            }

            renderTooltip((int) (resolution.getScaledWidth() * .5) - (Minecraft.getMinecraft().fontRendererObj.getStringWidth(global.get(0)) / 2), Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * (3 + BillData.bill.amendments.size()), global, 0x5577ff, 0x550000ff);

            Vec3 vec3 = player.getPositionEyes(1.0F);
            Vec3 vec3a = player.getLook(1.0F);
            Vec3 vec3b = vec3.addVector(vec3a.xCoord * 3, vec3a.yCoord * 3, vec3a.zCoord * 3);
            MovingObjectPosition movingobjectposition = Minecraft.getMinecraft().objectMouseOver;
            if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {

                Entity entity = movingobjectposition.entityHit;
                if (entity instanceof EntityCongressman) {
                    EntityCongressman congressman = (EntityCongressman) entity;
                    List<String> result = new ArrayList<String>();


                    result.add("Profession: " + congressman.type.memberName);


                    if (congressman.isSpeaker) {
                        result.add("Leader - Start vote or add ammendments");
                    }
                    if (!congressman.isSpeaker || congressman.type == EnumStage.PRESIDENT) {
                        for (Ammendment ammendment : congressman.desiredAmendments) {
                            result.add("Wants: " + ammendment.name);
                        }

                        for (Ammendment ammendment : congressman.hatedAmendments) {
                            result.add("Doesn't want: " + ammendment.name);
                        }
                        if (BillData.bill.porkBarrelCongressmen.contains(congressman)) {
                            result.add("Pork barrel bonus: 20");
                        }
                        result.add("Pork barrel penalty: -" + 2 * BillData.bill.porkBarrelCongressmen.size());
                        result.add("Peer Pressure: " + congressman.getPeerPressure());
                        result.add("Total opinion: " + (congressman.getOpinion() + congressman.getPeerPressure()));
                    }
                    renderTooltip(centerX, centerY, result, 0x5577ff, 0x550000ff);
                }

            }
        }
    }
}
