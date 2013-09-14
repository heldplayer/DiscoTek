
package net.specialattack.discotek.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.specialattack.discotek.Assets;
import net.specialattack.discotek.tileentity.TileEntityLight;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOrienter extends Item {

    @SideOnly(Side.CLIENT)
    private Icon[] icons;
    @SideOnly(Side.CLIENT)
    private Icon[] iconsCont;

    public ItemOrienter(int itemId) {
        super(itemId);
        this.setFull3D();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        return super.getItemDisplayName(stack) + stack.getItemDamage();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            stack.setItemDamage((stack.getItemDamage() + 1) % 3);
            return stack;
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TileEntityLight) {
            if (world.isRemote) {
                return true;
            }

            TileEntityLight light = (TileEntityLight) tile;

            switch (stack.getItemDamage()) {
            case 0:
                float yaw = light.getYaw(1.0F);
                if (player.isSneaking()) {
                    yaw += 0.01F;
                }
                else {
                    yaw -= 0.01F;
                }
                light.setYaw(yaw);
            break;
            case 1:
                float pitch = light.getPitch(1.0F);
                if (player.isSneaking()) {
                    pitch -= 0.01F;
                }
                else {
                    pitch += 0.01F;
                }
                light.setPitch(pitch);
            break;
            case 2:
                float focus = light.getFocus(1.0F);
                if (player.isSneaking()) {
                    focus -= 0.2F;
                }
                else {
                    focus += 0.2F;
                }
                light.setFocus(focus);
            break;
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.icons = new Icon[3];
        this.iconsCont = new Icon[3];
        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon(Assets.DOMAIN + "orienter" + i);
            this.iconsCont[i] = register.registerIcon(Assets.DOMAIN + "orienter-cont" + i);
        }
        // Change yaw
        // Change pitch
        // Change focus
    }

    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (player.isSneaking()) {
            return this.iconsCont[stack.getItemDamage() % this.icons.length];
        }
        else {
            return this.icons[stack.getItemDamage() % this.icons.length];
        }
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        return this.icons[stack.getItemDamage() % this.icons.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta) {
        if (Minecraft.getMinecraft().thePlayer.isSneaking()) {
            return this.iconsCont[meta % this.icons.length];
        }
        else {
            return this.icons[meta % this.icons.length];
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int itemId, CreativeTabs tab, List list) {
        for (int i = 0; i < this.icons.length; i++) {
            list.add(new ItemStack(itemId, 1, i));
        }
    }

}
