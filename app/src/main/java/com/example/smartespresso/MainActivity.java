package com.example.smartespresso;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.smartespresso.api.GetApi;
import com.example.smartespresso.api.PostApi;
import com.example.smartespresso.recipe.RecipeListActivity;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final int RECIPE_ACTIVITY_REQUEST_CODE = 0;
    RequestQueue queue;
    final Context ctx = this;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button turnOn = (Button) findViewById(R.id.text);
        //Button turnOff = (Button) findViewById(R.id.text);
        TextView txt = (TextView) findViewById(R.id.title);

        NumberPicker np = (NumberPicker) findViewById(R.id.brewTime);
        np.setMaxValue(100);
        np.setMinValue(10);


    }
    public void onTurnOn(View view){
        new onOffTask().execute("on");
    }
    public void onTurnOff(View view){
        new onOffTask().execute("off");
    }
    public void onBrew(View view){
        NumberPicker np = (NumberPicker) findViewById(R.id.brewTime);
        new postBrew().execute("brew",Integer.toString(np.getValue()));
    }
    class onOffTask extends GetApi {
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Turning on machine")){
                updateTempAndPower();
            }
            else{
                if(timer!=null){
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
            }
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }
    }
    class getTemp extends GetApi{
        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.temp);
            txt.setText(result+"\u00B0");

        }
    }
    class getPower extends GetApi{
        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.power);
            txt.setText(result);

        }
    }
    class postBrew extends PostApi {
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }
    }
    public void updateTempAndPower() {
        if(timer!=null)return;
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Log.d("Internet","getting temp");
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            new getTemp().execute("temp");
                            new getPower().execute("power");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000);
    }

    public void dbTest(View view){

        Intent myIntent = new Intent(this, RecipeListActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        startActivityForResult(myIntent,RECIPE_ACTIVITY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("activity","returned!");
        if (requestCode == RECIPE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                String recipe = data.getStringExtra("recipe");
                applyRecipe(recipe);
                //Toast.makeText(this, returnString, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void applyRecipe(String recipe){
        String[] recipeArr = recipe.split("\\s+");
        NumberPicker np = (NumberPicker) findViewById(R.id.brewTime);
        Log.d("recipe","setting brew time to: "+ Float.parseFloat(recipeArr[3]));
        np.setValue((int)Float.parseFloat(recipeArr[3]));
    }
}