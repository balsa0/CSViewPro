package com.csviewpro.service.filehandling;

import com.csviewpro.domain.exception.FileLoadingException;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		List<String[]> preProcessed = this.readAndPreProcess(file);

		// check emptyness
		if(preProcessed.size() == 0 )
			throw new FileLoadingException("A megnyitni kívánt fájl üres (0 sort tartalmaz).");

		log.info("Detected columns: " + preProcessed.get(0).length + ", rows: " + preProcessed.size() + ".");

		// then check if first row is the header
		boolean firstRowContainsHeader = isHeader(preProcessed.get(0));

		// if header is detected, remove it from the data
		List<String> header;
		if(firstRowContainsHeader)
			header = Arrays.asList(preProcessed.remove(0));
		else
			header = new ArrayList<>();

		// display detected header
		log.info("Detected header: " + (header.size() == 0 ? "no header" : StringUtils.join(header,",")) + ".");

		// detect number format
		Locale numberFormatLocale = detectDecimalFormatLocale(preProcessed);

		// display detected locale
		log.info("Detected number format (locale): " + numberFormatLocale);

		// detect types
		Map<Integer, Class> columnTypes = getColumnTypes(preProcessed, numberFormatLocale);

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

	/**
	 * Checks if row is header or not based on some character counting statistic.
	 * @param rowData row data.
	 * @return true if the row is a header row, false otherwise.
	 */
	private boolean isHeader(String[] rowData) {

		// join row to a long string
		String row = StringUtils.join(Arrays.asList(rowData), "");

		// store original length
		long originalLength = row.length();

		// remove numbers and other special characters
		row = row.replaceAll("([0-9]|[.,])", "");

		// if ratio of new and original length is greater than 80%, return true.
		return (double) row.length() / (double) originalLength >= 0.8;

	}

	public Locale detectDecimalFormatLocale(List<String[]> data){

		Pattern periodPattern = Pattern.compile("[0-9][.][0-9]");
		Pattern commaPattern = Pattern.compile("[0-9][,][0-9]");

		// detected '.'
		long detectedPeriodCount = 0l;
		// detected ','
		long detectedCommaCount = 0l;

		// select max 100 as sample data for detection
		for (int i = 0; i < data.size() /*&& i < 100*/; i++){

			String[] row = data.get(i);

			// iterate trough columns
			for (int j = 0; j < row.length; j++){

				String cell = row[j];

				Matcher periodMatcher = periodPattern.matcher(cell);
				Matcher commaMatcher = commaPattern.matcher(cell);

				while (periodMatcher.find())
					detectedPeriodCount++;

				while (commaMatcher.find())
					detectedCommaCount++;
			}
		}

		// return detected locale depending on counted chars
		return detectedCommaCount > detectedPeriodCount ? Locale.FRANCE : Locale.ENGLISH;
	}

//	public Map<Integer, Class> getColumnTypes(List<String[]> data, Locale numberFormatLocale){
//
//		// create type map
//		Map<Integer, Class> resolvedType = new HashMap<>();
//
//		// iterate over columns
//		for(int i = 0; i < data.get(0).length; i++){
//
//			long digitCount = 0;
//			long characterCount = 0;
//			long dateCount = 0;
//
//			// iterate over rows
//			for (String[] row : data){
//
//				// get cell
//				String cell = row[i];
//
//			}
//
//		}
//
//	}



}
