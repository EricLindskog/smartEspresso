package com.example.smartespresso.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import android.app.Fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartespresso.R;

import static android.app.Activity.RESULT_OK;

public class RecipeFragment extends Fragment {
    String txt;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recipe_fragment, container, false);
        txt = this.getArguments().getString("txt");

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        initItem();
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*Intent intent = new Intent();
                intent.putExtra("recipe", txt);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();*/
                changeLayout(R.layout.recipe_fragment_menu);
                initMenu();
                return true;
            }
        });
    }
    private void initItem(){
        TextView coffee = (TextView) view.findViewById(R.id.coffee);
        TextView dose = (TextView) view.findViewById(R.id.dose);
        TextView yield = (TextView) view.findViewById(R.id.yield);
        TextView brewTime = (TextView) view.findViewById(R.id.brewTime);
        TextView grind = (TextView) view.findViewById(R.id.grind);
        String[] recipe = txt.split("\\s+");
        coffee.setText(recipe[0]);
        dose.setText(recipe[1]);
        yield.setText(recipe[2]);
        brewTime.setText(recipe[3]);
        grind.setText(recipe[4]);

        Button select = (Button) view.findViewById(R.id.selectRecipe);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("recipe", txt);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }
    private void initMenu(){
        Button b = (Button) view.findViewById(R.id.recipeMenuRemove);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeListActivity rla = (RecipeListActivity) getActivity();
                rla.removeRecipe(txt.split("\\s+")[0]);
                changeLayout(R.layout.recipe_fragment);
                initItem();
            }
        });
        b = (Button) view.findViewById(R.id.recipeMenuCancel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout(R.layout.recipe_fragment);
                initItem();
            }
        });
    }
    private void changeLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup rootView = (ViewGroup) getView();
        view = inflater.inflate(id, rootView,false);
        rootView.removeAllViews();
        rootView.addView(view);
    }


}