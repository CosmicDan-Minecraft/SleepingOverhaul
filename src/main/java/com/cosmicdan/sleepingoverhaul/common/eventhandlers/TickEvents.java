package com.cosmicdan.sleepingoverhaul.common.eventhandlers;

import com.cosmicdan.cosmiclib.gamedata.Timekeeper;
import com.cosmicdan.sleepingoverhaul.common.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
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
public class TickEvents {
	private static final Timekeeper TIMEKEEPER = Timekeeper.getInstance();
	private static final Pattern REMINDER_COUNT_PATTERN = Pattern.compile("[COUNT]", Pattern.LITERAL);

	private final int clockDisplayRate = 10;
	private int clockDisplayCounter = 0;

	private final int sleepReminderRate = 2000;
	private int sleepReminderCounter = 1500;
	private final int sleepReminderLoopRate = 6;
	private int sleepReminderLoop = 0;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		doPlayerTick(event);
	}

	private void doPlayerTick(TickEvent.PlayerTickEvent event) {
		if (Side.CLIENT == event.side) {
			if (clockDisplayRate <= clockDisplayCounter) {
				clockDisplayCounter = 0;
				if (null != Minecraft.getMinecraft().player) {
					if (Minecraft.getMinecraft().player.isPlayerSleeping()) {
						Minecraft.getMinecraft().player.sendStatusMessage(new TextComponentString(TIMEKEEPER.getNiceTimeString(false)), true);
					}
				}
			}

			if (sleepReminderRate <= sleepReminderCounter) {
				if (sleepReminderLoopRate > sleepReminderLoop) {
					sleepReminderCounter = sleepReminderRate - 100;
					sleepReminderLoop++;
				} else {
					sleepReminderCounter = 0;
					sleepReminderLoop = 0;
				}
				if (null != Minecraft.getMinecraft().player) {
					if (!Minecraft.getMinecraft().player.isPlayerSleeping()) {
						int totalAsleepPlayers = 0;
						final List<EntityPlayer> allPlayers = event.player.world.playerEntities;
						for (final EntityPlayer player : event.player.world.playerEntities) {
							if (player.isPlayerSleeping())
								totalAsleepPlayers++;
						}
						if (0.5d <= ((double)totalAsleepPlayers / allPlayers.size())) {
							final String reminderText = REMINDER_COUNT_PATTERN.matcher(
									ModConfig.CLIENT.reminderText).replaceAll(Matcher.quoteReplacement(totalAsleepPlayers + "/" + allPlayers.size()));
							Minecraft.getMinecraft().player.sendStatusMessage(new TextComponentString(reminderText), true);
						}
					}
				}
			}

			clockDisplayCounter += 1;
			sleepReminderCounter += 1;
		}
	}
}
