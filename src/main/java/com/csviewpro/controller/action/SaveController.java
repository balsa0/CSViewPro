package com.csviewpro.controller.action;

import com.csviewpro.controller.view.NotificationsController;
import com.csviewpro.service.parser.ExcelExportService;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;

/**
 * Created by Balsa on 2016. 11. 21..
 */
@Controller
public class SaveController {

	// logger
	private final static Logger log = Logger.getLogger(SaveController.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private ExcelExportService excelExportService;

	@Autowired
	private NotificationsController notificationsController;

	/**
	 * This method creates an excel export
	 */
	public void exportExcelAction(){

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Excel file");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Excel táblázat","*.xls")
		);

		// open save dialog
		File chosenFile = fileChooser.showSaveDialog(context.getBean(Stage.class));

		if(chosenFile == null)
			return;

		try {
			// show notification
			notificationsController.showExportNotification(chosenFile.getName());
			// export the file
			excelExportService.exportToExcelFile(chosenFile);
		}catch (Exception e){
			// show alert
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Nem sikerült az exportálás");
			alert.setHeaderText(chosenFile.getName());
			alert.setContentText("Hiba lépett fel a fájl exportálása közben:\r\n"
					+ e.getLocalizedMessage());
			alert.show();

			log.error("Unsuccessful excel export.",e);
		}finally {
			// hide notifications
			notificationsController.hide();
		}

		log.info("Excel export was successful: " + chosenFile.getAbsolutePath());

	}

}
