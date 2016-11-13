package com.csviewpro.service;

import com.csviewpro.domain.exception.FileLoadingException;
import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Service
public class CsvParserService {

	// logger
	private final static Logger log = Logger.getLogger(CsvParserService.class);

	@Autowired
	TypeConverterService typeConverterService;

	/**
	 * Loads and processes a given file.
	 * @param file the file to load and process.
	 */
	public void loadFile(File file){

		// log entry
		log.info("Loading file: " + file.getAbsolutePath());

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
		Map<Integer, Class> columnTypes = getColumnTypes(preProcessed);

		// display detected types
		log.info("Detected column type: " +
				StringUtils.join(
						columnTypes.values()
								.stream()
								.map(aClass -> aClass.getSimpleName())
								.collect(Collectors.toList()),
						","
				) + ".");

		// convert types of the data set
		List<Object[]> data = convertTypes(preProcessed, columnTypes);

		log.info("Type conversion finished");

	}

	/**
	 * Reads and pre-processes a file (detect delimiter, encoding, skip lines)
	 * @param file the file to open an pre-process.
	 * @return List of rows as String array.
	 */
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

		// empty string as empty value
		parserSettings.setEmptyValue("");

		parserSettings.setReadInputOnSeparateThread(true);

		// create parser
		CsvParser parser = new CsvParser(parserSettings);

		// return the parsed list of string arrays
		return parser.parseAll(file, "UTF-8");

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

	/**
	 * This method detects number format locale (delimiter '.'/',').
	 * @param data the dataSet.
	 * @return returns a {@link Locale} based on the number format of dataset.
	 */
	public Locale detectDecimalFormatLocale(List<String[]> data){

		Pattern periodPattern = Pattern.compile("[0-9][.][0-9]");
		Pattern commaPattern = Pattern.compile("[0-9][,][0-9]");

		// detected '.'
		long detectedPeriodCount = 0l;
		// detected ','
		long detectedCommaCount = 0l;

		// select max 100 as sample data for detection
		for (int i = 0; i < data.size() && i < 100; i++){

			String[] row = data.get(i);

			// iterate trough columns
			for (int j = 0; j < row.length; j++){

				String cell = row[j];

				// skip if cell is empty or null
				if(null == cell || cell.isEmpty())
					continue;

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

	/**
	 * This method detects column types for the given dataSet. Detected types are: Long, Double, Date, String.
	 * @param data the dataSet given.
	 * @return map of column types in the following format (Index, Type).43
	 * 0
	 */
	public Map<Integer, Class> getColumnTypes(List<String[]> data){

		// create type map
		Map<Integer, Class> resolvedTypes = new HashMap<>();

		// regex patterns
		Pattern longPattern = Pattern.compile("^[0-9]+$");
		Pattern doublePattern = Pattern.compile("^[0-9]+([,.][0-9]+)$");

		// iterate over columns
		for(int i = 0; i < data.get(0).length; i++){

			long doubleCount = 0;
			long longCount = 0;

			// the minimum required detections
			long minimumLevel = data.size() - 1;

			// iterate over rows
			for (String[] row : data){

				// get cell
				String cell = row[i];

				// skip if cell is empty or null
				if(null == cell || cell.isEmpty())
					continue;

				// check for double
				if(doublePattern.matcher(cell).find()){
					doubleCount++;
				// check for long
				}else if(longPattern.matcher(cell).find()){
					longCount++;
				}
			}

			// evaluate results for column
			if(doubleCount == NumberUtils.max(doubleCount, longCount)
					&& doubleCount >= minimumLevel
			){
				// resolved type is double
				resolvedTypes.put(i, Double.class);

			}else if(longCount == NumberUtils.max(doubleCount, longCount)
					&& longCount >= minimumLevel
			){
				// resolved type is long
				resolvedTypes.put(i, Long.class);

			}else{
				// resolved type is a generic string
				resolvedTypes.put(i, String.class);

			}
		}

		return resolvedTypes;
	}



	public Map<Integer, ColumnRole> detectColumnRoles(List<String[]> data, Map<Integer, Class> columnTypes) {

		// regex patterns
		Pattern pNamePattern = Pattern.compile("^[A-z]{0,2}[0-9]{1,5}$");
		Pattern pCodePattern = Pattern.compile("^([A-z]?[0-9]{1,2})$");

		// collect geodetic systems to a list
		List<GeodeticSystem> geodeticSystems = Arrays.asList(GeodeticSystem.values());

		// create map for storing result
		Map<Integer, ColumnRole> result = new HashMap<>();

		// iterate over columns
		for(int i = 0; i < data.get(0).length; i++) {

			// get column class from map
			Class columnClass = columnTypes.get(i);

			// reset counts
			long pNameCount = 0;
			long pCodeCount = 0;
			long pXCount = 0;
			long pYCount = 0;
			long pZCount = 0;
			long unknownCount = 0;

			// iterate over rows
			for (String[] row : data) {

				// get cell
				String cell = row[i];

				// skip if cell is empty or null
				if (null == cell || cell.isEmpty())
					continue;



			}
		}



		return null;
	}

	/**
	 * This function converts data set of strings to the given types.
	 * @param data the data set tot convert.
	 * @param columnTypes the map describing the column types.
	 * @return the input data set with the converted types.
	 */
	public List<Object[]> convertTypes(List<String[]> data, Map<Integer, Class> columnTypes){

		List<Object[]> result = new ArrayList<>();

		// parse every row
		for (String[] row : data){
			result.add(convertRowTypes(row, columnTypes));
		}

		return result;
	}

	/**
	 * This function converts an array of strings (a data set row) to the correct types.
	 * @param data the input array as strings.
	 * @param columnTypes the map describing the column types.
	 * @return the input data set with the converted types.
	 */
	public Object[] convertRowTypes(String[] data, Map<Integer, Class> columnTypes){

		// create an equally sized array of objects
		Object[] result = new Object[data.length];

		// convert every cell
		for(int i = 0; i < data.length; i++){

			result[i] = typeConverterService.convertTo(
					columnTypes.get(i),
					data[i]
			);
		}

		// return converted row
		return result;
	}



}
