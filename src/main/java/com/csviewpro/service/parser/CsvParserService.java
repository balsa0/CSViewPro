package com.csviewpro.service.parser;

import com.csviewpro.domain.exception.FileLoadingException;
import com.csviewpro.domain.model.ColumnDescriptor;
import com.csviewpro.domain.model.DataSet;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.domain.model.HeaderDescriptor;
import com.csviewpro.domain.model.enumeration.ColumnRole;
import com.csviewpro.domain.model.enumeration.GeodeticSystem;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
 * This service is responsible for loading and automatically parsing files to data sets.
 */
@Service
public class CsvParserService {

	// logger
	private final static Logger log = Logger.getLogger(CsvParserService.class);

	@Autowired
	private TypeConverterService typeConverterService;

	/**
	 * Loads and processes a given file.
	 * @param file the file to load and process.
	 */
	public DataSet parseFile(File file){

		// log entry
		log.info("Parsing file: " + file.getAbsolutePath());

		// first read an pre-process the file
		List<String[]> preProcessed = this.readAndPreProcess(file);

		// check emptyness
		if(preProcessed.size() == 0 )
			throw new FileLoadingException("A megnyitni kívánt fájl üres (0 sort tartalmaz).");

		log.debug("Detected columns: " + preProcessed.get(0).length + ", rows: " + preProcessed.size() + ".");

		// then check if first row is the header
		boolean firstRowContainsHeader = isHeader(preProcessed.get(0));

		// if header is detected, remove it from the data
		List<String> header;
		if(firstRowContainsHeader)
			header = Arrays.asList(preProcessed.remove(0));
		else
			header = new ArrayList<>();

		// display detected header
		log.debug("Detected header: " + (header.size() == 0 ? "no header" : StringUtils.join(header,",")) + ".");

		// detect number format
		Locale numberFormatLocale = detectDecimalFormatLocale(preProcessed);

		// display detected locale
		log.debug("Detected number format (locale): " + numberFormatLocale);

		// detect types
		Map<Integer, Class> columnTypes = getColumnTypes(preProcessed);

		// display detected types
		log.debug("Detected column type: " +
				StringUtils.join(
						columnTypes.values()
								.stream()
								.map(aClass -> aClass.getSimpleName())
								.collect(Collectors.toList()),
						","
				) + ".");

		// convert types of the data set
		List<Object[]> data = convertTypes(preProcessed, columnTypes);

		log.debug("Type parser finished");

		// analyze header information
		HeaderDescriptor headerDescriptor = analyzeHeader(data, columnTypes, header, numberFormatLocale);

		log.debug("Header descriptor created: " + headerDescriptor);

		// assemble the data set
		return assembleDataSet(headerDescriptor, data);

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
	private Locale detectDecimalFormatLocale(List<String[]> data){

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
	private Map<Integer, Class> getColumnTypes(List<String[]> data){

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


	/**
	 * Analyses the content of the file and creates header descriptor based on the data.
	 * @param data the content to process.
	 * @param columnTypes the detected column types.
	 * @param header the detected header titles.
	 * @param numberFormat the detected number format.
	 * @return
	 */
	private HeaderDescriptor analyzeHeader(
			List<Object[]> data,
			Map<Integer, Class> columnTypes,
			List<String> header,
			Locale numberFormat
	) {

		// regex patterns
		Pattern pNamePattern = Pattern.compile("^[A-z]{0,2}[0-9]{1,5}$");
		Pattern pCodePattern = Pattern.compile("^([A-z]|[0-9]{1,2})$");

		// collect geodetic systems to a list
		List<GeodeticSystem> geodeticSystems = Arrays.asList(GeodeticSystem.values());

		// create map for storing roles
		Set<ColumnRole> takenRoles = new HashSet<>();

		// map for calculating geodetic system
		Map<GeodeticSystem, Long> geodeticSystemMatchMap = geodeticSystems
				.stream()
				.collect(Collectors.toMap(entry -> entry, entry -> 0l));

		// descriptor data
		Map<Integer, ColumnDescriptor> descriptorData = new HashMap<>();

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

			Set<String> pCodeBag = new HashSet<>();

			// iterate over rows
			for (Object[] row : data) {

				// skip empty rows
				if(row.length == 0)
					break;

				// get cell
				Object cell = row[i];

				// skip if cell is empty or null
				if (null == cell)
					continue;

				// only check geodetic system for numeric columns
				if(columnClass == Double.class){

					// cast object to number
					Number act = (Number) cell;

					// iterate trough geodetic systems
					for(GeodeticSystem geodeticSystem : geodeticSystems){

						// check for X coordinate
						if(act.doubleValue() >= geodeticSystem.getxMin() &&
								act.doubleValue() <= geodeticSystem.getxMax()){
							pXCount++;
							// count the geodetic system
							geodeticSystemMatchMap.replace(
									geodeticSystem, geodeticSystemMatchMap.get(geodeticSystem) + 1
							);
						}

						// check for Y coordinate
						if(act.doubleValue() >= geodeticSystem.getyMin() &&
								act.doubleValue() <= geodeticSystem.getyMax()){
							pYCount++;
							// count the geodetic system
							geodeticSystemMatchMap.replace(
									geodeticSystem, geodeticSystemMatchMap.get(geodeticSystem) + 1
							);
						}

						// check for relative Z coordinate
						if(act.doubleValue() >= -50.0 &&
								act.doubleValue() <= 50.0){
							pZCount++;
						}

						// check for absolute Z coordinate
						if(act.doubleValue() >= geodeticSystem.getzMin() &&
								act.doubleValue() <= geodeticSystem.getzMax()){
							pZCount++;
							// count the geodetic system
							geodeticSystemMatchMap.replace(
									geodeticSystem, geodeticSystemMatchMap.get(geodeticSystem) + 1
							);
						}
					}

				}else if(columnClass == String.class || columnClass == Long.class){

					// cast object to string
					String act = cell.toString();

					// ignore cell if string is empty
					if("".equals(act))
						continue;

					boolean match = false;

					// check for point name
					if(pNamePattern.matcher(act).find()){
						pNameCount++;
						match = true;
					}

					// check pCodeBag count
					Integer pCodeBagSize = pCodeBag.size();

					// add item to pCode bag
					pCodeBag.add(act);

					// check for point code
					// if no value has been inserted, value is possibly a point code
					if(pCodePattern.matcher(act).find() || pCodeBagSize == pCodeBag.size()){
						pCodeCount++;
						match = true;
					}

					// no match
					if(!match)
						unknownCount++;

				}else{
					unknownCount++;
				}
			}

			// create new list for storing possible roles
			List<Long> activeList = new ArrayList<>();

			// check if specific roles have been taken
			if(!takenRoles.contains(ColumnRole.POINTNAME))
				activeList.add(pNameCount);
			if(!takenRoles.contains(ColumnRole.POINTCODE))
				activeList.add(pCodeCount);
			if(!takenRoles.contains(ColumnRole.XCOORDINATE))
				activeList.add(pXCount);
			if(!takenRoles.contains(ColumnRole.YCOORDINATE))
				activeList.add(pYCount);
			if(!takenRoles.contains(ColumnRole.ZCOORDINATE))
				activeList.add(pZCount);
			activeList.add(unknownCount);

			// evaluate results
			Long max = Arrays.asList(
				pNameCount,
				pCodeCount,
				pXCount,
				pYCount,
				pZCount,
				unknownCount)
					.stream()
					.max(Long::compareTo)
					.get();

			// get header title for column
			String title = null;
			try {
				title = header.get(i);
			}catch(IndexOutOfBoundsException e){/*Ignore*/}

			// column is point name
			if(max == pNameCount && !takenRoles.contains(ColumnRole.POINTNAME)){
				descriptorData.put(i, new ColumnDescriptor(columnClass, title, ColumnRole.POINTNAME));
				takenRoles.add(ColumnRole.POINTNAME);
			// column is a point code
			}else if(max == pCodeCount && !takenRoles.contains(ColumnRole.POINTCODE)){
				descriptorData.put(i, new ColumnDescriptor(columnClass, title, ColumnRole.POINTCODE));
				takenRoles.add(ColumnRole.POINTCODE);
			// column is a X coordinate
			}else if(max == pXCount && !takenRoles.contains(ColumnRole.XCOORDINATE)){
				descriptorData.put(i, new ColumnDescriptor(columnClass, title, ColumnRole.XCOORDINATE));
				takenRoles.add(ColumnRole.XCOORDINATE);
			// column is a Y coordinate
			}else if(max == pYCount && !takenRoles.contains(ColumnRole.YCOORDINATE)){
				descriptorData.put(i, new ColumnDescriptor(columnClass, title, ColumnRole.YCOORDINATE));
				takenRoles.add(ColumnRole.YCOORDINATE);
			// column is a Z coordinate
			}else if(max == pZCount && !takenRoles.contains(ColumnRole.ZCOORDINATE)){
				descriptorData.put(i, new ColumnDescriptor(columnClass, title, ColumnRole.ZCOORDINATE));
				takenRoles.add(ColumnRole.ZCOORDINATE);
			// column is unknown
			}else{
				descriptorData.put(i, new ColumnDescriptor(columnClass, title, ColumnRole.OTHER));
			}

		}

		// detect geodetic system
		GeodeticSystem detected = null;

		Long maxMatch = geodeticSystemMatchMap.values()
				.stream()
				.max(Long::compareTo)
				.get();

		// check witch one is detected
		for(GeodeticSystem system : geodeticSystems){

			// check if it equals max
			if(maxMatch == geodeticSystemMatchMap.get(system)){
				detected = system;
				break;
			}

		}

		// assembling header descriptor
		return new HeaderDescriptor(descriptorData, detected, numberFormat);
	}

	/**
	 * Creates a data set from a headerDescriptor and a list of arrays.
	 * @param headerDescriptor the {@link HeaderDescriptor} that stores header data.
	 * @param data the data to use.
	 * @return a new {@link DataSet} from data and headerDescriptor.
	 */
	private DataSet assembleDataSet(HeaderDescriptor headerDescriptor, List<Object[]> data){

		// create empty list for the points
		List<RowData> pointList = new LinkedList<>();

//		// reverse lookup map
//		Map<ColumnRole, Integer> lookupMap = headerDescriptor
//				.getDescriptorData().entrySet()
//				.stream()
//				// exclude other roles
//				.filter(e -> e.getValue().getRole() != ColumnRole.OTHER)
//				.collect(Collectors.toMap(
//						e -> e.getValue().getRole(),
//						e -> e.getKey()
//				));

//		// check if coordinates are available
//		if(lookupMap.get(ColumnRole.XCOORDINATE) == null || lookupMap.get(ColumnRole.YCOORDINATE) == null) {
//			log.error("X or Y coords are not defined.");
//			throw new FileLoadingException("Az X vagy Y koordináta nem felismerhető.");
//		}

		for(Object[] row : data){

			// skip if row is empty
			if(row.length == 0)
				continue;

			// create new geo point
			RowData point = new RowData();

			// try to parse coordinates
			try{
				// create additional map
				for(int i = 0; i < row.length; i++){

					// real value
					Object cell = row[i];

					// observable value
					ObservableValue propertyValue;

					// convert cell value to observable value
					if(headerDescriptor.getDescriptorData().get(i).getType().equals(String.class))
						propertyValue = new SimpleStringProperty((String) cell);
					else if(headerDescriptor.getDescriptorData().get(i).getType().equals(Long.class))
						propertyValue = new SimpleLongProperty((Long) cell);
					else if(headerDescriptor.getDescriptorData().get(i).getType().equals(Double.class))
						propertyValue = new SimpleDoubleProperty((Double) cell);
					else
						propertyValue = new SimpleStringProperty(cell.toString());

					// add value to point data
					point.put(i, propertyValue);
				}

			}catch (Exception e){
				throw new FileLoadingException("Could not load coordinates for row: "+row);
			}

			// add point to the list
			pointList.add(point);

		}

		return new DataSet(headerDescriptor, pointList);
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
