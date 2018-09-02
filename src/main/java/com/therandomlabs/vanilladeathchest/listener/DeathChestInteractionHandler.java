package com.therandomlabs.vanilladeathchest.listener;

import com.therandomlabs.vanilladeathchest.api.deathchest.DeathChest;
import com.therandomlabs.vanilladeathchest.api.deathchest.DeathChestManager;
import com.therandomlabs.vanilladeathchest.api.listener.BlockHarvestListener;
import com.therandomlabs.vanilladeathchest.api.listener.RightClickBlockListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DeathChestInteractionHandler implements BlockHarvestListener, RightClickBlockListener {
	private static BlockPos harvesting;

	@Override
	public EnumActionResult onBlockHarvest(World world, EntityPlayerMP player, BlockPos pos) {
		if(harvesting == pos) {
			return EnumActionResult.SUCCESS;
		}

		final EnumActionResult result = onBlockInteract(world, player, pos);

		if(result == EnumActionResult.SUCCESS) {
			final DeathChest chest = DeathChestManager.getDeathChest(world, pos);

			if(chest.isDoubleChest()) {
				harvesting = chest.getPos().equals(pos) ? pos.east() : pos.west();
				player.interactionManager.tryHarvestBlock(harvesting);
			}

			DeathChestManager.removeDeathChest(world, pos);
		}

		return result;
	}

	@Override
	public EnumActionResult onRightClickBlock(World world, EntityPlayerMP player, ItemStack stack,
			EnumHand hand, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return onBlockInteract(world, player, pos);
	}

	public static EnumActionResult onBlockInteract(World world, EntityPlayer player, BlockPos pos) {
		final DeathChest deathChest = DeathChestManager.getDeathChest(world, pos);

		if(deathChest == null) {
			return EnumActionResult.FAIL;
		}

		if(deathChest.canInteract(player)) {
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}
}
