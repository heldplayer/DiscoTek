package net.specialattack.forge.discotek.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelDimmer extends ModelBase {

    public ModelRenderer base;

    public ModelDimmer() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);

        this.base.setTextureOffset(0, 0);
        this.base.addBox(-8.0F, -8.0F, -4.0F, 16, 5, 8);

        this.base.setTextureOffset(0, 13);
        this.base.addBox(-4.0F, -8.0F, -8.0F, 8, 5, 4, 0.0F);

        this.base.setTextureOffset(24, 13);
        this.base.addBox(-4.0F, -8.0F, 4.0F, 8, 5, 4, 0.0F);
    }

    public void renderAll() {
        this.base.render(0.0625F);
    }

}
