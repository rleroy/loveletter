package com.leroy.ronan.google.gdata;

import java.io.IOException;
import java.util.List;

import com.google.gdata.util.ServiceException;

public class SpreadSheetManager {

	private String doc;
	private String sheet;
	private List<List<String>> data;
	private GoogleDriveClient client;
	
	public SpreadSheetManager(GoogleDriveClient client, String doc, String sheet, int nbRows, int nbCols) throws IOException, ServiceException{
		this.client = client;
		this.doc = doc;
		this.sheet = sheet;
		
		this.client.setWorksheetSize(doc, sheet, nbRows, nbCols);
		this.data = client.getCellValues(doc, sheet);
	}
	
	public void update() throws IOException, ServiceException{
		client.writeCells(doc, sheet, 1, 1, data);
	}
	
	public void push(int i, int j, String[][] tab) {
		for (int r = 0; r < tab.length; r++){
			for (int c = 0; c < tab[r].length; c++){
				this.data.get(r+i-1).set(c+j-1, tab[r][c]);
			}
		}
	}

	public void push(int i, int j, List<List<String>> tab) {
		for (int r = 0; r < tab.size(); r++){
			for (int c = 0; c < tab.get(r).size(); c++){
				this.data.get(r+i-1).set(c+j-1, tab.get(r).get(c));
			}
		}
	}

	public String get(int row, int col) {
		return data.get(row-1).get(col-1);
	}
}
