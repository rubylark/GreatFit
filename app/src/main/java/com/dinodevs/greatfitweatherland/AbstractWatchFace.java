package com.dinodevs.greatfitweatherland;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;

import com.dinodevs.greatfitweatherland.data.DataType;
import com.dinodevs.greatfitweatherland.data.MultipleWatchDataListenerAdapter;
import com.dinodevs.greatfitweatherland.widget.AnalogClockWidget;
import com.dinodevs.greatfitweatherland.widget.ClockWidget;
import com.dinodevs.greatfitweatherland.widget.DigitalClockWidget;
import com.dinodevs.greatfitweatherland.widget.Widget;

/**
 * Abstract base class for watch faces
 */
public abstract class AbstractWatchFace extends com.huami.watch.watchface.AbstractWatchFace {


    public ClockWidget clock;
    final LinkedList<Widget> widgets = new LinkedList<>();
    private Intent slptIntent;

    private class DigitalEngine extends com.huami.watch.watchface.AbstractWatchFace.DigitalEngine {

        private final DigitalClockWidget widget;

        public DigitalEngine(DigitalClockWidget widget) {
            this.widget = widget;
        }

        @Override
        protected void onPrepareResources(Resources resources) {
            this.widget.init(AbstractWatchFace.this);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                widget.init(AbstractWatchFace.this);
                for (DataType type : widget.getDataTypes()) {
                    registerWatchDataListener(new MultipleWatchDataListenerAdapter(widget, type));
                }
            }
        }

        @Override
        protected void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm) {
            widget.onDrawDigital(canvas, width, height, centerX, centerY, seconds, minutes, hours, year, month, day, week, ampm);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                canvas.translate(widget.getX(), widget.getY());
                widget.draw(canvas, width, height, centerX, centerY);
                canvas.translate(-widget.getX(), -widget.getY());
            }
        }
    }

    private class AnalogEngine extends com.huami.watch.watchface.AbstractWatchFace.AnalogEngine {

        private final AnalogClockWidget widget;

        public AnalogEngine(AnalogClockWidget widget) {
            this.widget = widget;
        }

        @Override
        protected void onPrepareResources(Resources resources) {
            this.widget.init(AbstractWatchFace.this);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                widget.init(AbstractWatchFace.this);
                for (DataType type : widget.getDataTypes()) {
                    registerWatchDataListener(new MultipleWatchDataListenerAdapter(widget, type));
                }
            }
        }

        @Override
        protected void onDrawAnalog(Canvas canvas, float width, float height, float centerX, float centerY, float secRot, float minRot, float hrRot) {
            widget.onDrawAnalog(canvas, width, height, centerX, centerY, secRot, minRot, hrRot);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                canvas.translate(widget.getX(), widget.getY());
                widget.draw(canvas, width, height, centerX, centerY);
                canvas.translate(-widget.getX(), -widget.getY());
            }
        }
    }


    protected AbstractWatchFace(ClockWidget clock, Widget... widgets) {
        this.clock = clock;
        this.widgets.addAll(Arrays.asList(widgets));
    }

    protected AbstractWatchFace() {}

    // Status bar (ex.battery charging)
    public final Engine onCreateEngine() {

        return AnalogClockWidget.class.isInstance(this.clock) ? new AnalogEngine((AnalogClockWidget) this.clock) : new DigitalEngine((DigitalClockWidget) this.clock);
    }

    public void onCreate() {
        super.onCreate();
        this.slptIntent = new Intent(this, this.slptClockClass());
    }

    public void restartSlpt(){
        // Sent some stuff
        //Bundle b = new Bundle();
        //b.putSerializable("key", this.widgets);
        //this.slptIntent.putExtras(b); //Put your id to your next Intent

        // Start Slpt
        try {
            this.stopService(this.slptIntent);
            this.startService(this.slptIntent);
            Log.w("DinoDevs-GreatFit", "Slpt service restarted" );
        }catch(Exception e){
            Log.w("DinoDevs-GreatFit", "Problem restarting slpt: "+e.toString() );
        }
    }
}
