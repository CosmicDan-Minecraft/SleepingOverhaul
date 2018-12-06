package com.cosmicdan.sleepingoverhaul.common.eventhandlers;

import com.cosmicdan.sleepingoverhaul.common.ModConfig;
import com.cosmicdan.sleepingoverhaul.common.interop.ModAccessors;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@Log4j2(topic =  "SleepingOverhaul/SleepingVoteHandler")
public class SleepingVoteHandler {
	private static final Pattern REMINDER_PATTERN_COUNT = Pattern.compile("(COUNT)", Pattern.LITERAL);
	private static final Pattern REMINDER_PATTERN_TIMEOUT = Pattern.compile("(TIMEOUT)", Pattern.LITERAL);

	private static final int MAIN_LOOP_MAX = 20; // every second(-ish)
	private static int MAIN_LOOP_COUNTER = MAIN_LOOP_MAX;

	// counter for reminder display (status text is too short by default, so we repeat it a few times)
	private int reminderDisplayCount = 0;
	// counter for reminder delay (to show every x seconds)
	private int reminderRepeatCount = ModConfig.SLEEP_VOTE.reminderTextRepeatTime; // initial max

	private int actionTimeout = ModConfig.SLEEP_VOTE.actionTimeout;

	private boolean isTimelapseEnabled = false;
	private static volatile boolean IS_KICK_VOTE_IN_PROGRESS = false;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	//public void playerSleepingCheck(final TickEvent.WorldTickEvent event) {
	public void playerSleepingCheck(final TickEvent.ServerTickEvent event) {
		if ((Side.SERVER == event.side) && (TickEvent.Phase.END == event.phase)) {
			if (MAIN_LOOP_MAX == MAIN_LOOP_COUNTER) {
				// a second has passed
				MAIN_LOOP_COUNTER = 0;

				// get all asleep players
				final val allPlayerEntities = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
				int asleepPlayers = 0;
				int awakePlayers = 0;
				for (final EntityPlayer entityPlayer : allPlayerEntities) {
					if (!entityPlayer.isSpectator()) {
						if (entityPlayer.isPlayerSleeping())
							asleepPlayers++;
						else
							awakePlayers++;
					}
				}

				final int allPlayers = asleepPlayers + awakePlayers; // we don't want to count spectators

				if ((ModConfig.SLEEP_VOTE.percent / 100.0) <= ((double) asleepPlayers / allPlayers)) {
					// sleep vote threshold reached
					if (0 == awakePlayers) {
						notifyPlayers(allPlayerEntities, new TextComponentString(ModConfig.SLEEP_VOTE.allAsleepText));
						performSleepAction(allPlayerEntities);
						// also set actionTimeout to allow wake messages
						actionTimeout = 0;
					} else if (0 == actionTimeout) {
						// actionTimeout elapsed
						notifyPlayers(allPlayerEntities, new TextComponentString(ModConfig.SLEEP_VOTE.votePassedText));
						performSleepAction(allPlayerEntities);
					} else {
						// do reminder text stuff

						if (ModConfig.SLEEP_VOTE.reminderTextRepeatTime <= reminderRepeatCount) {
							// repeat count has been reached (or surpassed, since we keep incrementing it to keep the reminder display on)
							if (ModConfig.SLEEP_VOTE.reminderTextDisplayTime >= reminderDisplayCount) {
								// reminder count has not been reached - show reminder

								// probably a "nicer" way to do this but meh
								final String reminderTextCountReplaced = REMINDER_PATTERN_COUNT.matcher(ModConfig.SLEEP_VOTE.reminderText)
										.replaceAll(Matcher.quoteReplacement(asleepPlayers + "/" + allPlayers));
								final String reminderText = REMINDER_PATTERN_TIMEOUT.matcher(reminderTextCountReplaced)
										.replaceAll(Matcher.quoteReplacement(String.valueOf(actionTimeout)));

								final ITextComponent reminderTextComponent = new TextComponentString(reminderText);

								notifyPlayers(allPlayerEntities, reminderTextComponent);
							}
						}
						actionTimeout--;
					}

					// do counter adjustments outside the for-each player loop
					if (ModConfig.SLEEP_VOTE.reminderTextRepeatTime <= reminderRepeatCount) {
						// repeat count has been reached (or surpassed, since we keep incrementing it to keep the reminder display on)
						if (ModConfig.SLEEP_VOTE.reminderTextDisplayTime >= reminderDisplayCount) {
							// reminder display count has not been reached - increment it
							reminderDisplayCount++;
						} else {
							// reminder display count reached - reset it
							reminderDisplayCount = 0;
							// also reset the repeat/delay counter now
							reminderRepeatCount = 0;
						}
					}
					reminderRepeatCount++;
				} else {
					IS_KICK_VOTE_IN_PROGRESS = false;
					if (actionTimeout != ModConfig.SLEEP_VOTE.actionTimeout) {
						// a pending action timeout or timelapse was occuring - notify all players
						final String cancelledTextReplaced = REMINDER_PATTERN_COUNT.matcher(ModConfig.SLEEP_VOTE.cancelledText)
								.replaceAll(Matcher.quoteReplacement(asleepPlayers + "/" + allPlayers));
						notifyPlayers(allPlayerEntities, new TextComponentString(cancelledTextReplaced));
					}
					// reset all counters
					actionTimeout = ModConfig.SLEEP_VOTE.actionTimeout;
					reminderDisplayCount = ModConfig.SLEEP_VOTE.reminderTextDisplayTime;
					reminderRepeatCount = ModConfig.SLEEP_VOTE.reminderTextRepeatTime;
					// reset tickrate
					enableTimelapse(false);

				}
			}
			MAIN_LOOP_COUNTER++;
		}
	}

	private void performSleepAction(final List<EntityPlayerMP> allPlayerEntities) {
		switch (ModConfig.SLEEP_VOTE.action) {
			case NONE:
				break;
			case TIMELAPSE_ANYWAY:
				enableTimelapse(true);
				break;
			case CMC_KICK_AWAKE:
				// we wont reset this flag until players actually wake up
				IS_KICK_VOTE_IN_PROGRESS = true;
				for (final EntityPlayerMP player : allPlayerEntities) {
					if (!player.isPlayerSleeping())
						player.connection.disconnect(new TextComponentString(ModConfig.SLEEP_VOTE.kickText));
				}
				enableTimelapse(true);
				break;
			case SKIP_TO_DAY:
				for (final WorldServer worldServer : FMLCommonHandler.instance().getMinecraftServerInstance().worlds) {
					worldServer.updateAllPlayersSleepingFlag();
				}
				break;
		}
	}

	private static void notifyPlayers(final List<EntityPlayerMP> allPlayers, final ITextComponent playerMessage) {
		for (final EntityPlayer entityPlayer : allPlayers) {
			entityPlayer.sendStatusMessage(playerMessage, true);
		}
	}

	private void enableTimelapse(final boolean enable) {
		if (ModAccessors.TICKRATE_CHANGER_LOADED && (enable != isTimelapseEnabled)) {
			isTimelapseEnabled = enable;
			ModAccessors.TICKRATE_CHANGER.changeTickrate(enable ? ModConfig.TIMELAPSE_MODE.rate : 20.0f);
		}
	}

	static void forceUpdate() {
		MAIN_LOOP_COUNTER = MAIN_LOOP_MAX;
	}

	@SuppressWarnings("SuspiciousGetterSetter")
	public static boolean isKickVoteInProgress() {
		return IS_KICK_VOTE_IN_PROGRESS;
	}
}
