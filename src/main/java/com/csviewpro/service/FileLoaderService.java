package com.csviewpro.service;

import com.csviewpro.domain.model.DataSet;
import com.csviewpro.service.parser.CsvParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Balsa on 2016. 11. 14..
 */
@Service
public class FileLoaderService {

	@Autowired
	private CsvParserService csvParserService;

	@Autowired
	private WorkspaceDataService workspaceDataService;

	/**
	 * Loads a file to workspace
	 * @param file the file to load.
	 */
	public void loadFile(File file){

		// first unload previous file
		unloadFile();

		// then parse the file we want to load
		DataSet loaded = csvParserService.parseFile(file);

		// finally load the file to the workspace
		workspaceDataService.setActiveDataSet(loaded);

	}

	/**
	 * Unloads currently active file
	 */
	public void unloadFile(){
		workspaceDataService.setActiveDataSet(null);
	}


}
