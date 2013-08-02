
package net.specialattack.modjam.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTruss extends ModelBase {

    public ModelRenderer base;

    public ModelTruss() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(256, 256);
        this.base.addBox(06.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);
    }

    public void renderAll() {
        this.base.render(0.0625F);
    }

}
