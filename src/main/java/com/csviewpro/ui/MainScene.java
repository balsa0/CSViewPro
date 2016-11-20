package com.csviewpro.ui;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by Balsa on 2016. 10. 15..
 */
public class MainScene extends Scene{

	public MainScene(@NamedArg("root") Parent root) {
		super(root);
		setStyleSheets();
	}

	private void setStyleSheets(){
		getStylesheets().addAll(
				getStyleRef("tableview.css"),
				getStyleRef("mapview.css")
		);
	}

	public String getStyleRef(String fileName){
		return getClass().getClassLoader().getResource("styles/" + fileName).toExternalForm();
	}

}
