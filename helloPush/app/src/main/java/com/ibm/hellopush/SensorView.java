package com.ibm.hellopush;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by nprathap on 2/26/2016.
 */
public class SensorView extends Activity{
    static String json = "";
    Button btnBedroom,btnSensorAction,btnSmokeSensor,btnImageAction,btnSoundAction,btnSmokeAction,btnTempAction;
    Button btnSmokeIndicator,btnSoundIndicator,btnRainIndicator,btnDoorOpenIndicator,btnIntruderIndicator;
    String strSmokeSensorActionResponse;
    TextView txtInstructions,textSmoke;
    String strResponse="",SensorType="",SensorSource="",SensorState="",SensorLocation="",SensorEvent="",SensorDate="",PhotoUrl="",sToken="";
    String strPayloadResponse="Sensor Log : ";
    String sAlarmStatusURI="";
    SharedPreferences shPref;
    JSONObject jsonSensor;
    private Bitmap bitmap;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("On Resume ", "SensorView -  On Resume Life Cycle Method");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Onstart()", "SensorView - Onstart() Life Cycle Method");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPause() : ", "SensorView - OnPause Life Cycle Method");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("OnStop() ", "SensorView - OnStop Life Cycle Method");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("OnRestart() : ", "SensorView - OnRestart Life Cycle Method");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensorview);
        String[] arrySensors = {"DoorOpen", "Camera", "Intruder", "Sound", "Flood"};
        btnSmokeIndicator = (Button)findViewById(R.id.btnSmokeIndicator);
        btnSoundIndicator= (Button)findViewById(R.id.btnSoundIndicator);
        btnRainIndicator=(Button)findViewById(R.id.btnRainIndicator);
        btnDoorOpenIndicator=(Button)findViewById(R.id.btnDoorOpenIndicator);
        btnIntruderIndicator=(Button)findViewById(R.id.btnIntruderIndicator);
        shPref = getApplicationContext().getSharedPreferences("MY_PREF", 0);
        Log.i("Array length ",arrySensors.length+"");

        for (int i=0;i<arrySensors.length;i++) {
            Set<String> setSensor = shPref.getStringSet(arrySensors[i].toString(),null);
            if(setSensor!=null) {
                Log.i("Sensor "+arrySensors[i] + " : " , setSensor.toString());
                Iterator<String> itr = setSensor.iterator();
                while(itr.hasNext()){
                    try {
                         jsonSensor = new JSONObject(itr.next().toString());
                         System.out.println(" Iterating over HashSet : " + jsonSensor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SensorView.this, "Loading latest status of Sensors ", Toast.LENGTH_LONG).show();
                    setSensorAction(jsonSensor);
                    //Update Sensor status for each sensor
                    //shEditor.putStringSet(SensorType, sensorSet);
                    //shEditor.commit();
                }
            }
        }
        btnBedroom = (Button) findViewById(R.id.btnHousePlan1);
        btnBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(SensorView.this, MainActivity.class);
                startActivity(iIntent);
            }
        });
        btnSensorAction=(Button)findViewById(R.id.btnSensorAction);
        btnSensorAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(SensorView.this, SensorAction.class);
                startActivity(iIntent);
            }
        });

        /************************************************************************************
        ** Call Sensor Data Service and set the Sensor status
        ** REST URI
        ** Parama Meters
        ** Sample Output JSON
        ***********************************************************************************
        * */

    }
    void setSensorAction(JSONObject jsonSensor){
        try {

            String SensorType = (String)jsonSensor.get("Sensor_Type");


            Log.i("Set Sensor Action : ",SensorType);

            if(SensorType!=null) {
                if (SensorType.equalsIgnoreCase("Intruder")) {
                    btnIntruderIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
                if (SensorType.equalsIgnoreCase("Sound")) {
                    btnSoundIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
                if (SensorType.equalsIgnoreCase("Smoke")) {
                    btnSmokeIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
                if (SensorType.equalsIgnoreCase("Flood")) {
                    btnRainIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
                if ((SensorType.equalsIgnoreCase("DoorOpen")) || (SensorType.equalsIgnoreCase("Camera"))) {

                    PhotoUrl = (String) jsonSensor.get("photo_url");
                    sToken = (String) jsonSensor.get("token");

                    if (SensorType.equalsIgnoreCase("DoorOpen"))
                        btnDoorOpenIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

                    Log.i("PhotoUrl    ", PhotoUrl);
                    Log.i("token ", sToken);

                    if(PhotoUrl!=null) {
                        //Read Image from Phot URL
                        AsyncTaskGetImageAction asyncImageAction = new AsyncTaskGetImageAction();
                        asyncImageAction.execute();
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Bitmap GetBitmapfromUrl(String sURL, String Token) {
        try {
            //final String encodedURL = URLEncoder.encode(sURL, "UTF-8");
            URL url = new URL(sURL);
            Log.i("Object Storage URL : ", sURL.replace("\\", ""));
            Log.i("Object Storage Token : ", Token);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "multipart-manifest");
            connection.setRequestProperty("X-Auth-Token", Token);
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
            bitmap = GetBitmapfromUrl(PhotoUrl, sToken);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ImageView imgView = (ImageView) findViewById(R.id.imgView);
            imgView.setImageBitmap(bitmap);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ImageView imgView = (ImageView) findViewById(R.id.imgView);
            imgView.setImageBitmap(bitmap);

        }
    }
}
