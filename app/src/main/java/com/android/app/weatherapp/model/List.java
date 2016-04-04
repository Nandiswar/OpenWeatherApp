
package com.android.app.weatherapp.model;

import java.util.ArrayList;
//import javax.annotation.Generated;

//@Generated("org.jsonschema2pojo")
public class List {

    private long dt;
    private Main main;
    private java.util.List<Weather> weather = new ArrayList<Weather>();
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Sys_ sys;
    private String dtTxt;

    /**
     * 
     * @return
     *     The dt
     */
    public long getDt() {
        return dt;
    }

    /**
     * 
     * @param dt
     *     The dt
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    /**
     * 
     * @return
     *     The main
     */
    public Main getMain() {
        return main;
    }

    /**
     * 
     * @param main
     *     The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * 
     * @return
     *     The weather
     */
    public java.util.List<Weather> getWeather() {
        return weather;
    }

    /**
     * 
     * @param weather
     *     The weather
     */
    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    /**
     * 
     * @return
     *     The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     * 
     * @param clouds
     *     The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     * 
     * @return
     *     The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * 
     * @param wind
     *     The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     * 
     * @return
     *     The rain
     */
    public Rain getRain() {
        return rain;
    }

    /**
     * 
     * @param rain
     *     The rain
     */
    public void setRain(Rain rain) {
        this.rain = rain;
    }

    /**
     * 
     * @return
     *     The sys
     */
    public Sys_ getSys() {
        return sys;
    }

    /**
     * 
     * @param sys
     *     The sys
     */
    public void setSys(Sys_ sys) {
        this.sys = sys;
    }

    /**
     * 
     * @return
     *     The dtTxt
     */
    public String getDtTxt() {
        return dtTxt;
    }

    /**
     * 
     * @param dtTxt
     *     The dt_txt
     */
    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

}
