/*
 * Copyright (c) 2016-2017, Cameron Moberg <Moberg@tuta.io>
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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
package net.runelite.client.plugins.bosstimer;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.time.Instant;
import java.util.Set;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Actor;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.mytools.log;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@PluginDescriptor(
	name = "Boss Timers",
	description = "Show boss spawn timer and kill timer overlays",
	tags = {"combat", "pve", "overlay", "spawn"}
)
public class BossTimersPlugin extends Plugin
{
	static final String CONFIG_GROUP = "bossTimer";
	static final String KILL_TIMER_STYLE = "killTimerStyle";
	static final String CLEAR_CORP_KILL_TIMER_ON_TELE = "clearCorpKillTimerOnTele";
	static final String GENERAL_GRAARDOR_PB = "generalGraardorPB";
	static final String KRIL_TSUTSAROTH_PB = "krilTsutsarothPB";
	static final String KREEARRA_PB = "kree'arraPB";
	static final String COMMANDER_ZILYANA_PB = "commanderZilyanaPB";
	static final String CALLISTO_PB = "callistoPB";
	static final String CHAOS_ELEMENTAL_PB = "chaosElementalPB";
	static final String CHAOS_FANATIC_PB = "chaosFanaticPB";
	static final String CRAZY_ARCHAEOLOGIST_PB = "crazyArchaeologistPB";
	static final String KING_BLACK_DRAGON_PB = "kingBlackDragonPB";
	static final String SCORPIA_PB = "scorpiaPB";
	static final String VENENATIS_PB = "venenatisPB";
	static final String VETION_PB = "vet'ionPB";
	static final String DAGANNOTH_PRIME_PB = "dagannothPrimePB";
	static final String DAGANNOTH_REX_PB = "dagannothRexPB";
	static final String DAGANNOTH_SUPREME_PB = "dagannothSupremePB";
	static final String CORPOREAL_BEAST_PB = "corporealBeastPB";
	static final String GIANT_MOLE_PB = "giantMolePB";
	static final String DERANGED_ARCHAEOLOGIST_PB = "derangedArchaeologistPB";
	static final String CERBERUS_PB = "cerberusPB";
	static final String THERMONUCLEAR_SMOKE_DEVIL_PB = "thermonuclearSmokeDevilPB";
	static final String KRAKEN_PB = "krakenPB";
	static final String KALPHITE_QUEEN_PB = "kalphitePB";
	static final String DUSK_PB = "duskPB";
	static final String ZULRAH_PB = "zulrahPB";
	static final String VORKATH_PB = "vorkathPB";

	private static final Set<Integer> KRAKEN_LAIR_REGIONS = ImmutableSet.of(
			9116				// Kraken Lair
	);

	private static final Set<Integer> CERBERUS_REGIONS = ImmutableSet.of(
			4883 				// Cerberus Lair
	);

	private static final Set<Integer> GIANT_MOLE_REGIONS = ImmutableSet.of(
			6993, 				// Mole Lair North
			6992  				// Mole Lair South
	);

	private static final Set<Integer> CORPOREAL_BEAST_REGIONS = ImmutableSet.of(
			11844				// Corporeal Beast Lair
	);

	private static final Set<Integer> DAGANNOTH_KINGS_REGIONS = ImmutableSet.of(
			11589				// Dagannoth Lair
	);

	private static final Set<String> NON_SPAWN_HANDLED_BOSSES = ImmutableSet.of(
			"Zulrah",			// Zulrah
			"Vorkath",			// Vorkath
			"Kraken",			// Kraken
			"Cerberus"			// Cerberus
	);

	private static final Set<String> NON_DESPAWN_HANDLED_BOSSES = ImmutableSet.of(
			"Giant Mole",		// Giant Mole
			"Corporeal Beast",	// Corporeal Beast
			"Dagannoth Prime",	// Dagannoth Prime
			"Dagannoth Rex",	// Dagannoth Rex
			"Dagannoth Supreme"	// Dagannoth Supreme
	);

	private static final Set<String> NON_RESPAWN_HANDLED_BOSSES = ImmutableSet.of(
			"Zulrah",			// Zulrah
			"Vorkath"			// Vorkath
	);

	@Inject
	private Client client;

	@Inject
	private BossTimerConfig config;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KillTimerOverlay overlay;

	@Getter(AccessLevel.PACKAGE)
	private KillTimer[] killTimers;

	private NPC interacting;

	@Provides
	BossTimerConfig bossTimerConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BossTimerConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		infoBoxManager.removeIf(t -> t instanceof RespawnTimer);
		infoBoxManager.removeIf(t -> t instanceof KillTimer);
		killTimers = null;
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		// Get the Spawned NPC
		NPC npc = npcSpawned.getNpc();
		if (npc == null)
		{
			return;
		}

		// If not a boss that spawned, return
		if (Boss.find(npc.getName()) == null)
		{
			return;
		}

		// Boss found, set boss to the found Boss
		Boss boss = Boss.find(npc.getName());

		// Verify Boss is a spawn handled boss
		if (NON_SPAWN_HANDLED_BOSSES.contains(boss.getName()))
		{
			return;
		}

		log.debug("Boss: {} Has Spawned at time: {}", boss.getName(), Instant.now());

		setKillTimer(boss);
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		// Get Actor
		final Actor actor = event.getActor();

		// Return if not NPC
		if (!(actor instanceof NPC))
		{
			return;
		}

		// Get the NPC
		NPC npc = (NPC) actor;

		// Define a Boss
		Boss boss = null;

		// If Boss found
		if (Boss.find(npc.getName()) != null)
		{
			// Set boss to the found Boss
			boss = Boss.find(npc.getName());
		}

		// If NPC name is Enormous Tentacle, set the boss to Kraken. Kraken spawns as "Whirlpool", so instead look for Tentacle attack animation
		if (npc.getName() != null && npc.getName().equals("Enormous Tentacle"))
		{
			// Set boss to Kraken
			boss = Boss.find("Kraken");
		}

		// Verify a boss has been found/set
		if (boss == null)
		{
			return;
		}

		// Handle Boss Animations
		handleBossAnimation(npc, boss);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		// Check if KillTimer array is null
		if (killTimers == null)
		{
			return;
		}

		// Loop through KillTimer Array
		for (KillTimer killTimer : killTimers)
		{
			// If a KillTimer, kt, exists, check interacting with boss/clear timer for non despawn bosses
			if (killTimer != null)
			{
				// If there is a KillTimer set and the player is interacting with a Boss, set interacting NPC (allows for saving only Local Player's PB)
				checkInteractingWithBoss();

				// Clear KillTimer for special case bosses like Giant Mole/Corp
				clearKillTimerNonDespawnBosses(killTimer.getBoss());
			}
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		// Get the NPC
		NPC npc = npcDespawned.getNpc();
		if (npc == null)
		{
			return;
		}

		// If not a boss that despawned, return
		if (Boss.find(npc.getName()) == null)
		{
			return;
		}

		// Boss found, set boss to the found Boss
		Boss boss = Boss.find(npc.getName());

		log.debug("Boss: {} Has Despawned at time: {}", boss.getName(), Instant.now());

		// Get the KillTimer for the dead boss (If KillTimer is null, it's handle in handleBossDespawn)
		final KillTimer killTimer = getKillTimer(boss.getName());
		handleBossDespawn(npc, boss, killTimer);

		// Check if NPC is dead
		if (!npc.isDead())
		{
			return;
		}

		log.debug("Boss: {} Has Died at time: {}", boss.getName(), Instant.now());

		setRespawnTimer(boss);

		// If the KillTimer was not handled on NPC Despawned, handle it now and clear KillTimer
		if (killTimer != null)
		{
			killTimer.handleKillTime(interacting);
			clearKillTimer(boss);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		// If GameState changes to Login Screen or Hopping, clear KillTimer
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			infoBoxManager.removeIf(t -> t instanceof KillTimer);
			killTimers = null;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(CONFIG_GROUP))
		{
			return;
		}

		switch (event.getKey())
		{
			case KILL_TIMER_STYLE:
			{
				if (config.killTimerStyle() != BossTimersStyle.INFOBOX)
				{
					infoBoxManager.removeIf(t -> t instanceof KillTimer);
				}

				if (config.killTimerStyle() == BossTimersStyle.INFOBOX)
				{
					if (killTimers == null)
					{
						return;
					}

					for (KillTimer killTimer : killTimers)
					{
						if (killTimer != null)
						{
							infoBoxManager.addInfoBox(killTimer);
						}
					}
				}
				break;
			}
			case CLEAR_CORP_KILL_TIMER_ON_TELE:
			{
				if (config.clearCorpKillTimerOnTele() && getKillTimer("Corporeal Beast") != null
						&& !CORPOREAL_BEAST_REGIONS.contains(client.getLocalPlayer().getWorldLocation().getRegionID()))
				{
					Boss boss = Boss.CORPOREAL_BEAST;
					clearKillTimer(boss);
				}
				break;
			}
		}
	}

	private KillTimer getKillTimer(String bossName)
	{
		// If the killTimers array is null or the bossName is null/empty string, return null
		if (killTimers == null || Strings.isNullOrEmpty(bossName))
		{
			return null;
		}

		// Loop through the array and check if the boss name at each index is the same as the passed bossName
		for (KillTimer killTimer : killTimers)
		{
			if (killTimer != null && killTimer.getBoss().getName().equals(bossName))
			{
				// Return the KillTimer for this Boss
				return killTimer;
			}
		}

		return null;
	}

	private int getKillTimerIndex(KillTimer killTimer)
	{
		// If the killTimers array is null, return -1
		if (killTimers == null)
		{
			return -1;
		}

		// Loop through the array and check if the KillTimer is the same as the one passed in
		for (int i = 0; i < killTimers.length; i++)
		{
			if (killTimers[i] == killTimer)
			{
				// Return the index of found KillTimer
				return i;
			}
		}

		return -1;
	}

	private void setKillTimer(Boss boss)
	{
		// Player doesn't want KillTimers or a KillTimer exists for the spawned Boss
		if (config.killTimerStyle() == BossTimersStyle.OFF || getKillTimer(boss.getName()) != null)
		{
			return;
		}

		// Remove existing KillTimer
		infoBoxManager.removeIf(t -> t instanceof KillTimer && ((KillTimer) t).getBoss() == boss);

		// Check if the KillTimer array is null
		if (killTimers == null)
		{
			// If boss is a Dagannoth King, create array of size 3
			if (boss.getName().contains("Dagannoth"))
			{
				killTimers = new KillTimer[3];
			}
			// All other bosses (Not a Dagannoth King), create array of size 1
			else
			{
				killTimers = new KillTimer[1];
			}
			log.debug("Creating KillTimers Array for Boss {}, Size of Array {}", boss.getName(), killTimers.length);
		}

		log.debug("Creating KillTimer for: {}, Start time: {}", boss.getName(), Instant.now());

		KillTimer killTimer = null;

		// Loop through the array of KillTimers
		for (int i = 0; i < killTimers.length; i++)
		{
			// If the KillTimer at this index is null, create and put a KillTimer there and break from loop
			if (killTimers[i] == null)
			{
				// Create the KillTimer, with the Boss, BossImage, and this Plugin (Using InfoBox) and Set Tooltip
				killTimer = new KillTimer(boss, itemManager.getImage(boss.getItemSpriteId()), this, config);
				killTimer.setTooltip(boss.getName() + " Kill Timer");

				killTimers[i] = killTimer;
				log.debug("Placed killTimer for Boss {} into killTimers[{}]", killTimer.getBoss().getName(), i);
				break;
			}
			// Else tell the user the array already has the maximum number of KillTimers
			if (killTimers[i] != null && i == killTimers.length - 1)
			{
				log.debug("There are no more spaces in the KillTimer array for a KillTimer! (Max KillTimers: {})", killTimers.length);
			}
		}

		if (config.killTimerStyle() == BossTimersStyle.INFOBOX && killTimer != null)
		{
			infoBoxManager.addInfoBox(killTimer);
		}
	}

	private void setRespawnTimer(Boss boss)
	{
		// Dont set RespawnTimer for Zulrah/Vorkath
		if (NON_RESPAWN_HANDLED_BOSSES.contains(boss.getName()))
		{
			return;
		}

		// Remove existing RespawnTimer
		infoBoxManager.removeIf(t -> t instanceof RespawnTimer && ((RespawnTimer) t).getBoss() == boss);

		log.debug("Creating RespawnTimer for: {}, Timer: {} seconds", boss.getName(), boss.getSpawnTime());

		// Create the RespawnTimer, with the Boss, BossImage, and this Plugin (Using InfoBox) and Set Tooltip
		RespawnTimer respawnTimer = new RespawnTimer(boss, itemManager.getImage(boss.getItemSpriteId()), this);
		respawnTimer.setTooltip(boss.getName() + " Respawn Timer");

		// Add RespawnTimer InfoBox
		infoBoxManager.addInfoBox(respawnTimer);
	}

	private void clearKillTimer(Boss boss)
	{
		// Get the KillTimer for the Boss to remove, return if there is not a KillTimer for this boss
		final KillTimer killTimer = getKillTimer(boss.getName());
		if (killTimer == null)
		{
			return;
		}

		// Get the Index of the KillTimer to remove, return if index is -1, KillTimer index not found
		final int killTimerIndex = getKillTimerIndex(killTimer);
		if (killTimerIndex == -1)
		{
			return;
		}

		// Remove infobox for this Boss
		infoBoxManager.removeIf(t -> t instanceof KillTimer && ((KillTimer) t).getBoss() == boss);

		log.debug("Removing KillTimer for: {} at killTimers[{}], End time: {}", killTimer.getBoss().getName(), killTimerIndex, killTimer.getText());

		// If the bosses are DK's, clear the KillTimer for the specific DK that died
		if (boss.getName().contains("Dagannoth"))
		{
			killTimers[killTimerIndex] = null;
		}
		// Else the bosses aren't DK's, null out KillTimers array
		else
		{
			killTimers = null;
			log.debug("Set KillTimer Array to null after removing Boss {} from killTimers[{}]", boss.getName(), killTimerIndex);
		}
	}

	private void clearKillTimerNonDespawnBosses(Boss boss)
	{
		switch (boss)
		{
			case GIANT_MOLE:
			{
				// Check the RegionID to see if player has left the boss, Yes? Remove KillTimer
				int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
				if (!GIANT_MOLE_REGIONS.contains(regionID))
				{
					log.debug("Player has left Giant Mole's Lair, Clearing KillTimer for Boss {}.", boss.getName());
					clearKillTimer(boss);
				}
				break;
			}
			case CERBERUS:
			{
				// Check the RegionID to see if player has left the boss, Yes? Remove KillTimer
				int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
				if (!CERBERUS_REGIONS.contains(regionID))
				{
					log.debug("Player has left Cerberus's Lair, Clearing KillTimer for Boss {}.", boss.getName());
					clearKillTimer(boss);
				}
				break;
			}
			case DAGANNOTH_PRIME:
			case DAGANNOTH_REX:
			case DAGANNOTH_SUPREME:
			{
				// Check the RegionID to see if player has left the boss, Yes? Remove KillTimer
				int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
				if (!DAGANNOTH_KINGS_REGIONS.contains(regionID))
				{
					log.debug("Player has left Dagannoth King's Lair, Clearing KillTimer for Boss {}.", boss.getName());
					infoBoxManager.removeIf(t -> t instanceof KillTimer);
					killTimers = null;
				}
				break;
			}
			case KRAKEN:
			{
				// Check the RegionID to see if player has left the boss, Yes? Remove KillTimer
				int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
				if (!KRAKEN_LAIR_REGIONS.contains(regionID))
				{
					log.debug("Player has left Kraken's Lair, Clearing KillTimer for Boss {}.", boss.getName());
					clearKillTimer(boss);
				}
				break;
			}
			case CORPOREAL_BEAST:
			{
				if (!config.clearCorpKillTimerOnTele())
				{
					// Check the RegionID to see if the player has left the boss and their house, Yes? Remove KillTimer
					int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
					if (!CORPOREAL_BEAST_REGIONS.contains(regionID) && !client.isInInstancedRegion())
					{
						log.debug("Player has left Corporeal Beast's Lair and their Player Owned House, Clearing KillTimer for Boss {}.", boss.getName());
						clearKillTimer(boss);
					}
				}
				else
				{
					// Check the RegionID to see if the player has left the boss, Yes? Remove KillTimer
					int regionID = client.getLocalPlayer().getWorldLocation().getRegionID();
					if (!CORPOREAL_BEAST_REGIONS.contains(regionID))
					{
						log.debug("Player has left Corporeal Beast's Lair, Clearing KillTimer for Boss {}.", boss.getName());
						clearKillTimer(boss);
					}
				}
				break;
			}
		}
	}

	private void handleBossDespawn(NPC npc, Boss boss, KillTimer killTimer)
	{
		// Don't Clear KillTimer for Giant Mole Despawn Special Case or Corp Beast Solo Method Case
		if (killTimer == null || NON_DESPAWN_HANDLED_BOSSES.contains(boss.getName()))
		{
			return;
		}

		if (npc.isDead())
		{
			killTimer.handleKillTime(interacting);
		}
		clearKillTimer(boss);
	}

	private void handleBossAnimation(NPC npc, Boss boss)
	{
		// If there is not a KillTimer for the Boss whose animation changed, set one
		if (getKillTimer(boss.getName()) == null)
		{
			switch (npc.getAnimation())
			{
				// Set Zulrah KillTimer (Jagex starts timer when Zulrah throws clouds)
				case AnimationID.ZULRAH_CLOUDS_ATTACK:
				{
					log.debug("Boss: {} Has Spawned at time: {}, AnimationID: {}", boss.getName(), Instant.now(), npc.getAnimation());
					setKillTimer(boss);
					break;
				}
				// Set Vorkath KillTimer (Jagex starts timer when Vorkath First Attacks)
				case AnimationID.VORKATH_NORMAL_ATTACK:
				case AnimationID.VORKATH_FIRE_BOMB_ATTACK:
				{
					log.debug("Boss: {} Has Spawned at time: {}, AnimationID: {}", boss.getName(), Instant.now(), npc.getAnimation());
					setKillTimer(boss);
					break;
				}
				// Set Cerberus KillTimer (CERBERUS_5863, ANIMATIONID_4486)
				case AnimationID.CERBERUS_WAKE_UP:
				{
					log.debug("Boss: {} Has Spawned at time: {}, AnimationID: {}", boss.getName(), Instant.now(), npc.getAnimation());
					setKillTimer(boss);
					break;
				}
				// Set Kraken KillTimer (ENORMOUS TENTACLE, ANIMATIONID_3860)
				case AnimationID.KRAKEN_TENTACLE_ATTACK:
				{
					log.debug("Boss: {} Has Spawned at time: {}, AnimationID: {}", boss.getName(), Instant.now(), npc.getAnimation());
					setKillTimer(boss);
					break;
				}
			}
		}
		// If the Array of KillTimers has the Boss whose animation changed, Handle Boss Despawn Animation (killTimerExists)
		else
		{
			switch (npc.getAnimation())
			{
				// Clear Cerberus KillTimer (CERBERUS_5866, ANIMATIONID_4487)
				case AnimationID.CERBERUS_RETURN_TO_SLEEP:
				{
					log.debug("Boss: {} Has Despawned at time: {}, AnimationID: {}", boss.getName(), Instant.now(), npc.getAnimation());
					clearKillTimer(boss);
					break;
				}
			}
		}
	}

	private void checkInteractingWithBoss()
	{
		// Check if a killTimerExists and the player is interacting with a Boss (For saving Local Player PB ONLY!)
		if (client.getLocalPlayer().getInteracting() != null)
		{
			// Get the actor being interacted with
			Actor actor = client.getLocalPlayer().getInteracting();

			// Verify the actor is an NPC
			if (!(actor instanceof NPC))
			{
				return;
			}

			// Get the NPC interacting with
			interacting = (NPC) client.getLocalPlayer().getInteracting();

			// Check if the NPC interacting is a boss, No? null out interacting
			if (Boss.find(interacting.getName()) == null)
			{
				interacting = null;
			}
		}
	}
}
