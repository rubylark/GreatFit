package com.dinodevs.greatfitweatherland.widget;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.Log;

import com.dinodevs.greatfitweatherland.settings.LoadSettings;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.sport.SlptTodayFloorNumView;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.dinodevs.greatfitweatherland.data.DataType;
import com.dinodevs.greatfitweatherland.data.WeatherData;
import com.dinodevs.greatfitweatherland.resource.ResourceManager;
import com.dinodevs.greatfitweatherland.R;


public class WeatherWidget extends AbstractWidget {
    private WeatherData weather;
    private Service mService;

    private List<String> weatherImageStrList;
    private Bitmap weatherImageIcon;
    private TextPaint temperaturePaint;
    private TextPaint cityPaint;
    private TextPaint humidityPaint;
    private TextPaint uvPaint;
    private TextPaint wind_directionPaint;
    private TextPaint wind_strengthPaint;
    private TextPaint weather_imgPaint;

    private Bitmap temperatureIcon;
    private Bitmap cityIcon;
    private Bitmap humidityIcon;
    private Bitmap uvIcon;
    private Bitmap wind_directionIcon;
    private Bitmap wind_strengthIcon;

    private LoadSettings settings;

    // Constructor
    public WeatherWidget(LoadSettings settings) {
        this.settings = settings;

        // Load weather icons
        String[] weatherIconNames = new String[]{
            "sunny", //0
            "cloudy", //1
            "overcast", //2
            "fog", //..
            "smog",
            "shower",
            "thunder_shower",
            "light_rain",
            "moderate_rain",
            "heavy_rain",
            "rainstorm",
            "torrential_rain",
            "sleet",
            "freezing_rain",
            "hail",
            "light_snow",
            "moderate_snow",
            "heavy_snow",
            "snowstorm",
            "dust",
            "blowing_sand", //..
            "sand_storm", //21
            "unknown" //22
        };
        this.weatherImageStrList =  Arrays.asList(weatherIconNames);
    }

    // Screen-on init (runs once)
    @Override
    public void init(Service service) {
        this.mService = service;

        // Temperature
        if(settings.temperature>0) {
            this.temperaturePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.temperaturePaint.setColor(settings.temperatureColor);
            this.temperaturePaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.temperaturePaint.setTextSize(settings.temperatureFontSize);
            this.temperaturePaint.setTextAlign((settings.temperatureAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
            if(settings.temperatureIcon){
                this.temperatureIcon = Util.decodeImage(service.getResources(),"icons/temperature.png");
            }
        }

        // City
        if(settings.city>0) {
            this.cityPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.cityPaint.setColor(settings.cityColor);
            this.cityPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.cityPaint.setTextSize(settings.cityFontSize);
            this.cityPaint.setTextAlign((settings.cityAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
            if(settings.cityIcon){
                this.cityIcon = Util.decodeImage(service.getResources(),"icons/city.png");
            }
        }

        // Humidity
        if(settings.humidity>0) {
            this.humidityPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.humidityPaint.setColor(settings.humidityColor);
            this.humidityPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.humidityPaint.setTextSize(settings.humidityFontSize);
            this.humidityPaint.setTextAlign((settings.humidityAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
            if(settings.humidityIcon){
                this.humidityIcon = Util.decodeImage(service.getResources(),"icons/humidity.png");
            }
        }

        // UV
        if(settings.uv>0) {
            this.uvPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.uvPaint.setColor(settings.uvColor);
            this.uvPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.uvPaint.setTextSize(settings.uvFontSize);
            this.uvPaint.setTextAlign((settings.uvAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
            if(settings.uvIcon){
                this.uvIcon = Util.decodeImage(service.getResources(),"icons/uv.png");
            }
        }

        // Wind Direction
        if(settings.wind_direction>0) {
            this.wind_directionPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.wind_directionPaint.setColor(settings.wind_directionColor);
            this.wind_directionPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.wind_directionPaint.setTextSize(settings.wind_directionFontSize);
            this.wind_directionPaint.setTextAlign((settings.wind_directionAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
            if(settings.wind_directionIcon){
                this.wind_directionIcon = Util.decodeImage(service.getResources(),"icons/wind_direction.png");
            }
        }

        // Wind Strength
        if(settings.wind_strength>0) {
            this.wind_strengthPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.wind_strengthPaint.setColor(settings.wind_strengthColor);
            this.wind_strengthPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.wind_strengthPaint.setTextSize(settings.wind_strengthFontSize);
            this.wind_strengthPaint.setTextAlign((settings.wind_strengthAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
            if(settings.wind_strengthIcon){
                this.wind_strengthIcon = Util.decodeImage(service.getResources(),"icons/wind_strength.png");
            }
        }

        // Load weather icons
        if(settings.weather_img>0) {
            // Get weather data
            this.weather = getSlptWeather();
            this.weatherImageIcon = Util.decodeImage(service.getResources(),"weather/"+this.weatherImageStrList.get(22)+".png");
            this.weather_imgPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            this.weather_imgPaint.setColor(settings.weather_imgColor);
            this.weather_imgPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE));
            this.weather_imgPaint.setTextSize(settings.weather_imgFontSize);
            this.weather_imgPaint.setTextAlign((settings.weather_imgAlignLeft) ? Paint.Align.LEFT : Paint.Align.CENTER);
        }
    }

    // Register listener
    @Override
    public List<DataType> getDataTypes() {
        return Collections.singletonList(DataType.WEATHER);
    }

    // Updater
    @Override
    public void onDataUpdate(DataType type, Object value) {
        //this.weather = (WeatherData) value;
        // Value = weather info [tempFlag:1, tempString:29, weatherType:0
        //Log.w("DinoDevs-GreatFit", "Data Update: "+type.toString()+" => "+value.toString() );

        // Get ALL weather data
        this.weather = getSlptWeather();

        this.weatherImageIcon = Util.decodeImage(mService.getResources(),"weather/"+this.weatherImageStrList.get(this.weather.weatherType)+".png");
    }

    // Screen on
    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY) {
        // Draw Temperature
        if(settings.temperature>0) {
            if(settings.temperatureIcon){
                canvas.drawBitmap(this.temperatureIcon, settings.temperatureIconLeft, settings.temperatureIconTop, settings.mGPaint);
            }

            String units = (settings.temperatureUnits) ? weather.getUnits() : ""; //"ºC"
            canvas.drawText(weather.getTemperature() + units, settings.temperatureLeft, settings.temperatureTop, temperaturePaint);
        }

        // Draw City
        if(settings.city>0) {
            if(settings.cityIcon){
                canvas.drawBitmap(this.cityIcon, settings.cityIconLeft, settings.cityIconTop, settings.mGPaint);
            }
            canvas.drawText(weather.city, settings.cityLeft, settings.cityTop, cityPaint);
        }

        // Draw Humidity
        if(settings.humidity>0) {
            if(settings.humidityIcon){
                canvas.drawBitmap(this.humidityIcon, settings.humidityIconLeft, settings.humidityIconTop, settings.mGPaint);
            }
            canvas.drawText(weather.humidity, settings.humidityLeft, settings.humidityTop, humidityPaint);
        }

        // Draw UV ray
        if(settings.uv>0) {
            if(settings.uvIcon){
                canvas.drawBitmap(this.uvIcon, settings.uvIconLeft, settings.uvIconTop, settings.mGPaint);
            }
            canvas.drawText(weather.uv, settings.uvLeft, settings.uvTop, uvPaint);
        }

        // Draw Wind Direction
        if(settings.wind_direction>0) {
            if(settings.wind_directionIcon){
                canvas.drawBitmap(this.wind_directionIcon, settings.wind_directionIconLeft, settings.wind_directionIconTop, settings.mGPaint);
            }
            canvas.drawText((settings.wind_direction_as_arrows)?weather.windArrow:weather.windDirection, settings.wind_directionLeft, settings.wind_directionTop, wind_directionPaint);
        }

        // Draw Wind Strength
        if(settings.wind_strength>0) {
            if(settings.wind_strengthIcon){
                canvas.drawBitmap(this.wind_strengthIcon, settings.wind_strengthIconLeft, settings.wind_strengthIconTop, settings.mGPaint);
            }
            canvas.drawText(weather.windStrength, settings.wind_strengthLeft, settings.wind_strengthTop, wind_strengthPaint);
        }

        // Draw Weather icon
        if(settings.weather_img>0) {
            canvas.drawBitmap(this.weatherImageIcon, settings.weather_imgIconLeft, settings.weather_imgIconTop, settings.mGPaint);
            if(settings.weather_imgIcon) {//In the weather image widget, if icon is disabled, temperature is not shown!
                String units = (settings.weather_imgUnits) ? weather.getUnits() : ""; //"ºC"
                canvas.drawText(weather.getTemperature() + units, settings.weather_imgLeft, settings.weather_imgTop, weather_imgPaint);
            }
        }
    }

    /* Get Weather Data on screen off
    based on HuamiWatchFaces.jar!\com\huami\watch\watchface\widget\slpt\SlptWeatherWidget.class
    and AmazfitWeather.jar!\com\huami\watch\weather\WeatherUtil.class */
    public WeatherData getSlptWeather() {
        // Default variables
        String tempUnit = "1";
        String temp = "n/a";
        int weatherType = 22;
        String city = "n/a";
        String humidity = "n/a";
        String uv = "n/a";
        String windDirection = "n/a";
        String windStrength = "n/a";

        // Get ALL data from system
        String str = Settings.System.getString(this.mService.getApplicationContext().getContentResolver(), "WeatherInfo");

        // WeatherInfo
        // {"isAlert":true, "isNotification":true, "tempFormatted":"28ºC",
        // "tempUnit":"C", "v":1, "weatherCode":0, "aqi":-1, "aqiLevel":0, "city":"Somewhere",
        // "forecasts":[{"tempFormatted":"31ºC/21ºC","tempMax":31,"tempMin":21,"weatherCodeFrom":0,"weatherCodeTo":0,"day":1,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"33ºC/23ºC","tempMax":33,"tempMin":23,"weatherCodeFrom":0,"weatherCodeTo":0,"day":2,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"34ºC/24ºC","tempMax":34,"tempMin":24,"weatherCodeFrom":0,"weatherCodeTo":0,"day":3,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"34ºC/23ºC","tempMax":34,"tempMin":23,"weatherCodeFrom":0,"weatherCodeTo":0,"day":4,"weatherFrom":0,"weatherTo":0},{"tempFormatted":"32ºC/22ºC","tempMax":32,"tempMin":22,"weatherCodeFrom":0,"weatherCodeTo":0,"day":5,"weatherFrom":0,"weatherTo":0}],
        // "pm25":-1, "sd":"50%", //(Humidity)
        // "temp":28, "time":1531292274457, "uv":"Strong",
        // "weather":0, "windDirection":"NW", "windStrength":"7.4km/h"}

        // WeatherCheckedSummary
        // {"tempUnit":"1","temp":"31\/21","weatherCodeFrom":0}

        // Extract data from JSON
        JSONObject weather_data;
        try {
            weather_data = new JSONObject(str);
            tempUnit = weather_data.getString("tempUnit");
            temp = weather_data.getString("temp");
            //weatherType = weather_data.getInt("weatherCodeFrom");
            weatherType = weather_data.getInt("weatherCode");
            city = weather_data.getString("city");
            humidity = weather_data.getString("sd");
            uv = weather_data.getString("uv");
            windDirection = weather_data.getString("windDirection");
            windStrength = weather_data.getString("windStrength");
        }
        catch (JSONException e) {
          // Nothing
        }

        // Unknown weather
        if(weatherType<0 || weatherType>22){
            return new WeatherData("1", "n/a", 22);
        }

        return new WeatherData(tempUnit, temp, weatherType, city, humidity, uv, windDirection, windStrength);
    }

    // Screen-off (SLPT)
    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        return buildSlptViewComponent(service, false);
    }

    // Screen-off (SLPT) - Better screen quality
    public List<SlptViewComponent> buildSlptViewComponent(Service service, boolean better_resolution) {
        better_resolution = better_resolution && settings.better_resolution_when_raising_hand;
        List<SlptViewComponent> slpt_objects = new ArrayList<>();

        // Get weather data
        this.mService = service;
        this.weather = getSlptWeather();


        // Draw temperature
        if(settings.temperature>0){
            // Show or Not icon
            if (settings.temperatureIcon) {
                SlptPictureView temperatureIcon = new SlptPictureView();
                temperatureIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/temperature.png") );
                temperatureIcon.setStart(
                        (int) settings.temperatureIconLeft,
                        (int) settings.temperatureIconTop
                );
                slpt_objects.add(temperatureIcon);
            }

            SlptLinearLayout temperatureLayout = new SlptLinearLayout();
            // Show temperature with units or not
            SlptPictureView temperatureNum = new SlptPictureView();
            temperatureNum.setStringPicture(this.weather.tempString + (settings.temperatureUnits ? this.weather.getUnits() : ""));
            temperatureNum.setTextAttr(
                    settings.temperatureFontSize,
                    settings.temperatureColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            temperatureLayout.add(temperatureNum);
            // Position based on screen on
            temperatureLayout.alignX = 2;
            temperatureLayout.alignY = 0;
            int tmp_left = (int) settings.temperatureLeft;
            if(!settings.temperatureAlignLeft) {
                // If text is centered, set rectangle
                temperatureLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (settings.temperatureFontSize)
                );
                tmp_left = -320;
            }
            temperatureLayout.setStart(
                    (int) tmp_left,
                    (int) (settings.temperatureTop-((float)settings.font_ratio/100)*settings.temperatureFontSize)
            );
            slpt_objects.add(temperatureLayout);
        }

        // Weather Icons
        if(settings.weather_img>0){
            SlptPictureView weatherIcon = new SlptPictureView();
            weatherIcon.setImagePicture( SimpleFile.readFileFromAssets(service, String.format(( (better_resolution)?"":"slpt_" )+"weather/%s.png", this.weatherImageStrList.get(this.weather.weatherType))) );
            weatherIcon.setStart(
                    (int) settings.weather_imgIconLeft,
                    (int) settings.weather_imgIconTop
            );
            slpt_objects.add(weatherIcon);

            if(settings.weather_imgIcon) {//In the weather image widget, if icon is disabled, temperature is not shown!
                SlptLinearLayout weatherLayout = new SlptLinearLayout();
                // Show temperature with units or not
                SlptPictureView weather_imgNum = new SlptPictureView();
                weather_imgNum.setStringPicture(this.weather.tempString + (settings.weather_imgUnits ? this.weather.getUnits() : ""));
                weather_imgNum.setTextAttr(
                        settings.weather_imgFontSize,
                        settings.weather_imgColor,
                        ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
                );
                weatherLayout.add(weather_imgNum);
                // Position based on screen on
                weatherLayout.alignX = 2;
                weatherLayout.alignY = 0;
                int tmp_left = (int) settings.weather_imgLeft;
                if (!settings.weather_imgAlignLeft) {
                    // If text is centered, set rectangle
                    weatherLayout.setRect(
                            (int) (2 * tmp_left + 640),
                            (int) (settings.weather_imgFontSize)
                    );
                    tmp_left = -320;
                }
                weatherLayout.setStart(
                        (int) tmp_left,
                        (int) (settings.weather_imgTop - ((float) settings.font_ratio / 100) * settings.weather_imgFontSize)
                );
                slpt_objects.add(weatherLayout);
            }
        }

        // Draw City
        if(settings.city>0){
            // Show or Not icon
            if (settings.cityIcon) {
                SlptPictureView cityIcon = new SlptPictureView();
                cityIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/city.png") );
                cityIcon.setStart(
                        (int) settings.cityIconLeft,
                        (int) settings.cityIconTop
                );
                slpt_objects.add(cityIcon);
            }

            SlptLinearLayout cityLayout = new SlptLinearLayout();
            SlptPictureView cityText = new SlptPictureView();
            cityText.setStringPicture(this.weather.city);
            cityText.setTextAttr(
                    settings.cityFontSize,
                    settings.cityColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            cityLayout.add(cityText);
            cityLayout.setTextAttrForAll(
                    settings.cityFontSize,
                    settings.cityColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            // Position based on screen on
            cityLayout.alignX = 2;
            cityLayout.alignY = 0;
            int tmp_left = (int) settings.cityLeft;
            if(!settings.cityAlignLeft) {
                // If text is centered, set rectangle
                cityLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (settings.cityFontSize)
                );
                tmp_left = -320;
            }
            cityLayout.setStart(
                    (int) tmp_left,
                    (int) (settings.cityTop-((float)settings.font_ratio/100)*settings.cityFontSize)
            );
            slpt_objects.add(cityLayout);
        }

        // Draw Humidity
        if(settings.humidity>0){
            // Show or Not icon
            if (settings.humidityIcon) {
                SlptPictureView humidityIcon = new SlptPictureView();
                humidityIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/humidity.png") );
                humidityIcon.setStart(
                        (int) settings.humidityIconLeft,
                        (int) settings.humidityIconTop
                );
                slpt_objects.add(humidityIcon);
            }

            SlptLinearLayout humidityLayout = new SlptLinearLayout();
            SlptPictureView humidityNum = new SlptPictureView();
            humidityNum.setStringPicture(this.weather.humidity);
            humidityNum.setTextAttr(
                    settings.humidityFontSize,
                    settings.humidityColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            humidityLayout.add(humidityNum);
            humidityLayout.setTextAttrForAll(
                    settings.humidityFontSize,
                    settings.humidityColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            // Position based on screen on
            humidityLayout.alignX = 2;
            humidityLayout.alignY = 0;
            int tmp_left = (int) settings.humidityLeft;
            if(!settings.humidityAlignLeft) {
                // If text is centered, set rectangle
                humidityLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (settings.humidityFontSize)
                );
                tmp_left = -320;
            }
            humidityLayout.setStart(
                    (int) tmp_left,
                    (int) (settings.humidityTop-((float)settings.font_ratio/100)*settings.humidityFontSize)
            );
            slpt_objects.add(humidityLayout);
        }

        // Draw UV rays (Strong)
        if(settings.uv>0){
            // Show or Not icon
            if (settings.uvIcon) {
                SlptPictureView uvIcon = new SlptPictureView();
                uvIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/uv.png") );
                uvIcon.setStart(
                        (int) settings.uvIconLeft,
                        (int) settings.uvIconTop
                );
                slpt_objects.add(uvIcon);
            }

            SlptLinearLayout uvLayout = new SlptLinearLayout();
            SlptPictureView uvNum = new SlptPictureView();
            uvNum.setStringPicture(this.weather.uv);
            uvNum.setTextAttr(
                    settings.uvFontSize,
                    settings.uvColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            uvLayout.add(uvNum);
            uvLayout.setTextAttrForAll(
                    settings.uvFontSize,
                    settings.uvColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            // Position based on screen on
            uvLayout.alignX = 2;
            uvLayout.alignY = 0;
            int tmp_left = (int) settings.uvLeft;
            if(!settings.uvAlignLeft) {
                // If text is centered, set rectangle
                uvLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (settings.uvFontSize)
                );
                tmp_left = -320;
            }
            uvLayout.setStart(
                    (int) tmp_left,
                    (int) (settings.uvTop-((float)settings.font_ratio/100)*settings.uvFontSize)
            );
            slpt_objects.add(uvLayout);
        }

        // Draw Wind Direction
        if(settings.wind_direction>0){
            // Show or Not icon
            if (settings.wind_directionIcon) {
                SlptPictureView wind_directionIcon = new SlptPictureView();
                wind_directionIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/wind_direction.png") );
                wind_directionIcon.setStart(
                        (int) settings.wind_directionIconLeft,
                        (int) settings.wind_directionIconTop
                );
                slpt_objects.add(wind_directionIcon);
            }
            SlptLinearLayout wind_directionLayout = new SlptLinearLayout();
            SlptPictureView wind_directionText = new SlptPictureView();
            // todo
            wind_directionText.setStringPicture( (settings.wind_direction_as_arrows)? this.weather.windArrow : this.weather.windDirection );
            wind_directionText.setTextAttr(
                    settings.wind_directionFontSize,
                    settings.wind_directionColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            wind_directionLayout.add(wind_directionText);
            wind_directionLayout.setTextAttrForAll(
                    settings.wind_directionFontSize,
                    settings.wind_directionColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            // Position based on screen on
            wind_directionLayout.alignX = 2;
            wind_directionLayout.alignY = 0;
            int tmp_left = (int) settings.wind_directionLeft;
            if(!settings.wind_directionAlignLeft) {
                // If text is centered, set rectangle
                wind_directionLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (settings.wind_directionFontSize)
                );
                tmp_left = -320;
            }
            wind_directionLayout.setStart(
                    (int) tmp_left,
                    (int) (settings.wind_directionTop-((float)settings.font_ratio/100)*settings.wind_directionFontSize)
            );
            slpt_objects.add(wind_directionLayout);
        }

        // Draw Wind Strength (ex. 7.4km/h)
        if(settings.wind_strength>0){
            // Show or Not icon
            if (settings.wind_strengthIcon) {
                SlptPictureView wind_strengthIcon = new SlptPictureView();
                wind_strengthIcon.setImagePicture( SimpleFile.readFileFromAssets(service, ( (better_resolution)?"":"slpt_" )+"icons/wind_strength.png") );
                wind_strengthIcon.setStart(
                        (int) settings.wind_strengthIconLeft,
                        (int) settings.wind_strengthIconTop
                );
                slpt_objects.add(wind_strengthIcon);
            }
            SlptLinearLayout wind_strengthLayout = new SlptLinearLayout();
            SlptPictureView wind_strengthText = new SlptPictureView();
            wind_strengthText.setStringPicture(this.weather.windStrength);
            wind_strengthText.setTextAttr(
                    settings.wind_strengthFontSize,
                    settings.wind_strengthColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            wind_strengthLayout.add(wind_strengthText);
            wind_strengthLayout.setTextAttrForAll(
                    settings.wind_strengthFontSize,
                    settings.wind_strengthColor,
                    ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.FONT_FILE)
            );
            // Position based on screen on
            wind_strengthLayout.alignX = 2;
            wind_strengthLayout.alignY = 0;
            int tmp_left = (int) settings.wind_strengthLeft;
            if(!settings.wind_strengthAlignLeft) {
                // If text is centered, set rectangle
                wind_strengthLayout.setRect(
                        (int) (2 * tmp_left + 640),
                        (int) (settings.wind_strengthFontSize)
                );
                tmp_left = -320;
            }
            wind_strengthLayout.setStart(
                    (int) tmp_left,
                    (int) (settings.wind_strengthTop-((float)settings.font_ratio/100)*settings.wind_strengthFontSize)
            );
            slpt_objects.add(wind_strengthLayout);
        }

        return slpt_objects;
    }
}
