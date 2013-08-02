
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
        this.base.addBox(4.0f, 5.0f, 0.0f, 1, 6, 14, 0.0f);
        //Right
        this.base.addBox(11.0f, 5.0f, 0.0f, 1, 6, 14, 0.0f);
        //Top
        this.base.addBox(5.0f, 4.0f, 0.0f, 6, 1, 14, 0.0f);
        //Bottom
        this.base.addBox(5.0f, 11.0f, 0.0f, 6, 1, 14, 0.0f);
        
        //Back
        this.base.addBox(5.0f, 5.0f, 14.0f, 6, 6, 1);
        this.base.addBox(6.0f, 6.0f, 15.0f, 4, 4, 1);
        
    }

    public void renderAll() {
        this.base.render(0.0625F);
    }

}
