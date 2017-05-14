package gr.teicm.msc.igdb_mobile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    private EditText textAuthor;
    private EditText textTitle;
    private Button buttonSearch;
    private Button button;
    private TextView dateFrom ;
    private TextView dateTo ;
    private DatePickerDialog.OnDateSetListener mDateFromSetListener;
    private DatePickerDialog.OnDateSetListener mDateToSetListener;
    private static final String TAG = "MainActivity";
    private Button clearFrom,clearTo;
    private Spinner spinGenre;
    private Spinner spinPublisher;
    private Spinner spinPlatform;
    private EditText priceFrom;
    private EditText priceTo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataStore.Init(getApplicationContext());

        textTitle = (EditText)findViewById(R.id.editTextTitle);
        buttonSearch = (Button)findViewById(R.id.buttonSearch);
        dateFrom = (TextView)findViewById(R.id.dateFrom);
        dateTo = (TextView)findViewById(R.id.dateTo);
        clearFrom = (Button)findViewById(R.id.clearFrom);
        clearTo = (Button)findViewById(R.id.clearTo);
        spinGenre = (Spinner) findViewById(R.id.spinnerGenre);
        spinPublisher = (Spinner) findViewById(R.id.spinnerPublisher);
        spinPlatform =(Spinner) findViewById(R.id.spinnerPlatform);
        priceFrom = (EditText) findViewById(R.id.priceFrom);
        priceTo = (EditText) findViewById(R.id.priceTo);

        clearFrom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dateFrom.setText("Select Date");
            }
        });

        clearTo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dateTo.setText("Select Date");
            }
        });

        //Date Time Picker
        dateFrom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                //Log.d(TAG, String.valueOf(year));

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Light_Dialog,mDateFromSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Light_Dialog,mDateToSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateFromSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month +=1 ;
                String date = year + "-" + month + "-" + dayOfMonth;
                dateFrom.setText(date);
            }
        };

        mDateToSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month +=1 ;
                String date = year + "-" + month + "-" + dayOfMonth;
                dateTo.setText(date);
            }
        };

        buttonSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String searchQuery = textTitle.getText().toString();
                String genreQuery = spinGenre.getSelectedItem().toString();
                String platformQuery = spinPlatform.getSelectedItem().toString();
                String publisherQuery = spinPublisher.getSelectedItem().toString();
                String priceFromQuery = priceFrom.getText().toString();
                String priceToQuery = priceTo.getText().toString();
                String dateFromQuery ;
                String dateToQuery;

                if ( dateFrom.getText().toString().equals(new String("Select Date"))  )
                {
                    dateFromQuery = "";
                }
                else
                {
                    dateFromQuery = dateFrom.getText().toString();
                }

                if ( dateTo.getText().toString().equals(new String("Select Date")) )
                {
                    dateToQuery = "";
                }
                else
                {
                    dateToQuery = dateTo.getText().toString();
                }

                //Build JSonQueryString
                String queryURL = "http://igeor.pythonanywhere.com/gamelist?";

                if ( !searchQuery.isEmpty() )
                {
                    queryURL += "&" ;
                    queryURL += "search="+searchQuery ;
                }

                if (!genreQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "GameGenre="+genreQuery;
                }

                if (!platformQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "GamePlatform="+platformQuery;
                }

                if (!publisherQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "GamePublisher="+publisherQuery;
                }

                if (!priceFromQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "PriceFrom="+priceFromQuery;
                }

                if (!priceToQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "PriceTo="+priceToQuery;
                }

                if (!dateFromQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "DateFrom="+dateFromQuery;
                }

                if (!dateToQuery.isEmpty())
                {
                    queryURL += "&";
                    queryURL += "DateTo="+dateToQuery;
                }

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("queryString", queryURL);
                startActivity(intent);
                //Log.v(TAG,queryURL);
            }
        });

        setTitle("IGDB Mobile");

        //Fill the Spinners dynamic
        DownloadTask genreTask = new DownloadTask();
        DownloadTask platformTask = new DownloadTask();
        DownloadTask publisherTask = new DownloadTask();

        genreTask.execute("http://igeor.pythonanywhere.com/genrelist?format=json","GenreTitle","spinnerGenre");
        platformTask.execute("http://igeor.pythonanywhere.com/platformlist?format=json","PlatformName","spinnerPlatform");
        publisherTask.execute("http://igeor.pythonanywhere.com/publisherlist?format=json","PublisherName","spinnerPublisher");
    }

    //Dynamic populate spinners
    public class DownloadTask extends AsyncTask<String,Void,String>
    {
        String what ;
        Spinner spinner;

        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            what = urls[1];

            int resId = getResources().getIdentifier(urls[2], "id", getPackageName());
            spinner = (Spinner) findViewById(resId);

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data!=-1)
                {
                    char current = (char)data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try
            {
                JSONArray jsonArray = new JSONArray(result);
                List<String> stringList =  new ArrayList<String>();
                stringList.add("");

                for (int i = 0 ; i < jsonArray.length(); i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String myString = obj.getString(what);
                    stringList.add(myString);
                }
                //Set Adapter
                ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, stringList);
                spinner.setAdapter(adp);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                finish();
                return true;
            case R.id.menu_settings:
                Toast.makeText(this, "John Georgoudakis\nGeorge Tzoumas\n2017", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
