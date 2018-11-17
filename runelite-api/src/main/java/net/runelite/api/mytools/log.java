/*
 * Copyright (c) 2018, DannysPVM <DannysPVM@gmail.com>
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
package net.runelite.api.mytools;

import java.time.LocalDateTime;
import org.slf4j.helpers.MessageFormatter;

/**
 * For logging debug messages to the console when running RuneLite without the --debug flag.
 * Allows for not having to search the spam ridden console for my personal debug messages.
 */
public abstract class log
{
	/**
	 * @param messagePattern The String containing the debug message to log with '{}' delimiters.
	 * @param argArray The Array of Object variables to place sequentially between '{}' delimiters.
	 * @apiNote debug method adds a timestamp and the debug source path to the debug message.
	 */
	public static void debug(String messagePattern, Object... argArray)
	{
		LocalDateTime localDateTime = LocalDateTime.now();

		StringBuilder timeStamp = new StringBuilder(String.format("%04d-%02d-%02d %02d:%02d:%02d",
			localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth(),
			localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()));

		StringBuilder debugSource = new StringBuilder(" [DEBUG] - ").append(Thread.currentThread().getStackTrace()[2].getClassName()).append(" - ");
		StringBuilder message = new StringBuilder(MessageFormatter.arrayFormat(messagePattern, argArray).getMessage());
		StringBuilder debugMessage = new StringBuilder(timeStamp).append(debugSource).append(message);

		System.out.println(debugMessage);
	}
}
