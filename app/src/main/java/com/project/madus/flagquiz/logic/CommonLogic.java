package com.project.madus.flagquiz.logic;

import android.database.Cursor;

import com.project.madus.flagquiz.model.FlagDataModel;

import java.util.ArrayList;

public interface CommonLogic {
    ArrayList<FlagDataModel> getFlagDataModel(Cursor result);
}
