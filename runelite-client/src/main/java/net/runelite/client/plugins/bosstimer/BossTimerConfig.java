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

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(BossTimersPlugin.CONFIG_GROUP)
public interface BossTimerConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = BossTimersPlugin.KILL_TIMER_STYLE,
		name = "Kill Timer Style",
		description = "Configures whether to show Kill Timer as an InfoBox, Panel, or Off"
	)
	default BossTimersStyle killTimerStyle()
	{
		return BossTimersStyle.INFOBOX;
	}

	@ConfigItem(
		position = 2,
		keyName = BossTimersPlugin.CLEAR_CORP_KILL_TIMER_ON_TELE,
		name = "Reset Corp Timer on Teleport",
		description = "Configures whether or not to clear Corp Kill Timer on teleport from Corp Lair (Solo's)"
	)
	default boolean clearCorpKillTimerOnTele()
	{
		return false;
	}




	/**
	 *  Store Personal Best Kill Times for ALL Bosses (default to 1 Hour, first kill of each boss allows for recoloring time)
	 *  Note: All Personal Best methods are called via Method Reflection, They ARE Used!
	 */
	@ConfigItem(
		keyName = BossTimersPlugin.GENERAL_GRAARDOR_PB,
		name = "",
		description = "",
		hidden = true
	)
	default Duration generalGraardorPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
		keyName = BossTimersPlugin.GENERAL_GRAARDOR_PB,
		name = "",
		description = ""
	)
	void generalGraardorPB(Duration personalBest);



	@ConfigItem(
		keyName = BossTimersPlugin.KRIL_TSUTSAROTH_PB,
		name = "",
		description = "",
		hidden = true
	)
	default Duration krilTsutsarothPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
		keyName = BossTimersPlugin.KRIL_TSUTSAROTH_PB,
		name = "",
		description = ""
	)
	void krilTsutsarothPB(Duration personaBest);



	@ConfigItem(
		keyName = BossTimersPlugin.KREEARRA_PB,
		name = "",
		description = "",
		hidden = true
	)
	default Duration kreearraPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
		keyName = BossTimersPlugin.KREEARRA_PB,
		name = "",
		description = ""
	)
	void kreearraPB(Duration personalBest);



	@ConfigItem(
		keyName = BossTimersPlugin.COMMANDER_ZILYANA_PB,
		name = "",
		description = "",
		hidden = true
	)
	default Duration commanderZilyanaPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
		keyName = BossTimersPlugin.COMMANDER_ZILYANA_PB,
		name = "",
		description = ""
	)
	void commanderZilyanaPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.CALLISTO_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration callistoPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.CALLISTO_PB,
			name = "",
			description = ""
	)
	void callistoPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.CHAOS_ELEMENTAL_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration chaosElementalPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.CHAOS_ELEMENTAL_PB,
			name = "",
			description = ""
	)
	void chaosElementalPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.CHAOS_FANATIC_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration chaosFanaticPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.CHAOS_FANATIC_PB,
			name = "",
			description = ""
	)
	void chaosFanaticPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.CRAZY_ARCHAEOLOGIST_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration crazyArchaeologistPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.CRAZY_ARCHAEOLOGIST_PB,
			name = "",
			description = ""
	)
	void crazyArchaeologistPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.KING_BLACK_DRAGON_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration kingBlackDragonPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.KING_BLACK_DRAGON_PB,
			name = "",
			description = ""
	)
	void kingBlackDragonPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.SCORPIA_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration scorpiaPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.SCORPIA_PB,
			name = "",
			description = ""
	)
	void scorpiaPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.VENENATIS_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration venenatisPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.VENENATIS_PB,
			name = "",
			description = ""
	)
	void venenatisPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.VETION_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration vetionPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.VETION_PB,
			name = "",
			description = ""
	)
	void vetionPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.DAGANNOTH_PRIME_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration dagannothPrimePB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.DAGANNOTH_PRIME_PB,
			name = "",
			description = ""
	)
	void dagannothPrimePB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.DAGANNOTH_REX_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration dagannothRexPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.DAGANNOTH_REX_PB,
			name = "",
			description = ""
	)
	void dagannothRexPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.DAGANNOTH_SUPREME_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration dagannothSupremePB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.DAGANNOTH_SUPREME_PB,
			name = "",
			description = ""
	)
	void dagannothSupremePB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.CORPOREAL_BEAST_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration corporealBeastPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.CORPOREAL_BEAST_PB,
			name = "",
			description = ""
	)
	void corporealBeastPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.GIANT_MOLE_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration giantMolePB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.GIANT_MOLE_PB,
			name = "",
			description = ""
	)
	void giantMolePB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.DERANGED_ARCHAEOLOGIST_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration derangedArchaeologistPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.DERANGED_ARCHAEOLOGIST_PB,
			name = "",
			description = ""
	)
	void derangedArchaeologistPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.CERBERUS_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration cerberusPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.CERBERUS_PB,
			name = "",
			description = ""
	)
	void cerberusPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.THERMONUCLEAR_SMOKE_DEVIL_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration thermonuclearSmokeDevilPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.THERMONUCLEAR_SMOKE_DEVIL_PB,
			name = "",
			description = ""
	)
	void thermonuclearSmokeDevilPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.KRAKEN_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration krakenPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.KRAKEN_PB,
			name = "",
			description = ""
	)
	void krakenPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.KALPHITE_QUEEN_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration kalphiteQueenPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.KALPHITE_QUEEN_PB,
			name = "",
			description = ""
	)
	void kalphiteQueenPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.DUSK_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration duskPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.DUSK_PB,
			name = "",
			description = ""
	)
	void duskPB(Duration personalBest);



	@ConfigItem(
		keyName = BossTimersPlugin.ZULRAH_PB,
		name = "",
		description = "",
		hidden = true
	)
	default Duration zulrahPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
		keyName = BossTimersPlugin.ZULRAH_PB,
		name = "",
		description = ""
	)
	void zulrahPB(Duration personalBest);



	@ConfigItem(
			keyName = BossTimersPlugin.VORKATH_PB,
			name = "",
			description = "",
			hidden = true
	)
	default Duration vorkathPB()
	{
		return Duration.of(1, ChronoUnit.HOURS);
	}

	@ConfigItem(
			keyName = BossTimersPlugin.VORKATH_PB,
			name = "",
			description = ""
	)
	void vorkathPB(Duration personalBest);
}
