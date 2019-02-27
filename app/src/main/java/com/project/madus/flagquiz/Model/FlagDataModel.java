package com.project.madus.flagquiz.Model;

public class FlagDataModel {

    int id;
    String code;
    String name;


    public FlagDataModel(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
