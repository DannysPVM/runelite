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

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.runelite.api.NPC;
import net.runelite.api.mytools.log;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.Stopwatch;

class KillTimer extends Stopwatch
{
	private final Boss boss;
	private Duration personalBest;
	private Instant pbEndTime;
	private BossTimerConfig config;

	KillTimer(Boss boss, BufferedImage bossImage, Plugin plugin, BossTimerConfig config)
	{
		super(bossImage, plugin);
		this.boss = boss;
		this.config = config;
		this.personalBest = loadPersonalBest();
	}

	public Boss getBoss()
	{
		return boss;
	}

	private Duration loadPersonalBest()
	{
		// Get the bosses name from the boss
		String bossName = boss.getName();

		// Fix the bosses name to match the config name for each bosses PB
		bossName = Character.toLowerCase(bossName.charAt(0)) + bossName.substring(1).replaceAll(" ", "").replaceAll("'", "").concat("PB");

		// Load the players personal best for the boss
		try
		{
			// Get the Method, m, that matches the bossName with a no parameter (For loading KillTimer value)
			Method m = config.getClass().getDeclaredMethod(bossName);

			// Get the Duration, d, returned from invoking the method, m, from the config instance ex. Duration d = config.krilTsutsarothPB();
			Duration d = (Duration) m.invoke(config);

			// Set the Duration to the players personal best
			personalBest = Duration.of(d.getSeconds(), ChronoUnit.SECONDS);

			log.debug("Loaded {}'s Personal Best Kill Time: {}", boss.getName(), personalBest.toString());

			// Verify the players personalBest is not Negative or Zero!
			Preconditions.checkArgument(!personalBest.isNegative() && !personalBest.isZero(), "Personal Best is <= 0!");
			pbEndTime = getStartTime().plus(personalBest);

			return personalBest;
		}
		// Handle every potential exception that can be thrown
		catch (NoSuchMethodException e)
		{
			log.debug("No Such Method Exception {}", e.getMessage());
		}
		catch (SecurityException e)
		{
			log.debug("Security Exception {}", e.getMessage());
		}
		catch (IllegalAccessException e)
		{
			log.debug("Illegal Access Exception {}", e.getMessage());
		}
		catch (IllegalArgumentException e)
		{
			log.debug("Illegal Argument Exception {}", e.getMessage());
		}
		catch (InvocationTargetException e)
		{
			log.debug("Invocation Target Exception {}", e.getMessage());
		}
		// Unable to load Personal Best, Return default of 1 HOUR (3600 SECONDS)
		return Duration.of(3600, ChronoUnit.SECONDS);
	}

	private boolean newPersonalBest()
	{
		return Instant.now().isBefore(pbEndTime);
	}

	private boolean interactingWithBoss(NPC interacting)
	{
		return (interacting != null && interacting.getName().equals(boss.getName()));
	}

	void handleKillTime(NPC interacting)
	{
		if (!interactingWithBoss(interacting))
		{
			log.debug("Kill Time for boss {} was not saved, the Local Player was not attacking the boss when it died.", boss.getName());
			return;
		}

		if (!newPersonalBest())
		{
			log.debug("Kill Time for boss {} was not saved, not a new Personal Best. (Current Personal Best {}, KillTimer End Time {})", boss.getName(), this.personalBest, this.getElapsedTime());
			return;
		}

		savePersonalBest();
	}

	private void savePersonalBest()
	{
		// Get the bosses name from the boss
		String bossName = boss.getName();

		// Fix the bosses name to match the config name for each bosses PB
		bossName = Character.toLowerCase(bossName.charAt(0)) + bossName.substring(1).replaceAll(" ", "").replaceAll("'", "").concat("PB");

		// Save the players personal best for the boss
		try
		{
			// Get the Method, m, that matches the bossName with a Duration parameter (For saving KillTimer value)
			Method m = config.getClass().getDeclaredMethod(bossName, Duration.class);

			// Invoke the Method, m, to save the players Personal Best Kill Time ex. config.krilTsusarothPB(KillTimer.getElapsedTime());
			m.invoke(config, this.getElapsedTime());

			log.debug("New Personal Best for Boss: {}, Saved Kill Time: {}", boss.getName(), this.getText());
		}
		// Handle every potential exception that can be thrown
		catch (NoSuchMethodException e)
		{
			log.debug("No Such Method Exception {}", e.getMessage());
		}
		catch (SecurityException e)
		{
			log.debug("Security Exception {}", e.getMessage());
		}
		catch (IllegalAccessException e)
		{
			log.debug("Illegal Access Exception {}", e.getMessage());
		}
		catch (IllegalArgumentException e)
		{
			log.debug("Illegal Argument Exception {}", e.getMessage());
		}
		catch (InvocationTargetException e)
		{
			log.debug("Invocation Target Exception {}", e.getMessage());
		}
	}

	@Override
	public Color getTextColor()
	{
		Duration timeLeft = Duration.between(Instant.now(), pbEndTime);
		if (timeLeft.getSeconds() > (personalBest.getSeconds() * .10))
		{
			return Color.WHITE;
		}
		else if ((timeLeft.getSeconds() < personalBest.getSeconds() * .10) && !timeLeft.isNegative())
		{
			return Color.YELLOW;
		}
		else if (timeLeft.isNegative())
		{
			return Color.RED;
		}

		return Color.WHITE;
	}
}
