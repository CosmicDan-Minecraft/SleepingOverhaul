package com.cosmicdan.sleepingoverhaul.client.eventhandlers;

import com.cosmicdan.cosmiclib.gamedata.Timekeeper;
import com.cosmicdan.sleepingoverhaul.common.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
public class RenderGameOverlayEvents {
	private static final Timekeeper TIMEKEEPER = Timekeeper.getInstance();

	/**
	 * Client-side hook for rendering the clock when the player lies in a bed
	 */
	@SuppressWarnings("IntegerDivisionInFloatingPointContext")
	@SubscribeEvent
	public void drawSleepingClock(final RenderGameOverlayEvent.Post event) {
		if (ModConfig.CLIENT_SETTINGS.showClockInBed && (RenderGameOverlayEvent.ElementType.ALL == event.getType())) {
			// draw clock for sleeping players
			final EntityPlayerSP thisPlayer = Minecraft.getMinecraft().player;
			if (thisPlayer.isPlayerSleeping()) {
				// draw text ourselves in the middle of the screen (using the status message gets blocked by the chat window)
				final String clockText;
				if (-1 == thisPlayer.dimension) {
					// for nether, just show question marks (the time never changes; I will update TimeKeeper some other time)
					clockText = "??:??";
				} else
					clockText = TIMEKEEPER.getNiceTimeString(false);
				final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
				final int width = res.getScaledWidth();
				final int height = res.getScaledHeight();
				final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
				GlStateManager.pushMatrix();
				GlStateManager.translate((width / 2), (height / 2), 0.0F);
				fontRenderer.drawString(clockText, -fontRenderer.getStringWidth(clockText) / 2, -50, 0xFFFFFF, true);
				GlStateManager.popMatrix();
			}
		}
	}
}
