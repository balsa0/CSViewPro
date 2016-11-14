package com.csviewpro.service;

import com.csviewpro.domain.model.DataSet;
import org.springframework.stereotype.Service;

/**
 * Created by Balsa on 2016. 11. 14..
 */
@Service
public class WorkspaceDataService {

	private DataSet activeDataSet;

	public DataSet getActiveDataSet() {
		return activeDataSet;
	}

	public void setActiveDataSet(DataSet activeDataSet) {
		this.activeDataSet = activeDataSet;
	}
}
