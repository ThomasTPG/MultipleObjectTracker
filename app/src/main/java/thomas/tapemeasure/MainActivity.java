package thomas.tapemeasure;


        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.sql.SQLClientInfoException;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.DashPathEffect;
        import android.graphics.Paint;
        import android.hardware.Camera;
        import android.hardware.Camera.CameraInfo;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.util.DisplayMetrics;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.FrameLayout;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;

public class MainActivity extends Activity{
    private Camera mCamera;
    private CameraView mPreview;
    private Context myContext;
    private AspectFrameLayout cameraPreview;
    private boolean cameraFront = false;
    static Bitmap bitmap;
    FrameLayout layout;
    CameraOverlay cOverlay;
    int height;
    int width;
    int MAXNUMBEROFLOCATIONS = 4;
    int currentNumberOfLocations = 0;
    UpdateLocation[] locations = new UpdateLocation[MAXNUMBEROFLOCATIONS];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout=(FrameLayout)findViewById(R.id.rootView);
        cOverlay = (CameraOverlay) findViewById(R.id.camera_overlay);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        myContext = this;
        initialize();
    }


    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        ((ViewGroup)mPreview.getParent()).removeView(mPreview);
        initialize();
    }

    private void createCamera()
    {
        if (mCamera == null) {
            mCamera = Camera.open(findBackFacingCamera());
            mPreview = new CameraView(myContext, mCamera);

            Camera.Size cameraPreviewSize = mCamera.getParameters().getPreviewSize();
            cameraPreview.setAspectRatio((double) cameraPreviewSize.width / cameraPreviewSize.height);
            cOverlay.setAspectRatio((double) cameraPreviewSize.width / cameraPreviewSize.height);
            //mPreview.refreshCamera(mCamera);
        }
    }

    public void initialize() {
        cameraPreview = (AspectFrameLayout) findViewById(R.id.continuousCapture_afl);
        createCamera();
        cameraPreview.addView(mPreview);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    UpdateLocation newLocation = new UpdateLocation(width,height, (int) event.getX(), (int) event.getY(), myContext);
                    newLocation.getOriginalBitArray(bitmap);
                    if (currentNumberOfLocations < MAXNUMBEROFLOCATIONS)
                    {
                        locations[currentNumberOfLocations] = newLocation;
                        currentNumberOfLocations++;
                    }
                    if (!trackLocations.isAlive())
                    {
                        trackLocations.start();
                    }
                }
                return true;
            }
        });

    }

    Thread trackLocations = new Thread(new Runnable() {
        @Override
        public void run() {
            while(currentNumberOfLocations > 0)
            {
                for (int ii = 0; ii < MAXNUMBEROFLOCATIONS; ii ++)
                {
                    if (locations[ii] != null)
                    {
                        if (!locations[ii].finishedLoop(bitmap))
                        {
                            int[] coOrds = locations[ii].getCoOrds();
                            cOverlay.setCoOrds(coOrds[0],coOrds[1], ii);
                        }
                        else
                        {
                            locations[ii] = null;
                            currentNumberOfLocations --;
                        }
                    }
                }

                cOverlay.postInvalidate();
            }
        }
    });

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void releaseCamera() {
        // stop and release camera
        mPreview.getHolder().removeCallback(mPreview);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            bitmap = BitmapFactory.decodeByteArray(data,0, data.length);

            mCamera.startPreview();
        }

    };
}