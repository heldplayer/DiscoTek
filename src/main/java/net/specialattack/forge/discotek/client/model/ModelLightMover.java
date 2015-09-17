package net.specialattack.forge.discotek.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelLightMover extends ModelBase {

    public ModelRenderer base;
    public ModelRenderer lens;

    public ModelLightMover() {

        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        //Four sides. + Back

        //Left
        this.base.setTextureOffset(0, 0);
        this.base.addBox(-4.0F, -3.0F, -3.0F, 1, 6, 5, 0.0F);
        //Right
        this.base.setTextureOffset(12, 0);
        this.base.addBox(3.0F, -3.0F, -3.0F, 1, 6, 5, 0.0F);
        //Top
        this.base.setTextureOffset(24, 0);
        this.base.addBox(-3.0F, -4.0F, -3.0F, 6, 1, 5, 0.0F);
        //Bottom
        this.base.setTextureOffset(24, 6);
        this.base.addBox(-3.0F, 3.0F, -3.0F, 6, 1, 5, 0.0F);

        //Back
        this.base.setTextureOffset(46, 0);
        this.base.addBox(-3.0F, -3.0F, 2.0F, 6, 6, 1);
        this.base.setTextureOffset(46, 7);
        this.base.addBox(-2.0F, -2.0F, 3.0F, 4, 4, 1);

        this.lens = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.lens.setTextureOffset(0, 11);
        this.lens.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 1);
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
