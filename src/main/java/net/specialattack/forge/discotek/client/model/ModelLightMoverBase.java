
package net.specialattack.forge.discotek.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLightMoverBase extends ModelBase {

    public ModelRenderer base;

    public ModelLightMoverBase() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        //Base
        this.base.setTextureOffset(0, 12);
        this.base.addBox(-7.0F, -8.0F, -7.0F, 14, 3, 14, 0.0F);
    }

    public void renderAll() {

        this.base.renderWithRotation(0.0625F);
    }

    public void setRotations(float pitch, float yaw) {
        this.base.rotateAngleY = yaw;
    }

}
