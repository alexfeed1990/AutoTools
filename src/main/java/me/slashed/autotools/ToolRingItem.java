package me.slashed.autotools;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolRingItem extends Item {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, autotools.MOD_ID);

    public static final RegistryObject<Item> ToolRing = ITEMS.register("tool_ring",
            () -> new ToolRingItem(new Item.Properties().group(ItemGroup.MISC).defaultMaxDamage(500)));

    private BlockPos pos;
    private int timer = 0;
    private int oldSlot = -1;

    public ToolRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        Minecraft mc = Minecraft.getInstance();

        if(!(entity instanceof PlayerEntity)) return;
        PlayerEntity player = ((PlayerEntity) entity);
        //if(mc.player == null) return;

        if (world.isRemote && player.getHeldItem(Hand.MAIN_HAND) == stack) {
            player.sendStatusMessage(new TranslationTextComponent("item.autotools.tool_ring.help_message"), true);
        }
        if (!(player.getHeldItem(Hand.OFF_HAND) == stack)) return;

        if(mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null) {
            if(mc.objectMouseOver instanceof EntityRayTraceResult) return;
            setSlot(((BlockRayTraceResult) mc.objectMouseOver).getPos(), stack, player);
        }

        if(oldSlot == -1) return;
        if(timer <= 0) {
            player.inventory.currentItem = oldSlot;
            oldSlot = -1;
            return;
        }

        if(!mc.gameSettings.keyBindAttack.isKeyDown() || player.isCreative()) timer--;
    }

    public void setSlot(BlockPos pos, ItemStack toolRing, PlayerEntity player) {
        Minecraft mc = Minecraft.getInstance();
        if (player.isCreative()) return;

        BlockState state = mc.world.getBlockState(pos);
        int bestSlot = -1;

        for (int slot = 0; slot < 9; slot++) {
            if (slot == player.inventory.currentItem)
                continue;

            ItemStack stack = player.inventory.getStackInSlot(slot);

            boolean speed = getBreakSpeed(stack, state);
            if (speed) {
                bestSlot = slot;
                toolRing.damageItem(1, player, (ent) -> {
                    player.sendBreakAnimation(EquipmentSlotType.OFFHAND);
                });
            }
        }

        if (bestSlot == -1) return;
        if (oldSlot == -1) oldSlot = player.inventory.currentItem;

        player.inventory.currentItem = bestSlot;
        this.pos = pos;
        timer = 4;
    }

    private boolean getBreakSpeed(ItemStack stack, BlockState state)
    {
        return stack.getToolTypes().stream().anyMatch(state::isToolEffective);
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == Items.ENDER_PEARL.getItem();
    }
}
