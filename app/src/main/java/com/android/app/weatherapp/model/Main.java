
package com.android.app.weatherapp.model;

//import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

//@Generated("org.jsonschema2pojo")
public class Main {

    private float temp;
    @SerializedName("temp_min")
    private float tempMin;
    @SerializedName("temp_max")
    private float tempMax;
    private float pressure;
    private float seaLevel;
    @SerializedName("grnd_level")
    private float grndLevel;
    private float humidity;
    @SerializedName("temp_kf")
    private float tempKf;

    /**
     * 
     * @return
     *     The temp
     */
    public float getTemp() {
        return temp;
    }

    /**
     * 
     * @param temp
     *     The temp
     */
    public void setTemp(float temp) {
        this.temp = temp;
    }

    /**
     * 
     * @return
     *     The pressure
     */
    public float getPressure() {
        return pressure;
    }

    /**
     * 
     * @param pressure
     *     The pressure
     */
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    /**
     * 
     * @return
     *     The humidity
     */
    public float getHumidity() {
        return humidity;
    }

    /**
     * 
     * @param humidity
     *     The humidity
     */
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    /**
     * 
     * @return
     *     The tempMin
     */
    public float getTempMin() {
        return tempMin;
    }

    /**
     * 
     * @param tempMin
     *     The temp_min
     */
    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    /**
     * 
     * @return
     *     The tempMax
     */
    public float getTempMax() {
        return tempMax;
    }

    /**
     * 
     * @param tempMax
     *     The temp_max
     */
    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    /**
     * 
     * @return
     *     The seaLevel
     */
    public float getSeaLevel() {
        return seaLevel;
    }

    /**
     * 
     * @param seaLevel
     *     The sea_level
     */
    public void setSeaLevel(float seaLevel) {
        this.seaLevel = seaLevel;
    }

    /**
     * 
     * @return
     *     The grndLevel
     */
    public float getGrndLevel() {
        return grndLevel;
    }

    /**
     * 
     * @param grndLevel
     *     The grnd_level
     */
    public void setGrndLevel(float grndLevel) {
        this.grndLevel = grndLevel;
    }

    public float getTempKf() {
        return tempKf;
    }

    public void setTempKf(float tempKf) {
        this.tempKf = tempKf;
    }
}
