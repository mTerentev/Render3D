package com.mycompany.render3d;

import android.app.*;
import android.opengl.*;
import android.os.*;
import android.util.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		MeshCreator mc = new MeshCreator();
		iAnimate not_animate =new iAnimate(){

			@Override
			public mMatrix Animate(float time, mMatrix MMatrix)
			{
				return MMatrix;
			}
		};
		
		Object3D[] scene = {
			new Object3D(mc.tor(50, 20, 2,0.5f), new iAnimate(){

					@Override
					public mMatrix Animate(float time, mMatrix MMatrix)
					{
						return MMatrix.MMultiply(new mMatrix(4).Rotate(20*time,0,1,0));
					}
				}).Move(0, -2f, 0),
			
			new Object3D(mc.plane(),not_animate).Move(0,-4.5f,0),
			new Object3D(mc.sphere(30, 30, 1.5f),not_animate).Move(0, 2.5f, 0)
		};
		GLSurfaceView mGLSurfaceView = new GLSurfaceView(this);
		Renderer mRenderer = new Renderer(this, scene);

		mGLSurfaceView.setEGLContextClientVersion(3);
		mGLSurfaceView.setRenderer(mRenderer);
        setContentView(mGLSurfaceView);
		
    }
}
