package com.csviewpro.controller.filehandling;

import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Balsa on 2016. 10. 19..
 */
@Controller
public class FileHistoryController {

	public List<File> getHistory(){
		return new ArrayList<>();
	}

	public List<File> getFavourites(){
		return Arrays.asList(
				new File("C:\\Users\\Balsa\\OneDrive\\Prog\\CSView\\MintaFileok\\CSV\\szanazug.txt")
		);
	}

}
