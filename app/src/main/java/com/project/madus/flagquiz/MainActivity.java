package com.project.madus.flagquiz;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button start;
    Button quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.button_start);
        quit = findViewById(R.id.button_quit);

        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");
        start.setTypeface(font);
        quit.setTypeface(font);


    }

    public void start_click(View view) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("start");
//        alertDialogBuilder.show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void quit_click(View view) {
        System.exit(0);
    }
}
