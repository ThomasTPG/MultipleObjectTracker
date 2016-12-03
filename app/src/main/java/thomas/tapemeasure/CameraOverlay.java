package thomas.tapemeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Thomas on 29/09/2016.
 */
public class CameraOverlay extends AspectFrameLayout {

    final int radius = 20;
    int mXPos = 10;
    int mYPos = 10;
    int numberOfLocations = 4;
    int[] xCoOrds = new int[numberOfLocations];
    int[] yCoOrds = new int[numberOfLocations];

    public CameraOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public CameraOverlay(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public void setCoOrds(int x, int y, int ii)
    {
        xCoOrds[ii] = x;
        yCoOrds[ii] = y;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Paint p = new Paint();
        p.setColor(Color.RED);
        System.out.println(canvas.getHeight() + " " + canvas.getWidth());
        DashPathEffect dashPath = new DashPathEffect(new float[]{5,5}, (float)2.0);
        p.setPathEffect(dashPath);
        p.setStyle(Paint.Style.STROKE);
        for (int kk = 0; kk < numberOfLocations; kk ++)
        {
            canvas.drawCircle(xCoOrds[kk], yCoOrds[kk], radius, p);
        }

        super.onDraw(canvas);
    }


}
