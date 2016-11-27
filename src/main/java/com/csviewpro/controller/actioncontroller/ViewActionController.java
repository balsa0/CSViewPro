package com.csviewpro.controller.actioncontroller;

import com.csviewpro.controller.util.ApplicationUiStateController;
import com.csviewpro.controller.view.TableGridController;
import com.csviewpro.ui.view.common.AnalysisChartView;
import com.csviewpro.ui.view.common.PointRelationChartView;
import com.csviewpro.ui.view.numeric.assets.TableGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Balsa on 2016. 11. 18..
 */
@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Controller
public class ViewActionController {

	@Autowired
	private TableGrid tableGrid;

	@Autowired
	private TableGridController tableGridController;

	@Autowired
	private ApplicationUiStateController uiStateController;

	@Autowired
	private AnalysisChartView analysisChartView;

	@Autowired
	private PointRelationChartView pointRelationChartView;


	public void refreshView(){
		switch (uiStateController.getActiveState()){
			case STATE_FILE_OPEN_NUMERIC:
				tableGrid.refresh();
				break;
			case STATE_FILE_OPEN_GRAPHIC:
			default:
				break;
		}
	}

	public void reloadView(){
		switch (uiStateController.getActiveState()){
			case STATE_FILE_OPEN_NUMERIC:
				tableGridController.rebuildTable();
				break;
			case STATE_FILE_OPEN_GRAPHIC:
			default:
				break;
		}
	}

	public void graphicalAnalysisAction(){
		analysisChartView.showAndUpdate();
	}

	public void pointRelationAnalysis(){
		pointRelationChartView.showAndUpdate();
	}

}
