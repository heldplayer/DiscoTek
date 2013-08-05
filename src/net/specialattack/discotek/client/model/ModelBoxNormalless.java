
package net.specialattack.discotek.client.model;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TexturedQuad;

public class ModelBoxNormalless extends ModelBox {

    public ModelBoxNormalless(ModelRenderer par1ModelRenderer, int par2, int par3, float par4, float par5, float par6, int par7, int par8, int par9, float par10) {
        super(par1ModelRenderer, par2, par3, par4, par5, par6, par7, par8, par9, par10);

        TexturedQuad[] quads = this.getMyQuads();

        if (quads != null) {
            for (int i = 0; i < quads.length; i++) {
                quads[i] = new TexturedNormallessQuad(quads[i]);
            }
        }
    }

    private TexturedQuad[] getMyQuads() {
        try {
            return (TexturedQuad[]) quads.get(this);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Field quads;

    static {
        Class<ModelBoxNormalless> clazz = ModelBoxNormalless.class;
        try {
            quads = clazz.getSuperclass().getDeclaredField("quadList");
            quads.setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
