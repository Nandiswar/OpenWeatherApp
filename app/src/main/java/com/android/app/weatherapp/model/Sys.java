
package com.android.app.weatherapp.model;

//import javax.annotation.Generated;

//@Generated("org.jsonschema2pojo")
public class Sys {

    private float message;
    private String country;
    private long sunrise;
    private long sunset;
	private long population;
    /**
     * 
     * @return
     *     The message
     */
    public float getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(float message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 
     * @return
     *     The sunrise
     */
    public long getSunrise() {
        return sunrise;
    }

    /**
     * 
     * @param sunrise
     *     The sunrise
     */
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    /**
     * 
     * @return
     *     The sunset
     */
    public long getSunset() {
        return sunset;
    }

    /**
     * 
     * @param sunset
     *     The sunset
     */
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
	
	public long getPopulation() {
        return population;
    }
	
	/**
     * 
     * @param population
     *     The population
     */
    public void setPopulation(long population) {
        this.population = population;
    }
	

}
