
package net.specialattack.discotek.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDMXRedstone extends ModelBase {

    public ModelRenderer base;

    public ModelDMXRedstone() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.base.addBox(-4.0F, -8.0F, -4.0F, 8, 5, 8, 0.0F);

        this.base.addBox(-8.0F, -8.0F, -4.0F, 4, 5, 8, 0.0F);

        this.base.addBox(4.0F, -8.0F, -4.0F, 4, 5, 8, 0.0F);

        this.base.addBox(-4.0F, -8.0F, -8.0F, 8, 5, 4, 0.0F);

        this.base.addBox(-4.0F, -8.0F, 4.0F, 8, 5, 4, 0.0F);
    }

    public void renderAll() {
        this.base.render(0.0625F);
    }

}
