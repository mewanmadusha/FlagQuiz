package com.project.madus.flagquiz.logic.logicImpl;

import android.database.Cursor;

import com.project.madus.flagquiz.logic.CommonLogic;
import com.project.madus.flagquiz.model.FlagDataModel;

import java.util.ArrayList;

public class CommonLogicImpl implements CommonLogic {

    @Override
    public ArrayList<FlagDataModel> getFlagDataModel(Cursor result) {
        ArrayList<FlagDataModel> dataList = new ArrayList<FlagDataModel>();
        while (result.moveToNext()) {
            dataList.add(new FlagDataModel(result.getInt(result.getColumnIndex("id")), result.getString(result.getColumnIndex("flag_code")), result.getString(result.getColumnIndex("flag_name"))));
        }
        result.close();


        return dataList;
    }
}
