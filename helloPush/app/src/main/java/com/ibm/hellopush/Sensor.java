package com.ibm.hellopush;

/**
 * Created by nprathap on 3/15/2016.
 */
public class Sensor {

    private String SensorType;
    private String SensorSource;
    private String SensorState;
    private String SensorLocation;
    private String SensorEvent;
    private String SensorDate;
    private String PhotoUrl;
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getSensorType() {
        return SensorType;
    }

    public void setSensorType(String sensorType) {
        SensorType = sensorType;
    }

    public String getSensorSource() {
        return SensorSource;
    }

    public void setSensorSource(String sensorSource) {
        SensorSource = sensorSource;
    }

    public String getSensorState() {
        return SensorState;
    }

    public void setSensorState(String sensorState) {
        SensorState = sensorState;
    }

    public String getSensorLocation() {
        return SensorLocation;
    }

    public void setSensorLocation(String sensorLocation) {
        SensorLocation = sensorLocation;
    }

    public String getSensorEvent() {
        return SensorEvent;
    }

    public void setSensorEvent(String sensorEvent) {
        SensorEvent = sensorEvent;
    }

    public String getSensorDate() {
        return SensorDate;
    }

    public void setSensorDate(String sensorDate) {
        SensorDate = sensorDate;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }


}
