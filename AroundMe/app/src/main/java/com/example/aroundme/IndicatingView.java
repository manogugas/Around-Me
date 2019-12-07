package com.example.aroundme;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

public class IndicatingView extends View
{
    public  static  final  int NOTEXECUTED = 0;
    public  static  final  int SUCCESS = 1;
    public  static  final  int FAILED = 2;
    public  static  final  int LOADING1 = 3;
    public  static  final  int LOADING2 = 4;
    public  static  final  int LOADING3 = 5;
    public  static  final  int LOADING4 = 6;
    public  static  final  int LOADING5 = 7;
    public  static  final  int WAITING = 8;

    int state = NOTEXECUTED;

    int radius; // at the beggining radius equals 0

    public IndicatingView(Context context) {
        super(context);
    }

    public IndicatingView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public IndicatingView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public int getState(){return state;}

    public  void setState(int state) {this.state = state;}

    /*
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == View.VISIBLE) {
            startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.animacija));
        }
        else {
            clearAnimation();
        }
    }

    public void StartAnim()
    {
        startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.animacija));
    }

    public  void StopAnim()
    {
        clearAnimation();
    }*/

    @Override
    protected void onDraw(final Canvas canvas)
    {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        final Paint paint;

        switch (state){
            case SUCCESS:
                paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(20f);
                //checkmark
                canvas.drawLine(0, 0, width/2, height, paint);
                canvas.drawLine(width/2, height, width, height/2, paint);
                break;

            case FAILED:
                paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(20f);
                // X
                canvas.drawLine(0, 0, width, height, paint);
                canvas.drawLine(0, height, width, 0, paint);
                break;

            case WAITING:

                paint = new Paint();
                paint.setColor(Color.DKGRAY);
                //paint.setStrokeWidth(20f);
                paint.setStyle(Paint.Style.FILL);

                // triangle
                /*
                final Point point1_draw = new Point(width/2, 0);
                final Point point2_draw = new Point(0, height);
                final Point point3_draw = new Point(width, height);


                final Path path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(point1_draw.x,point1_draw.y);
                path.lineTo(point2_draw.x,point2_draw.y);
                path.lineTo(point3_draw.x,point3_draw.y);
                path.lineTo(point1_draw.x,point1_draw.y);
                path.close();*/


                // triangle
                final Point point1_draw = new Point(0, 0);
                final Point point2_draw = new Point(0, height);
                final Point point3_draw = new Point(width, height);
                final Point point4_draw = new Point(width, 0);

                final Path path = new Path();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(point1_draw.x,point1_draw.y);
                path.lineTo(point2_draw.x,point2_draw.y);
                path.lineTo(point3_draw.x,point3_draw.y);
                path.lineTo(point4_draw.x,point4_draw.y);
                path.lineTo(point1_draw.x,point1_draw.y);
                path.close();

                paint.setColor(Color.RED);
                canvas.drawPath(path, paint);

                break;

            case LOADING1:
                paint = new Paint();
                paint.setColor(0x11005500);
                paint.setStrokeWidth(20f);
                // X
                canvas.drawRect(0, 0, height, width, paint);
                break;

            case LOADING2:
                paint = new Paint();
                paint.setColor(0x33005500);
                paint.setStrokeWidth(20f);
                // X
                canvas.drawRect(0, 0, height, width, paint);
                paint.setColor(0x33005500);
                canvas.drawRect(width, 0, height, width, paint);
                break;

            case LOADING3:
                paint = new Paint();
                paint.setColor(0x11005500);
                paint.setStrokeWidth(20f);
                // X
                canvas.drawRect(0, 0, height, width, paint);
                paint.setColor(0x33005500);
                canvas.drawRect(width, 0, height, width, paint);
                paint.setColor(0x55005500);
                canvas.drawRect(width, 0, height*2, width, paint);
                break;

            case LOADING4:
                paint = new Paint();
                paint.setColor(0x11005500);
                paint.setStrokeWidth(20f);
                // X
                canvas.drawRect(0, 0, height, width, paint);
                paint.setColor(0x33005500);
                canvas.drawRect(width, 0, height, width, paint);
                paint.setColor(0x55005500);
                canvas.drawRect(width, 0, height*2, width, paint);
                paint.setColor(0x99005500);
                canvas.drawRect(width, 0, height*3, width, paint);
                break;

            case LOADING5:
                paint = new Paint();
                paint.setColor(0x11005500);
                paint.setStrokeWidth(20f);
                // X
                canvas.drawRect(0, 0, height, width, paint);
                paint.setColor(0x33005500);
                canvas.drawRect(width, 0, height, width, paint);
                paint.setColor(0x55005500);
                canvas.drawRect(width, 0, height*2, width, paint);
                paint.setColor(0x99005500);
                canvas.drawRect(width, 0, height*3, width, paint);
                paint.setColor(0xFF005500);
                canvas.drawRect(width, 0, height*4, width, paint);
                break;

            default:
                //nothing
                break;
        }
    }
}
