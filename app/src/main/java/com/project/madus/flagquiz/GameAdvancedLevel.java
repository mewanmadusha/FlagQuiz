package com.project.madus.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.madus.flagquiz.model.FlagDataModel;
import com.project.madus.flagquiz.database.FlagDataBaseHealper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameAdvancedLevel extends AppCompatActivity {


    /*
     * this use as milisecods
     * 10000miliseconds=10 seconds
     * */
    private static final long COUNTDOWN_FOR_QUIZ = 10000;
    private CountDownTimer countDownTimer;
    private long timeLeft;


    int score=0;

    ImageView game_image_4_1;
    ImageView game_image_4_2;
    ImageView game_image_4_3;
    EditText flag_one_answer;
    EditText flag_two_answer;
    EditText flag_three_answer;
    Button button;
    TextView text_result_game4;
    TextView score_text;
    TextView textTimerGame4;

    TextView flag_one_answer_lable;
    TextView flag_two_answer_lable;
    TextView flag_three_answer_lable;

    TextView game4Headertext;
    boolean ans1flag=false;
    boolean ans2flag=false;
    boolean ans3flag=false;

    int submitButtonAttempt=3;
    FlagDataBaseHealper flagDataBaseHealper;

    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    FlagDataModel flagDataModel;

    /*
     * switch status variable
     * */
    String message;

    int timerAttempt = 0;
    boolean timeoutFlag = false;

    int win = 0;
     FlagDataModel flagDataModel1;
     FlagDataModel flagDataModel2;
     FlagDataModel flagDataModel3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_advanced_level);

        game_image_4_1=(ImageView)findViewById(R.id.game_image_4_1);
        game_image_4_2=(ImageView)findViewById(R.id.game_image_4_2);
        game_image_4_3=(ImageView)findViewById(R.id.game_image_4_3);
        flag_one_answer=(EditText)findViewById(R.id.flag_one_answer);
        flag_two_answer=(EditText)findViewById(R.id.flag_two_answer);
        flag_three_answer=(EditText)findViewById(R.id.flag_three_answer);
        button=(Button) findViewById(R.id.button_game4);

        flag_one_answer_lable=(TextView)findViewById(R.id.flag_one_answer_lable);
        flag_two_answer_lable=(TextView)findViewById(R.id.flag_two_answer_lable);
        flag_three_answer_lable=(TextView)findViewById(R.id.flag_three_answer_lable);

        game4Headertext = findViewById(R.id.advanced_flag_text);

        textTimerGame4 = (TextView) findViewById(R.id.text_timer_game4);


        text_result_game4=(TextView)findViewById(R.id.text_result_game4);
        score_text=(TextView)findViewById(R.id.score);
        score_text.setText(String.valueOf(score));

        /*
         * configure font
         * */
        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");
        flag_three_answer_lable.setTypeface(font);
        flag_two_answer_lable.setTypeface(font);
        flag_one_answer_lable.setTypeface(font);
        button.setTypeface(font);
        textTimerGame4.setTypeface(font);
        game4Headertext.setTypeface(font);


        /*
        * get data from intent switch status
        * */
        Intent intent = getIntent();
        message = intent.getStringExtra(Home.EXTRA_MESSAGE);

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

        /*
        * put random three images into 3 image view and checking with answers
        * */
        StartGame();

//        flag_one_answer.addTextChangedListener(filterTextWatcher);
//        flag_two_answer.addTextChangedListener(filterTextWatcher);
//        flag_three_answer.addTextChangedListener(filterTextWatcher);

        if (message.equals("ON")) {
            timeLeft = COUNTDOWN_FOR_QUIZ;
            beingCountDown();

        }


    }

    private void beingCountDown() {
        timerAttempt++;
        timeoutFlag = false;
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                timeLeft = 0;
                timeoutFlag = true;
                button.performClick();


            }
        }.start();
    }

    private void updateCountDownText() {


//        int minutes =(int) (timeLeft/1000)/60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String formatedTime = String.format(Locale.getDefault(), "%02d", seconds);

        textTimerGame4.setText(formatedTime);

    }

    private void resetTimer() {
        timeLeft = COUNTDOWN_FOR_QUIZ;
        updateCountDownText();
        beingCountDown();
    }


    private void pauseTimer() {
        countDownTimer.cancel();
    }

    /*
     * real time check wether input is correct or not
     * */
//    private TextWatcher filterTextWatcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
////            Log.i("---------------",flag_one_answer.getText().toString().toLowerCase());
////            Log.i("---------------",flagDataModel1.getName().toLowerCase());
//
//            if(flag_one_answer.getText().toString().toLowerCase().equals(flagDataModel1.getName().toLowerCase())){
//                flag_one_answer.setTextColor(Color.GREEN);
//            }else {
//                flag_one_answer.setTextColor(Color.RED);
//            }
//
//            if(flag_two_answer.getText().toString().toLowerCase().equals(flagDataModel2.getName().toLowerCase())){
//                flag_two_answer.setTextColor(Color.GREEN);
//            }else {
//                flag_two_answer.setTextColor(Color.RED);
//            }
//
//            if(flag_three_answer.getText().toString().toLowerCase().equals(flagDataModel3.getName().toLowerCase())){
//                flag_three_answer.setTextColor(Color.GREEN);
//            }else {
//                flag_three_answer.setTextColor(Color.RED);
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    private void StartGame() {

         flagDataModel1=getRandomChestItem(flagDataModelsCopy);
         flagDataModel2=getRandomChestItem(flagDataModelsCopy);
         flagDataModel3=getRandomChestItem(flagDataModelsCopy);

        Log.i("info","**********************flag one info :"+flagDataModel1.getCode().toLowerCase()+"------------->"+flagDataModel1.getName());

        game_image_4_1.setImageDrawable
                (

                        getResources().getDrawable(getResourceID(flagDataModel1.getCode().toLowerCase(),"drawable",getApplicationContext()))
                );

        Log.i("info","**********************flag two info :"+flagDataModel2.getCode().toLowerCase()+"------------->"+flagDataModel2.getName());

        game_image_4_2.setImageDrawable
                (

                        getResources().getDrawable(getResourceID(flagDataModel2.getCode().toLowerCase(),"drawable",getApplicationContext()))
                );

        Log.i("info","**********************flag three info :"+flagDataModel3.getCode().toLowerCase()+"------------->"+flagDataModel3.getName());

        game_image_4_3.setImageDrawable
                (

                        getResources().getDrawable(getResourceID(flagDataModel3.getCode().toLowerCase(),"drawable",getApplicationContext()))
                );




    }

    /**
     * @param data
     * @return
     * to get random index from flagdatamodel arrayList
     */
    public  FlagDataModel getRandomChestItem(List<FlagDataModel> data) {
        return data.get(new Random().nextInt(data.size()));
    }

    /**
     * @param resName
     * @param resType
     * @param ctx
     * @return
     */
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


        if (message.equals("ON")) {
            if (timerAttempt <= 3 && timeoutFlag == true) {
                resetTimer();
            }
        }



        String button_value = button.getText().toString();

        if (button_value.equals("SUBMIT")) {

            if(submitButtonAttempt>0){



            String ans1 = flag_one_answer.getText().toString().toLowerCase();
            String ans2 = flag_two_answer.getText().toString().toLowerCase();
            String ans3 = flag_three_answer.getText().toString().toLowerCase();

            if (ans1.equals(flagDataModel1.getName().toLowerCase()) && ans1flag==false)
            {
                flag_one_answer.setTextColor(Color.GREEN);
                flag_one_answer.setFocusable(false);
                score++;
                win++;
                ans1flag=true;
            }else if(ans1flag==false)  {
                flag_one_answer.setTextColor(Color.RED);

            }

            if (ans2.equals(flagDataModel2.getName().toLowerCase()) &&ans2flag==false)
            {
                flag_two_answer.setTextColor(Color.GREEN);
                flag_two_answer.setFocusable(false);
                score++;
                win++;
                ans2flag=true;
            }else if(ans2flag==false) {

                flag_two_answer.setTextColor(Color.RED);
            }

            if (ans3.equals(flagDataModel3.getName().toLowerCase()) && ans3flag==false)
            {
                flag_three_answer.setTextColor(Color.GREEN);
                flag_three_answer.setFocusable(false);
                score++;
                win++;
                ans3flag=true;
            }else if(ans3flag==false) {

                flag_three_answer.setTextColor(Color.RED);
            }



                score_text.setText(String.valueOf(score));

            }
            submitButtonAttempt--;



             if(submitButtonAttempt==0 || win==3) {

                if (win < 3) {

                    if (message.equals("ON")) {
                        pauseTimer();
                        textTimerGame4.setText("");
                    }
                    text_result_game4.setText("WRONG!!");
                    text_result_game4.setTextColor(Color.RED);

                    if(ans1flag==false) {
                        flag_one_answer_lable.setText(flagDataModel1.getName());
                        flag_one_answer_lable.setTextColor(Color.BLUE);
                    }
                    if(ans2flag==false) {
                        flag_two_answer_lable.setText(flagDataModel2.getName());
                        flag_two_answer_lable.setTextColor(Color.BLUE);
                    }
                    if(ans3flag==false) {
                        flag_three_answer_lable.setText(flagDataModel3.getName());
                        flag_three_answer_lable.setTextColor(Color.BLUE);
                    }
                }
                if (win == 3) {
                    text_result_game4.setText("CORRECT!!");
                    text_result_game4.setTextColor(Color.GREEN);
                    if (message.equals("ON")) {
                        pauseTimer();
                        textTimerGame4.setText("");
                    }




                }
                button.setText("NEXT");
            }
            }


        if(button_value.equals("NEXT")){

            if (message.equals("ON")) {
                resetTimer();
            }
            StartGame();
            flag_one_answer_lable.setText("");
            flag_two_answer_lable.setText("");
            flag_three_answer_lable.setText("");
            flag_one_answer.setText("");
            flag_two_answer.setText("");
            flag_three_answer.setText("");
            text_result_game4.setText("");
            flag_one_answer.setTextColor(Color.BLACK);
            flag_two_answer.setTextColor(Color.BLACK);
            flag_three_answer.setTextColor(Color.BLACK);
            submitButtonAttempt=3;
            win=0;
            ans3flag=false;
            ans2flag=false;
            ans1flag=false;
            flag_one_answer.setFocusableInTouchMode(true);
            flag_two_answer.setFocusableInTouchMode(true);
            flag_three_answer.setFocusableInTouchMode(true);
            button.setText("SUBMIT");
            timerAttempt = 0;


        }

    }


    public void back(View view) {
        finish();
    }
}
