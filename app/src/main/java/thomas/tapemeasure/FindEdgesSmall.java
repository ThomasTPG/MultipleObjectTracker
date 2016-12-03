package thomas.tapemeasure;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by Thomas on 07/08/2016.
 */
public class FindEdgesSmall {

    Bitmap mBmp;
    int mWidth;
    int mHeight;
    int THRESHOLD = 30;
    int reduction = 1;
    int mXRegion;
    int mYRegion;
    int xLower;
    int xUpper;
    int yLower;
    int yUpper;
    byte[][] bitArray;
    int squareSizeRecorded;

    public FindEdgesSmall(Bitmap bmp, float xRegion, float yRegion)
    {
        mBmp = bmp;
        mWidth = bmp.getWidth();
        mHeight = bmp.getHeight();

        mXRegion = (int) (xRegion * mWidth);
        mYRegion = (int) (yRegion * mHeight);
    }

    public int getmWidth()
    {return mWidth;}

    public int getmHeight()
    {return mHeight;}



    public byte[][] getBitArray(int squareSize)
    {
        squareSizeRecorded = squareSize;
        bitArray = new byte[squareSizeRecorded][squareSizeRecorded];
        xLower = Math.max(1, mXRegion - squareSizeRecorded / 2);
        xUpper = Math.min(mWidth, mXRegion + squareSizeRecorded / 2 - 1);
        yLower = Math.max(1, mYRegion - squareSizeRecorded / 2);
        yUpper = Math.min(mHeight, mYRegion + squareSizeRecorded / 2 - 1);
        searchForDifferences();

        return bitArray;
    }

    public void searchForDifferences()
    {
        for (int x = xLower; x < xUpper - 1; x++)
        {
            for (int y = yLower; y < yUpper - 1; y++)
            {
                if (x == 0 || x == mWidth - 1 || y == 0 || y == mHeight - 1)
                {
                    if (x == 0)
                    {
                        if (y == 0)
                        {
                            checkCorner(1);
                        }
                        else if (y == mHeight - 1)
                        {
                            checkCorner(4);
                        }
                        else
                        {
                            checkSide(4);
                        }
                    }
                    else if (x == mWidth - 1)
                    {
                        if (y == 0)
                        {
                            checkCorner(2);
                        }
                        else if (y == mHeight - 1)
                        {
                            checkCorner(3);
                        }
                        else
                        {
                            checkSide(2);
                        }
                    }
                    else if (y == 0)
                    {
                        checkSide(1);
                    }
                    else
                    {
                        checkSide(3);
                    }

                }
                else
                {
                    checkCenter(x, y);
                }
            }
        }
    }

    private void checkCenter(int x, int y)
    {
        int pixel = mBmp.getPixel(x, y);
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        if (comparePixel(mBmp.getPixel(x - 1, y - 1), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x - 1, y), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x - 1, y + 1), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x, y - 1), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x, y + 1), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x + 1, y - 1), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x + 1, y), redValue, blueValue, greenValue) ||
                comparePixel(mBmp.getPixel(x + 1, y + 1), redValue, blueValue, greenValue))
        {
            bitArray[x - xLower][y - yLower] = 1;
        }
        else
        {
            bitArray[x - xLower][y - yLower] = 0;
        }
    }

    /**
     *  Sets the pixel int he new Bitmap to the correct colour
     * @param corner Integer: 1 for top left, and then clockwise
     */
    private void checkCorner(int corner)
    {
        int pixel;
        int redValue;
        int blueValue;
        int greenValue;
        switch(corner)
        {
            case(1):
                pixel = mBmp.getPixel(0, 0);
                redValue = Color.red(pixel);
                blueValue = Color.blue(pixel);
                greenValue = Color.green(pixel);
                if (comparePixel(mBmp.getPixel(1, 0), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(1, 1), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(0, 1), redValue, blueValue, greenValue))
                {
                    bitArray[0][0] = 1;
                }
                else
                {
                    bitArray[0][0] = 0;
                }
                break;
            case(2):
                pixel = mBmp.getPixel(mWidth - 1, 0);
                redValue = Color.red(pixel);
                blueValue = Color.blue(pixel);
                greenValue = Color.green(pixel);
                if (comparePixel(mBmp.getPixel(mWidth - 2, 0), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(mWidth - 2, 1), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(mWidth - 1, 1), redValue, blueValue, greenValue))
                {
                    bitArray[squareSizeRecorded - 1][0] = 1;
                }
                else
                {
                    bitArray[squareSizeRecorded - 1][0] = 0;
                }
                break;
            case(3):
                pixel = mBmp.getPixel(mWidth - 1, mHeight - 1);
                redValue = Color.red(pixel);
                blueValue = Color.blue(pixel);
                greenValue = Color.green(pixel);
                if (comparePixel(mBmp.getPixel(mWidth - 2, mHeight - 1), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(mWidth - 2, mHeight - 2), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(mWidth - 1, mHeight - 2), redValue, blueValue, greenValue))
                {
                    bitArray[squareSizeRecorded - 1][squareSizeRecorded - 1] = 1;
                }
                else
                {
                    bitArray[squareSizeRecorded - 1][squareSizeRecorded - 1] = 1;
                }
                break;
            case(4):
                pixel = mBmp.getPixel(0, mHeight - 1);
                redValue = Color.red(pixel);
                blueValue = Color.blue(pixel);
                greenValue = Color.green(pixel);
                if (comparePixel(mBmp.getPixel(1, mHeight - 1), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(0, mHeight - 2), redValue, blueValue, greenValue) ||
                        comparePixel(mBmp.getPixel(1, mHeight - 2), redValue, blueValue, greenValue))
                {
                    bitArray[0][squareSizeRecorded - 1] = 1;
                }
                else
                {
                    bitArray[0][squareSizeRecorded - 1] = 0;
                }
                break;
        }
    }

    /**
     * Checks the side for edges
     * @param side Top side is 1, then clockwise
     */
    private void checkSide(int side)
    {
        int pixel;
        int redValue;
        int blueValue;
        int greenValue;
        switch(side) {
            case (1):
                for (int x = 1; x < squareSizeRecorded; x++) {
                    pixel = mBmp.getPixel(x, 0);
                    redValue = Color.red(pixel);
                    blueValue = Color.blue(pixel);
                    greenValue = Color.green(pixel);
                    boolean edge = false;
                    for (int ii = -1; ii < 2; ii++) {
                        for (int jj = 0; jj < 2; jj++) {
                            edge = (edge || comparePixel(mBmp.getPixel(x + ii, jj), redValue, blueValue, greenValue));
                        }

                    }
                    if (edge) {
                        bitArray[x - xLower][0] = 1;
                    } else {
                        bitArray[x - xLower][0] = 0;
                    }
                }

                break;
            case (2):
                for (int y = 1; y < squareSizeRecorded; y++) {
                    pixel = mBmp.getPixel(mWidth - 1, y);
                    redValue = Color.red(pixel);
                    blueValue = Color.blue(pixel);
                    greenValue = Color.green(pixel);
                    boolean edge = false;
                    for (int ii = -1; ii < 2; ii++) {
                        for (int jj = 0; jj < 2; jj++) {
                            edge = (edge || comparePixel(mBmp.getPixel(mWidth - 1 - jj, y + ii), redValue, blueValue, greenValue));
                        }

                    }
                    if (edge) {
                        bitArray[squareSizeRecorded - 1][y - yLower] = 1;
                    } else {
                        bitArray[squareSizeRecorded - 1][y - yLower] = 0;
                    }
                }
                break;
            case (3):
                for (int x = 1; x < squareSizeRecorded; x++) {
                    pixel = mBmp.getPixel(x, mHeight - 1);
                    redValue = Color.red(pixel);
                    blueValue = Color.blue(pixel);
                    greenValue = Color.green(pixel);
                    boolean edge = false;
                    for (int ii = -1; ii < 2; ii++) {
                        for (int jj = 0; jj < 2; jj++) {
                            edge = (edge || comparePixel(mBmp.getPixel(x + ii, mHeight - 1 - jj), redValue, blueValue, greenValue));
                        }

                    }
                    if (edge) {
                        bitArray[x - xLower][squareSizeRecorded - 1] = 1;
                    } else {
                        bitArray[x - xLower][squareSizeRecorded - 1] = 0;
                    }
                }
                break;
            case (4):
                for (int y = 1; y < squareSizeRecorded; y++) {
                    pixel = mBmp.getPixel(0, y);
                    redValue = Color.red(pixel);
                    blueValue = Color.blue(pixel);
                    greenValue = Color.green(pixel);
                    boolean edge = false;
                    for (int ii = -1; ii < 2; ii++) {
                        for (int jj = 0; jj < 2; jj++) {
                            edge = (edge || comparePixel(mBmp.getPixel(jj, y + ii), redValue, blueValue, greenValue));
                        }

                    }
                    if (edge) {
                        bitArray[0][y - yLower] = 1;
                    } else {
                        bitArray[0][y - yLower] = 0;
                    }
                }

                break;
        }
    }

    private boolean comparePixel(int pxl, int red, int blue, int green)
    {
        int redToCompare = Color.red(pxl);
        int blueToCompare = Color.blue(pxl);
        int greenToCompare = Color.green(pxl);
        int totalColourToCompare = redToCompare + blueToCompare + greenToCompare;
        int totalColour = red + blue + green;
        /*
        if (totalColour > totalColourToCompare)
        {
            return(Math.abs(redToCompare - red) + Math.abs(blueToCompare - blue) + Math.abs(greenToCompare - green) > THRESHOLD);
        }
        return false;
        */
        return(Math.abs(redToCompare - red) + Math.abs(blueToCompare - blue) + Math.abs(greenToCompare - green) > THRESHOLD);

    }





}
