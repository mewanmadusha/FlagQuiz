package com.project.madus.flagquiz;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.madus.flagquiz.database.FlagDataBaseHealper;
import com.project.madus.flagquiz.database.FlagDataModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class GameGuessTheCountry extends AppCompatActivity {

    ImageView imageView;
    Spinner spinner;
    TextView textViewResult;
    Button checkButton;
    Button nextQuizButton;

    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    ArrayList<String> countryNames=new ArrayList<>();
    FlagDataModel flagDataModel;
    FlagDataBaseHealper  flagDataBaseHealper;

    private Random randomGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_guess_the_country);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Home.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.text_message);
        textView.setText(message);

        spinner=findViewById(R.id.spinner);
        textViewResult=findViewById(R.id.text_result);
        checkButton=findViewById(R.id.button_check_game2);
        nextQuizButton=findViewById(R.id.button_next);


//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

//        String flagData=intent.getStringExtra("DATA_SET");
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<FlagDataModel>>(){}.getType();
//        flagDataModels= gson.fromJson(flagData, type);
//        flagDataModelsCopy=flagDataModels;

        flagDataBaseHealper=new FlagDataBaseHealper(this);
        Cursor result=flagDataBaseHealper.getAllFlagData();

        /*
         * populate cursor data into flagdata object array
         * */
        flagDataModels =getflagdatafromCursor(result);
        flagDataModelsCopy=flagDataModels;
        for(FlagDataModel data: flagDataModels){

            countryNames.add(data.getName());
        }



        /*
         * set dropdown data
         * spinner data
         * */
        sppinerDataSetup();

        /*
        * to genarate random flag
        * */
        genarateRandomFlag();

    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // app icon in action bar clicked; go home
//                Intent intent = new Intent(this, Home.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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

    private void sppinerDataSetup() {


        spinner=findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,
                        countryNames); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private void genarateRandomFlag() {

        flagDataModel=getRandomChestItem(flagDataModelsCopy);

        imageView=findViewById(R.id.game_image_1);
        Log.i("info","**********************flag info :"+flagDataModel.getCode().toLowerCase()+"---->"+flagDataModel.getName());
        imageView.setImageDrawable
                (

                        getResources().getDrawable(getResourceID(flagDataModel.getCode().toLowerCase(),"drawable",getApplicationContext()))
                );


    }


    /**
     * @param data
     * @return
     */
    public  FlagDataModel getRandomChestItem(List<FlagDataModel> data) {
        return data.get(new Random().nextInt(data.size()));
    }


    protected final static int getResourceID(final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }


    /**
     * @param view
     * to check givenanswer
     */
    public void gameOneCheckAnswer(View view) {



        String spinnerValue=spinner.getSelectedItem().toString();

        if (flagDataModel.getName()== spinnerValue){

            textViewResult.setVisibility(View.VISIBLE);
            textViewResult.setText("Answe is Correct : it is "+flagDataModel.getName());
            textViewResult.setTextColor(Color.GREEN);
        }else {
            textViewResult.setVisibility(View.VISIBLE);
            textViewResult.setText("Answer is Wrong!! : it is "+flagDataModel.getName());
            textViewResult.setTextColor(Color.RED);
        }

        checkButton.setVisibility(View.GONE);
        nextQuizButton.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);

        flagDataModelsCopy.remove(flagDataModel.getId());
    }


    /**
     * @param view
     * move forward to next quation
     */
    public void nextQuiz(View view) {

        /*
         * to genarate random flag
         * */
        sppinerDataSetup();
        genarateRandomFlag();
        checkButton.setVisibility(View.VISIBLE);
        nextQuizButton.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        textViewResult.setVisibility(View.GONE);


        /*
        * to avoid duplicate ramdom index
        * removed alredy genarated index
        * checking
        * */
        Log.i("Array size",String.valueOf(flagDataModelsCopy.size()));

    }
}
