package com.csviewpro.service.parser;

import com.csviewpro.domain.model.DataSetMetaData;
import com.csviewpro.domain.model.RowData;
import com.csviewpro.service.WorkspaceDataService;
import jxl.Workbook;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Balsa on 2016. 11. 21..
 */
@Service
public class ExcelExportService {

	// logger
	private final static Logger log = Logger.getLogger(ExcelExportService.class);


	@Autowired
	private WorkspaceDataService workspaceDataService;

	public void exportToExcelFile(File outputFile) throws IOException {

		// create output stream for file
		OutputStream os = new FileOutputStream(outputFile);

		// create writable workbook
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet("Export", 0);

		// get actual points
		List<RowData> rawData = workspaceDataService.getActiveDataSet().getPoints();

		// get header descriptor
		DataSetMetaData dataSetMetaData = workspaceDataService.getActiveDataSet().getDataSetMetaData();

		// go trough every row
		for (int i = 0; i < rawData.size(); i++) {
			// get actual row
			RowData row = rawData.get(i);
			// go trough columns
			for (int j = 0; j < row.size(); j++) {
				// get cell value
				Object cellValue = row.get(j).getValue();

				// get actual column class
				Class columnClass = dataSetMetaData.getDescriptorData().get(j).getType();

				// create a cell
				WritableCell cell;
				// create
				if(Number.class.isAssignableFrom(columnClass)){
					cell = new jxl.write.Number(j, i, ((Number) cellValue).doubleValue());
				}else{
					cell = new jxl.write.Label(j, i, "" + cellValue);
				}

				// write cell value to sheet
				try {
					sheet.addCell(cell);
				}catch (WriteException e){
					log.error("Ignoring error: Could not write cell (" + j + ", " + i + ") " + cellValue, e);
				}

			}

		}

		// write the sheet
		workbook.write();

		try {
			workbook.close();
		} catch (WriteException e){
			throw new IOException("Exception while writing XLS.", e);
		}

	}

}
