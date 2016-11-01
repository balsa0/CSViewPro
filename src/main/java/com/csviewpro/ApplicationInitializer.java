package com.csviewpro;

import com.csviewpro.config.ApplicationConfiguration;
import com.csviewpro.domain.ApplicationPreferences;
import com.csviewpro.ui.MainScene;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.prefs.Preferences;

public class ApplicationInitializer extends Application {

	public static final String APPLICATION_NAME = "CSView Pro";

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// create application context
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

		// register primary stage as a bean
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
		beanFactory.registerSingleton(primaryStage.getClass().getCanonicalName(), primaryStage);

		primaryStage.setTitle(APPLICATION_NAME);
		primaryStage.setMinHeight(300);
		primaryStage.setMinWidth(400);

		// setting icon for application
		primaryStage.getIcons().addAll(
				new Image(getClass().getClassLoader().getResourceAsStream( "icons/icon.png" )),
				new Image(getClass().getClassLoader().getResourceAsStream( "icons/icon.ico" ))
		);

		// init preferences
		Preferences preferences = Preferences.userNodeForPackage(ApplicationInitializer.class);

		// retrieve window size and pos preferences
		Double window_x = preferences.getDouble(ApplicationPreferences.APP_LAST_POSITION_X, 100);
		Double window_y = preferences.getDouble(ApplicationPreferences.APP_LAST_POSITION_Y, 100);
		Double window_width = preferences.getDouble(ApplicationPreferences.APP_LAST_POSITION_WIDTH, 500);
		Double window_height = preferences.getDouble(ApplicationPreferences.APP_LAST_POSITION_HEIGHT, 400);
		Boolean window_maximized = preferences.getBoolean(ApplicationPreferences.APP_LAST_POSITION_MAXIMIZED, false);

		// set window size and pos
		primaryStage.setX(window_x);
		primaryStage.setY(window_y);
		primaryStage.setWidth(window_width);
		primaryStage.setHeight(window_height);
		primaryStage.setMaximized(window_maximized);

		// setup app close event
		primaryStage.setOnCloseRequest(event -> closeAppHandler(event, primaryStage, preferences));

		// set main scene
		primaryStage.setScene(context.getBean(MainScene.class));

		// show the application
		primaryStage.show();

	}

	public void closeAppHandler(Event event, Stage stage, Preferences preferences){

		// save if the app has been maximized
		preferences.putBoolean(ApplicationPreferences.APP_LAST_POSITION_MAXIMIZED, stage.isMaximized());

		// if window is not full screen
		if(stage.isMaximized()) {
			stage.setMaximized(false);
		}

		// update last position preferences
		preferences.putDouble(ApplicationPreferences.APP_LAST_POSITION_X, stage.getX());
		preferences.putDouble(ApplicationPreferences.APP_LAST_POSITION_Y, stage.getY());
		preferences.putDouble(ApplicationPreferences.APP_LAST_POSITION_WIDTH, stage.getWidth());
		preferences.putDouble(ApplicationPreferences.APP_LAST_POSITION_HEIGHT, stage.getHeight());

	}

}

