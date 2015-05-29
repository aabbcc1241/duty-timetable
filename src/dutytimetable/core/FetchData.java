package dutytimetable.core;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import dutytimetable.debug.Debug;

import java.net.URL;
import java.util.List;

class CellSheet {
    CellRow[] cellRows;
}

class CellRow {
    List<CellData> cellDatas;
}

class CellData {
    String columnHeader;
    String value;

    public CellData(String columnHeader, String value) {
        this.columnHeader = columnHeader.intern();
        this.value = value;
    }
}

public class FetchData {

    public static final String GOOGLE_ACCOUNT_USERNAME = "hhb_hkcc@yahoo.com.hk";
    public static final String GOOGLE_ACCOUNT_PASSWORD = "123Abc123";
    public static final String SERVICE_NAME = "Print Google Spreadsheet Demo";
    public static final String PROJECT_ID = "analog-ace-95909";
    public static final String PROJECT_NAME = "121880314592 ";

    public static final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/15Zs-LJz8msbCQi7K-tY1v0heh76_99xwfaadIe3ZQkY";

    public void getData() throws Exception {
        String status = "";

        //try {
        /** Our view of Google Spreadsheets as an authenticated Google user. */
        SpreadsheetService service = new SpreadsheetService(SERVICE_NAME);



        // Login and prompt the user to pick a sheet to use.
        Debug.println("try to login");
        //service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME, GOOGLE_ACCOUNT_PASSWORD);
        String token=service.getAuthToken(GOOGLE_ACCOUNT_USERNAME, GOOGLE_ACCOUNT_PASSWORD, null, null, "wise", PROJECT_NAME);
        System.out.println(token);
        //service.setAuthSubToken();
        Debug.println("success login");

        // Load sheet
        URL metafeedUrl = new URL(SPREADSHEET_URL);
        SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl, SpreadsheetEntry.class);
        List<WorksheetEntry> worksheets = spreadsheet.getWorksheets();

            /*new working part*/
        for (int weekDay = 1; weekDay <= 5; weekDay++) {
            Debug.println("Fetching Data (weekday: " + weekDay + ")");
            CellFeed cellFeed = service.getFeed(worksheets.get(weekDay).getCellFeedUrl(), CellFeed.class);
            for (CellEntry cellEntry : cellFeed.getEntries()) {

            }
        }

            /*demo part*/
        for (int weekDay = 1; weekDay <= 5; weekDay++) {
            System.out.println("Week number: " + weekDay);

            URL listFeedUrl = worksheets.get(weekDay).getListFeedUrl();

            URL cellFeedUrl = worksheets.get(weekDay).getCellFeedUrl();
            CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

            for (CellEntry cellEntry : cellFeed.getEntries()) {
                Cell cell = cellEntry.getCell();
                System.out.println();
                System.out.println("cell row: " + cell.getRow());
                System.out.println("cell col: " + cell.getCol());
                System.out.println("cell value: " + cell.getValue());
            }


            // Print entries
            ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);

            for (ListEntry entry : feed.getEntries()) {
                //System.out.println("new row");
                for (String tag : entry.getCustomElements().getTags()) {
                    new CellData(tag, entry.getCustomElements().getValue(tag));
                }
            }
        }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    private void getDataByWeekDay(CellFeed cellFeed) {

    }

    public static void main(String[] args) throws Exception {
        new FetchData().getData();
    }
}