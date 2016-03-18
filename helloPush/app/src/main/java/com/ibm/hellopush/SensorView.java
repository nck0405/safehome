package com.ibm.hellopush;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by nprathap on 2/26/2016.
 */
public class SensorView extends Activity{
    Button btnBedroom,btnSensorAction,btnSmokeSensor,btnImageAction,btnSoundAction,btnSmokeAction,btnTempAction;
    Button btnSmokeIndicator,btnSoundIndicator,btnRainIndicator,btnDoorOpenIndicator,btnIntruderIndicator;
    String strSmokeSensorActionResponse;
    TextView txtInstructions,textSmoke;
    String strResponse="",SensorType="",SensorSource="",SensorState="",SensorLocation="",SensorEvent="",SensorDate="",PhotoUrl="",sToken="";
    String strPayloadResponse="Sensor Log : ";
    String sAlarmStatusURI="";
    static String json = "";
    SharedPreferences shPref;
    JSONObject jsonSensor;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensorview);
        String[] arrySensors = {"doorOpen","Camera","intruder","Sound","Flood"};

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
                //setSensorDoorOpen.
                Iterator<String> itr = setSensor.iterator();
                while(itr.hasNext()){
                    try {
                         jsonSensor = new JSONObject(itr.next().toString());
                         System.out.println(" Iterating over HashSet : " + jsonSensor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setSensorAction(jsonSensor);
                    //Update Sensor status for each sensor
                }
            }

        }

        /*Set<String> setSensorDoorOpen = shPref.getStringSet("doorOpen",null);
        if(setSensorDoorOpen!=null)
            Log.i("Sensor  doorOpen ",setSensorDoorOpen.toString());

        Set<String> setSensorCamera = shPref.getStringSet("Camera",null);
        if(setSensorCamera!=null)
            Log.i("Sensor Camers",setSensorCamera.toString());

        Set<String> setSensorIntruder = shPref.getStringSet("intruder",null);
        if(setSensorIntruder!=null)
            Log.i("Sensor Intruder",setSensorIntruder.toString());

        Set<String> setSensorSound = shPref.getStringSet("Sound",null);
        if(setSensorSound!=null)
            Log.i("Sensor Sound",setSensorSound.toString());

        Set<String> setSensorFlood = shPref.getStringSet("Flood",null);
        if(setSensorFlood!=null)
            Log.i("Sensor Sound",setSensorFlood.toString());*/

        //setSensors.
        //shPref.getAll()

       /* SensorSource=getIntent().getStringExtra("Source");
        Log.i("SensorV Requestsource ",SensorSource);
        //Identify the source of Sensor View if it is from Push Notification or General Navigation request.

        if ((SensorSource!=null) && (SensorSource.equalsIgnoreCase(""))) {
            SensorType = getIntent().getStringExtra("SensorType");
            SensorState = getIntent().getStringExtra("SensorState");
            SensorLocation = getIntent().getStringExtra("SensorLocation");
            SensorEvent = getIntent().getStringExtra("SensorEvent");
            SensorDate = getIntent().getStringExtra("SensorDate");
            PhotoUrl = getIntent().getStringExtra("PhotoUrl");
            PhotoUrl = getIntent().getStringExtra("PhotoUrl");
            sToken = getIntent().getStringExtra("Token");
            textSmoke = (TextView) findViewById(R.id.textSmoke);
        }


        if(SensorType!=null)
            Log.e("From Main view ",SensorType );

        if( (SensorSource!=null) && (SensorSource.equalsIgnoreCase("PushNotification"))) {
            Log.e("From Main view", "check URL Show Image if required");

            if( (SensorState.equalsIgnoreCase("Active")) && (SensorEvent.equalsIgnoreCase("1")) ){
                if(SensorType.equalsIgnoreCase("doorOpen")) {
                        btnSmokeIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    Log.e("PhotoUrl    ", PhotoUrl);
                    if(PhotoUrl!=null) {
                            //Read Image from Phot URL
                            String s1= getImageResponse(PhotoUrl,sToken);
                            Log.e("Requested Image",s1);
                            ImageView imgView = (ImageView) findViewById(R.id.imgView);
                            Drawable drawable = getResources().getDrawable(R.drawable.bedroom2);
                            imgView.setImageDrawable(drawable);
                        }

                    }

            }
        }
*/        btnBedroom=(Button)findViewById(R.id.btnHousePlan1);
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

       /* btnSmokeAction =(Button)findViewById(R.id.btnSmokeAction);
        btnSmokeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskGetSensorSmokeAlarmAction actionSensorSmokeAlarm=new AsyncTaskGetSensorSmokeAlarmAction ();
                actionSensorSmokeAlarm.execute();
            }
        });*/

     /*   btnSoundAction =(Button)findViewById(R.id.btnSoundAction);
        btnSoundAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SensorView.this, "Calling Action to invoke Sound Alarm", Toast.LENGTH_LONG).show();
            }
        });*/

        /*btnImageAction =(Button)findViewById(R.id.btnImageAction);
        btnImageAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SensorView.this, "Calling Action to get Image", Toast.LENGTH_LONG).show();
                ImageView imgView=(ImageView) findViewById(R.id.imgView);
                Drawable drawable  = getResources().getDrawable(R.drawable.bedroom2);
                imgView.setImageDrawable(drawable);

            }
        });*/



        /************************************************************************************
        ** Call Sensor Data Service and set the Sensor status
        ** REST URI
        ** Parama Meters
        ** Sample Output JSON
        ***********************************************************************************
        * */
        Toast.makeText(SensorView.this,"Loading latest status of Sensors ",Toast.LENGTH_LONG).show();

        /*AsyncTaskGetSensorData postformerid=new AsyncTaskGetSensorData();
        Log.e("REST", "Calling REST Service to get Sensor latest status");
        postformerid.execute();
        this.getSensorData();
        txtInstructions = (TextView)findViewById(R.id.txtInstructions);
        Toast.makeText(SensorView.this, txtInstructions.getText().toString()+strResponse, Toast.LENGTH_LONG).show();*/
    }
    void setSensorAction(JSONObject jsonSensor){
        try {

            String SensorType = (String)jsonSensor.get("Sensor_Type");
            Log.i("Set Sensor Action : ",SensorType);
            if(SensorType!=null) {
                if (SensorType.equalsIgnoreCase("intruder")) {
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
                if(SensorType.equalsIgnoreCase("doorOpen")) {
                    btnDoorOpenIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    Log.e("PhotoUrl    ", PhotoUrl);
                    if(PhotoUrl!=null) {
                        //Read Image from Phot URL
                        String s1= getImageResponse(PhotoUrl,sToken);
                        Log.e("Requested Image",s1);
                        ImageView imgView = (ImageView) findViewById(R.id.imgView);
                        Drawable drawable = getResources().getDrawable(R.drawable.bedroom2);
                        imgView.setImageDrawable(drawable);
                    }

                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    void getSensorData(){
        //show Sensor Status
        Toast.makeText(SensorView.this,"Message from Create Sensor View",Toast.LENGTH_LONG).show();
       // Read the latest status of Sensor Data and Set the Sensor Indicator and Action
    }

    void SensorView(){
        Log.e("REST","Call from SensorView Constructor ");
    }
   /* *//*
    **  AsyncTaskGetSensorFireAlarmAction
    **  Calls Alarm Action
     *//*
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
            Toast.makeText(SensorView.this, strSmokeSensorActionResponse, Toast.LENGTH_LONG).show();
            Log.e("Action from Sensor", strSmokeSensorActionResponse);
            //Change button Label based on response
            if(strSmokeSensorActionResponse.equalsIgnoreCase("Alarm Stopped")){
                btnSmokeAction.setText("FIRE ALARM : OFF");
            }else
            if(strSmokeSensorActionResponse.equalsIgnoreCase("Alarm Raised")){
                btnSmokeAction.setText("FIRE ALARM : ON");
            }
        }



        @Override
        protected Void doInBackground(Void... params) {

 *//*           HttpClient client = new DefaultHttpClient();
            HttpGet get =new HttpGet(sAlarmStatusURI);//farmerIdSync
            StringEntity se= null;
            HttpResponse response = null;

            try {
                response = client.execute(get);
            } catch (ClientProtocolException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if(response != null) {
                int statuscode = response.getStatusLine().getStatusCode();
                Log.e("status_code_new async", statuscode+"");

                if(statuscode== HttpStatus.SC_OK) {

                    try {
                        strFireSensorActionResponse = EntityUtils.toString(response.getEntity());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.e("Status of Action", strResponse);
                }
            }*//*
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
                json = sb.toString();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            return null;
        }
    }*/
    class AsyncTaskGetSensorData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(DriverRegistrationPage.this, "", "Loading...", true);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // JSONObject Parent = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            Log.e("From SensorView","Background");

           /* HttpClient client = new DefaultHttpClient();
            //https://da72333a-a8b2-4f77-900e-c74c7a8f47de-bluemix.cloudant.com/sensordata/_all_docs?include_docs=true&descending=true
            //http://119.81.101.60/RestServiceMock/secure/farmerSync
            HttpPost post = new HttpPost("https://da72333a-a8b2-4f77-900e-c74c7a8f47de-bluemix.cloudant.com/sensordata/_all_docs?include_docs=true&descending=true");//farmerIdSync
            StringEntity se = null;
            try {
                se = new StringEntity( jsonObj.toString());
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            post.setEntity(se);
            HttpResponse response = null;
            try {
                response = client.execute(post);
            } catch (ClientProtocolException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if(response != null) {

                int statuscode = response.getStatusLine().getStatusCode();
                Log.e("status_code_new async", statuscode+"");

                if(statuscode== HttpStatus.SC_OK) {

                    try {
                        strResponse = EntityUtils.toString(response.getEntity());

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.e("strResponse_new async", strResponse);
                }
            }*/

            try {

                URL url1 = new URL("https://da72333a-a8b2-4f77-900e-c74c7a8f47de-bluemix.cloudant.com/sensordata/_all_docs?include_docs=true&descending=true&limit=5");
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
                Log.e("Response output", sb.toString());
                strResponse = sb.toString();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
              return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("From POst Execute ----",strResponse);
            readSensorDataResponse(strResponse);
            txtInstructions.setText(strPayloadResponse);
        }
    }

    protected String getImageResponse(String sURL,String sToken){
        String sResponse="";
        //Read image from Photo URL
        try {

            URL url1 = new URL("https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-11-16-1758");
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type", "multipart-manifest");
            urlConnection.setRequestProperty("X-Auth-Token",sToken);

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
            //strFireSensorActionResponse=sb.toString();
            sResponse = sb.toString();

            //res = http.urlopen('GET','https://dal.objectstorage.open.softlayer.com/v1/AUTH_a9d5aed2ad114ad59cbc862da2a4d066/SafeHomePhoto/03-11-16-1758',
            // headers={'multipart-manifest':'put','X-Auth-Token':token})
            //image_file.write(res.data)

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return sResponse;


    }
    protected void readSensorDataResponse(String Response){
            try {
                // instantiate our json parser
                //JSONParser jParser = new JSONParser();
                Log.e("Json object ","");
                JSONObject json = new JSONObject(Response);
                // result=getJSONUrl(url); //<< get json string from server
                // JSONObject jsonObject = new JSONObject(result);
                Log.e("Json object Rows ",json+"");
                JSONArray dataJsonArr = null;
                // get the array of users
                dataJsonArr = json.getJSONArray("rows");
                // loop through all users
                Log.e("Json array Rows ",dataJsonArr+"");
                Log.e("Sensor size",dataJsonArr.length()+"");
                for (int i = 0; i < dataJsonArr.length() ; i++) {  //dataJsonArr.length()

                    JSONObject c = dataJsonArr.getJSONObject(i);
//                                rd.save(resid, querycd, restext, rem, rdate);

                    // Storing each json item in variable
                    JSONObject jsonObjDoc = (JSONObject)c.get("doc");
                    JSONObject jsonObjSensor = (JSONObject)jsonObjDoc.get("payload");

                    //Log.e("JSON Arrayt",jsonObjSensor+"");
                    String sensorId= (String)jsonObjSensor.get("Sensor_Id");
                    String sensorType = (String)jsonObjSensor.get("Sensor_Type");
                    String sensorLocation = (String)jsonObjSensor.get("Sensor_Location");
                    String sensorEvent = (String)jsonObjSensor.get("Sensor_Event");
                    String sensorDate = (String)jsonObjSensor.get("date");
                    String sensorState = (String)jsonObjSensor.get("Sensor_State");
                    // show the values in our logcat
                    strPayloadResponse = strPayloadResponse + "\n" +  sensorDate + " : Sensor Type  " + sensorType + "  is " + sensorState;
                    Log.e("dataaaaaaaa", "formarId: " + sensorId);

                    if( (sensorType.equalsIgnoreCase("Fire")) && (sensorState.equalsIgnoreCase("Active")) && (sensorEvent.equalsIgnoreCase("1")) ){
                        //textFire.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        btnSmokeIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

                    }
                    if( (sensorType.equalsIgnoreCase("Fire")) && (sensorState.equalsIgnoreCase("Active")) && (sensorEvent.equalsIgnoreCase("1")) ){
                        //textFire.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        btnSmokeIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
