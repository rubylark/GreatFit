package com.dinodevs.greatfitweatherland;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dinodevs.greatfitweatherland.settings.LoadSettings;
import com.dinodevs.greatfitweatherland.widget.BatteryWidget;
import com.dinodevs.greatfitweatherland.widget.CaloriesWidget;
import com.dinodevs.greatfitweatherland.widget.FloorWidget;
import com.dinodevs.greatfitweatherland.widget.HeartRateWidget;
import com.dinodevs.greatfitweatherland.widget.MainClock;
import com.dinodevs.greatfitweatherland.widget.GreatWidget;
import com.dinodevs.greatfitweatherland.widget.SportTodayDistanceWidget;
import com.dinodevs.greatfitweatherland.widget.SportTotalDistanceWidget;
import com.dinodevs.greatfitweatherland.widget.StepsWidget;
import com.dinodevs.greatfitweatherland.widget.WeatherWidget;
import com.dinodevs.greatfitweatherland.widget.Widget;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

/**
 * Splt version of the watch.
 */

public class GreatFitWeatherLandSlpt extends AbstractWatchFaceSlpt {
    Context context;
    public GreatFitWeatherLandSlpt() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this.getApplicationContext();

        // Load settings
        LoadSettings settings = new LoadSettings(context);

        this.clock = new MainClock(settings);

        if(settings.isHeartRate()) {
            this.widgets.add(new HeartRateWidget(settings));
        }
        if(settings.isStepsRate()) {
            this.widgets.add(new StepsWidget(settings));
        }
        if(settings.isTodayDistanceRate()) {
            this.widgets.add(new SportTodayDistanceWidget(settings));
        }
        if(settings.isTotalDistanceRate()) {
            this.widgets.add(new SportTotalDistanceWidget(settings));
        }
        if(settings.isCalories()) {
            this.widgets.add(new CaloriesWidget(settings));
        }
        if(settings.isFloor()) {
            this.widgets.add(new FloorWidget(settings));
        }
        if(settings.isBattery()) {
            this.widgets.add(new BatteryWidget(settings));
        }
        if(settings.isWeather()) {
            this.widgets.add(new WeatherWidget(settings));
        }
        if(settings.isGreat()) {
            this.widgets.add(new GreatWidget(settings));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected SlptLayout createClockLayout26WC() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();
        for (SlptViewComponent component : clock.buildSlptViewComponent(this, true)) {
            result.add(component);
        }
        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this, true)) {
                result.add(component);
            }
        }

        return result;
    }

    @Override
    protected SlptLayout createClockLayout8C() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();
        for (SlptViewComponent component : clock.buildSlptViewComponent(this)) {
            result.add(component);
        }
        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
        }

        return result;
    }


    protected void initWatchFaceConfig() {
        Log.w("DinoDevs-GreatFit", "Initiating watchface");

        //this.getResources().getBoolean(R.bool.seconds)

        Context context = this.getApplicationContext();
        boolean needRefreshSecond = Util.needSlptRefreshSecond(context);
        if (needRefreshSecond) {
            this.setClockPeriodSecond(true);
        }
    }
}