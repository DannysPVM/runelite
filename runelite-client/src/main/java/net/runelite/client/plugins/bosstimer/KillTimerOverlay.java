/*
 * Copyright (c) 2018, Danny <DannysPVM@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.bosstimer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.swing.plaf.ColorUIResource;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class KillTimerOverlay extends Overlay
{
	private final PanelComponent panelComponent = new PanelComponent();
	private final Client client;
	private final BossTimersPlugin plugin;
	private final BossTimerConfig config;

	@Inject
	private KillTimerOverlay(Client client, BossTimersPlugin plugin, BossTimerConfig config)
	{
		this.plugin = plugin;
		this.client = client;
		this.config = config;

		setPosition(OverlayPosition.BOTTOM_LEFT);
		setPriority(OverlayPriority.MED);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.killTimerStyle() != BossTimersStyle.PANEL)
		{
			return null;
		}

		// Get the KillTimers array
		KillTimer[] killTimers = plugin.getKillTimers();
		if (killTimers == null)
		{
			return null;
		}

		// Build Panel
		buildKillTimerPanel(panelComponent, killTimers);

		// Render Panel
		return panelComponent.render(graphics);
	}

	private void buildKillTimerPanel(PanelComponent panelComponent, KillTimer[] killTimers)
	{
		// Clear Panel Component Children
		panelComponent.getChildren().clear();

		// Create Panel Header Srting
		String panelHeader = "Kill Timer";

		// If there are more than 1 KillTimers in the array, add an s to panelHeader
		if (killTimers.length > 1)
		{
			panelHeader = panelHeader.concat("s");
		}

		// Boolean for adding the title to the panel (w/o it, if all 3 DK's die, the title remains)
		boolean addedTitle = false;

		// Loop through the KillTimer array
		for (KillTimer killTimer : killTimers)
		{
			// For each non-null KillTimer, add the Bosses name and Kill Time
			if (killTimer != null)
			{
				// If the title has not been added to panel
				if (!addedTitle)
				{
					// Add Title Containing Kill Timer Caption (Yellow font)
					panelComponent.getChildren().add(TitleComponent.builder()
							.text(panelHeader)
							.color(ColorUIResource.YELLOW)
							.build()
					);

					// Set addedTitle to true, only add the title once per 'render cycle'
					addedTitle = true;
				}

				// Add Kill Times (font color depends on how close to a Personal Best the Player is)
				panelComponent.getChildren().add(LineComponent.builder()
						// Dagannoth Kings names Overflow, Just truncate to the unique name identifier, i.e. Rex
						.left(killTimer.getBoss().getName().replaceAll("Dagannoth", ""))
						.leftColor(ColorUIResource.WHITE)
						.right(killTimer.getText())
						.rightColor(killTimer.getTextColor())
						.build()
				);
			}
		}
	}
}
