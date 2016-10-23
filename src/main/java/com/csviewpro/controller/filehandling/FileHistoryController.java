package com.csviewpro.controller.filehandling;

import com.csviewpro.persistence.ApplicationPreferences;
import com.csviewpro.ui.view.FileHistoryView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Controller
public class FileHistoryController {

	// logger
	private final static Logger log = Logger.getLogger(FileHistoryController.class);

	@Autowired
	ApplicationContext applicationContext;

	// preferences
	private Preferences fileHistoryPreferences;

	/**
	 * Init
	 */
	@PostConstruct
	public void init(){
		// load preferences
		fileHistoryPreferences = Preferences.userNodeForPackage(FileHistoryController.class);
	}

	/**
	 * Reads and returns most recent files from file history preference.
	 * @return file history stored in application settings.
	 */
	public List<File> getRecentFiles(){
		return getFileList(ApplicationPreferences.APP_FILEHISTORY_RECENT);
	}

	/**
	 * Reads and returns fav-ed files from file history/favourites preference.
	 * @return file history stored in application settings.
	 */
	public List<File> getFavourites(){
		return getFileList(ApplicationPreferences.APP_FILEHISTORY_FAVOURITES);
	}

	/**
	 * Returns a file list from a setting that stores list of file names.
	 * @param key the key of the setting.
	 * @return list of files.
	 */
	private List<File> getFileList(String key){
		// return filename list mapped to list of files
		return ApplicationPreferences.getAsList(fileHistoryPreferences, key)
				.stream()
				.map(fileName -> new File(fileName))
				.collect(Collectors.toList());
	}

	/**
	 * Adds a new entry to the list of favourites.
	 * @param file the file to add.
	 */
	public void addFavourite(File file){
		addFavourite(file.getAbsolutePath());
	}

	/**
	 * Adds a new entry to the list of favourites.
	 * @param filePath the absolute path of the file to add.
	 */
	public void addFavourite(String filePath){

		// get fav list
		List<String> favList = ApplicationPreferences.getAsList(
				fileHistoryPreferences,
				ApplicationPreferences.APP_FILEHISTORY_FAVOURITES
		);

		// add new item to the first place
		favList.add(0, filePath);

		// convert list to set, then to list again to remove duplications
		List<String> newFavs = new ArrayList<>(new LinkedHashSet<>(favList));

		// update the list
		ApplicationPreferences.putAsList(
				fileHistoryPreferences,
				ApplicationPreferences.APP_FILEHISTORY_FAVOURITES,
				newFavs
		);

		// update view
		applicationContext.getBean(FileHistoryView.class).updateContent();

		log.info("Added new favourite: "+filePath);
	}

	/**
	 * Adds new entry to the list of the recent files.
	 * @param file the file to add.
	 */
	public void addRecentFile(File file){
		addRecentFile(file.getAbsolutePath());
	}

	/**
	 * Adds new entry to the list of the recent files.
	 * @param filePath the path of the file to add.
	 */
	public void addRecentFile(String filePath){

		// get fav list
		List<String> recentList = ApplicationPreferences.getAsList(
				fileHistoryPreferences,
				ApplicationPreferences.APP_FILEHISTORY_RECENT
		);

		// add new item to the first place
		recentList.add(0, filePath);

		// convert list to set, then to list again to remove duplications
		List<String> newRecents = new ArrayList<>(new LinkedHashSet<>(recentList));

		// if reference list is too long, limit the list size
		if(newRecents.size() > 25){
			// remove items from the end of the list (>25)
			for(int i = newRecents.size(); i < 25; i-- ){
				newRecents.remove(i);
			}
		}

		// update the list
		ApplicationPreferences.putAsList(
				fileHistoryPreferences,
				ApplicationPreferences.APP_FILEHISTORY_RECENT,
				newRecents
		);

		// update view
		applicationContext.getBean(FileHistoryView.class).updateContent();


		log.info("Added recent file: "+filePath);
	}

}
