package com.example.smartespresso;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//import androidx.fragment.app.Fragment

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.smartespresso.api.GetApi;
import com.example.smartespresso.api.PostApi;
import com.example.smartespresso.notification.NotificationService;
import com.example.smartespresso.recipe.RecipeListActivity;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final int RECIPE_ACTIVITY_REQUEST_CODE = 0;
    private static final int TARGET_TEMPERATURE = 93;
    RequestQueue queue;
    private boolean backgroundTaskIsRunning = false;
    final Context ctx = this;
    Timer timer;
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createChannel();

        NumberPicker np = (NumberPicker) findViewById(R.id.brewTime);
        np.setMaxValue(100);
        np.setMinValue(10);


    }
    public void onTurnOn(View view){
        new onOffTask().execute("on");
    }
    public void onTurnOff(View view){
        new onOffTask().execute("off");
        handler.removeCallbacksAndMessages(null);
        backgroundTaskIsRunning = false;
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
                    clearTimer();
                }
                resetPowerControls();
                Snackbar sb = Snackbar.make(findViewById(R.id.main_layout),
                        R.string.power_warning, Snackbar.LENGTH_LONG);
                sb.show();
            }
            Toast t = Toast.makeText(ctx,result,Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
            t.show();
            //StyleableToast.makeText(getApplicationContext(), "Hello World!", Toast.LENGTH_LONG, R.style.mytoast).show();
        }
    }
    class getTemp extends GetApi{
        @Override
        protected void onPostExecute(String result) {
            Log.d("temp","temp is: "+result);
            TextView txt = (TextView) findViewById(R.id.temp);
            txt.setText(result+"\u00B0");
            int t = Integer.parseInt(result);
            if(t==TARGET_TEMPERATURE){
                new NotificationService().postNotification(getApplication());
            }
        }
    }
    private Runnable doUpdate = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d("API","getting temp");
                new getTemp().execute("temp");
                new getPower().execute("power");
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.postDelayed(this,15000);
        }
    };
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
        backgroundTaskIsRunning=true;
        handler.post(doUpdate);
    }

    public void viewRecipies(View view){

        Intent myIntent = new Intent(this, RecipeListActivity.class);
        startActivityForResult(myIntent,RECIPE_ACTIVITY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        TextView coffee = findViewById(R.id.activeCoffee);
        TextView dose = findViewById(R.id.activeDose);
        TextView yield = findViewById(R.id.activeYield);
        TextView brewTime = findViewById(R.id.activeBrewTime);
        coffee.setText(recipeArr[0]);
        dose.setText(recipeArr[1]);
        yield.setText(recipeArr[2]);
        brewTime.setText(recipeArr[3]);
    }
    private void clearTimer(){
        timer.cancel();
        timer.purge();
        timer = null;
    }
    private void resetPowerControls(){
        TextView p = (TextView) findViewById(R.id.power);
        TextView t = (TextView) findViewById(R.id.temp);
        p.setText(R.string.power_default);
        t.setText(R.string.temp_default);
    }

    private void createChannel(){
        CharSequence name = "temp";
        String description = "temperature information";//getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(name.toString(), name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    @Override
    public void onResume(){
        super.onResume();
        resetHandler();
        Log.d("app","on resume triggered, handler was running: "+ backgroundTaskIsRunning);
    }
    @Override
    public void onStop(){
        super.onStop();
        resetHandler();

        Log.d("app","on stop triggered, handler running: "+ backgroundTaskIsRunning);
    }
    private void resetHandler(){

        handler.removeCallbacksAndMessages(null);
        if(backgroundTaskIsRunning) {
            updateTempAndPower();
        }
    }
}