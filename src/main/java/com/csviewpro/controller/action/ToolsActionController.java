package com.csviewpro.controller.action;

import com.csviewpro.ui.view.common.AnalysisChartView;
import com.csviewpro.ui.view.common.DistanceMatrixView;
import com.csviewpro.ui.view.common.PointRelationChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@Controller
public class ToolsActionController {

	@Autowired
	private AnalysisChartView analysisChartView;

	@Autowired
	private PointRelationChartView pointRelationChartView;

	@Autowired
	private DistanceMatrixView distanceMatrixView;

	public void graphicalAnalysisAction(){
		analysisChartView.showAndUpdate();
	}

	public void pointRelationAnalysisAction(){
		pointRelationChartView.showAndUpdate();
	}

	public void showDistanceMatrixAction(){
		distanceMatrixView.showAndUpdate();
	}

}
