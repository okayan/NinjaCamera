package miyu.oka.ninjacamera;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

@SuppressLint("NewApi")
public class CustomGL20Renderer implements CustomGLSurfaceView.Renderer
{

    DirectVideo mDirectVideo;
    int mTexture;
    private SurfaceTexture mSurface;
    MainActivity mActivity;

    public CustomGL20Renderer(MainActivity activity)
    {
    	mActivity = activity;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
    	mTexture = createTexture();
        mDirectVideo = new DirectVideo(mTexture);
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        mActivity.startCamera(mTexture);
    }

	public void onDrawFrame(GL10 unused)
    {
            float[] mtx = new float[16];
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mSurface.updateTexImage();
            mSurface.getTransformMatrix(mtx); 

            mDirectVideo.draw();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
    }

    static public int loadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    static private int createTexture()
    {
        int[] texture = new int[1];

        GLES20.glGenTextures(1,texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
             GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);        
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
             GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
             GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
             GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    public void setSurface(SurfaceTexture surface)
    {
    	mSurface = surface;
    }
}
