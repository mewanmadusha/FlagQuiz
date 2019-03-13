package com.project.madus.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.madus.flagquiz.database.FlagDataBaseHealper;
import com.project.madus.flagquiz.logic.CommonLogic;
import com.project.madus.flagquiz.logic.logicImpl.CommonLogicImpl;
import com.project.madus.flagquiz.model.FlagDataModel;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 *
 */
public class GameGuessTheCountry extends AppCompatActivity {


    /*
     * this use as milisecods
     * 10000miliseconds=10 seconds
     * */
    private static final long COUNTDOWN_FOR_QUIZ = 10000;
    private CountDownTimer countDownTimer;
    private long timeLeft;


    ImageView imageView;
    Spinner spinner;
    TextView textViewResult;
    Button checkButton;
    Button nextQuizButton;
    private TextView textTimer;
    TextView chooseText;
    TextView answer;
    TextView game1_header;

    CommonLogic commonLogic = new CommonLogicImpl();

    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    ArrayList<String> countryNames=new ArrayList<>();
    FlagDataModel flagDataModel;
    FlagDataBaseHealper  flagDataBaseHealper;
    boolean correct = false;


    /*
     * timer swithc on off status
     * */
    String message;
    private Random randomGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_guess_the_country);

        Intent intent = getIntent();
        message = intent.getStringExtra(Home.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.text_message);
        textView.setText(message);

        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");
        textView.setTypeface(font);

        spinner=findViewById(R.id.spinner);
        textViewResult=findViewById(R.id.text_result);
        checkButton=findViewById(R.id.button_check_game2);
        answer = findViewById(R.id.answer_text);
        game1_header = findViewById(R.id.head_game1);
//        nextQuizButton=findViewById(R.id.button_next);
        textTimer = findViewById(R.id.text_timer_game1);
        chooseText = findViewById(R.id.choose_flag_text);
        checkButton.setTypeface(font);
        textViewResult.setTypeface(font);
        answer.setTypeface(font);
        game1_header.setTypeface(font);
        chooseText.setTypeface(font);
        textTimer.setTypeface(font);


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

        if (message.equals("ON")) {
            timeLeft = COUNTDOWN_FOR_QUIZ;
            beingCountDown();
        }


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


        return commonLogic.getFlagDataModel(result);


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


        if (checkButton.getText().equals("Check")) {

            String spinnerValue = spinner.getSelectedItem().toString();

            if (flagDataModel.getName() == spinnerValue) {

                textViewResult.setVisibility(View.VISIBLE);
                answer.setVisibility(View.VISIBLE);
                textViewResult.setText("Answer is Correct");
                answer.setText(flagDataModel.getName());
                textViewResult.setTextColor(Color.GREEN);
                answer.setTextColor(Color.BLUE);
                textTimer.setText("");

                if (message.equals("ON")) {
                    pauseTimer();
                }
                correct = true;
            } else {
                answer.setVisibility(View.VISIBLE);
                textViewResult.setVisibility(View.VISIBLE);
                textViewResult.setText("Answer is Wrong!!");
                answer.setText(flagDataModel.getName());
                answer.setTextColor(Color.BLUE);
                textViewResult.setTextColor(Color.RED);
                textTimer.setText("");

                if (message.equals("ON")) {
                    pauseTimer();
                }
            }

//            checkButton.setVisibility(View.GONE);
//            nextQuizButton.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);

            flagDataModelsCopy.remove(flagDataModel.getId());

            checkButton.setText("Next");

        } else if (checkButton.getText().equals("Next")) {


            /*
             * to genarate random flag
             * */
            sppinerDataSetup();
            genarateRandomFlag();
//            checkButton.setVisibility(View.VISIBLE);
//            nextQuizButton.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            textViewResult.setVisibility(View.GONE);
            answer.setVisibility(View.GONE);
            checkButton.setText("Check");
            correct = false;
            /*
             * to avoid duplicate ramdom index
             * removed alredy genarated index
             * checking
             * */
            Log.i("Array size", String.valueOf(flagDataModelsCopy.size()));


            /*
             * timer set to 10 secods count down
             * */


            if (message.equals("ON")) {
                resetTimer();
            }
        }
    }

    private void resetTimer() {
        timeLeft = COUNTDOWN_FOR_QUIZ;
        updateCountDownText();
        beingCountDown();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
    }

    private void beingCountDown() {

        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                timeLeft = 0;
                checkButton.performClick();


            }
        }.start();
    }

    private void updateCountDownText() {

//        int minutes =(int) (timeLeft/1000)/60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String formatedTime = String.format(Locale.getDefault(), "%02d", seconds);

        textTimer.setText(formatedTime);


    }

    public void back(View view) {
        if (message.equals("ON")) {
            pauseTimer();
        }
        finish();
    }


    /**
     * @param view
     * move forward to next quation
     */
//    public void nextQuiz(View view) {
//
//    }
}
