package thomas.tapemeasure;

import android.graphics.Bitmap;

/**
 * Created by Thomas on 09/08/2016.
 */
public class CompareBmps {

    int mSizeSmall;
    int mSizeLarge;
    int topLeftX;
    int topLeftY;
    int INITIALTHRESHOLD = 200;
    int THRESHOLD;
    byte[][] smallArray;
    byte[][] largeArray;
    int mWidth;
    int mHeight;
    float xPos;
    float yPos;
    byte[][] newArray;
    float xPositionOfSmallBitmap;
    float yPositionOfSmallBitmap;

    public CompareBmps(byte[][] bitArraySmall, byte[][] bitArrayLarge, int width, int height, float x, float y)
    {
        mSizeSmall = bitArraySmall.length;
        INITIALTHRESHOLD = mSizeSmall*mSizeSmall/4;
        mSizeLarge = bitArrayLarge.length;
        smallArray = bitArraySmall;
        largeArray = bitArrayLarge;
        xPositionOfSmallBitmap = x;
        yPositionOfSmallBitmap = y;
        mHeight = height;
        mWidth = width;
        runComparison();

    }

    public void runComparison()
    {
        THRESHOLD = INITIALTHRESHOLD;
        topLeftX = 0;
        topLeftY = 0;
        for (int ii = 0; ii < (mSizeLarge - mSizeSmall); ii++)
        {
            for (int jj = 0; jj < (mSizeLarge - mSizeSmall); jj++)
            {
                int sum = 0;
                for (int row = 0; row < mSizeSmall; row ++)
                {
                    for (int col = 0; col < mSizeSmall; col++)
                    {
                        sum = sum + Math.abs(smallArray[row][col] - largeArray[ii + row][jj + col]);
                        if (sum > THRESHOLD)
                        {
                            break;
                        }
                    }
                    if (sum > THRESHOLD)
                    {
                        break;
                    }
                }
                if (sum < THRESHOLD)
                {
                    THRESHOLD = sum;
                    topLeftX = ii;
                    topLeftY = jj;
                }
            }
        }

        int centreX = (topLeftX + mSizeSmall/2) - mSizeLarge/2;
        int centreY = topLeftY + mSizeSmall/2 - mSizeLarge/2;
        xPos = xPositionOfSmallBitmap + (float) centreX / (float) mWidth;
        yPos = yPositionOfSmallBitmap + (float) centreY / (float) mHeight;
        newArray = new byte[mSizeSmall][mSizeSmall];
        for (int ii = 0; ii < mSizeSmall; ii ++)
        {
            for (int jj = 0; jj < mSizeSmall; jj ++)
            {
                newArray[ii][jj] = largeArray[ii + topLeftX][jj + topLeftY];
            }
        }
    }

    public boolean getThresholdReached() {
        return (THRESHOLD == INITIALTHRESHOLD);
    }

    public float getxPos()
    {
        return xPos;
    }

    public float getyPos()
    {
        return yPos;
    }

    public byte[][] getNewArray()
    {
        return newArray;
    }

}
