package com.leroy.ronan.google.gdata;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * A sample application showing how to efficiently use batch updates with the
 * Spreadsheets API to create new cells.
 *
 * The specified spreadsheet key will be filled in with 'RnCn' identifier of
 * each cell, up to the {@code MAX_ROWS} and {@code MAX_COLS} constants defined
 * in this class.
 *
 * Usage: java BatchCellUpdater --username [user] --password [pass] --key [spreadsheet-key]
 *
 * @author Josh Danziger
 */
public class BatchCellUpdater {

	/** The number of rows to fill in the destination workbook */
	private static final int MAX_ROWS = 10;

	/** The number of columns to fill in the destination workbook */
	private static final int MAX_COLS = 3;

	/**
	 * A basic struct to store cell row/column information and the associated RnCn
	 * identifier.
	 */
	private static class CellAddress {
		public final int row;
		public final int col;
		public final String idString;

		/**
		 * Constructs a CellAddress representing the specified {@code row} and
		 * {@code col}.  The idString will be set in 'RnCn' notation.
		 */
		public CellAddress(int row, int col) {
			this.row = row;
			this.col = col;
			this.idString = String.format("R%sC%s", row, col);
		}
	}

	public static void test(SpreadsheetService ssSvc, URL cellFeedUrl)
			throws AuthenticationException, MalformedURLException, IOException, ServiceException {
		long startTime = System.currentTimeMillis();

		//FeedURLFactory urlFactory = FeedURLFactory.getDefault();
		//URL cellFeedUrl = urlFactory.getCellFeedUrl(key, "od6", "private", "full");
		//CellFeed cellFeed = ssSvc.getFeed(cellFeedUrl, CellFeed.class);

		// Build list of cell addresses to be filled in
		List<CellAddress> cellAddrs = new ArrayList<CellAddress>();
		for (int row = 1; row <= MAX_ROWS; ++row) {
			for (int col = 1; col <= MAX_COLS; ++col) {
				cellAddrs.add(new CellAddress(row, col));
			}
		}

		// Prepare the update
		// getCellEntryMap is what makes the update fast.
		Map<String, CellEntry> cellEntries = getCellEntryMap(ssSvc, cellFeedUrl, cellAddrs);

		CellFeed batchRequest = new CellFeed();
		for (CellAddress cellAddr : cellAddrs) {
			CellEntry batchEntry = new CellEntry(cellEntries.get(cellAddr.idString));
			batchEntry.changeInputValueLocal(cellAddr.idString);
			BatchUtils.setBatchId(batchEntry, cellAddr.idString);
			BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
			batchRequest.getEntries().add(batchEntry);
		}

		// Submit the update
		//Link batchLink = cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
		//CellFeed batchResponse = ssSvc.batch(new URL(batchLink.getHref()), batchRequest);

        CellFeed cellFeed = ssSvc.getFeed(cellFeedUrl, CellFeed.class);      
        Link batchLink =  cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);

        ssSvc.setHeader("If-Match", "*");
        CellFeed batchResponse = ssSvc.batch(new URL(batchLink.getHref()), batchRequest);
        ssSvc.setHeader("If-Match", null);

		
		// Check the results
		boolean isSuccess = true;
		for (CellEntry entry : batchResponse.getEntries()) {
			String batchId = BatchUtils.getBatchId(entry);
			if (!BatchUtils.isSuccess(entry)) {
				isSuccess = false;
				BatchStatus status = BatchUtils.getBatchStatus(entry);
				System.out.printf("%s failed (%s) %s", batchId, status.getReason(), status.getContent());
			}
		}

		System.out.println(isSuccess ? "\nBatch operations successful." : "\nBatch operations failed");
		System.out.printf("\n%s ms elapsed\n", System.currentTimeMillis() - startTime);
	}

	/**
	 * Connects to the specified {@link SpreadsheetService} and uses a batch
	 * request to retrieve a {@link CellEntry} for each cell enumerated in {@code
	 * cellAddrs}. Each cell entry is placed into a map keyed by its RnCn
	 * identifier.
	 *
	 * @param ssSvc the spreadsheet service to use.
	 * @param cellFeedUrl url of the cell feed.
	 * @param cellAddrs list of cell addresses to be retrieved.
	 * @return a map consisting of one {@link CellEntry} for each address in {@code
	 *         cellAddrs}
	 */
	public static Map<String, CellEntry> getCellEntryMap(
			SpreadsheetService ssSvc, URL cellFeedUrl, List<CellAddress> cellAddrs)
					throws IOException, ServiceException {
		CellFeed batchRequest = new CellFeed();
		for (CellAddress cellId : cellAddrs) {
			CellEntry batchEntry = new CellEntry(cellId.row, cellId.col, cellId.idString);
			batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(), cellId.idString));
			BatchUtils.setBatchId(batchEntry, cellId.idString);
			BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.QUERY);
			batchRequest.getEntries().add(batchEntry);
		}

		CellFeed cellFeed = ssSvc.getFeed(cellFeedUrl, CellFeed.class);
		CellFeed queryBatchResponse =
				ssSvc.batch(new URL(cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM).getHref()),
						batchRequest);

		Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>(cellAddrs.size());
		for (CellEntry entry : queryBatchResponse.getEntries()) {
			cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
			System.out.printf("batch %s {CellEntry: id=%s editLink=%s inputValue=%s\n",
					BatchUtils.getBatchId(entry), entry.getId(), entry.getEditLink().getHref(),
					entry.getCell().getInputValue());
		}

		return cellEntryMap;
	}
}
