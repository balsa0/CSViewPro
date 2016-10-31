package com.csviewpro.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

/**
 * This is a helper class for storing preference keys and some static helper methods for application preferences.
 */
public class ApplicationPreferences {

	// logger
	private final static Logger log = Logger.getLogger(ApplicationPreferences.class);

	// default list separator
	private static final Character DEFAULT_LIST_SEPARATOR = '|';

	// last window position
	public static final String APP_LAST_POSITION_WIDTH = "app_last_position_width";
	public static final String APP_LAST_POSITION_HEIGHT = "app_last_position_height";
	public static final String APP_LAST_POSITION_X = "app_last_position_x";
	public static final String APP_LAST_POSITION_Y = "app_last_position_y";
	public static final String APP_LAST_POSITION_MAXIMIZED = "app_last_maximized";

	// file history and favourites
	public static final String APP_FILEHISTORY_RECENT = "app_filehistory_recent";
	public static final String APP_FILEHISTORY_FAVOURITES = "app_filehistory_favourites";

	// loader
	public static final String APP_LOADFILE_LAST_DIR = "app_loadfile_last_dir";


	/**
	 * Returns a setting value as list (items separated by DEFAULT_LIST_SEPARATOR character).
	 * @param preferences the preferences to use.
	 * @param key the key of the preference.
	 * @return list of strings stored in the preference.
	 */
	public static List<String> getAsList(Preferences preferences, String key) {
		return ApplicationPreferences.getAsList(preferences, key, DEFAULT_LIST_SEPARATOR);
	}

	/**
	 * Returns a setting value as list (items separated by character).
	 * @param preferences the preferences to use.
	 * @param key the key of the preference.
	 * @param separator the separator character.
	 * @return list of strings stored in the preference.
	 */
	public static List<String> getAsList(Preferences preferences, String key, Character separator){

		// retrieve settings
		String result = preferences.get(key, null);

		// debug
		log.debug("Raw preference list: " + result);

		// if setting is empty
		if(result == null)
			return new ArrayList<>();

		// return the setting as list - using ArrayList decorator to avoid problems caused
		// by immutable list of Arrays.asList() method.
		return new ArrayList<>(Arrays.asList(
				result.split(Pattern.quote(separator.toString()))
		));

	}

	/**
	 * Puts a specific list of strings to the given preference key. The separator used is DEFAULT_LIST_SEPARATOR.
	 * @param preferences the preferences to put the list.
	 * @param key the key to use.
	 * @param list the list of strings to put.
	 */
	public static void putAsList(Preferences preferences, String key, List<String> list){
		ApplicationPreferences.putAsList(preferences, key, list, DEFAULT_LIST_SEPARATOR);
	}

	/**
	 * Puts a specific list of strings to the given preference key. The separator given in parameter will be used..
	 * @param preferences the preferences to put the list.
	 * @param key the key to use.
	 * @param list the list of strings to put.
	 * @param separator the separator to use.
	 */
	public static void putAsList(Preferences preferences, String key, List<String> list, Character separator){
		// null check for separator
		if(separator == null)
			throw new RuntimeException("Separator used to store preference list can not be null.");
		// store concatenated preference
		preferences.put(key, StringUtils.join(list, separator));
	}

}
