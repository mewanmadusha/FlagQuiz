package com.project.madus.flagquiz;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.project.madus.flagquiz.Model.FlagDataModel;
import com.project.madus.flagquiz.database.FlagDataBaseHealper;

import java.util.ArrayList;
import java.util.List;

public class GameAdvancedLevel extends AppCompatActivity {


    FlagDataBaseHealper flagDataBaseHealper;

    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    FlagDataModel flagDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_advanced_level);

        /*
        * get data from intent switch status
        * */
        Intent intent = getIntent();
        String message = intent.getStringExtra(Home.EXTRA_MESSAGE);

        flagDataBaseHealper=new FlagDataBaseHealper(this);
        Cursor result=flagDataBaseHealper.getAllFlagData();


        /*
         * populate cursor data into flagdata object array
         * */
        flagDataModels =getflagdatafromCursor(result);
        /*
        * to avoid duplicate same flag in one game use copy of array list and if
        * falg object already used it removed from copy array
        * */
        flagDataModelsCopy=flagDataModels;


    }

    /**
     * @param result
     * @return
     *
     * insted of using Gson I have create that methode to get cursor data in arraylist
     */
    private ArrayList<FlagDataModel> getflagdatafromCursor(Cursor result) {

        ArrayList<FlagDataModel> dataList = new ArrayList<FlagDataModel>();
        while(result.moveToNext()) {
            dataList.add(new FlagDataModel(result.getInt(result.getColumnIndex("id")), result.getString(result.getColumnIndex("flag_code")), result.getString(result.getColumnIndex("flag_name"))));
        }
        result.close();

        return  dataList;
    }


    public void Game_four_button(View view) {


    }
}
