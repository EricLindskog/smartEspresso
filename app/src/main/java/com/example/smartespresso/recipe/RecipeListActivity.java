package com.example.smartespresso.recipe;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.smartespresso.R;
import com.example.smartespresso.db.RecipeDatabase;
import com.example.smartespresso.recipe.RecipeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RecipeListActivity extends AppCompatActivity {
    RecipeDatabase rdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        SQLiteDatabase db = openOrCreateDatabase("recipes.db", MODE_PRIVATE, null);
        rdb = new RecipeDatabase(db);
        rdb.addTable();
        fillList();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRecipeDialog(view);
            }
        });
    }
    private void fillList(){

        String[] recipes = rdb.getRecipesString();
        for(int i = 0; i< recipes.length; i++){
            addRecipeFragment(recipes[i], Integer.toString(i));
        }

    }
    private void refreshList(){
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        fillList();
    }
    private void addRecipeFragment(String txt, String tag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("txt",txt);
        RecipeFragment rf = new RecipeFragment();
        rf.setArguments(args);
        fragmentTransaction.add(R.id.recipeList, rf, tag);
        fragmentTransaction.commit();
    }
    private void addNewRecipeDialog(View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_recipe, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        dialogView.findViewById(R.id.submitRecipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRecipe(dialogView);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public void removeRecipe(String key){
        rdb.removeRecipe(key);
        refreshList();
    }
    private void submitRecipe(View v) {
        EditText enterCoffee = v.findViewById(R.id.editCoffee);
        EditText enterDose = v.findViewById(R.id.editDose);
        EditText enterYield = v.findViewById(R.id.editYield);
        EditText enterBrewTime = v.findViewById(R.id.editBrewTime);
        EditText enterGrind = v.findViewById(R.id.editGrind);

        int dose = 0; int yield = 0; float brewTime = 0f;
        try{dose = Integer.parseInt(enterDose.getText().toString());}
            catch (Exception e){Log.d("Add Recipe", "dose is not a number"); }
        try{yield = Integer.parseInt(enterYield.getText().toString());}
            catch (Exception e){Log.d("Add Recipe", "yield is not a number"); }
        try{brewTime = Float.parseFloat(enterBrewTime.getText().toString());}
            catch (Exception e){Log.d("Add Recipe", "brewtime is not a number"); }

        rdb.addRecipe(enterCoffee.getText().toString(),
                dose,
                yield,
                brewTime,
                enterGrind.getText().toString());
        refreshList();
    }
}