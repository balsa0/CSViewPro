package com.csviewpro.ui;

import com.csviewpro.ui.menu.MainMenuBar;
import com.csviewpro.ui.view.FileHistoryView;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.NotificationPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Component
public class NotificationsWrapperLayout extends NotificationPane{

	@Autowired
	MainLayout mainLayout;

	@PostConstruct
	public void init(){
		// add elements
		setContent(mainLayout);

		// disable close button
//		setCloseButtonVisible(false);

		// disable content on show
		setOnShowing(event -> {
			getContent().setDisable(true);
		});

		// enable content on hide
		setOnHiding(event -> {
			getContent().setDisable(false);
		});

		// set loading indicator as graphic
		ProgressIndicator spinner = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
		spinner.setPrefSize(25,25);

		setGraphic(spinner);
	}


}
