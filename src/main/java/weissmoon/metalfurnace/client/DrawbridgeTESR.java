package weissmoon.metalfurnace.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;
import weissmoon.metalfurnace.ModData;
import weissmoon.metalfurnace.block.BlockMetalFurnace;
import weissmoon.metalfurnace.block.TileMetalFurnace;

/**
 * Created by Weissmoon on 4/6/19.
 */
public class DrawbridgeTESR extends TileEntitySpecialRenderer<TileMetalFurnace> {

    @Override
    public void render(TileMetalFurnace te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
//        ITextComponent itextcomponent = te.getDisplayName();

//        if (itextcomponent != null && this.rendererDispatcher.cameraHitResult != null && te.getPos().equals(this.rendererDispatcher.cameraHitResult.getBlockPos()))
//        {
//            this.setLightmapDisabled(true);
//            this.drawNameplate(te, itextcomponent.getFormattedText(), x, y, z, 12);
//            this.setLightmapDisabled(false);
//        }
        this.renderDrawbrigde(x, y, z, te);
    }

    public void renderDrawbrigde(double x, double y, double z, TileMetalFurnace tileDrawbrigde){
        GL11.glPushMatrix();
        try {
            GL11.glTranslated(x + .5, y + .5, z + .5);
            EnumFacing facing = Minecraft.getMinecraft().world.getBlockState(tileDrawbrigde.getPos()).getValue(BlockMetalFurnace.FACING);
            switch(facing) {
                case UP:
                    GL11.glRotatef(90 , 1, 0, 0);
                    break;
                case DOWN:
                    GL11.glRotatef(270, 1, 0, 0);
                    break;
                case NORTH:
                    GL11.glRotatef(0  , 0, 1, 0);
                    break;
                case WEST:
                    GL11.glRotatef(90 , 0, 1, 0);
                    break;
                case SOUTH:
                    GL11.glRotatef(180, 0, 1, 0);
                    break;
                case EAST:
                    GL11.glRotatef(270, 0, 1, 0);
                    break;
            }
            //Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModData.metalFurnace), ItemCameraTransforms.TransformType.NONE);
            //ModelLoader.getInventoryVariant();
        }finally {
            GL11.glPopMatrix();
        }
    }
}
