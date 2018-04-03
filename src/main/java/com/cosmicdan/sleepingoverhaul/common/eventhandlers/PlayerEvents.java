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
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@Log4j2(topic = "SleepingTweaks/PlayerEvents")
public class PlayerEvents {
	private static final Timekeeper TIMEKEEPER = Timekeeper.getInstance();

	@SuppressWarnings({"MethodWithMoreThanThreeNegations", "MethodWithMultipleReturnPoints", "OverlyComplexMethod", "OverlyLongMethod"})
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
		// Adapted from EntityPlayer#trySleep
		final BlockPos bedPos = event.getPos();

		final EnumFacing enumfacing = event.getEntityPlayer().world.getBlockState(bedPos).getValue(BlockHorizontal.FACING);
		if (!event.getEntityPlayer().world.isRemote) {
			if (event.getEntityPlayer().isPlayerSleeping() || !event.getEntityPlayer().isEntityAlive()) {
				event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
				return;
			}

			if (!canSleepHere(event.getEntityPlayer().world.provider.isSurfaceWorld())) {
				event.setResult(EntityPlayer.SleepResult.NOT_POSSIBLE_HERE);
				return;
			}

			if (!canSleepNow(event.getEntityPlayer().world.isDaytime())) {
				event.setResult(EntityPlayer.SleepResult.NOT_POSSIBLE_NOW);
				return;
			}

			val isBedInRange = (boolean) EntityPlayerMirror.METHOD___BED_IN_RANGE.call(event.getEntityPlayer(), bedPos, enumfacing);
			if (!isBedInRange) {
				// Bed out of range
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

				@SuppressWarnings("unchecked")
				val sleepEnemyPredicate = (Predicate<EntityMob>) EntityPlayerMirror.CONSTRUCTOR_SLEEP_ENEMY_PREDICATE.construct(null, event.getEntityPlayer());
				final List<EntityMob> mobFoundList = event.getEntityPlayer().world.getEntitiesWithinAABB(EntityMob.class, searchBox, sleepEnemyPredicate);

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
		if (event.getEntityPlayer().world.isBlockLoaded(bedPos))
			state = event.getEntityPlayer().world.getBlockState(bedPos);
		final val defaultOffsetXZ = 0.5F;
		final val offsetY = 0.6875F;
		if ((null != state) && state.getBlock().isBed(state, event.getEntityPlayer().world, bedPos, event.getEntityPlayer())) {
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

		if (!event.getEntityPlayer().world.isRemote) {
			final val allPlayers = event.getEntityPlayer().world.playerEntities;
			int asleepPlayers = 0;
			if (!allPlayers.isEmpty()) {
				for (final EntityPlayer entityPlayer : allPlayers) {
					if (!entityPlayer.isSpectator() && entityPlayer.isPlayerSleeping())
						asleepPlayers++;
				}
				// TODO: sleep vote kick thing
				/*
				if (asleepPlayers < allPlayers.size()) {
					// 0.5d = ratio of players for sleep kick
					if (0.5d < ((double)asleepPlayers / allPlayers.size())) {
						// initiate kick warning for non-asleep players
					} else {
						// show info that x players are sleeping, y needed
					}
				}
				*/
			}

			if (ModAccessors.TICKRATE_CHANGER_LOADED) {
				if (asleepPlayers == allPlayers.size()) {
					log.info("All players sleeping, Timelapse engaged!");
					ModAccessors.TICKRATE_CHANGER.changeTickrate(ModConfig.TIMELAPSE_MODE.rate);
				}
			} else {
				event.getEntityPlayer().world.updateAllPlayersSleepingFlag();
			}

		}
		event.setResult(EntityPlayer.SleepResult.OK);
	}

	private static boolean canSleepHere(boolean isOverworld) {
		//return ModConfig.RESTRICTIONS.onlyOverworld ? isOverworld : true;
		return isOverworld;
	}

	private static boolean canSleepNow(boolean isDaytime) {
		boolean canSleepNow = true;
		if (!ModConfig.CMC.allowSleepDuringDay) {
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
	public void onPlayerWakeUp(PlayerWakeUpEvent event) {
		if (ModAccessors.TICKRATE_CHANGER_LOADED) {
			// always reset the tickrate to stock if anybody wakes up
			ModAccessors.TICKRATE_CHANGER.changeTickrate(20.0f);
		}
	}
}
