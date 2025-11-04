package com.cudzer.pebbleworks.items;

import com.cudzer.pebbleworks.PebbleworksMod;
import com.cudzer.pebbleworks.core.PW_Logger;
import com.cudzer.pebbleworks.entity.PebbleEntity;
import com.cudzer.pebbleworks.entity.PebbleItemEntity;
import com.cudzer.pebbleworks.entity.PebbleJobType;
import com.cudzer.pebbleworks.entity.PebbleVariant;
import com.cudzer.pebbleworks.registry.PW_Entities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PebbleItem extends Item {
    public PebbleItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();

        // Calculate spawn position
        BlockPos spawnPos = blockPos.relative(direction);

        // Spawn the entity
        spawnPebble(level, itemStack, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);

        // Consume item
        if (player != null && !player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        level.playSound(null, spawnPos, SoundEvents.PLAYER_SPLASH_HIGH_SPEED, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            // Create the projectile
            PebbleItemEntity projectile = new PebbleItemEntity(level, player);
            projectile.setItem(itemStack); // Pass the item (and its NBT) to the projectile
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        PW_Logger.info("Checking custom data for tool tip: " + customData);
        if (customData == null) {
            return; // No custom data found
        }
        CompoundTag mainTag = customData.copyTag();
        PW_Logger.info("Loading the following from nbt:" + mainTag);
        if (!mainTag.contains("PebbleData", Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag nbt = mainTag.getCompound("PebbleData");
        if (nbt != null) {
            // Add Name
            if (nbt.contains("CustomName")) {
                try {
                    Component customName = Component.Serializer.fromJson(nbt.getString("CustomName"), context.registries());
                    if(customName != null) {
                        tooltip.add(customName.copy().withStyle(ChatFormatting.ITALIC));
                    }
                } catch (Exception e) {
                    tooltip.add(Component.literal(nbt.getString("CustomName")).withStyle(ChatFormatting.ITALIC));
                }
            }

            // Add Health
            if (nbt.contains("Health")) {
                float health = nbt.getFloat("Health");
                tooltip.add(Component.translatable("tooltip.pebbleworks.health", String.format("%.1f", health))
                        .withStyle(ChatFormatting.GRAY));
            }

            // Add Job
            if (nbt.contains("Job")) {
                try {
                    PebbleJobType job = PebbleJobType.valueOf(nbt.getString("Job"));
                    tooltip.add(Component.translatable("tooltip.pebbleworks.job", job.name())
                            .withStyle(ChatFormatting.BLUE));
                } catch (Exception e) {
                    // ignore
                }
            }

            // Add Variant
            if (nbt.contains("Variant")) {
                int variant = nbt.getInt("Variant");
                var name = PebbleVariant.byId(variant);
                tooltip.add(Component.translatable("tooltip.pebbleworks.variant", name.getName())
                        .withStyle(ChatFormatting.GRAY));
            }

        } else {
            tooltip.add(Component.translatable("tooltip.pebbleworks.empty").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static void spawnPebble(Level level, ItemStack itemStack, double x, double y, double z) {
        if (level.isClientSide) return;

        PebbleEntity pebble = PW_Entities.PEBBLE.get().create(level);
        if (pebble == null) return;

        pebble.setPos(x, y, z);

        // This is the key part: load the data from the item into the new entity
        pebble.loadFromItem(itemStack);

        level.addFreshEntity(pebble);
    }
}
