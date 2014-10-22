package net.specialattack.forge.discotek.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.specialattack.forge.discotek.Assets;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class ItemOrienter extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    @SideOnly(Side.CLIENT)
    private IIcon[] iconsCont;

    public ItemOrienter() {
        super();
        this.setFull3D();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        if (Minecraft.getMinecraft().thePlayer.isSneaking()) {
            return this.iconsCont[meta % this.icons.length];
        } else {
            return this.icons[meta % this.icons.length];
        }
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileEntityLight) {
            if (world.isRemote) {
                return true;
            }

            TileEntityLight light = (TileEntityLight) tile;

            switch (stack.getItemDamage()) {
                case 0:
                    float rotation = light.getFloat("rotation", 1.0F);
                    if (player.isSneaking()) {
                        rotation += 0.1F;
                    } else {
                        rotation -= 0.1F;
                    }
                    light.setValue("rotation", rotation);
                    break;
                case 1:
                    float pitch = light.getFloat("pitch", 1.0F);
                    if (player.isSneaking()) {
                        pitch -= 0.01F;
                    } else {
                        pitch += 0.01F;
                    }
                    light.setValue("pitch", pitch);
                    break;
                case 2:
                    float focus = light.getFloat("focus", 1.0F);
                    if (player.isSneaking()) {
                        focus -= 0.2F;
                    } else {
                        focus += 0.2F;
                    }
                    light.setValue("focus", focus);
                    break;
            }
        }

        return false;
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
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return super.getUnlocalizedNameInefficiently(stack) + stack.getItemDamage();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < this.icons.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.icons = new IIcon[3];
        this.iconsCont = new IIcon[3];
        for (int i = 0; i < this.icons.length; i++) {
            this.icons[i] = register.registerIcon(Assets.DOMAIN + "orienter" + i);
            this.iconsCont[i] = register.registerIcon(Assets.DOMAIN + "orienter-cont" + i);
        }
        // Change yaw
        // Change pitch
        // Change focus
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (player.isSneaking()) {
            return this.iconsCont[stack.getItemDamage() % this.icons.length];
        } else {
            return this.icons[stack.getItemDamage() % this.icons.length];
        }
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.icons[stack.getItemDamage() % this.icons.length];
    }

}
