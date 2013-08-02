
package net.specialattack.modjam.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLightParCan extends ModelBase {

    public ModelRenderer base;

    public ModelLightParCan() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(256, 256);
        //Four sides. + Back

        //Left
        this.base.addBox(-4.0F, -3.0F, -8.0F, 1, 6, 14, 0.0F);
        //Right
        this.base.addBox(3.0F, -3.0F, -8.0F, 1, 6, 14, 0.0F);
        //Top
        this.base.addBox(-3.0F, -4.0F, -8.0F, 6, 1, 14, 0.0F);
        //Bottom
        this.base.addBox(-3.0F, 3.0F, -8.0F, 6, 1, 14, 0.0F);

        //Back
        this.base.addBox(-3.0F, -3.0F, 6.0F, 6, 6, 1);
        this.base.addBox(-2.0F, -2.0F, 7.0F, 4, 4, 1);
    }

    public void renderAll() {
        this.base.renderWithRotation(0.0625F);
    }

    public void setRotations(float pitch, float yaw) {
        this.base.rotateAngleY = yaw;
        this.base.rotateAngleX = pitch;
    }

}
