package com.csviewpro;

import com.csviewpro.config.ApplicationConfiguration;
import com.csviewpro.config.UiConfiguration;
import com.csviewpro.ui.MainScene;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationInitializer extends Application {

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// create application context
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		UiConfiguration uiConfiguration = context.getBean(UiConfiguration.class);

		// register primary stage as a bean
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) context).getBeanFactory();
		beanFactory.registerSingleton(primaryStage.getClass().getCanonicalName(), primaryStage);

		primaryStage.setTitle("CSView Pro");
		primaryStage.setMinHeight(300);
		primaryStage.setMinWidth(400);

		// setting icon for application
		primaryStage.getIcons().addAll(
				new Image(getClass().getClassLoader().getResourceAsStream( "icons/icon.png" )),
				new Image(getClass().getClassLoader().getResourceAsStream( "icons/icon.ico" ))
		);

		// set main scene
		primaryStage.setScene(context.getBean(MainScene.class));

		// show the application
		primaryStage.show();

	}


}

