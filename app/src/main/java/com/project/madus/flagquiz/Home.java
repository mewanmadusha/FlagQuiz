package com.project.madus.flagquiz;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.madus.flagquiz.database.FlagDataBaseHealper;
import com.project.madus.flagquiz.model.FlagDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 *
 */
public class Home extends AppCompatActivity {

    Switch simpleSwitch;
    Button submit;
    String statusSwitch;
    public static final int TEXT_REQUEST = 1;
    public static final String EXTRA_MESSAGE =
            "com.project.madus.flagquiz.extra.MESSAGE";

    FlagDataBaseHealper  flagDataBaseHealper;

    ArrayList<FlagDataModel> flagDataArray = new ArrayList<FlagDataModel>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         simpleSwitch = (Switch) findViewById(R.id.switch_time);
         submit = (Button) findViewById(R.id.button_submit);
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 if (simpleSwitch.isChecked())
                     statusSwitch = simpleSwitch.getTextOn().toString();
                 else
                     statusSwitch = simpleSwitch.getTextOff().toString();

                 Toast.makeText(getApplicationContext(), "timer :" + statusSwitch + "\n", Toast.LENGTH_LONG).show(); // display the current state for switch's
             }
         });

         /*
         * calling constructor of databasehelper.class
         * to create db
         * */
        flagDataBaseHealper=new FlagDataBaseHealper(this);

        /*
        * get data from db
        *   get data from db to send gaming activities
        * put that data in to array list
        * */
        Cursor result=flagDataBaseHealper.getAllFlagData();

        /*
        * save to sqllite db data when home page starts
        * check with previously saved it or not
        * */
        int a=result.getCount();
        if (result.getCount() == 0 ) {
            saveToFlagDB();
        }







        while(result.moveToNext()) {
            int id=Integer.parseInt(result.getString(result.getColumnIndex("id")));
            String code=result.getString(result.getColumnIndex("flag_code")); //add the item
            String name=result.getString(result.getColumnIndex("flag_name")); //add the item
            FlagDataModel flagDataModel=new FlagDataModel(id,code,name);
            flagDataArray.add(flagDataModel);



        }



    }

    private void saveToFlagDB() {

        /*
         * read json file and put it into  parameter: string json
         *
         * */
        String json= loadJSONFromAsset();

        /*
         * to detect key value pairs from json stirng
         * we should parse it into jsonobject parameter: JSONObject jsonObject
         * */
        JSONObject jsonObject = null;
        try {
            jsonObject  = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("err","error parse json object");
        }

        JSONArray array=jsonObject.names();

        for (int i =0;i<array.length();i++){
            String key = null;
            String value=null;
            try {
                Log.d("key",array.getString(i));
                key=array.getString(i);
                Log.d("value",jsonObject.getString(key));
                value=jsonObject.getString(key);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            flagDataBaseHealper.insertFlagData(key,value);
        }



    }


    /**
     * @param view
     *
     * ----------GAME 1 --------------
     */
    public void gameGuessCountry(View view) {
        /*
         * Pass timer on off switch status through the intent
         * */
        Intent intent = new Intent(this, GameGuessTheCountry.class);
        simpleSwitch = (Switch) findViewById(R.id.switch_time);

        if (simpleSwitch.isChecked())
            statusSwitch = simpleSwitch.getTextOn().toString();
        else
            statusSwitch = simpleSwitch.getTextOff().toString();

        Gson gson = new Gson();
        String jsonflag = gson.toJson(flagDataArray);

        intent.putExtra(EXTRA_MESSAGE, statusSwitch);
        intent.putExtra("DATA_SET", jsonflag);
//        startActivity(intent);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    /**
     * @param view
     *
     * ----------GAME 2 --------------
     */
    public void gameGuessHint(View view) {
        /*
         * Pass timer on off switch status through the intent
         * */
        Intent intent = new Intent(this, GameGuessHint.class);
        simpleSwitch = (Switch) findViewById(R.id.switch_time);

        if (simpleSwitch.isChecked())
            statusSwitch = simpleSwitch.getTextOn().toString();
        else
            statusSwitch = simpleSwitch.getTextOff().toString();

//        Gson gson = new Gson();
//        String jsonflag = gson.toJson(flagDataArray);

        intent.putExtra(EXTRA_MESSAGE, statusSwitch);
//        intent.putExtra("DATA_SET", jsonflag);
//        startActivity(intent);
        startActivityForResult(intent, TEXT_REQUEST);
    }


    /**
     * @param view
     *
     * ----------GAME 3 --------------
     */
    public void gameGuessFlag(View view) {
        /*
         * Pass timer on off switch status through the intent
         * */
        Intent intent = new Intent(this, GameGuessTheFlag.class);
        if (simpleSwitch.isChecked())
            statusSwitch = simpleSwitch.getTextOn().toString();
        else
            statusSwitch = simpleSwitch.getTextOff().toString();

//        Gson gson = new Gson();
//        String jsonflag = gson.toJson(flagDataArray);

        intent.putExtra(EXTRA_MESSAGE, statusSwitch);
//        startActivity(intent);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    /**
     * @param view
     *
     * ----------GAME 4 ---------------
     */
    public void gameAdvancedLevel(View view) {

        /*
        * Pass timer on off switch status through the intent
        * */
        Intent intent = new Intent(this, GameAdvancedLevel  .class);
        if (simpleSwitch.isChecked())
            statusSwitch = simpleSwitch.getTextOn().toString();
        else
            statusSwitch = simpleSwitch.getTextOff().toString();
        intent.putExtra(EXTRA_MESSAGE, statusSwitch);
        startActivityForResult(intent, TEXT_REQUEST);
    }


    /**
     * @return
     *read json file into string
     */
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("countries.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
