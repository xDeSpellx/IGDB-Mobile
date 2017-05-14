package gr.teicm.msc.igdb_mobile;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore
{

    public static String KEY_POSITION = "POSITION";
    public static String KEY_ID = "ID";
    public static String KEY_TITLE = "TITLE";
    public static String KEY_AUTHOR = "AUTHOR";
    public static String KEY_GENREID = "GENREID";
    public static String KEY_GENRENAME = "GENRENAME";
    public static String KEY_AMAZONURL = "AMAZONURL";
    public static String KEY_COVERURL = "COVERURL";

    public static Context AppContext = null;
    public static Resources AppResources = null;
    public static String[] Genres = null;
    public static ArrayList<HashMap<String, Object>> Books = new ArrayList<HashMap<String, Object>>();

    public static void Init(Context context){
        AppContext = context;
        AppResources = AppContext.getResources();
        Genres = AppResources.getStringArray(R.array.book_genres);
    }

    public static void LoadBooks(String filterAuthor, String filterTitle, int filterGenreId) {
        DataStore.Books.clear();
        filterAuthor = NetworkUtils.UrlEncode(filterAuthor);
        filterTitle = NetworkUtils.UrlEncode(filterTitle);
        String urlString = String.format("http://informatics.teicm.gr/msc/android/getbooks.php?author=%s&title=%s&genreid=%d", filterAuthor, filterTitle, filterGenreId);
        String contents = NetworkUtils.getFileContentsFromFromUrl(urlString);
        JSONObject json = JsonParser.getJsonObject(contents);
        JSONArray jBooks = json.optJSONArray("Books");
        if (jBooks == null) return;
        int nBooks = jBooks.length();
        for (int i=0; i<nBooks; i++){
            JSONObject jCurBook = jBooks.optJSONObject(i);
            int bookID = jCurBook.optInt(DataStore.KEY_ID, 0);
            String bookTitle = jCurBook.optString(DataStore.KEY_TITLE);
            String bookAuthor = jCurBook.optString(DataStore.KEY_AUTHOR);
            int bookGenreId = jCurBook.optInt(DataStore.KEY_GENREID, 0);
            String bookAmazonUrl = jCurBook.optString(DataStore.KEY_AMAZONURL);
            String bookCoverUrl = jCurBook.optString(DataStore.KEY_COVERURL);

            //get Genre name by ID
            String bookGenreName = DataStore.Genres[bookGenreId];

            // hold each book in a HashMap (Associative Array)
            HashMap<String, Object> book = new HashMap<String, Object>();
            book.put(DataStore.KEY_ID, bookID);
            book.put(DataStore.KEY_TITLE, bookTitle);
            book.put(DataStore.KEY_AUTHOR, bookAuthor);
            book.put(DataStore.KEY_GENREID, bookGenreId);
            book.put(DataStore.KEY_GENRENAME, bookGenreName);
            book.put(DataStore.KEY_AMAZONURL, bookAmazonUrl);
            book.put(DataStore.KEY_COVERURL, bookCoverUrl);

            DataStore.Books.add(book);
        }
    }


}
