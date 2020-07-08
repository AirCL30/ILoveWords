package com.icecream.cc.bean;

/**
 * Created by 陈湘龙 on 2020/5/13.
 */
public class Word {
    private String name;
    private String IPA;
    private String meaning;
    private String example;

    public Word() {

    }

    public Word(String name, String IPA, String meaning, String example) {
        this.name = name;
        this.meaning = meaning;
        this.IPA = IPA;
        this.example = example;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getIPA() {
        return IPA;
    }

    public void setIPA(String IPA) {
        this.IPA = IPA;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
