package com.example.smartespresso.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecipeDatabase {
    SQLiteDatabase db;
    public RecipeDatabase(SQLiteDatabase db){
        this.db = db;
    }
    public void addTable(){
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipe (coffee VARCHAR(200) PRIMARY KEY, dose INT, yield INT, brewTime REAL)"
        );
    }
    public void addRecipe(String coffee, int dose, int yield, float brewTime){
        //Make some function for this maybe?
        try {

            ContentValues cvs = new ContentValues();
            cvs.put("coffee", coffee);
            cvs.put("dose", dose);
            cvs.put("yield", yield);
            cvs.put("brewTime",brewTime);
            db.insert("recipe", null, cvs);
        }
        catch (Exception e){
            Log.e("DB","Could not add recipe with coffee: "+coffee);
        }
    }
    public String[] getRecipesString(){
        ArrayList<String> ret = new ArrayList<String>();


        Cursor recipes = getRecipes();
        recipes.moveToFirst();
        while(recipes.move(1)){
            String recipe = "";
            recipe += (recipes.getString(0) + " "
                    + recipes.getInt(1) + " "
                    + recipes.getInt(2) + " "
                    + recipes.getFloat(3)
            );

            ret.add(recipe);
        }

        return ret.toArray(new String[0]);
    }
    public Cursor getRecipes(){
        Cursor c = db.query("recipe",null,null,null,null,null,null);
        return c;
    }

}
