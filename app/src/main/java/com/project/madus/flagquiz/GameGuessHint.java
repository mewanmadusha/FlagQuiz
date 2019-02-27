package com.project.madus.flagquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.madus.flagquiz.database.FlagDataBaseHealper;
import com.project.madus.flagquiz.Model.FlagDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGuessHint extends AppCompatActivity {

    ImageView imageView2;
    TextView textViewdash;
    TextView textViewResult2;
    TextView text_lifecount;
    Button checkButton2;
    Button nextQuizButton2;
    String hash="";
    EditText editText_input;
    int life=3;
    TextView text_status;


    List<FlagDataModel> flagDataModels= new ArrayList<FlagDataModel>();
    List<FlagDataModel> flagDataModelsCopy= new ArrayList<FlagDataModel>();
    FlagDataModel flagDataModel;
    FlagDataBaseHealper  flagDataBaseHealper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_guess_hint);


        textViewdash=findViewById(R.id.Text_answer_dash);
        textViewResult2=findViewById(R.id.Text_result_game2);
        checkButton2=findViewById(R.id.button_check_game2);
        nextQuizButton2=findViewById(R.id.button_next_game2);
        text_lifecount=findViewById(R.id.text_lifecount);
        text_status=findViewById(R.id.text_status);

        Intent intent = getIntent();
        String message = intent.getStringExtra(Home.EXTRA_MESSAGE);



//        String flagData=intent.getStringExtra("DATA_SET");

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

        /*
         * to genarate random flag
         * and also display character blank spaces to fill
         * */
        genarateRandomFlag();
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

    private void genarateRandomFlag() {

        life=3;
        flagDataModel = getRandomChestItem(flagDataModelsCopy);

        imageView2 = findViewById(R.id.game_image_2);
        Log.i("info", "**********************flag info :" + flagDataModel.getCode().toLowerCase() + "---->" + flagDataModel.getName());
        imageView2.setImageDrawable
                (

                        getResources().getDrawable(getResourceID(flagDataModel.getCode().toLowerCase(), "drawable", getApplicationContext()))
                );



        /*
         * get stringname of flag and genarate _ string to display in mobile app
         * converts string in to car array
         * */
        char[] stringToCharArray = flagDataModel.getName().toCharArray();
        for (char output : stringToCharArray) {

                if (Character.isWhitespace(output)) {
                    hash += "  ";
                } else {
                    hash += "_ ";
                }

        }
        textViewdash.setVisibility(View.VISIBLE);
        textViewdash.setText(hash);
        text_lifecount.setText(String.valueOf(life));
    }

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

    public void check_character(View view) {


        Boolean map = false;
    if(life>0) {

        if (hash.contains("_")) {

            editText_input = findViewById(R.id.editText_input);
            Character inputString = editText_input.getText().charAt(0);

            char[] stringToCharArray = flagDataModel.getName().toLowerCase().toCharArray();

            StringBuilder hashcode = new StringBuilder(hash);
            int i=0;
            for (char output : stringToCharArray) {

                if (output == inputString) {

                    map = true;
                  hashcode.setCharAt(i,inputString);
//                    hash += inputString;

                }
                i+=2;



            }
            hash=hashcode.toString();
            textViewdash.setText(hash);

            if (map == false) {
                life--;
                text_lifecount.setText(String.valueOf(life));
                Toast.makeText(this, "You have only " + life + " life",
                        Toast.LENGTH_SHORT).show();
                text_status.setVisibility(View.VISIBLE);
                text_status.setText("INCORRECT CHARCTER!!");
                text_status.setTextColor(Color.YELLOW);
            }else {
                text_status.setVisibility(View.GONE);
            }
        }
        else {
            nextQuizButton2.setVisibility(View.VISIBLE);
            checkButton2.setVisibility(View.GONE);
            textViewResult2.setVisibility(View.VISIBLE);
            textViewResult2.setText(flagDataModel.getName());
            textViewResult2.setTextColor(Color.BLUE);
            text_status.setVisibility(View.VISIBLE);
            text_status.setText("CORRECT!! ");
            text_status.setTextColor(Color.GREEN);
        }
        }

        if(life<1){

            nextQuizButton2.setVisibility(View.VISIBLE);
            checkButton2.setVisibility(View.GONE);
            textViewResult2.setVisibility(View.VISIBLE);
            textViewResult2.setText(flagDataModel.getName());
            textViewResult2.setTextColor(Color.BLUE);
            text_status.setVisibility(View.VISIBLE);
            text_status.setText("WRONG!! ");
            text_status.setTextColor(Color.RED);
            editText_input.setVisibility(View.GONE);
        }

        if (!hash.contains("_")){

            nextQuizButton2.setVisibility(View.VISIBLE);
            checkButton2.setVisibility(View.GONE);
            textViewResult2.setVisibility(View.VISIBLE);
            textViewResult2.setText(flagDataModel.getName());
            textViewResult2.setTextColor(Color.BLUE);
            text_status.setVisibility(View.VISIBLE);
            text_status.setText("CORRECT!! ");
            text_status.setTextColor(Color.GREEN);
            editText_input.setVisibility(View.GONE);

        }


        editText_input.getText().clear();
    }

    public void next_quiz_game2(View view) {


        nextQuizButton2.setVisibility(View.GONE);
        checkButton2.setVisibility(View.VISIBLE);
        textViewResult2.setVisibility(View.GONE);
        text_status.setVisibility(View.GONE);
        editText_input.setVisibility(View.VISIBLE);
        textViewdash.setText("");
        hash="";
        life=3;
        genarateRandomFlag();

    }
}
