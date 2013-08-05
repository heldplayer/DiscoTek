
package net.specialattack.discotek.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLightMover extends ModelBase {

    public ModelRenderer base;
    public ModelRenderer lens;

    @SuppressWarnings("unchecked")
    public ModelLightMover() {

        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        //Four sides. + Back

        //Left
        this.base.addBox(-4.0F, -3.0F, -3.0F, 1, 6, 5, 0.0F);
        //Right
        this.base.addBox(3.0F, -3.0F, -3.0F, 1, 6, 5, 0.0F);
        //Top
        this.base.addBox(-3.0F, -4.0F, -3.0F, 6, 1, 5, 0.0F);
        //Bottom
        this.base.addBox(-3.0F, 3.0F, -3.0F, 6, 1, 5, 0.0F);

        //Back
        this.base.addBox(-3.0F, -3.0F, 2.0F, 6, 6, 1);
        this.base.addBox(-2.0F, -2.0F, 3.0F, 4, 4, 1);

        this.lens = new ModelRenderer(this, 0, 0);
        this.lens.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 1);
        this.lens.cubeList.add(new ModelBoxNormalless(this.lens, 0, 0, -3.0F, -3.0F, -3.0F, 6, 6, 1, 0.0F));
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
