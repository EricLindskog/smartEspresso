package com.example.smartespresso;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.smartespresso.db.RecipeDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;

public class RecipeListActivity extends AppCompatActivity {
    RecipeDatabase rdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        SQLiteDatabase db = openOrCreateDatabase("recipes.db", MODE_PRIVATE, null);
        rdb = new RecipeDatabase(db);
        fillList(rdb);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRecipeDialog(view);
            }
        });
    }
    private void fillList(RecipeDatabase rdb){
        Log.d("db","filling list");

        String[] recipes = rdb.getRecipesString();
        for(int i = 0; i< recipes.length; i++){
            addRecipeFragment(recipes[i], Integer.toString(i));
            Log.d("db","adding recipe: "+recipes[i]);
        }

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
    private void submitRecipe(View v) {
        EditText enterCoffee = (EditText) v.findViewById(R.id.editCoffee);
        EditText enterDose = (EditText) v.findViewById(R.id.editDose);
        EditText enterYield = (EditText) v.findViewById(R.id.editYield);
        Log.d("dia","submitted: "+enterCoffee.getText());

    }
}