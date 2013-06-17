package miyu.oka.ninjacamera;

import android.content.Context;
import android.opengl.GLSurfaceView;

class CustomGLSurfaceView extends GLSurfaceView
{
    CustomGL20Renderer renderer;
    public CustomGLSurfaceView(Context context)
    {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new CustomGL20Renderer((MainActivity)context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }
    public CustomGL20Renderer getRenderer()
    {
        return renderer;
    }
}