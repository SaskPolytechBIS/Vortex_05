package com.example.mscarealpha.ui.home;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature, micon, mcity, mWeatherType;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject) {
        try {
            weatherData weatherD = new weatherData();
            weatherD.mcity = jsonObject.getString("name");
            weatherD.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            weatherD.micon = updateWeatherIcon(weatherD.mCondition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherD.mTemperature = Integer.toString(roundedValue);
            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition) {
        if (condition >= 200 && condition <= 232) {
            return "rain_thunder";
        } else if (condition >= 300 && condition <= 321) {
            return "rain";
        } else if (condition >= 500 && condition <= 531) {
            return "rain";
        } else if (condition >= 600 && condition <= 622) {
            return "snow2";
        } else if (condition >= 701 && condition <= 781) {
            return "fog";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "day_partial_cloud";
        } else if (condition >= 900 && condition <= 902) {
            return "thunder";
        } else if (condition == 903) {
            return "snow";
        } else if (condition == 904) {
            return "day_clear";
        } else if (condition >= 905 && condition <= 1000) {
            return "thunder";
        } else {
            return "dunno";
        }
    }

    public String getmTemperature() {
        return mTemperature + "°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}