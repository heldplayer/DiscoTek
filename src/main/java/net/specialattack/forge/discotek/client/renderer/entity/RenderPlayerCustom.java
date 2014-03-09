
package net.specialattack.forge.discotek.client.renderer.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlayerCustom extends RendererLivingEntity {

    private ModelBiped modelBipedMain;
    private ModelBiped modelArmorChestplate;
    private ModelBiped modelArmor;

    public RenderPlayerCustom() {
        super(new ModelBiped(0.0F), 0.5F);
        this.modelBipedMain = (ModelBiped) this.mainModel;
        this.modelArmorChestplate = new ModelBiped(1.0F);
        this.modelArmor = new ModelBiped(0.5F);
    }

    protected int shouldRenderPass(AbstractClientPlayer player, int pass, float partialTicks) {
        return -1;
    }

    protected void func_82408_c(AbstractClientPlayer player, int par2, float partialTicks) {
        ItemStack itemstack = player.inventory.armorItemInSlot(3 - par2);

        if (itemstack != null) {
            Item item = itemstack.getItem();

            if (item instanceof ItemArmor) {
                this.bindTexture(RenderBiped.getArmorResource(player, itemstack, par2, "overlay"));
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }
        }
    }

    public void doRender(AbstractClientPlayer player, double posX, double posY, double posZ, float pitch, float partialTicks) {
        double d3 = posY - player.yOffset;

        super.doRender(player, posX, d3, posZ, pitch, partialTicks);
        this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
    }

    protected ResourceLocation getEntityTexture(AbstractClientPlayer player) {
        return player.getLocationSkin();
    }

    protected void renderEquippedItems(AbstractClientPlayer player, float partialTicks) {
        super.renderEquippedItems(player, partialTicks);

        if (player.getCommandSenderName().equals("deadmau5") && player.getTextureSkin().isTextureUploaded()) {
            this.bindTexture(player.getLocationSkin());

            for (int i = 0; i < 2; ++i) {
                float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
                float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
                GL11.glPushMatrix();
                GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (i * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
                float f = 1.3333334F;
                GL11.glScalef(f, f, f);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean hasCape = player.getTextureCape().isTextureUploaded();

        if (hasCape && !player.isInvisible() && !player.getHideCape()) {
            this.bindTexture(player.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
            double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
            double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
            float f5 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
            double d1 = MathHelper.sin(f5 * (float) Math.PI / 180.0F);
            double d2 = (-MathHelper.cos(f5 * (float) Math.PI / 180.0F));
            float f6 = (float) d4 * 10.0F;

            if (f6 < -6.0F) {
                f6 = -6.0F;
            }

            if (f6 > 32.0F) {
                f6 = 32.0F;
            }

            float f7 = (float) (d3 * d1 + d0 * d2) * 100.0F;
            float f8 = (float) (d3 * d2 - d0 * d1) * 100.0F;

            if (f7 < 0.0F) {
                f7 = 0.0F;
            }

            float f9 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
            f6 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f9;

            GL11.glRotatef(6.0F + f7 / 2.0F + f6, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f8 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f8 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
    }

    protected void preRenderCallback(AbstractClientPlayer player, float partialTicks) {
        float f = 0.9375F;
        GL11.glScalef(f, f, f);
    }

    protected void func_96449_a(AbstractClientPlayer player, double posX, double posY, double posZ, String name, float par9, double distance) {
        //super.func_96449_a(player, posX, posY, posZ, name, par9, distance);
    }

    public void renderFirstPersonArm(EntityPlayer player) {
        float f = 1.0F;
        GL11.glColor3f(f, f, f);
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
        this.modelBipedMain.bipedRightArm.render(0.0625F);
    }

    protected void renderLivingAt(AbstractClientPlayer player, double posX, double posY, double posZ) {
        super.renderLivingAt(player, posX, posY, posZ);
    }

    protected void rotateCorpse(AbstractClientPlayer player, float par2, float par3, float par4) {
        super.rotateCorpse(player, par2, par3, par4);
    }

    @Override
    protected void func_96449_a(EntityLivingBase player, double posX, double posY, double posZ, String name, float par9, double distance) {
        this.func_96449_a((AbstractClientPlayer) player, posX, posY, posZ, name, par9, distance);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase player, float partialTicks) {
        this.preRenderCallback((AbstractClientPlayer) player, partialTicks);
    }

    @Override
    protected void func_82408_c(EntityLivingBase player, int par2, float partialTicks) {
        this.func_82408_c((AbstractClientPlayer) player, par2, partialTicks);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase player, int pass, float partialTicks) {
        return this.shouldRenderPass((AbstractClientPlayer) player, pass, partialTicks);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase player, float partialTicks) {
        this.renderEquippedItems((AbstractClientPlayer) player, partialTicks);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase player, float par2, float par3, float partialTicks) {
        this.rotateCorpse((AbstractClientPlayer) player, par2, par3, partialTicks);
    }

    @Override
    protected void renderLivingAt(EntityLivingBase player, double posX, double posY, double posZ) {
        this.renderLivingAt((AbstractClientPlayer) player, posX, posY, posZ);
    }

    @Override
    public void doRender(EntityLivingBase player, double posX, double posY, double posZ, float par8, float partialTicks) {
        this.doRender((AbstractClientPlayer) player, posX, posY, posZ, par8, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity player) {
        return this.getEntityTexture((AbstractClientPlayer) player);
    }

    @Override
    public void doRender(Entity player, double posX, double posY, double posZ, float par8, float partialTicks) {
        this.doRender((AbstractClientPlayer) player, posX, posY, posZ, par8, partialTicks);
    }

}
