package net.specialattack.forge.discotek.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelLightParCan extends ModelBase {

    public ModelRenderer base;
    public ModelRenderer lens;

    public ModelLightParCan() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        //Four sides. + Back

        //Left
        this.base.setTextureOffset(0, 0);
        this.base.addBox(-4.0F, -3.0F, -8.0F, 1, 6, 14, 0.0F);
        //Right
        this.base.setTextureOffset(30, 0);
        this.base.addBox(3.0F, -3.0F, -8.0F, 1, 6, 14, 0.0F);
        //Top
        this.base.setTextureOffset(0, 20);
        this.base.addBox(-3.0F, -4.0F, -8.0F, 6, 1, 14, 0.0F);
        //Bottom
        this.base.setTextureOffset(0, 35);
        this.base.addBox(-3.0F, 3.0F, -8.0F, 6, 1, 14, 0.0F);

        //Back
        this.base.setTextureOffset(0, 0);
        this.base.addBox(-3.0F, -3.0F, 6.0F, 6, 6, 1);
        this.base.setTextureOffset(0, 7);
        this.base.addBox(-2.0F, -2.0F, 7.0F, 4, 4, 1);

        this.lens = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.lens.setTextureOffset(16, 0);
        this.lens.addBox(-3.0F, -3.0F, -8.0F, 6, 6, 1);
    }

    public void render() {
        this.base.renderWithRotation(0.0625F);
    }

    public void renderLens() {
        this.lens.renderWithRotation(0.0625F);
    }

    public void setRotations(float pitch, float yaw) {
        this.base.rotateAngleY = yaw;
        this.base.rotateAngleX = pitch;
        this.lens.rotateAngleY = yaw;
        this.lens.rotateAngleX = pitch;
    }
}
