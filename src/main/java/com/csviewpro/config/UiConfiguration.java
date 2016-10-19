package com.csviewpro.config;

import com.csviewpro.ui.MainLayout;
import com.csviewpro.ui.MainScene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Configuration
@ComponentScan("com.csviewpro.ui")
public class UiConfiguration {

	@Bean
	@Autowired
	public MainScene mainScene(MainLayout mainLayout){
		return new MainScene(mainLayout);
	}


}
