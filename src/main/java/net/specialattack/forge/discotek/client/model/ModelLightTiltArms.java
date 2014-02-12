
package net.specialattack.forge.discotek.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLightTiltArms extends ModelBase {

    public ModelRenderer base;

    public ModelLightTiltArms() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        //Left vert
        this.base.setTextureOffset(0, 29);
        this.base.addBox(-6.0F, -5.0F, -2.0F, 2, 8, 4, 0.0F);
        //Right Vert
        this.base.setTextureOffset(12, 29);
        this.base.addBox(4.0F, -5.0F, -2.0F, 2, 8, 4, 0.0F);

        //Left vert
        this.base.setTextureOffset(24, 29);
        this.base.addBox(-6.0F, 3.0F, -1.0F, 2, 1, 2, 0.0F);
        //Right Vert
        this.base.setTextureOffset(32, 29);
        this.base.addBox(4.0F, 3.0F, -1.0F, 2, 1, 2, 0.0F);

        this.base.setTextureOffset(0, 41);
        this.base.addBox(-4.0F, -5.0F, -2.0F, 8, 2, 4, 0.0F);
    }

    public void renderAll() {

        this.base.renderWithRotation(0.0625F);

    }

    public void setRotations(float pitch, float yaw) {
        this.base.rotateAngleY = yaw;
    }

}
