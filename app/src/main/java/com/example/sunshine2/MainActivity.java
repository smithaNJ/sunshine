package com.example.sunshine2;

import android.icu.text.IDNA;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import static android.R.attr.mode;
import static android.os.Build.ID;
import static android.provider.Contacts.SettingsColumns.KEY;
import static com.example.sunshine2.R.id.action_settings;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_details,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid=item.getItemId();
        switch(itemid){
            case R.id.action_settings:
                new FetchWeatherTask().execute("1277333");
                //http://api.openweathermap.org/data/2.5/forecast?id={524901}&APP ID={6f2241a533a39c62c586a9cf148730ab}&mode=json&units=metric&cnt=7");
        }
        return super.onOptionsItemSelected(item);
    }
}
