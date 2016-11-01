package com.example.skysiteofi2.elorganista;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by skysiteofi2 on 28/10/16.
 */

public class WebServices extends AsyncTask<String, Void, String>
{
    private String parametro;
    public WebServices(String parametro) {
        this.parametro = parametro;
    }


    @Override
    protected String doInBackground(String... params) {

            String res=PostData(params);
            return res;
    }

    @Override
    protected void onPostExecute(String result) {

    }
    public String PostData(String[] value) {

        try {
            Log.e("url",value[0]);
            URL url = new URL("https://elorganista.herokuapp.com/"+value[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String urlParameters ="token=familia88appOrganista&"+ parametro;
            urlConnection.setRequestMethod("POST");

            urlConnection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();
//            int responseCode = urlConnection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            br.close();

            return responseOutput.toString();
        }catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
