package com.csviewpro.service.filehandling;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Service
public class CsvParserService {

	// logger
	private final static Logger log = Logger.getLogger(CsvParserService.class);

	public void loadFile(File file){

		// log entry
		log.info("Loading file: "+file.getAbsolutePath());

		// first read an pre-process the file
		this.readAndPreProcess(file);
	}

	private List<String[]> readAndPreProcess(File file){

		// create configuration object
		CsvParserSettings parserSettings = new CsvParserSettings();

		// set automatic separator detection
		parserSettings.setDelimiterDetectionEnabled(true);

		// enable header extraction
		parserSettings.setHeaderExtractionEnabled(false);

		// skip empty lines
		parserSettings.setSkipEmptyLines(true);

		// ignore trailing and leading whitespaces
		parserSettings.setIgnoreLeadingWhitespaces(true);
		parserSettings.setIgnoreTrailingWhitespaces(true);

		// create parser
		CsvParser parser = new CsvParser(parserSettings);

		// return the parsed list of string arrays
		return parser.parseAll(file);

	}

}
