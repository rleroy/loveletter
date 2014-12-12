package com.leroy.ronan.google.gdata;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class GoogleDriveClient {

	private static final String META_FEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";

	private SpreadsheetService service;
	private SpreadsheetFeed feed;
	private Map<String, Map<String, WorksheetEntry>> cellFeedUrls;

	/**
	 * @param name Name of your application.
	 * @param email Your gdrive account.
	 * @param password Your gdrive password.
	 * @throws IOException
	 * @throws ServiceException
	 */
	public GoogleDriveClient(String name, String email, String password) throws IOException, ServiceException{
		service = new SpreadsheetService(name);
		service.setUserCredentials(email, password);
		URL metafeedUrl = new URL(META_FEED_URL);
		feed = service.getFeed(metafeedUrl, SpreadsheetFeed.class);
	}

	private WorksheetEntry getWorksheet(String title, String sheet) throws IOException, ServiceException{
		if (cellFeedUrls == null){
			cellFeedUrls = new HashMap<String, Map<String,WorksheetEntry>>();
		}
		Map<String, WorksheetEntry> submap = cellFeedUrls.get(title);
		if (submap == null){
			submap = new HashMap<String, WorksheetEntry>();
			cellFeedUrls.put(title, submap);
		}
		WorksheetEntry res = submap.get(sheet);
		if (res == null){
			SpreadsheetEntry spreadSheetEntry = null;
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
			for (int i = 0; i < spreadsheets.size(); i++) {
				SpreadsheetEntry entry = spreadsheets.get(i);
				if (entry.getTitle().getPlainText().equals(title)) {
					spreadSheetEntry = entry;
					break;
				}
			}

			WorksheetFeed worksheetFeed = service.getFeed(spreadSheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class);
			List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			WorksheetEntry worksheet = null;
			for (int i = 0; i < worksheets.size(); i++) {
				WorksheetEntry cur = worksheets.get(i);
				if (cur.getTitle().getPlainText().equals(sheet)){
					worksheet = cur;
					break;
				}
			}

			if (worksheet == null){
				worksheet = new WorksheetEntry();
				worksheet.setTitle(new PlainTextConstruct(sheet));
				worksheet.setColCount(1);
				worksheet.setRowCount(1);
				URL worksheetFeedUrl = spreadSheetEntry.getWorksheetFeedUrl();
				service.insert(worksheetFeedUrl, worksheet);
				worksheet = getWorksheet(title, sheet);
			}

			submap.put(sheet, worksheet);
			res = worksheet;
		}
		return res;
	}

	private URL getCellFeedUrl(String title, String sheet) throws IOException, ServiceException{
		URL res = null;
		WorksheetEntry worksheet = getWorksheet(title, sheet);
		if (worksheet != null){
			res = worksheet.getCellFeedUrl();
		}
		return res;
	}

	public void setWorksheetSize(String title, String sheet, int row, int col) throws IOException, ServiceException{
		WorksheetEntry worksheet = getWorksheet(title, sheet);
		if (worksheet != null){
			worksheet.setColCount(col);
			worksheet.setRowCount(row);
			worksheet.update();
		}
	}

	public void writeCell(String title, String sheet, int row, int col, String value) throws IOException, ServiceException{
		CellQuery query = new CellQuery(getCellFeedUrl(title, sheet));
		query.setMinimumRow(row);
		query.setMaximumRow(row);
		query.setMinimumCol(col);
		query.setMaximumCol(col);
		query.setReturnEmpty(true);
		CellFeed cellList = service.query(query, CellFeed.class);
		if(cellList!=null){
			for (CellEntry cell : cellList.getEntries()) {
				cell.changeInputValueLocal(value);
				cell.update();
			}
		}
	}

	/**
	 * A basic struct to store cell row/column information and the associated RnCn identifier.
	 */
	private static class CellAddress {
		public final int row;
		public final int col;
		public final String idString;
		public final String value;

		/**
		 * Constructs a CellAddress representing the specified {@code row} and
		 * {@code col}.  The idString will be set in 'RnCn' notation.
		 */
		public CellAddress(int row, int col, String value) {
			this.row = row;
			this.col = col;
			this.idString = String.format("R%sC%s", row, col);
			this.value = value;
		}
	}

	public void writeCells(String title, String sheet, int minrow, int mincol, String[][] values) throws IOException, ServiceException{
		List<CellAddress> cellAddrs = new ArrayList<CellAddress>();
		for (int row = 0; row < values.length; row++) {
			String[] line = values[row];
			for (int col = 0; col < line.length; col++) {
				cellAddrs.add(new CellAddress(row+minrow, col+mincol, line[col]));
			}
		}
		
		writeCells(title, sheet, cellAddrs);
	}
	
	public void writeCells(String title, String sheet, int minrow, int mincol, List<List<String>> values) throws IOException, ServiceException{
		List<CellAddress> cellAddrs = new ArrayList<CellAddress>();
		for (int row = 0; row < values.size(); row++) {
			List<String> line = values.get(row);
			for (int col = 0; col < line.size(); col++) {
				cellAddrs.add(new CellAddress(row+minrow, col+mincol, line.get(col)));
			}
		}
		
		writeCells(title, sheet, cellAddrs);
	}

	private void writeCells(String title, String sheet, List<CellAddress> cellAddrs) throws IOException, ServiceException, BatchInterruptedException, MalformedURLException {
		Map<String, CellEntry> cellEntries = getCellEntryMap(title, sheet, cellAddrs);

		CellFeed batchRequest = new CellFeed();
		for (CellAddress cellAddr : cellAddrs) {
			CellEntry batchEntry = new CellEntry(cellEntries.get(cellAddr.idString));
			batchEntry.changeInputValueLocal(cellAddr.value);
			BatchUtils.setBatchId(batchEntry, cellAddr.idString);
			BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
			batchRequest.getEntries().add(batchEntry);
		}

		// Submit the update
		Link batchLink = service.getFeed(getCellFeedUrl(title, sheet), CellFeed.class).getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
		service.setHeader("If-Match", "*");
		CellFeed batchResponse = service.batch(new URL(batchLink.getHref()), batchRequest);
		service.setHeader("If-Match", null);
	}
	
	/**
	 * Connects to the specified {@link SpreadsheetService} and uses a batch request to retrieve a {@link CellEntry} 
	 * for each cell enumerated in {@code cellAddrs}. Each cell entry is placed into a map keyed by its RnCn identifier.
	 *
	 * @param ssSvc the spreadsheet service to use.
	 * @param cellFeedUrl url of the cell feed.
	 * @param cellAddrs list of cell addresses to be retrieved.
	 * @return a map consisting of one {@link CellEntry} for each address in {@code cellAddrs}
	 */
	private Map<String, CellEntry> getCellEntryMap(String title, String sheet, List<CellAddress> cellAddrs) throws IOException, ServiceException {
		CellFeed batchRequest = new CellFeed();
		for (CellAddress cellId : cellAddrs) {
			CellEntry batchEntry = new CellEntry(cellId.row, cellId.col, cellId.idString);
			batchEntry.setId(String.format("%s/%s", getCellFeedUrl(title, sheet).toString(), cellId.idString));
			BatchUtils.setBatchId(batchEntry, cellId.idString);
			BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.QUERY);
			batchRequest.getEntries().add(batchEntry);
		}

		CellFeed cellFeed = service.getFeed(getCellFeedUrl(title, sheet), CellFeed.class);
		CellFeed queryBatchResponse = service.batch(new URL(cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM).getHref()), batchRequest);

		Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>(cellAddrs.size());
		for (CellEntry entry : queryBatchResponse.getEntries()) {
			cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
		}

		return cellEntryMap;
	}

	
	public void insertLine(String title, String sheet, String[][] values) throws IOException, ServiceException{
		URL listFeedUrl = getWorksheet(title, sheet).getListFeedUrl();

		// Create a local representation of the new row.
		ListEntry listEntry = new ListEntry();
		for (String[] value : values){
			listEntry.getCustomElements().setValueLocal(value[0], value[1]);
		}

		// Send the new row to the API for insertion.
		listEntry = service.insert(listFeedUrl, listEntry);
	}

	public String getCellValue(String title, String sheet, int row, String header, String sort, boolean reverse) throws MalformedURLException, URISyntaxException, IOException, ServiceException{
		String res = null;

		URL listFeedUrl = new URI(getWorksheet(title, sheet).getListFeedUrl().toString() + "?orderby="+sort+"&reverse="+reverse).toURL();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		if (listFeed != null){
			ListEntry line = listFeed.getEntries().get(row);
			if (line != null){
				res = line.getCustomElements().getValue(header);
			}
		}

		return res;
	}

	public String getCellValue(String title, String sheet, int row, int col) throws IOException, ServiceException {
		CellQuery query = new CellQuery(getCellFeedUrl(title, sheet));
		query.setMinimumRow(row);
		query.setMaximumRow(row);
		query.setMinimumCol(col);
		query.setMaximumCol(col);
		query.setReturnEmpty(true);
		CellFeed cellList = service.query(query, CellFeed.class);
		if(cellList!=null){
			for (CellEntry cell : cellList.getEntries()) {
				return cell.getCell().getValue();
			}
		}
		return null;
	}
	
	public List<List<String>> getCellValues(String title, String sheet, int minrow, int maxrow, int mincol, int maxcol) throws IOException, ServiceException{
		List<List<String>> res = new ArrayList<List<String>>();
		while (res.size() <= maxrow-minrow){
			List<String> line = new ArrayList<String>();
			while (line.size() <= maxcol-mincol){
				line.add("");
			}
			res.add(line);
		}
		
		CellQuery query = new CellQuery(getCellFeedUrl(title, sheet));
		query.setMinimumRow(minrow);
		query.setMaximumRow(maxrow);
		query.setMinimumCol(mincol);
		query.setMaximumCol(maxcol);
		query.setReturnEmpty(true);
		CellFeed cellList = service.query(query, CellFeed.class);
		if(cellList!=null){
			for (CellEntry cell : cellList.getEntries()) {
				int row = cell.getCell().getRow();
				int col = cell.getCell().getCol();
				res.get(row-1).set(col-1, cell.getCell().getValue());
			}
		}
		return res;
	}
	
	public List<List<String>> getCellValues(String doc, String sheet) throws IOException, ServiceException {
		WorksheetEntry worksheet = getWorksheet(doc, sheet);
		return getCellValues(doc, sheet, 1, worksheet.getRowCount(), 1, worksheet.getColCount());
	}
}
