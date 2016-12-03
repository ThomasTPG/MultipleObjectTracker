package thomas.tapemeasure;

/**
 * Created by Thomas on 26/10/2016.
 */
public class Normalize {

    public Normalize()
    {

    }

    public int[][] getNormalizedArray(int [][] b)
    {
        int height = b.length;
        int width = b[0].length;
        int intensitySum = 0;
        int intensitySquareSum = 0;
        for (int ii = 0; ii < width; ii++)
        {
            for (int jj = 0; jj < height; jj++)
            {
                intensitySum = intensitySum + b[jj][ii];
                intensitySquareSum = intensitySquareSum + b[jj][ii]^2;
            }
        }
        float mean = intensitySum / (width * height);
        float meanSquare = intensitySquareSum / (width * height);
        float standardDeviation = (float) Math.sqrt(meanSquare - mean);

        for (int ii = 0; ii < width; ii++)
        {
            for (int jj = 0; jj < height; jj++)
            {
                b[jj][ii] = (int) ((b[jj][ii] - mean)/standardDeviation);
            }
        }

        return b;
    }
}
