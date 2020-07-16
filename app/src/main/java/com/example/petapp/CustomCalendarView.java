package com.example.petapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import androidx.core.view.GestureDetectorCompat;

import java.util.Date;

public class CustomCalendarView extends View {
    
    private CustomCalendarController eController;
    private GestureDetectorCompat eGestureDetector;
    
    public CustomCalendarView(Context iContext, AttributeSet attrs, int defStyleAttr) {
        super(iContext, attrs, defStyleAttr);
        eController = new CustomCalendarController(new OverScroller(getContext()), new Rect(), getContext(), VelocityTracker.obtain());
        GestureDetector.SimpleOnGestureListener iGestureListener = new GestureDetector.SimpleOnGestureListener() {
            
            @Override
            public boolean onDown(MotionEvent iEvent) {
                return true;
            }
            
            @Override
            public boolean onSingleTapUp(MotionEvent iEvent) {
                eController.tap(iEvent);
                invalidate();
                return super.onSingleTapUp(iEvent);
                
            }
            
            @Override
            public boolean onScroll(MotionEvent iEvent1, MotionEvent iEvent2, float iX, float iY) {
                eController.checkScroll(iX);
                invalidate();
                return true;
                
            }
            
        };
        eGestureDetector = new GestureDetectorCompat(getContext(), iGestureListener);
        
    }
    
    public CustomCalendarView(Context iContext) {
        this(iContext, null);
    }
    
    public CustomCalendarView(Context iContext, AttributeSet iAttrs) {
        this(iContext, iAttrs, 0);
    }
    
    public void setDate(Date iDate) {
        eController.setDate(iDate);
        invalidate();
        
    }
    
    public void setListener(CustomCalendarViewListener iListener) {
        eController.setListener(iListener);
    }
    
    @Override
    protected void onDraw(Canvas iCanvas) {
        eController.drawCalendar(iCanvas);
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent iEvent) {
        eController.touch(iEvent);
        invalidate();
        return eGestureDetector.onTouchEvent(iEvent);
        
    }
    
    @Override
    protected void onMeasure(int iParentWidth, int iParentHeight) {
        super.onMeasure(iParentWidth, iParentHeight);
        int iWidth = MeasureSpec.getSize(iParentWidth);
        int iHeight = MeasureSpec.getSize(iParentHeight);
        eController.measure(iWidth, iHeight, getPaddingRight(), getPaddingLeft());
        
    }
    
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (eController.computeScrollOffset())
            invalidate();
        
    }
    
    public interface CustomCalendarViewListener {
        
        void onDaySelected(Date dateClicked);
        
        void onScroll(Date firstDayOfNewMonth);
        
    }
    
}
