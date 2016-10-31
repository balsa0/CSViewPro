package com.csviewpro.controller;

import com.csviewpro.ui.NotificationsWrapperLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 10. 29..
 */
@Controller
public class NotificationsController {

	@Autowired
	NotificationsWrapperLayout notificationsWrapperLayout;

	public void showLoaderNotification(String fileName){
		notificationsWrapperLayout.setCloseButtonVisible(false);
		notificationsWrapperLayout.setText(" Betöltés - "+fileName);
		notificationsWrapperLayout.show();
	}

	public void hide(){
		notificationsWrapperLayout.hide();
	}

}
