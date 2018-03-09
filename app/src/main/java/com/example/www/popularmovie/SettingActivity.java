package com.example.www.popularmovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.movie_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(MainActivity.MOVIE_TYPE_POSITION);
        mSpinner.setOnItemSelectedListener(this);
        setTitle(R.string.setting_menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.MOVIE_TYPE_POSITION = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
