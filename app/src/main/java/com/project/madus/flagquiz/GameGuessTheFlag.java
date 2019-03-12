package com.project.madus.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.madus.flagquiz.database.FlagDataBaseHealper;
import com.project.madus.flagquiz.logic.CommonLogic;
import com.project.madus.flagquiz.logic.logicImpl.CommonLogicImpl;
import com.project.madus.flagquiz.model.FlagDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameGuessTheFlag extends AppCompatActivity {


    /*
     * this use as milisecods
     * 10000miliseconds=10 seconds
     * */
    private static final long COUNTDOWN_FOR_QUIZ = 10000;
    private CountDownTimer countDownTimer;
    private long timeLeft;

    ImageView game_image_3_1;
    ImageView game_image_3_2;
    ImageView game_image_3_3;
    TextView text_country_name;
    TextView text_result_game3;
    TextView game3_header;
    TextView pickFlagText;


    FlagDataBaseHealper  flagDataBaseHealper;

    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    FlagDataModel flagDataModel;
    FlagDataModel flagDataModelRan1;
    FlagDataModel flagDataModelRan2;

    private CommonLogic commonLogic = new CommonLogicImpl();


    ImageView randomimageView;
    String message;
    Button buttonNext;
    private TextView textTimerGame3;
    boolean timerOutFlagGame3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_guess_the_flag);

        Intent intent = getIntent();
        message = intent.getStringExtra(Home.EXTRA_MESSAGE);

        flagDataBaseHealper=new FlagDataBaseHealper(this);
        Cursor result=flagDataBaseHealper.getAllFlagData();

        game_image_3_1 = findViewById(R.id.game_image_3_1);
        game_image_3_2=findViewById(R.id.game_image_3_2);
        game_image_3_3=findViewById(R.id.game_image_3_3);

        game_image_3_1.setTag("ImageOne");
        game_image_3_2.setTag("ImageTwo");
        game_image_3_3.setTag("ImageThree");
        game3_header = findViewById(R.id.game3_header_text);
        pickFlagText = findViewById(R.id.pick_flag_text);

        buttonNext = findViewById(R.id.button_next_game3);
        textTimerGame3 = findViewById(R.id.text_timer_game3);

        text_country_name=findViewById(R.id.text_country_name);
        text_result_game3=findViewById(R.id.text_result_game3);
        Typeface font = Typeface.createFromAsset(getAssets(), "font.ttf");

        text_country_name.setTypeface(font);
        buttonNext.setTypeface(font);
        text_result_game3.setTypeface(font);
        game3_header.setTypeface(font);
        pickFlagText.setTypeface(font);


        /*
         * populate cursor data into flagdata object array
         * */
        flagDataModels =getflagdatafromCursor(result);
        flagDataModelsCopy=flagDataModels;

        getRandomFlag();

        if (message.equals("ON")) {
            timeLeft = COUNTDOWN_FOR_QUIZ;
            beingCountDown();
        }
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

                timerOutFlagGame3 = true;
                timeLeft = 0;
                checkvalue(0);


            }
        }.start();
    }

    private void updateCountDownText() {

//        int minutes =(int) (timeLeft/1000)/60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String formatedTime = String.format(Locale.getDefault(), "%02d", seconds);

        textTimerGame3.setText(formatedTime);
    }

    private void resetTimer() {
        timeLeft = COUNTDOWN_FOR_QUIZ;
        updateCountDownText();
        beingCountDown();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
    }

    private void getRandomFlag() {
        flagDataModel=getRandomChestItem(flagDataModelsCopy);
        flagDataModelRan1=getRandomChestItem(flagDataModelsCopy);
        flagDataModelRan2=getRandomChestItem(flagDataModelsCopy);




//        imageView=findViewById(R.id.game_image_1);
        ArrayList<ImageView> imageArr=new ArrayList<>();
        imageArr.add(game_image_3_1);
        imageArr.add(game_image_3_2);
        imageArr.add(game_image_3_3);


         randomimageView =getRandomChestImageView(imageArr);


        Log.i("info","**********************flag info :"+flagDataModel.getCode().toLowerCase()+"---->"+flagDataModel.getName()+ "   image position ------> "+randomimageView.getTag());
        randomimageView.setImageDrawable
                (

                        getResources().getDrawable(getResourceID(flagDataModel.getCode().toLowerCase(),"drawable",getApplicationContext()))
                );

        imageArr.remove(randomimageView);

        imageArr.get(0).setImageDrawable(

                getResources().getDrawable(getResourceID(flagDataModelRan1.getCode().toLowerCase(),"drawable",getApplicationContext()))

        );
        imageArr.get(1).setImageDrawable(

                getResources().getDrawable(getResourceID(flagDataModelRan2.getCode().toLowerCase(),"drawable",getApplicationContext()))

        );


        text_country_name.setText(flagDataModel.getName());


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
     * @param items
     * @return
     * to get ramdom pick from imageview list
     */
    public  ImageView getRandomChestImageView(List<ImageView> items) {
        return items.get(new Random().nextInt(items.size()));
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
     * insted of using Gson I have create that methode to get cursor data to arraylist
     */
    private ArrayList<FlagDataModel> getflagdatafromCursor(Cursor result) {

//        ArrayList<FlagDataModel> dataList = new ArrayList<FlagDataModel>();
////        while(result.moveToNext()) {
////            dataList.add(new FlagDataModel(result.getInt(result.getColumnIndex("id")), result.getString(result.getColumnIndex("flag_code")), result.getString(result.getColumnIndex("flag_name"))));
////        }
////        result.close();
////
////        return  dataList;

        return commonLogic.getFlagDataModel(result);
    }


    public void next_quiz(View view) {
        getRandomFlag();
        game_image_3_3.setBackground(null);
        game_image_3_1.setBackground(null);
        game_image_3_2.setBackground(null);

        text_result_game3.setText("");
        if (message.equals("ON")) {
            resetTimer();
        }

    }

    public void imageThreeClick(View view) {

        Drawable highlight = getResources().getDrawable(R.drawable.highlight);
        game_image_3_3.setBackground(highlight);
        game_image_3_1.setBackground(null);
        game_image_3_2.setBackground(null);
        int value=3;
        checkvalue(value);
    }


    public void imagetwoClick(View view) {
        Drawable highlight = getResources().getDrawable(R.drawable.highlight);
        game_image_3_2.setBackground(highlight);
        game_image_3_3.setBackground(null);
        game_image_3_1.setBackground(null);
        int value=2;
        checkvalue(value);
    }

    public void imageOneClick(View view) {
        Drawable highlight = getResources().getDrawable(R.drawable.highlight);
        game_image_3_3.setBackground(null);
        game_image_3_2.setBackground(null);
        game_image_3_1.setBackground(highlight);
        int value=1;
        checkvalue(value);
    }


    private void checkvalue(int value) {


       String currentPosition= (String) randomimageView.getTag();

       if (currentPosition== "ImageOne" && value ==1){

           text_result_game3.setText("CORRECT!!");
           text_result_game3.setTextColor(Color.GREEN);
           if (message.equals("ON")) {
               textTimerGame3.setText("");
               pauseTimer();
           }

       }
        else  if (currentPosition== "ImageTwo" && value ==2){
            text_result_game3.setText("CORRECT!!");
            text_result_game3.setTextColor(Color.GREEN);
           if (message.equals("ON")) {
               textTimerGame3.setText("");
               pauseTimer();
           }
        }
       else if (currentPosition== "ImageThree" && value ==3){
            text_result_game3.setText("CORRECT!!");
            text_result_game3.setTextColor(Color.GREEN);
           if (message.equals("ON")) {
               textTimerGame3.setText("");
               pauseTimer();
           }
        }else {

            text_result_game3.setText("WRONG!!");
            text_result_game3.setTextColor(Color.RED);
           if (message.equals("ON") && timerOutFlagGame3 == true) {
               textTimerGame3.setText("");
               pauseTimer();
           }

           timerOutFlagGame3 = false;
        }
    }


    public void back(View view) {
        finish();
    }
}
