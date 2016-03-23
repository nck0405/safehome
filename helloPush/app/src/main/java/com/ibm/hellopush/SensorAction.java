package com.ibm.hellopush;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nprathap on 3/13/2016.
 */
public class SensorAction extends Activity {
    Button btnSensorControl, btnSensorActions, btnSmokeAction, btnSoundAction, btnImageAction, btnSensorHome;
    String sAlarmStatusURI = "", strSmokeSensorActionResponse = "", sImageActionURL = "", strImageSensorActionResponse = "";
    ImageView i;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_actions);

        btnSensorHome = (Button) findViewById(R.id.btnHousePlan1);
        btnSensorHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(SensorAction.this, MainActivity.class);
                startActivity(iIntent);
            }
        });

        btnSensorControl = (Button) findViewById(R.id.btnSensorControl);
        btnSensorControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(SensorAction.this, SensorView.class);
                startActivity(iIntent);
            }
        });

        btnSmokeAction = (Button) findViewById(R.id.btnSmokeAction);
        btnSmokeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskGetSensorSmokeAlarmAction actionSensorSmokeAlarm=new AsyncTaskGetSensorSmokeAlarmAction ();
                actionSensorSmokeAlarm.execute();
            }
        });

        btnSoundAction = (Button) findViewById(R.id.btnSoundAction);
        btnSoundAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:123456789"));
                try {
                    startActivity(callIntent);
                } catch (Exception e) {
                }
            }
        });
        btnImageAction = (Button) findViewById(R.id.btnImageViewAction);
        btnImageAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent callIntent = new Intent(Intent.ACTION_CALL);
                //callIntent.setData(Uri.parse("tel:123456789"));
                try {
                    Toast.makeText(SensorAction.this, "Calling Action to get Image", Toast.LENGTH_LONG).show();
                    i = (ImageView) findViewById(R.id.imgCamView);
                    AsyncTaskGetImageAction asyncImageAction = new AsyncTaskGetImageAction();
                    asyncImageAction.execute();
                    Toast.makeText(SensorAction.this, strImageSensorActionResponse, Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e("Error", e.toString());
                }
            }
        });
    }

    public Bitmap GetBitmapfromUrl(String scr) {
        try {
            //final String encodedURL = URLEncoder.encode(urlAsString, "UTF-8");
            URL url = new URL("https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-22-16-1311");
            // https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-22-16-1111
            Log.i("ur;l", scr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "multipart-manifest");
            connection.setRequestProperty("X-Auth-Token", "gAAAAABW8UQ7hI5dw40vodunucn7gYE3dni8FkCgxrRX8e3ATtZO1ECa6j4xt2zjUJWn_ZKX8XoNCWp2TpKgFda33sqPTr3t-1aD1geP48FQK7Duw5imo9omiCpW4Jby2gPbaKrBLRdX308dWznDTGUQjtxes_A660y4sFtHB4sr3EoQ69GCUgI");
            //{"Sensor_Event":"1","Sensor_Type":"Intruder","token":"gAAAAABW8UQ7hI5dw40vodunucn7gYE3dni8FkCgxrRX8e3ATtZO1ECa6j4xt2zjUJWn_ZKX8XoNCWp2TpKgFda33sqPTr3t-1aD1geP48FQK7Duw5imo9omiCpW4Jby2gPbaKrBLRdX308dWznDTGUQjtxes_A660y4sFtHB4sr3EoQ69GCUgI","nid":"111117","tag":"Push.ALL","Sensor_Id":"11","photo_url":"https:\/\/dal.objectstorage.open.softlayer.com\/v1\/AUTH_a9d5aed2ad114ad59cbc862da2a4d066\/SafeHomePhoto\/03-22-16-1311","Sensor_State":"Active","Sensor_Location":"Bed Room","date":"03-22-16-13:11"}
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Log.i("input", input.toString());
            Bitmap bmp = BitmapFactory.decodeStream(input); //
            return bmp;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    class AsyncTaskGetImageAction extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            //bitmap = GetBitmapfromUrl("https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-22-16-1111");
            try {
                URL url1 = new URL(sImageActionURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.e("check_url---------", sImageActionURL);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                strImageSensorActionResponse = sb.toString();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
            //safehome.mybluemix.net/safehome/command?myCommand=photo
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            i.setImageBitmap(bitmap);
        }

        protected void onPreExecute() {
            sImageActionURL = "http://safehome.mybluemix.net/safehome/command?myCommand=photo";
            super.onPreExecute();
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
            sAlarmStatusURI = "http://safehome.mybluemix.net/safehome/command?myCommand=";
            if (btnSmokeAction.getText().toString().equalsIgnoreCase("FIRE ALARM : OFF")) {
                sAlarmStatusURI = sAlarmStatusURI + "startalarm";
            } else {
                sAlarmStatusURI = sAlarmStatusURI + "stopalarm";
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(SensorAction.this, strSmokeSensorActionResponse, Toast.LENGTH_LONG).show();
            Log.e("Action from Sensor", strSmokeSensorActionResponse);
            //Change button Label based on response
            if (strSmokeSensorActionResponse.trim().equalsIgnoreCase("Alarm Stopped")) {
                btnSmokeAction.setText("FIRE ALARM : OFF");
            } else if (strSmokeSensorActionResponse.trim().equalsIgnoreCase("Alarm Raised")) {
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                in.close();
                strSmokeSensorActionResponse = sb.toString();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }
    }

}
