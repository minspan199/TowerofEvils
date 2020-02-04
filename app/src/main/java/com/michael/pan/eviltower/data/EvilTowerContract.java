package com.michael.pan.eviltower.data;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.michael.pan.eviltower.utilities.JSONUtil;

import static com.michael.pan.eviltower.utilities.GameUtil.findIndexOfStringArray;

public class EvilTowerContract {

	public static final String CONTENT_AUTHORITY = "com.michael.pan.eviltower";// CONTENTURI has better to contain only letters
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH = "eviltower";
	public static final String PATH_PRIMARY = "primary_data";
	public static final String PATH_SECONDARY = "secondary_data";
	public static final String PATH_THIRD = "third_data";

	public static final String TAG_WIZARD_BLUE = "wizard-blue";
	public static final String TAG_EXPERIENCE = "experience";
	public static final String TAG_EXPERIENCE_VISITED = "experience-visited";
	public static final String TAG_SECRET_MERCHANT = "secret-merchant";
	public static final String TAG_PRISONER = "prisoner";
	public static final String TAG_ANGEL_VISITED = "angel-visited";
	public static final String TAG_ANGEL = "angel";
	public static final String TAG_TECHNICIAN = "technician";
	public static final String TAG_KEY_MERCHANT_VISITED = "key-merchant-visited";
	public static final String TAG_KEY_MERCHANT = "key-merchant";
	public static final String TAG_GIANT_STORE = "giant-store";
	public static final String TAG_SUPER_STORE = "super-store";
	public static final String TAG_LARGE_STORE = "large-store";
	public static final String TAG_STORE = "store";
	public static final String TAG_RED_DRAGON = "red-dragon";
	public static final String TAG_GIANT_SPIDER = "giant-spider";
	public static final String TAG_TORCH_LIGHT = "torch-light";
	public static final String TAG_GOING_DOWN = "going-down";
	public static final String TAG_GOING_RIGHT = "going-right";
	public static final String TAG_GOING_LEFT = "going-left";
	public static final String TAG_GOING_UP = "going-up";
	public static final String TAG_UNKNOWN = "UNKNOWN";
	public static final String TAG_OK = "ok";
	public static final String TAG_CANCEL = "cancel";
	public static final String FRAGMENT_TAG_DIALOG_VIEW = "dialog-view";
	public static final String TAG_GOT_BLUE_KEY = "got-blue-key";
	public static final String TAG_GOT_YELLOW_KEY = "got-yellow-key";
	public static final String TAG_LEVELED_UP_ONE = "leveled-up-one";
	public static final String TAG_LEVELED_UP_FIVE = "leveled-up-five";
	public static final String TAG_LEVELED_UP_SEVEN = "leveled-up-seven";
	public static final String TAG_LEVELED_UP_TEN = "leveled-up-ten";
	public static final String TAG_GOT_RED_KEY = "got-red-key";
	public static final String TAG_GOT_KEY_BOX = "got-key-box";
	public static final String TAG_ENEMY_AFTER = "enemy-after";
	public static final String TAG_LOSE = "lose";
	public static final String TAG_WIN = "win";
	public static final String TAG_BATTLE_VIEW = "battle-view";
	public static final String TAG_START_GAME = "startGame";
	public static final String TAG_ROLLBACK = "rollback";
	public static final String TAG_FLOOR = "floor";
	public static final String TAG_UPDATE = "update";
	public static final String EXTRA_TREASURE_LIST = "treasure-list";
	public static final String EXTRA_USER_DATA_JSON = "user-data";
	public static final String TAG_NONZERO = "NONZERO";
	public static final String JSON_TAG_DATA = "data";
	public static final String JSON_TAG_NAME = "name";
	public static final String TAG_BACKGROUND = "background";
	public static final String TAG_NPCS = "npcs";
	public static final String TAG_ENEMIES = "enemies";
	public static final String TAG_TREASURES = "treasures";
	public static final String JSON_TAG_LAYERS = "layers";
	public static final String URL_ABOUT_AUTHOR_HTML = "file:///android_asset/about_author/about_author/index.html";
	public static final String URL_ABOUT_GAME_HTML = "file:///android_asset/about_game/about_game/index.html";
	public static final String URL_GAME_RULES_HTML = "file:///android_asset/game_rules/game_rules/index.html";
	public static final String EXTRA_USER_POSITION = "user_position";
	public static final String TAG_DETAILS = "details";
	public static final String TAG_DELETE = "delete";
	public static final String TAG_RESUME = "resume";
	public static final String TAG_NONAME = "NONAME";
	public static final String TAG_REAL_PRINCESS = "real-princess";
	public static final String TAG_CRYSTAL_EXIT = "crystal-exit";
	public static final String TAG_GAME_OVER = "game-over";
	public static final String TAG_SNAPSHOT_IN_THE_GAME = "in-the-game";
	public static final String TAG_AFTER_THE_GAME = "after-the-game";
	public static final String FRAGMENT_TAG_STORE_VIEW = "store-view";
	public static final String FRAGMENT_TAG_ENEMY_LIST = "enemy-list";
	public static final String ERROR_MAXIMUM_LEVEL_ONE = "maximum-level-reached-one";
	public static final String ERROR_MAXIMUM_LEVEL_SEVEN = "maximum-level-reached-one-seven";
	public static final String ERROR_MAXIMUM_LEVEL_FIVE = "maximum-level-reached-one-five";
	public static final String IF_NEW_USER = "if-new-user";
	public static final String KEY_X_TOUCH = "xTouch";
	public static final String KEY_Y_TOUCH = "yTouch";
	public static final String KEY_MATRIX_VALUE = "value";
	public static final String TAG_DIFFICULTY_EASY = "Easy";
	public static final String TAG_DIFFICULTY_MEDIUM = "Medium";
	public static final String TAG_DIFFICULTY_HARD = "Hard";
	public static final String EXTRA_DIFFICULTY = "difficulty-extra";
	public static final String TAG_USER_ROW_UPDATE = "user-image-update";
	public static final String TAG_SAVE_USER_DATA = "save-user-data";
	public static final String TAG_INSERT_NEW_USER = "insert-new-user";
	public static final String TAG_RELOAD_GAME = "reload-game";
	public static final String TAG_SAVE_GAME = "save-game";
	public static final String EXTRA_SAVING_SLOT = "extra-saving-slot";
	public static final String EXTRA_GAME_FINISHED = "extra-game-finished";
	public static final String EXTRA_SCORE = "extra-score";
	public static final String ARG_SECTION_NUMBER = "section_number";
	public static final String TAG_EMPTY = "EMPTY";
	public static final String TAG_GAME_COMPLETING = "tag-game-completing";
	public static final String TAG_CROWN = "tag-crown";
	public static final String EXTRA_STATISTICS = "extra-statistics";
	public static final String EXTRA_LAN_SETTINGS = "extra-lan-settings";
	public static final String TAG_NEW_USER = "new-user";
	public static final String TAG_EMPTY_LIST = "empty-list";
	public static final String USER_ICON_TRANSITION = "user-icon-transition";
	public static final String RESUME_BUTTON_TRANSITION = "resume-button-transition";
	public static final String DETAILS_BUTTON_TRANSITION = "details-button-transition";
	public static final String DIFFICULTY_TEXT_TRANSITION = "difficulty-text-transition";
	public static final String USER_NAME_TRANSITION = "user-name-transition";

	public static final class EvilTowerEntry implements BaseColumns {

		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();
		//Insert Property Step 1
		public static final String COLUMN_USERDATA_JSON = "user_data_json";
		public static final String PRIMARY_TABLE_NAME = "evil_tower_primary_data";
		public static final String SECONDARY_TABLE_NAME = "evil_tower_secondary_data";
		public static final String THIRD_TABLE_NAME = "evil_tower_third_data";
		public static final String COMPLETION_TABLE_NAME = "evil_tower_third_data";
		public static final String COLUMN_UPDATE_TIME = "record_time";
		public static final String COLUMN_NAME = "user_name";
		public static final String COLUMN_ID = "user_id";
		public static final String COLUMN_LEVEL = "level";
		public static final String COLUMN_ATTACK = "attack";
		public static final String COLUMN_DEFENSE = "defense";
		public static final String COLUMN_ENERGY = "energy";
		public static final String COLUMN_FLOOR = "floor";
		public static final String COLUMN_POSITION_X = "positionX";
		public static final String COLUMN_POSITION_Y = "positionY";
		public static final String COLUMN_EXPERIENCE = "experience";
		public static final String COLUMN_TREASURES_JSON = "treasures_json";
		public static final String COLUMN_MAP_JSON = "map_json";
		public static final String COLUMN_USER_ICON = "user_icon";
		public static final String COLUMN_IF_PROLOGUE = "if-first-time-starting";
		public static final String COLUMN_DATA_LOCATION = "data-slot";
		public static final String COLUMN_COMPLETION = "game-completed";
		public static final String LOCATION_PRIMARY = "primary-slot";
		public static final String LOCATION_SECONDARY = "secondary-slot";
		public static final String LOCATION_THIRD = "third-slot";
		public static final String COLUMN_FLY_WING_DATA = "fly-wing-data";
		public static final String COLUMN_DIFFICULTY = "difficulty-of-game";
		public static final String COLUMN_SCORE = "game-score";

		public static final String EXTRA_USER_ID = "extra-user-id";
		public static final String EXTRA_USER_DATA = "extra-user-data";
		public static final String EXTRA_USER_NAME = "extra-user-name";
		public static final String EXTRA_TREASURE_JSON_STRING = "treasure-list-JSON";
		public static final String EXTRA_POSITION_ID = "treasure-iconClicked-idOfCOLUMN_ID";
		public static final String EXTRA_COIN = "treasure-iconClicked-idOfCOLUMN_ID";
		public static final String EXTRA_USER_DATA_JSON_STRING = "user-data-JSON";
		public static final String EXTRA_MAP_JSON_STRING = "map-data-extras";
		public static final String EXTRA_URI = "uri-extras";
		public static final String EXTRA_BOOL_NEW_GAME = "extra-boolean";
		public static final String EXTRA_CLICKED_NEW_AT_MAIN = "new-button-iconClicked";
		public static final String EXTRA_CLICKED_LOAD_AT_MAIN = "load-button-iconClicked";
		public static final String EXTRAS_EVENT_TYPE = "extra-string-for-fragments";
		public static final String EXTRA_NPC_TYPE = "npc-type-extra";

		public static final int INDEX_ATTACK_LIVEDATA = 0;
		public static final int INDEX_DEFENSE_LIVEDATA = 1;
		public static final int INDEX_FLOOR_LIVEDATA = 2;
		public static final int INDEX_ENERGY_LIVEDATA = 3;
		public static final int INDEX_EXPERIENCE_LIVEDATA = 4;
		public static final int INDEX_POSITION_X_LIVEDATA = 5;
		public static final int INDEX_POSITION_Y_LIVEDATA = 6;
		public static final int INDEX_LEVEL_LIVEDATA = 7;
		public static final int INDEX_R_KEY_LIVEDATA = 8;
		public static final int INDEX_B_KEY_LIVEDATA = 9;
		public static final int INDEX_Y_KEY_LIVEDATA = 10;
		public static final int INDEX_G_KEY_LIVEDATA = 11;
		public static final int INDEX_COINS_LIVEDATA = 12;
		public static final int INDEX_ICON_LIVEDATA = 13;
		public static final int INDEX_DIFFICULTY_LIVEDATA = 14;

		public static final int FLY_MESSAGE_FLY_IN = 0;
		public static final int DELETE_CONFIRM_REQUEST = 392;

		public static final String KEY_USER_NAME = "key-user-name";
		public static final String KEY_USER_ID = "key-user-id";
		public static final String KEY_USER_NUMBER = "key-user-number";

		public static final String SCHEDULE_COIN_BONUS_WORK_NAME = "coin-bonus-work-name";

		public static final String ID_NOTIFICATION_CHANNEL = "game-notification-channel-id";
		public static final int ID_USER_DETAIL_LOADER = 995;
		public static final String STATUS_LOADING = "loading";
		public static final String STATES_UPSTAIRS = "upstairs";
		public static final String STATES_DOWNSTAIRS = "downstairs";
		public static final String STATES_NO_PATH = "No path";
		public static final String STATES_CANCEL_FLY_MESSAGE = "cancel-fly-message";
		public static final String STATES_NPC_EVENT = "npc";
		public static final String STATES_ENEMY_EVENT = "enemy";
		public static final String STATES_DOOR_EVENT = "door";
		public static final String STATES_TREASURE_EVENT = "treasures";
		public static final String STATES_SPECIAL_DOOR_EVENT = "special-door";

		public static final String[] floorName = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
			"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"30 Balcony", "31", "32", "33", "33R", "34", "34 Left Closet",
			"34 South Balcony", "35", "35 North Balcony", "B3", "B2", "B1"
		};
		public static final String[] floorNameZhCn = new String[]{"0层", "1层", "2层", "3层", "4层", "5层", "6层", "7层", "8层", "9层", "10层",
			"11层", "12层", "13层", "14层", "15层", "16层", "17层", "18层", "19层", "20层",
			"21层", "22层", "23层", "24层", "25层", "26层", "27层", "28层", "29层", "30层",
			"30层阳台", "31层", "32层", "33层", "33层右侧", "34层", "34层左侧",
			"34层南阳台", "35层", "35层北阳台", "附3层", "附2层", "附1层"
		};
		public static final String[] floorNameZhHk = new String[]{"0樓", "1樓", "2樓", "3樓", "4樓", "5樓", "6樓", "7樓", "8樓", "9樓", "10樓",
			"11樓", "12樓", "13樓", "14樓", "15樓", "16樓", "17樓", "18樓", "19樓", "20樓",
			"21樓", "22樓", "23樓", "24樓", "25樓", "26樓", "27樓", "28樓", "29樓", "30樓",
			"30樓陽臺", "31樓", "32樓", "33樓", "33樓右側", "34樓", "34樓左側",
			"34樓南陽臺", "35樓", "35樓北陽臺", "負3樓", "負2樓", "負1樓"
		};

		public static final Integer[] scrollInfo4Floors = new Integer[]{0, 12, 19, 20, 21, 25, 27, 37, 40, 41};
		public static final String ERROR_NO_BLUE_KEY = "no-blue-key";
		public static final String ERROR_LOW_EXPERIENCE = "insufficient-experience-cancel";
		public static final String ERROR_LOW_COIN = "insufficient-money-cancel";
		public static final String TAG_SOLD_BLUE_KEY = "sold-blue-key";
		public static final String TAG_ENEMY_BEFORE = "enemy-before";
		/*
		Insert Property Step 2
		public static final String[] USER_LOADER_PROJECTION =
			new String[]{_ID, COLUMN_NAME, COLUMN_UPDATE_TIME, COLUMN_ID, COLUMN_LEVEL,
				COLUMN_ATTACK, COLUMN_DEFENSE, COLUMN_ENERGY, COLUMN_FLOOR, COLUMN_POSITION_X,
				COLUMN_POSITION_Y, COLUMN_EXPERIENCE, COLUMN_TREASURES_JSON};
*/
		public static final String[] USER_LOADER_PROJECTION = new String[]{_ID, COLUMN_NAME, COLUMN_ID, COLUMN_LEVEL, COLUMN_UPDATE_TIME,
			COLUMN_USERDATA_JSON, COLUMN_TREASURES_JSON, COLUMN_MAP_JSON};
		//Insert Property Step 3
		public static final int INDEX_ID_IN_DATABASE = 0;
		/*

				public static final int INDEX_USER_ATTACK = findIndexOfStringArray(COLUMN_ATTACK);
				public static final int INDEX_USER_DEFENSE = findIndexOfStringArray(COLUMN_DEFENSE);
				public static final int INDEX_USER_ENERGY = findIndexOfStringArray(COLUMN_ENERGY);
				public static final int INDEX_USER_FLOOR = findIndexOfStringArray(COLUMN_FLOOR);
				public static final int INDEX_USER_POSITION_X = findIndexOfStringArray(COLUMN_POSITION_X);
				public static final int INDEX_USER_POSITION_Y = findIndexOfStringArray(COLUMN_POSITION_Y);
				public static final int INDEX_USER_EXPERIENCE = findIndexOfStringArray(COLUMN_EXPERIENCE);
		*/
		public static final int INDEX_RECORD_TIME = findIndexOfStringArray(COLUMN_UPDATE_TIME);
		public static final int INDEX_USER_LEVEL = findIndexOfStringArray(COLUMN_LEVEL);
		public static final int INDEX_USER_ID = findIndexOfStringArray(COLUMN_ID);
		public static final int INDEX_USER_NAME = findIndexOfStringArray(COLUMN_NAME);
		public static final int INDEX_USER_DATA = findIndexOfStringArray(COLUMN_USERDATA_JSON);
		public static final int INDEX_USER_TREASURES = findIndexOfStringArray(COLUMN_TREASURES_JSON);
		public static final int INDEX_USER_MAP = findIndexOfStringArray(COLUMN_MAP_JSON);

		public static final String HAVE_SENT_NOTIFICATION = "notification-sent";

		//Insert Property Step 4

		static String getDefaultValues(Context context, String s) {
			String s1;
			switch (s) {
				case COLUMN_LEVEL:
					s1 = "0";
					break;
				case COLUMN_UPDATE_TIME:
					s1 = String.valueOf(System.currentTimeMillis());
					break;
				case COLUMN_USERDATA_JSON:
					s1 = JSONUtil.getDefaultUserDataString(context);
					break;
				case COLUMN_TREASURES_JSON:
					s1 = JSONUtil.getDefaultTreasuresString(context);
					break;
				case COLUMN_MAP_JSON:
					MapData mapData = new MapData(context, null);
					s1 = mapData.jsonObject.toString();
					break;
				default:
					s1 = "ERROR";
			}
			return s1;
		}

		public static Uri buildUriByUserId(String id, int code) {
			switch (code) {
				case 0:
					return CONTENT_URI.buildUpon().appendPath(PATH_PRIMARY).appendPath(id).build();
				case 1:
					return CONTENT_URI.buildUpon().appendPath(PATH_SECONDARY).appendPath(id).build();
				case 2:
					return CONTENT_URI.buildUpon().appendPath(PATH_THIRD).appendPath(id).build();
				case 3:
					return CONTENT_URI.buildUpon().appendPath(PATH).appendPath(id).build();
			}
			return CONTENT_URI.buildUpon().appendPath(PATH).build();
		}
	}
}
