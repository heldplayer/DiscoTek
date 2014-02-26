
package net.specialattack.forge.discotek.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHologramPad extends ModelBase {

    public ModelRenderer base;

    public ModelHologramPad() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);

        this.base.setTextureOffset(0, 0);
        this.base.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
    }

    public void renderAll() {
        this.base.render(0.0625F);
    }

}
