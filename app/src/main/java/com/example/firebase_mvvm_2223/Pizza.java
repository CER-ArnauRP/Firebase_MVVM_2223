package com.example.firebase_mvvm_2223;

public class Pizza {

    private String mNom, mIngredients, mPreu, mUid;

    public Pizza() { }
    public Pizza(String nom, String ingredients, String preu, String uid) {
        mNom = nom;
        mIngredients = ingredients;
        mPreu = preu;
        mUid = uid;
    }

    public void setNom(String nom) { mNom = nom; }
    public void setIngredients(String ingredients) { mIngredients = ingredients; }
    public void setPreu(String preu) { mPreu = preu; }
    public void setUid(String uid) { mUid = uid; }

    public String getNom() { return mNom; }
    public String getIngredients() { return mIngredients; }
    public String getPreu() { return mPreu; }
    public String getUid() { return mUid; }
}
