package com.ibm.hellopush;
/**
 * Copyright 2015 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //
    // Maintain Device Registration state ????
    //
    SharedPreferences shPref;
    //
    private MFPPush push; // Push client
    private MFPPushResponseListener registrationResponselistener;
    private MFPPushNotificationListener notificationListener; // Notification listener to handle a push sent to the phone
    Button btnBedroom;
    Button btnSensorControl;
    Button btnSensorActions;
    SharedPreferences.Editor shEditor;
    String  SensorType,SensorState,SensorLocation,SensorEvent,SensorDate,PhotoUrl="",Token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        shPref = getApplicationContext().getSharedPreferences("MY_PREF",MODE_PRIVATE);
        shEditor = shPref.edit();

        btnBedroom=(Button)findViewById(R.id.btnBedroom);
        btnBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(MainActivity.this, SensorView.class);
                iIntent.putExtra("Source", "Bedroom");
                startActivity(iIntent);
            }
        });

        btnSensorControl=(Button)findViewById(R.id.btnSensorControl);
        btnSensorControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(MainActivity.this, SensorView.class);
                iIntent.putExtra("Source", "MenuSensorStatus");
                startActivity(iIntent);
            }
        });

        btnSensorActions=(Button)findViewById(R.id.btnSensorAction);
        btnSensorActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIntent = new Intent(MainActivity.this, SensorAction.class);
                iIntent.putExtra("Source", "MenuSensorAction");
                startActivity(iIntent);
            }
        });

        TextView buttonText = (TextView) findViewById(R.id.button_text);

        try {
            // initialize SDK with IBM Bluemix application ID and route
			// You can find your backendRoute and backendGUID in the Mobile Options section on top of your Bluemix application dashboard
            // TODO: Please replace <APPLICATION_ROUTE> with a valid ApplicationRoute and <APPLICATION_ID> with a valid ApplicationId
            //BMSClient.getInstance().initialize(this, "<APPLICATION_ROUTE>", "<APPLICATION_ID>");
            Log.i("Before call BMS Client", "");
            //BMSClient.getInstance().initialize(this, "http://nckmobapp.mybluemix.net", "bf5409da-b032-47ef-a18d-642f491ca330");
            Log.i("After call BMS Client", "");
            BMSClient.getInstance().initialize(this, "http://mysafehome.mybluemix.net", "0f1f229d-5240-4da2-bbe1-623e78434d9d");
            //BMSClient.getInstance().initialize(this, "http://nckmobilepush.mybluemix.net", "6053327c-cf7b-4819-8554-891b508a2cb5");
        }
        catch (MalformedURLException mue) {
            this.setStatus("Unable to parse Application Route URL\n Please verify you have entered your Application Route and Id correctly", false);
           // buttonText.setClickable(false);
        }

        // Initialize Push client
        MFPPush.getInstance().initialize(this);

        // Create notification listener and enable pop up notification when a message is received
        notificationListener = new MFPPushNotificationListener() {
            @Override
            public void onReceive(final MFPSimplePushNotification message) {
                Log.i(TAG, "Received a Push Notification Payload " + message.getPayload().toString());
               // Log.i(TAG, "Received a Push Notification Message " + message.getAlert());
                try {
                    JSONObject jb = new JSONObject(message.getPayload());
                   // Log.i(TAG, "Received a Push Notification: " +jb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                readJson(message.getPayload());
                runOnUiThread(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(" Alert from " + SensorType)
                              //  .setMessage(message.getAlert())
                                .setMessage(SensorDate + "\n Event location : " + SensorLocation + "\n Sensor status : " + SensorState)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent iIntent = new Intent(MainActivity.this, SensorView.class);
                                        iIntent.putExtra("SensorType", SensorType);
                                        iIntent.putExtra("SensorState", SensorState);
                                        iIntent.putExtra("SensorLocation", SensorLocation);
                                        iIntent.putExtra("SensorDate", SensorDate);
                                        iIntent.putExtra("SensorEvent", SensorEvent);
                                        iIntent.putExtra("PhotoUrl", PhotoUrl);
                                        iIntent.putExtra("Source", "PushNotification");
                                        iIntent.putExtra("Token", Token);
                                        startActivity(iIntent);
                                    }
                                })
                                .show();
                    }
                });
            }
        };
    }

    /**
     * Called when the register device button is pressed.
     * Attempts to register the device with your push service on Bluemix.
     * If successful, the push client sdk begins listening to the notification listener.
     *
     * @param view the button pressed
     */
    public void registerDevice(View view) {
        Log.i("Register Device called ", "Register Device called");
		TextView buttonText = (TextView) findViewById(R.id.button_text);
        buttonText.setClickable(false);
	
        // Grabs push client sdk instance
        push = MFPPush.getInstance();
        Log.i(TAG, "Registering for notifications");

        // Creates response listener to handle the response when a device is registered.
        registrationResponselistener = new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String s) {
                setStatus("Device Registered Successfully", true);
                Log.i(TAG, "Successfully registered for push notifications");
                push.listen(notificationListener);
            }

            @Override
            public void onFailure(MFPPushException e) {
                setStatus("Error registering for push notifications: " + e.getErrorMessage(), false);
                Log.e(TAG, e.getErrorMessage());
                push = null;
            }
        };

        // Attempt to register device using response listener created above
         push.register(registrationResponselistener);
        }
    private void readJson(String message){
        JSONObject jsonObj=null;
        JSONObject jsonObjSensor=null;
        try {
            jsonObjSensor=new JSONObject(message);
           // jsonObjSensor=(JSONObject)jsonObj.get("payload");
            Log.i("Sensor ---------" ,jsonObjSensor.get("Sensor_Type") +"");
            SensorType = (String)jsonObjSensor.get("Sensor_Type");
            SensorState = (String)jsonObjSensor.get("Sensor_State");
            SensorLocation = (String)jsonObjSensor.get("Sensor_Location");
            SensorEvent = (String)jsonObjSensor.get("Sensor_Event");
            SensorDate = (String)jsonObjSensor.get("date");
            PhotoUrl = (String)jsonObjSensor.get("photo_url");
            Token = (String)jsonObjSensor.get("token");
            Set<String> sensorSet = new HashSet<String>();
            sensorSet.add(jsonObjSensor.toString());

            shEditor.putStringSet(SensorType, sensorSet);
            shEditor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // If the device has been registered previously, hold push notifications when the app is paused
    @Override
    protected void onPause() {
        super.onPause();

        if (push != null) {
            push.hold();
        }
    }

    // If the device has been registered previously, ensure the client sdk is still using the notification listener from onCreate when app is resumed
    @Override
    protected void onResume() {
        super.onResume();
        if (push != null) {
            push.listen(notificationListener);
        }
    }

    /**
     * Manipulates text fields in the UI based on initialization and registration events
     * @param messageText String main text view
     * @param wasSuccessful Boolean dictates top 2 text view texts
     */
    private void setStatus(final String messageText, boolean wasSuccessful){
        final TextView buttonText = (TextView) findViewById(R.id.button_text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Device Registered Successfully ", Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this, "You are Connected ", Toast.LENGTH_LONG).show();
            }
        });
    }
}
