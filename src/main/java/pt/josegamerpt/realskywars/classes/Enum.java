package pt.josegamerpt.realskywars.classes;

public class Enum {

	public enum PlayerState {
		LOBBY_OR_NOGAME, CAGE, PLAYING, SPECTATOR, EXTERNAL_SPECTATOR
	}

	public enum GameState {
		AVAILABLE, STARTING, WAITING, PLAYING, FINISHING, RESETTING
	}

	public enum GameType {
		SOLO, TEAMS
	}
	
	public enum CageType {
		SOLO, TEAMS
	}

	public enum InteractionState {
		NONE, GUI_ROOMSETUP, GUI_ROOMSET, GUI_PLAYER, GUI_CHESTCONTENTS, GUI_CHESTMENU, GUI_CHESTTIER
	}

	public enum TierType {
		BASIC, NORMAL, OP, CAOS
	}
	
	public enum Categories {
		CAGEBLOCK, BOWPARTICLE, KITS, WINBLOCKS
	}
	
	public enum Selection {
		MAPVIEWER
	}
	
	public enum Selections {
		MAPV_SPECTATE, MAPV_AVAILABLE, MAPV_STARTING, MAPV_WAITING, MAPV_ALL
	}

	public enum TS {
		PLAYER_JOIN_ARENA, ARENA_CANCEL, ARENA_START_COUNTDOWN, LOBBY_TELEPORT, MATCH_LEAVE, PLAYER_LEAVE, MATCH_END, TITLE_WIN, MATCH_SPECTATE, TITLE_DEATHMATCH,
		CMD_NOPERM, CONFIG_RELOAD, ALREADY_IN_MATCH, CMD_COINS, NO_SETUPMODE, CMD_MATCH_CANCEL, CMD_MATCH_FORCESTART, NO_MATCH, LOBBY_SET, SETUP_NOT_FINISHED, CMD_MAPS, CMD_PLAYERS, CMD_FINISHSETUP, NOMAP_FOUND, NO_TIER_FOUND, TIER_SET, CHEST_BASIC, CHEST_NORMAL, CHEST_OP, CHEST_CAOS, SET_TIER, ADD_TIER, NO_PLAYER_FOUND, MAP_UNREGISTERED, MAP_EXISTS, LOBBYLOC_NOT_SET, INSUFICIENT_COINS, CMD_NOT_FOUND, CAGES_SET, ADDED_COINS, REMOVED_COINS, SET_COINS, SENDER_COINS, RECIEVER_COINS, LANGUAGE_SET, GENERATING_WORLD, NO_ARENA_BOUNDARIES, SAVING_ARENA, ARENA_REGISTERED, CHEST_VOTE, CHEST_ALREADY_VOTED, GAME_STATUS_SET, ARENA_RESET, MAP_RESET_DONE, SHOP_BUY, SHOP_ALREADY_BOUGHT, SHOP_NO_PERM, PROFILE_SELECTED, NOT_BUYABLE, NO_KIT_FOUND, DEL_PURCHASES, CAGEBLOCK, KITS, CMD_CANT_FORCESTART, SCOREBOARD_LOBBY_TITLE, SCOREBOARD_CAGE_TITLE, SCOREBOARD_SPECTATOR_TITLE, SCOREBOARD_PLAYING_TITLE, ITEMS_MAP_NOTFOUND_TITLE, ITEMS_MAP_TITLE, MAP_ALL, MAP_WAITING, MAP_SPECTATE, MAP_STARTING, MAP_AVAILABLE, MAP_PLAYING, MAP_FINISHING, MAP_RESETTING, COMPASS_TELEPORT, BOWPARTICLE, WINBLOCK, TEAM_LEAVE, TEAM_JOIN, TEAMMATE_DAMAGE_CANCEL, WINNER_BROADCAST, DELETEKIT_DONE
	}

    public enum TSsingle {
        BOSSBAR_ARENA_RUNTIME, BOSSBAR_ARENA_STARTING, BOSSBAR_ARENA_END, BOSSBAR_ARENA_WAIT, BOSSBAR_ARENA_DEATHMATCH
    }

    public enum TL {
        ARENA_START, END_LOG, SCOREBOARD_LOBBY_LINES, SCOREBOARD_CAGE_LINES, SCOREBOARD_SPECTATOR_LINES, SCOREBOARD_PLAYING_LINES, ITEMS_MAP_DESCRIPTION, TITLE_ROOMJOIN, INITSETUP_ARENA
    }

	public enum TrailType {
		BOW, WINBLOCK
	}
}
