package com.example.smartespresso.recipe;

import android.app.Activity;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.recipe_fragment, container, false);
        txt = this.getArguments().getString("txt");

        return layoutInflater;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        View v = this.getView();
        TextView coffee = (TextView) v.findViewById(R.id.coffee);
        TextView dose = (TextView) v.findViewById(R.id.dose);
        TextView yield = (TextView) v.findViewById(R.id.yield);
        TextView brewTime = (TextView) v.findViewById(R.id.brewTime);
        String[] recipe = txt.split("\\s+");
        coffee.setText(recipe[0]);
        dose.setText(recipe[1]);
        yield.setText(recipe[2]);
        brewTime.setText(recipe[3]);

        Button select = (Button) v.findViewById(R.id.selectRecipe);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView coffee = getView().findViewById(R.id.coffee);
                TextView dose = getView().findViewById(R.id.dose);
                TextView yield = getView().findViewById(R.id.yield);
                TextView brewTime = getView().findViewById(R.id.brewTime);
                String ret = (coffee.getText().toString() + " "
                        + dose.getText().toString() + " "
                        + yield.getText().toString() + " "
                        + brewTime.getText().toString() + " ");
                Intent intent = new Intent();
                intent.putExtra("recipe", ret);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }


}