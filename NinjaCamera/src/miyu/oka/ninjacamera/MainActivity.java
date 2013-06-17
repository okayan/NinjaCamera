package miyu.oka.ninjacamera;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends Activity implements SurfaceTexture.OnFrameAvailableListener
{
    private Camera mCamera;
    private CustomGLSurfaceView glSurfaceView;
    private SurfaceTexture surface;
    CustomGL20Renderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        glSurfaceView = new CustomGLSurfaceView(this);
        renderer = glSurfaceView.getRenderer();
        setContentView(glSurfaceView);
    }

    public void startCamera(int texture)
    {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);
        renderer.setSurface(surface);

        mCamera = Camera.open();

        try
        {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
            mCamera.takePicture(null, null, getJpegCallback());
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
    }

	private PictureCallback getJpegCallback() {

		PictureCallback jpeg = new PictureCallback() {
			
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				FileOutputStream fos;
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
					Date date = new Date(System.currentTimeMillis());
					String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + format.format(date) + ".jpeg";
					fos = new FileOutputStream(filePath);
					fos.write(data);
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		return jpeg;
	}

	public void onFrameAvailable(SurfaceTexture surfaceTexture)
    {
        glSurfaceView.requestRender();
    }

    @Override
    public void onPause()
    {
        mCamera.stopPreview();
        mCamera.release();
        System.exit(0);
    }

}
