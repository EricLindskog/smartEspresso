package com.example.smartespresso.api;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class PostApi extends AsyncTask<String, Void, String> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        HttpURLConnection conn = null;
        try {
            int timeout=100000;
            String tmp = params[0];
            String time = params[1];
            String json ="{\"data\" :"+ time+"}";
            URL url = new URL("http://192.168.1.78:8081/"+tmp);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //set the sending type and receiving type to json
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);

            if (json != null) {
                //set the content length of the body
                conn.setRequestProperty("Content-length", json.getBytes().length + "");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            //Connect to the server
            conn.connect();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    return sb.toString().trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "error";
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }

}