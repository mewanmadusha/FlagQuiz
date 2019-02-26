package com.project.madus.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.madus.flagquiz.database.FlagDataBaseHealper;
import com.project.madus.flagquiz.database.FlagDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGuessTheFlag extends AppCompatActivity {



    ImageView game_image_3_1;
    ImageView game_image_3_2;
    ImageView game_image_3_3;
    TextView text_country_name;
    TextView text_result_game3;


    FlagDataBaseHealper  flagDataBaseHealper;

    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    FlagDataModel flagDataModel;
    FlagDataModel flagDataModelRan1;
    FlagDataModel flagDataModelRan2;

    ImageView randomimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_guess_the_flag);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Home.EXTRA_MESSAGE);

        flagDataBaseHealper=new FlagDataBaseHealper(this);
        Cursor result=flagDataBaseHealper.getAllFlagData();

        game_image_3_1=findViewById(R.id.advanced_flag_text);
        game_image_3_2=findViewById(R.id.game_image_3_2);
        game_image_3_3=findViewById(R.id.game_image_3_3);

        game_image_3_1.setTag("ImageOne");
        game_image_3_2.setTag("ImageTwo");
        game_image_3_3.setTag("ImageThree");

        text_country_name=findViewById(R.id.text_country_name);
        text_result_game3=findViewById(R.id.text_result_game3);

        /*
        * populate cursor data into flagdata object array
        * */
        flagDataModels =getflagdatafromCursor(result);
        flagDataModelsCopy=flagDataModels;

        getRandomFlag();

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
     */
    public  FlagDataModel getRandomChestItem(List<FlagDataModel> data) {
        return data.get(new Random().nextInt(data.size()));
    }

    public  ImageView getRandomChestImageView(List<ImageView> items) {
        return items.get(new Random().nextInt(items.size()));
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


    public void next_quiz(View view) {
        getRandomFlag();
        text_result_game3.setText("");

    }

    public void imageThreeClick(View view) {

        int value=3;
        checkvalue(value);
    }


    public void imagetwoClick(View view) {
        int value=2;
        checkvalue(value);
    }

    public void imageOneClick(View view) {
        int value=1;
        checkvalue(value);
    }


    private void checkvalue(int value) {


       String currentPosition= (String) randomimageView.getTag();

       if (currentPosition== "ImageOne" && value ==1){

           text_result_game3.setText("CORRECT!!");
           text_result_game3.setTextColor(Color.GREEN);

       }
        else  if (currentPosition== "ImageTwo" && value ==2){
            text_result_game3.setText("CORRECT!!");
            text_result_game3.setTextColor(Color.GREEN);

        }
       else if (currentPosition== "ImageThree" && value ==3){
            text_result_game3.setText("CORRECT!!");
            text_result_game3.setTextColor(Color.GREEN);

        }else {

            text_result_game3.setText("WRONG!!");
            text_result_game3.setTextColor(Color.RED);

        }
    }


}