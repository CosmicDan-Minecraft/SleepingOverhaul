package com.cosmicdan.sleepingoverhaul.common.eventhandlers;

import com.cosmicdan.cosmiclib.gamedata.Timekeeper;
import com.cosmicdan.sleepingoverhaul.common.ModConfig;
import com.cosmicdan.sleepingoverhaul.common.interop.ModAccessors;
import com.cosmicdan.sleepingoverhaul.common.reflection.EntityMirror;
import com.cosmicdan.sleepingoverhaul.common.reflection.EntityPlayerMirror;
import com.google.common.base.Predicate;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@SuppressWarnings("ClassWithTooManyDependencies")
@Log4j2(topic = "SleepingTweaks/PlayerSleepHandler")
public class PlayerSleepHandler {
	private static final Timekeeper TIMEKEEPER = Timekeeper.getInstance();

	/**
	 * Hook for allowing sleeping during the day, if enabled in mod config
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSleepingTimeCheck(final SleepingTimeCheckEvent event) {
		if (ModConfig.RESTRICTIONS.allowSleepDuringDay)
			event.setResult(Event.Result.ALLOW);
	}

	/**
	 * Main hook for handling sleep. Most of this is replicated and adapted from EntityPlayer#trySleep, since Forge doesn't
	 * provide more specific events. As long as we return a result, the vanilla sleep handling will not be performed - despite Forge
	 * docs implying otherwise (hopefully it's not deprecated).
	 */
	@SuppressWarnings({"MethodWithMoreThanThreeNegations", "MethodWithMultipleReturnPoints", "OverlyComplexMethod", "OverlyLongMethod"})
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerSleepInBed(final PlayerSleepInBedEvent event) {
		final BlockPos bedPos = event.getPos();
		final World world = event.getEntityPlayer().world;
		final IBlockState bedBlockState = world.getBlockState(bedPos);

		if (!(bedBlockState.getBlock() instanceof BlockHorizontal))
			return;

		final EnumFacing enumfacing = world.getBlockState(bedPos).getValue(BlockHorizontal.FACING);
		if (!world.isRemote) {
			if (event.getEntityPlayer().isPlayerSleeping() || !event.getEntityPlayer().isEntityAlive()) {
				event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
				return;
			}

			if (!canSleepHere(world.provider.isSurfaceWorld())) {
				event.setResult(EntityPlayer.SleepResult.NOT_POSSIBLE_HERE);
				return;
			}

			if (!canSleepNow(world.isDaytime())) {
				event.setResult(EntityPlayer.SleepResult.NOT_POSSIBLE_NOW);
				return;
			}

			final boolean isBedInRange = EntityPlayerMirror.METHOD___BED_IN_RANGE.call(event.getEntityPlayer(), bedPos, enumfacing);
			if (!isBedInRange) {
				event.setResult(EntityPlayer.SleepResult.TOO_FAR_AWAY);
				return;
			}

			if ((0.0 < ModConfig.RESTRICTIONS.safetyCheckXZ) && (0.0 < ModConfig.RESTRICTIONS.safetyCheckY)) {
				final val mobSearchXZ = ModConfig.RESTRICTIONS.safetyCheckXZ;
				final val mobSearchY = ModConfig.RESTRICTIONS.safetyCheckY;
				final val searchBox = new AxisAlignedBB(
						bedPos.getX() - mobSearchXZ,
						bedPos.getY() - mobSearchY,
						bedPos.getZ() - mobSearchXZ,
						bedPos.getX() + mobSearchXZ,
						bedPos.getY() + mobSearchY,
						bedPos.getZ() + mobSearchXZ
				);

				@SuppressWarnings("unchecked") final val sleepEnemyPredicate = (Predicate<EntityMob>) EntityPlayerMirror.CONSTRUCTOR_SLEEP_ENEMY_PREDICATE.construct(null, event.getEntityPlayer());
				final List<EntityMob> mobFoundList = world.getEntitiesWithinAABB(EntityMob.class, searchBox, sleepEnemyPredicate);

				if (!mobFoundList.isEmpty()) {
					event.setResult(EntityPlayer.SleepResult.NOT_SAFE);
					return;
				}
			}
		}

		if (event.getEntityPlayer().isRiding()) {
			event.getEntityPlayer().dismountRidingEntity();
		}

		EntityPlayerMirror.METHOD___SPAWN_SHOULDER_ENTITIES.call(event.getEntityPlayer());
		final val sleepingSize = 0.2F;
		EntityMirror.METHOD___SET_SIZE.call(event.getEntityPlayer(), sleepingSize, sleepingSize);

		IBlockState state = null;
		if (world.isBlockLoaded(bedPos))
			state = world.getBlockState(bedPos);
		final val defaultOffsetXZ = 0.5F;
		final val offsetY = 0.6875F;
		if ((null != state) && state.getBlock().isBed(state, world, bedPos, event.getEntityPlayer())) {
			final float offsetX = defaultOffsetXZ + (enumfacing.getFrontOffsetX() * 0.4F);
			final float offsetZ = defaultOffsetXZ + (enumfacing.getFrontOffsetZ() * 0.4F);
			EntityPlayerMirror.METHOD___SET_RENDER_OFFSET_FOR_SLEEP.call(event.getEntityPlayer(), enumfacing);
			event.getEntityPlayer().setPosition(
					bedPos.getX() + offsetX,
					bedPos.getY() + offsetY,
					bedPos.getZ() + offsetZ
			);
		} else {
			event.getEntityPlayer().setPosition(
					bedPos.getX() + defaultOffsetXZ,
					bedPos.getY() + offsetY,
					bedPos.getZ() + defaultOffsetXZ
			);
		}

		EntityPlayerMirror.FIELD___SLEEPING.set(event.getEntityPlayer(), true);
		EntityPlayerMirror.FIELD___SLEEP_TIMER.set(event.getEntityPlayer(), 0);

		event.getEntityPlayer().bedLocation = bedPos;
		event.getEntityPlayer().motionX = 0.0D;
		event.getEntityPlayer().motionY = 0.0D;
		event.getEntityPlayer().motionZ = 0.0D;

		if (!world.isRemote) {
			if (!ModAccessors.TICKRATE_CHANGER_LOADED) {
				// use vanilla sleeping
				world.updateAllPlayersSleepingFlag();
			} // else, SleepingVoteHandler handles the actual logic for time-lapse sleep
		}
		event.setResult(EntityPlayer.SleepResult.OK);
	}

	private static boolean canSleepHere(final boolean isOverworld) {
		return ModConfig.RESTRICTIONS.sleepAnywhere || isOverworld;
	}

	private static boolean canSleepNow(final boolean isDaytime) {
		boolean canSleepNow = true;
		if (!ModConfig.RESTRICTIONS.allowSleepDuringDay) {
			if (ModConfig.CUSTOM_NIGHT_DETECTION.customStartEnabled) {
				final int currentTime = TIMEKEEPER.getDayTicksElapsed();
				if (currentTime < ModConfig.CUSTOM_NIGHT_DETECTION.customStartValue)
					canSleepNow = false;
			} else {
				canSleepNow = !isDaytime;
			}
		}
		return canSleepNow;
	}

	@SubscribeEvent
	public void onPlayerWakeUp(final PlayerWakeUpEvent event) {
		SleepingVoteHandler.forceUpdate();
	}

	@SubscribeEvent
	public void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
		SleepingVoteHandler.forceUpdate();
	}
}
