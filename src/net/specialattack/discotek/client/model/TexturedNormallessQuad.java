
package net.specialattack.discotek.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;

public class TexturedNormallessQuad extends TexturedQuad {

    public TexturedNormallessQuad(TexturedQuad quad) {
        super(quad.vertexPositions);
    }

    @Override
    public void draw(Tessellator tess, float scale) {
        tess.startDrawingQuads();

        for (int i = 0; i < 4; ++i) {
            PositionTextureVertex vertex = this.vertexPositions[i];
            tess.addVertexWithUV((double) ((float) vertex.vector3D.xCoord * scale), (double) ((float) vertex.vector3D.yCoord * scale), (double) ((float) vertex.vector3D.zCoord * scale), (double) vertex.texturePositionX, (double) vertex.texturePositionY);
        }

        tess.draw();
    }

}
