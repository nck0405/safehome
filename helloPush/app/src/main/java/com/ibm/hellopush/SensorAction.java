package com.ibm.hellopush;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nprathap on 3/13/2016.
 */
public class SensorAction extends Activity {
    Button btnSensorControl,btnSensorActions,btnSmokeAction,btnSoundAction,btnImageAction,btnSensorHome;
    String sAlarmStatusURI="",strSmokeSensorActionResponse="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_actions);

        btnSensorHome=(Button)findViewById(R.id.btnHousePlan1);
        btnSensorHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(SensorAction.this, MainActivity.class);
                startActivity(iIntent);
            }
        });
        btnSensorControl=(Button)findViewById(R.id.btnSensorControl);
        btnSensorControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(SensorAction.this, SensorView.class);
                startActivity(iIntent);
            }
        });

        btnSmokeAction =(Button)findViewById(R.id.btnSmokeAction);
        btnSmokeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskGetSensorSmokeAlarmAction actionSensorSmokeAlarm=new AsyncTaskGetSensorSmokeAlarmAction ();
                actionSensorSmokeAlarm.execute();
            }
        });

        btnSoundAction =(Button)findViewById(R.id.btnSoundAction);
        btnSoundAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                try {
                    startActivity(callIntent);
                }catch(Exception e){}
            }
        });
        btnImageAction =(Button)findViewById(R.id.btnImageViewAction);
        btnImageAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent callIntent = new Intent(Intent.ACTION_CALL);
                //callIntent.setData(Uri.parse("tel:123456789"));
                try {
                    //startActivity(callIntent);

                    Toast.makeText(SensorAction.this, "Calling Action to get Image", Toast.LENGTH_LONG).show();
                    // String s1= getImageResponse("url","token");

                    ImageDownloadTask imageDownloadAction=new ImageDownloadTask ();
                    imageDownloadAction.execute();

                   // ImageView imgView=(ImageView) findViewById(R.id.imgCamView);
                    //Drawable drawable  = getResources().getDrawable(R.drawable.bedroom2);
                    //imgView.setImageDrawable(drawable);
                }catch(Exception e){
                    Log.e("Error",e.toString());
                }
            }
        });
    }

    protected String getImageResponse(String sURL,String sToken){
        String sResponse="";
        //Read image from Photo URL
        try {
            Toast.makeText(SensorAction.this, "get Image Response ()", Toast.LENGTH_LONG).show();
            String sToken1="gAAAAABW4v0OzjNhJl5573A4vMnU9phUaC_RCrrH4E1V0dhvs-WemEhWy_NxF6vKzzvcpa50zOUsOR71ukUUB156uxhKbCnlXjsNLbFHm_EWlj6m97GXEiAjtKdAEv8XuoDeo9n3xev0TwngVZJu_5jxAiR4Dqjdq9GPKSXINZdmwRRMq6ASApc";
            URL url1 = new URL("https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-11-16-1758");
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "multipart-manifest");
            urlConnection.setRequestProperty("X-Auth-Token",sToken1);

           /* InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.e("check_url---------", sAlarmStatusURI);
            //Log.e("resp_code%%%%%%%%%%%%",  httpResponse.getStatusLine().getStatusCode()+"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            //strFireSensorActionResponse=sb.toString();
            sResponse = sb.toString();*/

            //****************************************************************************
            /*ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,"profile.jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
               // Bitmap bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fos.close();
            }
            return directory.getAbsolutePath();*/
            String filePath="";
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String filename="roomview.jpg";
            Log.i("Local filename:",""+filename);
            File file = new File(directory,filename);
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            if(downloadedSize==totalSize) filePath=file.getPath();
            //*****************************************************************************



            //res = http.urlopen('GET','https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-11-16-1758',
            // headers={'multipart-manifest':'put','X-Auth-Token':token})
            //image_file.write(res.data)

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return sResponse;


    }

    class ImageDownloadTask extends AsyncTask<Void,Void,Void>
    {


        protected void onPreExecute() {
            //display progress dialog.

        }
        protected Void doInBackground(Void... params) {
            URL url = null;
            try {
              /*  url = new URL("http://www.google.com");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                String responseMsg = con.getResponseMessage();
                int response = con.getResponseCode();*/
//              Toast.makeText(SensorAction.this, "get Image Response ()", Toast.LENGTH_LONG).show();
                Log.e("in","Response Image mthod");
                String sToken1="gAAAAABW64LnnXjqeQYobXIMm4EXnGLtyHKW6vVQKndlqNeLnhiJBAfwm8A82h6nRsGr_SIjNzMWN9jsEGsdkwn6iqU6EEUvDmUq2-B4NrnHhaRZ6MXQYA7ktQzPr4_lZ-QnbQbwkUSxi5gkSBOImCiAkknWtYVY7W59BT_mcBlxowuce7VVrYU";
                URL url1 = new URL("https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-17-16-1438");
                HttpURLConnection urlConnection;

                urlConnection = (HttpURLConnection) url1.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-type", "multipart-manifest");
                urlConnection.setRequestProperty("X-Auth-Token",sToken1);
                Log.e("Get Reponse Code",urlConnection.getResponseCode()+"");

                String filePath="";
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                String filename="roomview.jpg";
                Log.i("Local filename:",""+filename);
                File file = new File(getApplicationContext().getFilesDir(),filename);
                Log.i("File Path ...",file.getPath());
                if(file.createNewFile())
                {
                    file.createNewFile();
                }

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream outputStream;
                int totalSize = urlConnection.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ( (bufferLength = inputStream.read(buffer)) > 0 )
                {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(buffer, 0, bufferLength);
                    outputStream.close();
                }
                fileOutput.close();
                if(downloadedSize==totalSize) filePath=file.getPath();
                Log.i("file Path ####",filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }
    /*
   **  AsyncTaskGetSensorFireAlarmAction
   **  Calls Alarm Action
    */
    class AsyncTaskGetSensorSmokeAlarmAction extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            sAlarmStatusURI="http://safehome.mybluemix.net/safehome/command?myCommand=";
            if(btnSmokeAction.getText().toString().equalsIgnoreCase("FIRE ALARM : OFF")){
                sAlarmStatusURI = sAlarmStatusURI + "startalarm";
            }else{
                sAlarmStatusURI = sAlarmStatusURI + "stopalarm";
            }
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(SensorAction.this, strSmokeSensorActionResponse, Toast.LENGTH_LONG).show();
            Log.e("Action from Sensor", strSmokeSensorActionResponse);
            //Change button Label based on response
            if(strSmokeSensorActionResponse.trim().equalsIgnoreCase("Alarm Stopped")){
                btnSmokeAction.setText("FIRE ALARM : OFF");
            }else
            if(strSmokeSensorActionResponse.trim().equalsIgnoreCase("Alarm Raised")){
                btnSmokeAction.setText("FIRE ALARM : ON");
            }
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {

                URL url1 = new URL(sAlarmStatusURI);
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.e("check_url---------", sAlarmStatusURI);
                //Log.e("resp_code%%%%%%%%%%%%",  httpResponse.getStatusLine().getStatusCode()+"");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                strSmokeSensorActionResponse=sb.toString();

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }
    }


}
