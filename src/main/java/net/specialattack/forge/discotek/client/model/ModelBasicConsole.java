package net.specialattack.forge.discotek.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelBasicConsole extends ModelBase {

    public ModelRenderer base;

    public ModelBasicConsole() {
        this.base = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
        this.base.addBox(-6.0F, -8.0F, -4.0F, 12, 5, 8, 0.0F);
    }

    public void renderAll() {
        this.base.render(0.0625F);
    }
}
