
package net.specialattack.discotek.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLightYoke extends ModelBase {

    public ModelRenderer base;

    public ModelLightYoke() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        //Left vert
        this.base.setTextureOffset(30, 0);
        this.base.addBox(-5.0F, 0.0F, -1.0F, 1, 8, 2, 0.0F);
        //Top
        this.base.setTextureOffset(16, 10);
        this.base.addBox(-4.0F, 7.0F, -1.0F, 8, 1, 2, 0.0F);
        //Right Vert
        this.base.setTextureOffset(36, 0);
        this.base.addBox(4.0F, 0.0F, -1.0F, 1, 8, 2, 0.0F);
    }

    public void renderAll() {
        this.base.renderWithRotation(0.0625F);
    }

    public void setRotations(float pitch, float yaw) {
        this.base.rotateAngleY = yaw;
    }

}
