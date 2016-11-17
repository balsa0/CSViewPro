package com.csviewpro.controller.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Component
public class ImageUtil {

	public ImageView getResourceIconImage(String filename){
		return getResourceIconImage(filename, 16);
	}

	public ImageView getResourceIconImage(String filename, double size){

		ImageView icon = new ImageView(
				new Image(
						getClass().getClassLoader().getResourceAsStream( "icons/"+filename )
				)
		);

		icon.setFitHeight(size);
		icon.setFitWidth(size);

		return icon;
	}


}
