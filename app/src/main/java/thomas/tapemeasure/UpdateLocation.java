package thomas.tapemeasure;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Thomas on 03/12/2016.
 */
public class UpdateLocation {

    int[] velocityX = {0,0,0,0};
    int[] velocityY = {0,0,0,0};
    float xLocation1;
    float yLocation1;
    byte[][] bitArrayNew;
    byte[][] bitArrayOld;
    int LARGESQUAREDEFAULT;
    int mSquareLarge;
    int SMALLSQUAREDEFAULT = 40;
    int mSquareSmall;
    int xCoOrd;
    int yCoOrd;
    int height;
    int width;


    public UpdateLocation(int screenWidth, int screenHeight, int initialXCoOrd, int initialYCoOrd, Context c)
    {
        xCoOrd = initialXCoOrd;
        yCoOrd = initialYCoOrd;
        width = screenWidth;
        height = screenHeight;
        xLocation1 = (float) xCoOrd / screenWidth;
        yLocation1 = (float) yCoOrd / screenHeight;
        LARGESQUAREDEFAULT = c.getResources().getInteger(R.integer.LARGESQUAREDEFAULT);
        mSquareLarge = LARGESQUAREDEFAULT;
        SMALLSQUAREDEFAULT = c.getResources().getInteger(R.integer.SMALLSQUAREDEFAULT);
        mSquareSmall = SMALLSQUAREDEFAULT;
    }

    public void getOriginalBitArray(Bitmap bitmap)
    {
        FindEdgesSmall edgeFinder = new FindEdgesSmall(bitmap, xLocation1, yLocation1);
        bitArrayOld = edgeFinder.getBitArray(mSquareSmall);
    }

    public boolean finishedLoop(Bitmap bitmap)
    {


        FindEdgesSmall edgeFinder = new FindEdgesSmall(bitmap, xLocation1, yLocation1);
        bitArrayNew = edgeFinder.getBitArray(mSquareLarge);

        CompareBmps comparator = new CompareBmps(bitArrayOld, bitArrayNew, edgeFinder.getmWidth(), edgeFinder.getmHeight(), xLocation1, yLocation1);

        if (comparator.getThresholdReached())
        {
            //The threshold was reached when comparing the two images - they are too different
            // Calculate the previous x position
            xCoOrd = xCoOrd + meanVelocityX();
            yCoOrd = yCoOrd + meanVelocityY();
            // Increase the size of the square for which we compare the previous image to the current - try to find the lost object!
            mSquareSmall = Math.min(height, Math.min(width, mSquareSmall * 3/2));
            mSquareLarge = Math.min(height, Math.min(width, mSquareLarge * 3/2));
        }
        else
        {
            // Threshold not reached - images are similar enough. Update the co-ordinates and calculate the velocity
            bitArrayOld = comparator.getNewArray();
            xLocation1 = comparator.getxPos();
            yLocation1 = comparator.getyPos();
            for (int ii = 0;ii<velocityX.length - 1;ii++)
            {
                velocityX[ii] = velocityX[ii+1];
                velocityY[ii] = velocityY[ii+1];
            }
            velocityX[velocityX.length - 1] = (int) (xLocation1 * width) - xCoOrd;
            velocityY[velocityX.length - 1] = (int) (yLocation1 * height) - yCoOrd;
            System.out.println((xLocation1 * width) - xCoOrd);
            xCoOrd = (int) (xLocation1 * width);
            yCoOrd = (int) (yLocation1 * height);
            mSquareSmall = SMALLSQUAREDEFAULT;
            mSquareLarge = LARGESQUAREDEFAULT;
        }

        if( xCoOrd < 0 || yCoOrd < 0)
        {
            return true;
        }
        return false;
    }

    public int[] getCoOrds()
    {
        int[] coOrds = new int[2];
        coOrds[0] = xCoOrd;
        coOrds[1] = yCoOrd;
        return coOrds;
    }


    public int meanVelocityX()
    {
        int sum = 0;
        for (int ii = 0; ii < velocityX.length; ii++)
        {
            sum = sum + velocityX[ii];
        }
        return sum/velocityX.length;
    }

    public int meanVelocityY()
    {
        int sum = 0;
        for (int ii = 0; ii < velocityY.length; ii++)
        {
            sum = sum + velocityY[ii];
        }
        return sum/velocityY.length;
    }
}
