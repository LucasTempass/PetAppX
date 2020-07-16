package com.example.petapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.OverScroller;

import androidx.core.content.res.ResourcesCompat;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

class CustomCalendarController {
    
    private int ePaddingWidth = 40;
    private int ePaddingHeight = 40;
    private int eTxtHeight;
    private int eWidthPerDay;
    private int eCurrentMonth;
    private int eHeightPerDay;
    private int eTxtSize;
    private int eWidth;
    private int ePaddingRight;
    private int ePaddingLeft;
    private int eThreshold;
    private float eDistanceX;
    private boolean eIsSmoothScrolling;
    private boolean eIsScrolling;
    private CustomCalendarView.CustomCalendarViewListener eListener;
    private VelocityTracker eVelocityTracker;
    private boolean eIsHorizontal = false;
    private Date eDate = new Date();
    private Calendar eCalendar;
    private Calendar eCalendar2day;
    private Calendar eCalendarSettable;
    private Calendar ePreviousCalendar;
    private PointF eScrolledPosition = new PointF();
    private OverScroller eScroller;
    private Paint eCirclePaint = new Paint();
    private Paint eDayPaint = new Paint();
    private Rect eTxtSizeRect;
    private String[] eDayColumnNames;
    private int eColorDarkerGrey;
    private int eColorWhite;
    private Typeface eDayTypeFace;
    private int eColorPrimary;
    private int eColorGrey;
    private Typeface eMonthTypeFace;
    
    CustomCalendarController(OverScroller iScroller, Rect iTxtSizeRect, Context iContext, VelocityTracker iVelocityTracker) {
        this.eScroller = iScroller;
        this.eTxtSizeRect = iTxtSizeRect;
        this.eVelocityTracker = iVelocityTracker;
        eColorDarkerGrey = iContext.getResources().getColor(R.color.darker_gray);
        eColorWhite = iContext.getResources().getColor(R.color.white);
        eColorPrimary = iContext.getResources().getColor(R.color.colorPrimary);
        eColorGrey = iContext.getResources().getColor(R.color.grey);
        eTxtSize = 30;
        init(iContext);
        
    }
    
    private void init(Context iContext) {
        eCalendar = Calendar.getInstance();
        eCalendar2day = Calendar.getInstance();
        eCalendarSettable = Calendar.getInstance();
        ePreviousCalendar = Calendar.getInstance();
        ePreviousCalendar.setMinimalDaysInFirstWeek(1);
        eCalendarSettable.setMinimalDaysInFirstWeek(1);
        eCalendar2day.setMinimalDaysInFirstWeek(1);
        eCalendar.setMinimalDaysInFirstWeek(1);
        eDayColumnNames = getWeekNames();
        setFirstDayOfWeek();
        eDayPaint.setTextAlign(Paint.Align.CENTER);
        eDayPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        eDayTypeFace = ResourcesCompat.getFont(iContext, R.font.oswald_light);
        eMonthTypeFace = ResourcesCompat.getFont(iContext, R.font.grand);
        eDayPaint.setTypeface(eDayTypeFace);
        eDayPaint.setTextSize(eTxtSize);
        eDayPaint.setColor(eColorDarkerGrey);
        eDayPaint.getTextBounds("31", 0, "31".length(), eTxtSizeRect);
        eTxtHeight = eTxtSizeRect.height() * 3;
        eCalendar2day.setTime(new Date());
        eCalendar.setTime(eDate);
        set2Month(eCurrentMonth);
        if (eListener != null)
            eListener.onDaySelected(eCalendar.getTime());
        
    }
    
    void setDate(Date iTime) {
        eDistanceX = 0;
        eCurrentMonth = 0;
        eScrolledPosition.x = 0;
        eScroller.startScroll(0, 0, 0, 0);
        eDate = iTime;
        eCalendar.setTime(eDate);
        eCalendar2day = Calendar.getInstance();
        
    }
    
    private void set2Month(int iCurrent, int iMonth) {
        eCalendarSettable.setTime(eDate);
        eCalendarSettable.add(Calendar.MONTH, iCurrent + iMonth);
        eCalendarSettable.set(Calendar.HOUR_OF_DAY, 0);
        eCalendarSettable.set(Calendar.DAY_OF_MONTH, 1);
        
    }
    
    private void set2Month(int iCurrent) {
        eCalendarSettable.setTime(eDate);
        eCalendarSettable.add(Calendar.MONTH, iCurrent);
        eCalendarSettable.set(Calendar.HOUR_OF_DAY, 0);
        eCalendarSettable.set(Calendar.DAY_OF_MONTH, 1);
        
    }
    
    void setListener(CustomCalendarView.CustomCalendarViewListener iListener) {
        this.eListener = iListener;
    }
    
    private void setFirstDayOfWeek() {
        eDayColumnNames = getWeekNames();
        eCalendarSettable.setFirstDayOfWeek(Calendar.MONDAY);
        eCalendar2day.setFirstDayOfWeek(Calendar.MONDAY);
        eCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        ePreviousCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        
    }
    
    private boolean isScrolling() {
        float scrolledX = Math.abs(eScrolledPosition.x);
        int expectedScrollX = Math.abs(eWidth * eCurrentMonth);
        return scrolledX < expectedScrollX - 5 || scrolledX > expectedScrollX + 5;
        
    }
    
    void checkScroll(float iDistance) {
        if (eIsSmoothScrolling)
            return;
        if (!eIsHorizontal)
            eIsHorizontal = true;
        eIsScrolling = true;
        this.eDistanceX = iDistance;
        
    }
    
    private void cancelScroll() {
        float iDifference = (eScrolledPosition.x - (-eCurrentMonth * eWidth));
        eScroller.startScroll((int) eScrolledPosition.x, 0, (int) -iDifference, 0);
        eIsSmoothScrolling = false;
        
    }
    
    private void handleScrolling() {
        scrollSmooth();
        eIsHorizontal = false;
        set2Month(eCurrentMonth);
        eCalendarSettable.get(Calendar.MONTH);
        eCalendar.get(Calendar.MONTH);
        
    }
    
    boolean computeScrollOffset() {
        if (eScroller.computeScrollOffset()) {
            eScrolledPosition.x = eScroller.getCurrX();
            return true;
        }
        return false;
        
    }
    
    private String[] getWeekNames() {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] iDayNames = dateFormatSymbols.getShortWeekdays();
        return new String[]{iDayNames[2].substring(0, 1), iDayNames[3].substring(0, 1),
                iDayNames[4].substring(0, 1), iDayNames[5].substring(0, 1),
                iDayNames[6].substring(0, 1), iDayNames[7].substring(0, 1), iDayNames[1].substring(0, 1)};
        
    }
    
    private Date getFirstDayCurrentMonth() {
        Calendar iCalendar = Calendar.getInstance();
        iCalendar.setTime(eDate);
        iCalendar.add(Calendar.MONTH, eCurrentMonth);
        iCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return iCalendar.getTime();
        
    }
    
    private int getWeekDay(Calendar iCalendar) {
        int eDayOfWeek = iCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        eDayOfWeek = eDayOfWeek < 0 ? 7 + eDayOfWeek : eDayOfWeek;
        return eDayOfWeek;
        
    }
    
    private void drawMonth(Canvas iCanvas, Calendar iMonth, int iOffset) {
        boolean isSameMonthAsToday = iMonth.get(Calendar.MONTH) == eCalendar2day.get(Calendar.MONTH);
        boolean isSameYearAsToday = iMonth.get(Calendar.YEAR) == eCalendar2day.get(Calendar.YEAR);
        boolean isSameMonthAsCurrentCalendar = iMonth.get(Calendar.MONTH) == eCalendar.get(Calendar.MONTH) && iMonth.get(Calendar.YEAR) == eCalendar
                .get(Calendar.YEAR);
        int todayDayOfMonth = eCalendar2day.get(Calendar.DAY_OF_MONTH);
        int iLastDayMonth = iMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        ePreviousCalendar.setTimeInMillis(iMonth.getTimeInMillis());
        ePreviousCalendar.add(Calendar.MONTH, -1);
        int iLastPreviousDay = ePreviousCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int eDayColumn = 0, colDirection = 0, eDayRow = 0; eDayColumn <= 6; eDayRow++) {
            if (eDayRow == 7) {
                colDirection++;
                eDayRow = 0;
                eDayColumn++;
            }
            if (eDayColumn == eDayColumnNames.length)
                break;
            float xPosition = eWidthPerDay * eDayColumn + ePaddingWidth + ePaddingLeft + eScrolledPosition.x + iOffset - ePaddingRight;
            float yPosition = eDayRow * eHeightPerDay + ePaddingHeight;
            if (eDayRow == 0) {
                eDayPaint.setColor(eColorPrimary);
                eDayPaint.setTypeface(eMonthTypeFace);
                iCanvas.drawText(eDayColumnNames[colDirection], xPosition, ePaddingHeight, eDayPaint);
            }
            else {
                eDayPaint.setTypeface(eDayTypeFace);
                int iDay = ((eDayRow - 1) * 7 + colDirection + 1) - getWeekDay(iMonth);
                int iColor = eColorDarkerGrey;
                if (eCalendar.get(Calendar.DAY_OF_MONTH) == iDay && isSameMonthAsCurrentCalendar) {
                    drawCircle(iCanvas, xPosition, yPosition, eColorPrimary);
                    iColor = eColorWhite;
                }
                else if (isSameYearAsToday && isSameMonthAsToday && todayDayOfMonth == iDay) {
                    drawCircle(iCanvas, xPosition, yPosition, eColorDarkerGrey);
                    iColor = eColorWhite;
                }
                if (iDay <= 0) {
                    eDayPaint.setColor(eColorGrey);
                    iCanvas.drawText(String.valueOf(iLastPreviousDay + iDay), xPosition, yPosition, eDayPaint);
                }
                else if (iDay > iLastDayMonth) {
                    eDayPaint.setColor(eColorGrey);
                    iCanvas.drawText(String.valueOf(iDay - iLastDayMonth), xPosition, yPosition, eDayPaint);
                }
                else {
                    eDayPaint.setColor(iColor);
                    iCanvas.drawText(String.valueOf(iDay), xPosition, yPosition, eDayPaint);
                }
            }
        }
        
    }
    
    private void drawCircle(Canvas iCanvas, float x, float y, int iColor) {
        eCirclePaint.setStyle(Paint.Style.FILL);
        eCirclePaint.setColor(iColor);
        iCanvas.drawCircle(x, y - (eTxtHeight / 6f), (float)
                //||| Calculates diagonal using Pythagorean Theorem and then halves it |||
                (0.25 * Math.sqrt((eHeightPerDay * eHeightPerDay) + (eHeightPerDay * eHeightPerDay))), eCirclePaint);
        
    }
    
    void drawCalendar(Canvas iCanvas) {
        ePaddingWidth = eWidthPerDay / 2;
        ePaddingHeight = eHeightPerDay / 2;
        if (eIsHorizontal)
            eScrolledPosition.x -= eDistanceX;
        eDayPaint.setStyle(Paint.Style.STROKE);
        eDayPaint.setColor(Color.WHITE);
        eDayPaint.setTypeface(eDayTypeFace);
        set2Month(eCurrentMonth, -1);
        drawMonth(iCanvas, eCalendarSettable, (eWidth * (eCurrentMonth - 1)));
        set2Month(eCurrentMonth);
        drawMonth(iCanvas, eCalendarSettable, eWidth * eCurrentMonth);
        set2Month(eCurrentMonth, 1);
        drawMonth(iCanvas, eCalendarSettable, (eWidth * (eCurrentMonth + 1)));
        
    }
    
    void touch(MotionEvent iEvent) {
        if (eVelocityTracker == null)
            eVelocityTracker = VelocityTracker.obtain();
        eVelocityTracker.addMovement(iEvent);
        if (iEvent.getAction() == MotionEvent.ACTION_UP) {
            handleScrolling();
            eVelocityTracker.recycle();
            eVelocityTracker.clear();
            eVelocityTracker = null;
            eIsScrolling = false;
        }
        
    }
    
    void tap(MotionEvent e) {
        if (isScrolling())
            return;
        int eDayColumn = Math.round((ePaddingLeft + e.getX() - ePaddingWidth - ePaddingRight) / eWidthPerDay);
        int eDayRow = Math.round((e.getY() - ePaddingHeight) / eHeightPerDay);
        set2Month(eCurrentMonth);
        int eDayOfMonth = ((eDayRow - 1) * 7) - getWeekDay(eCalendarSettable);
        eDayOfMonth += eDayColumn;
        if (eDayOfMonth < eCalendarSettable.getActualMaximum(Calendar.DAY_OF_MONTH) && eDayOfMonth >= 0) {
            eCalendarSettable.add(Calendar.DATE, eDayOfMonth);
            eCalendar.setTimeInMillis(eCalendarSettable.getTimeInMillis());
        }
        if (eListener != null)
            eListener.onDaySelected(eCalendar.getTime());
        
    }
    
    void measure(int iWidth, int iHeight, int iPaddingRight, int iPaddingLeft) {
        eWidthPerDay = iWidth / 7;
        eHeightPerDay = iHeight / 7;
        this.eWidth = iWidth;
        this.eThreshold = iWidth / 3;
        this.ePaddingRight = iPaddingRight;
        this.ePaddingLeft = iPaddingLeft;
        
    }
    
    private void scrollSmooth() {
        int iDistance = (int) (eScrolledPosition.x - (eWidth * -eCurrentMonth));
        if (eIsScrolling && iDistance > eThreshold)
            scroll(1);
        else if (eIsScrolling && iDistance < -eThreshold)
            scroll(-1);
        else
            cancelScroll();
        
    }
    
    private void scroll(int iAdd) {
        eCurrentMonth -= iAdd;
        int iPosition = (-eCurrentMonth) * eWidth;
        int iDistance = (int) (iPosition - eScrolledPosition.x);
        eScroller.startScroll((int) eScrolledPosition.x, 0, iDistance, 0,
                (int) (Math.abs(iDistance) / (float) eWidth * 700));
        eListener.onScroll(getFirstDayCurrentMonth());
        
    }
    
}
