package dutytimetable.core;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;


public class readData {

    public static final String GOOGLE_ACCOUNT_USERNAME = "hhb_hkcc@yahoo.com.hk";
    public static final String GOOGLE_ACCOUNT_PASSWORD = "123Abc123";

    public static final String SPREADSHEET_URL ="https://spreadsheets.google.com/feeds/spreadsheets/15Zs-LJz8msbCQi7K-tY1v0heh76_99xwfaadIe3ZQkY";

    public String getData(){
        String status="";

        try{
            /** Our view of Google Spreadsheets as an authenticated Google user. */
            SpreadsheetService service = new SpreadsheetService("Print Google Spreadsheet Demo");

            // Login and prompt the user to pick a sheet to use.
            service.setUserCredentials(GOOGLE_ACCOUNT_USERNAME,
                    GOOGLE_ACCOUNT_PASSWORD);

            // Load sheet
            URL metafeedUrl = new URL(SPREADSHEET_URL);
            SpreadsheetEntry spreadsheet = service.getEntry(metafeedUrl,SpreadsheetEntry.class);
            URL listFeedUrl = spreadsheet.getWorksheets().get(0).getListFeedUrl();

            // Print entries
            ListFeed feed = service.getFeed(listFeedUrl, ListFeed.class);

            for (ListEntry entry : feed.getEntries()) {
                System.out.println("new row");
                for (String tag : entry.getCustomElements().getTags()) {
                    System.out.println("     " + tag + ": "
                            + entry.getCustomElements().getValue(tag));
                    status=entry.getCustomElements().getValue(tag);

                }
            }

        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println(status);
        return(status);
    }
    public static void main(String []args){
        new readData().getData();
    }
}