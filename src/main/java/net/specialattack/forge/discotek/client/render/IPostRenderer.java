package net.specialattack.forge.discotek.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.AxisAlignedBB;

public interface IPostRenderer {

    @SideOnly(Side.CLIENT)
    void renderPost(double posX, double posY, double posZ, float partialTicks);

    boolean shouldRemoveRenderer();

    AxisAlignedBB getRenderingAABB(IPostRenderer light, float partialTicks);

    int getPosX();

    int getPosY();

    int getPosZ();

    boolean rendersLight();
}
