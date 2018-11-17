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
package net.runelite.client.ui.overlay.infobox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import net.runelite.client.plugins.Plugin;

public class Stopwatch extends InfoBox
{
	@Getter
	private final Instant startTime;

	@Getter
	private Duration elapsedTime;

	public Stopwatch(BufferedImage image, Plugin plugin)
	{
		super(image, plugin);

		startTime = Instant.now();
	}

	@Override
	public String toString()
	{
		return "Stopwatch { " + "startTime = " + startTime + ", elapsedTime = " + elapsedTime + " }";
	}

	@Override
	public String getText()
	{
		elapsedTime = Duration.between(startTime, Instant.now());

		int seconds = (int) (elapsedTime.toMillis() / 1000L);

		int minutes = (seconds % 3600) / 60;
		int secs = seconds % 60;

		return String.format("%d:%02d", minutes, secs);
	}

	@Override
	public Color getTextColor()
	{
		return Color.WHITE;
	}

	@Override
	public boolean render()
	{
		Duration time = Duration.between(startTime, Instant.now());
		return !time.isNegative();
	}

	@Override
	public boolean cull()
	{
		Duration time = Duration.between(startTime, Instant.now());
		return time.isZero() || time.isNegative();
	}

}
