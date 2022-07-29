package com.example.eslah.custom_map;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import static android.content.ContentValues.TAG;

public class WrapperView extends FrameLayout {

    private GoogleMap googleMap;

    Activity activity = null;

    ScaleGestureDetector scaleGestureDetector;

    public WrapperView(Activity activity) {
        super(activity);
        this.activity=activity;
        scaleGestureDetector = new ScaleGestureDetector(activity ,new MyOnScaleGestureListener());
    }

    public void setGoogleMap(GoogleMap map){
        googleMap = map;
    }

    private boolean isZoomInProgress(MotionEvent event){
        if(event.getPointerCount()>1){
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        return isZoomInProgress(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return scaleGestureDetector.onTouchEvent(event);
    }

    public class MyOnScaleGestureListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float previousSpan = detector.getPreviousSpan();
            float currentSpan = detector.getCurrentSpan();
            float targetSpan;
            if (previousSpan > currentSpan) {
                targetSpan = previousSpan - currentSpan;
            } else {
                targetSpan = currentSpan - previousSpan;
            }
            float scaleFactor = detector.getScaleFactor();
            if (scaleFactor > 1) {
                if (googleMap.getCameraPosition().zoom != googleMap.getMaxZoomLevel()) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom + 0.05f));


                }
            } else {
                if (googleMap.getCameraPosition().zoom != googleMap.getMinZoomLevel()) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, googleMap.getCameraPosition().zoom - 0.05f));

                }
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

}