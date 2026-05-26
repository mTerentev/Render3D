package com.mycompany.render3d;


import android.app.*;
import android.content.res.*;
import android.opengl.*;
import android.util.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import javax.microedition.khronos.egl.EGLConfig;
import android.transition.*;
import android.renderscript.*;
import android.graphics.*;

import static android.opengl.GLES20.*;

public class Renderer implements GLSurfaceView.Renderer
{
	private Activity activity;

	Object3D[] scene;
	
    int mPositionHandle;
	int mColorHandle;
	int mNormaleHandle;
	int programHandle;
	int programHandleShades;
	int programDebugHandler;
	float aspect;
	float time;
    private final int mBytesPerFloat = 4;
	int SHADOW_WIDTH = 1024;
	int SHADOW_HEIGHT = 1024;
	
	int[] fb;
	int[] rb;
	int[] depthMap;
	
	
	private String LoadCodeFile(Activity activity,String file){
		AssetManager as = activity.getAssets();
		String result = "";
		try
		{
			InputStream inputStream = as.open(file);
			Scanner s = new Scanner(inputStream).useDelimiter("\\A");
			while(s.hasNext()){result += s.next();}
		}
		catch (IOException e)
		{
			Log.e("shaders_code","error reading file");
		}
		return result;
	}

    public Renderer(Activity activity, Object3D[] scene)
    {
		this.activity=activity;
		this.scene=scene;
		new Timer().schedule(new mTimerTask(),0,10);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        GLES20.glClearColor(0.5f, 0.5f, 0.7f, 1.0f);
		//GLES20.glEnable(GLES20.GL_BLEND);
		//GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final String vertexShader = LoadCodeFile(activity,"shaders/vertex.txt");
        final String fragmentShader = LoadCodeFile(activity,"shaders/fragment.txt");
		final String fragmentDepthMap = LoadCodeFile(activity, "shaders/fragment_depth_map.txt");

		final String vertexDebugShader = LoadCodeFile(activity,"shaders/vertexDebug.txt");
        final String fragmentDebugShader = LoadCodeFile(activity,"shaders/fragmentDebug.txt");

        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShaderHandle, vertexShader);
		GLES20.glCompileShader(vertexShaderHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(vertexShaderHandle));
		//Log.i("shaders_code",vertexShader+"\n");
		
		int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
		GLES20.glCompileShader(fragmentShaderHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(fragmentShaderHandle));
		//Log.i("shaders_code",fragmentShader+"\n");
		
		int fragmentDepthHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentDepthHandle, fragmentDepthMap);
		GLES20.glCompileShader(fragmentDepthHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(fragmentDepthHandle));
		//Log.i("shaders_code",fragmentDepthMap+"\n");

		int vertexDebugHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexDebugHandle, vertexDebugShader);
		GLES20.glCompileShader(vertexDebugHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(vertexDebugHandle));
		//Log.i("shaders_code",vertexShader+"\n");

		int fragmentDebugHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentDebugHandle, fragmentDebugShader);
		GLES20.glCompileShader(fragmentDebugHandle);
		Log.e("shaders",GLES20.glGetShaderInfoLog(fragmentDebugHandle));
		//Log.i("shaders_code",fragmentShader+"\n");

        programHandle = GLES20.glCreateProgram();
		GLES20.glAttachShader(programHandle, vertexShaderHandle);
		GLES20.glAttachShader(programHandle, fragmentShaderHandle);
		GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
		GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
		GLES20.glBindAttribLocation(programHandle, 2, "a_Normale");
		GLES20.glLinkProgram(programHandle);

		programHandleShades = GLES20.glCreateProgram();
		GLES20.glAttachShader(programHandleShades, vertexShaderHandle);
		GLES20.glAttachShader(programHandleShades, fragmentDepthHandle);
		GLES20.glBindAttribLocation(programHandleShades, 0, "a_Position");
		GLES20.glBindAttribLocation(programHandleShades, 1, "a_Color");
		GLES20.glBindAttribLocation(programHandleShades, 2, "a_Normale");
		GLES20.glLinkProgram(programHandleShades);

		programDebugHandler= GLES20.glCreateProgram();
		GLES20.glAttachShader(programDebugHandler, vertexDebugHandle);
		GLES20.glAttachShader(programDebugHandler, fragmentDebugHandle);
		GLES20.glBindAttribLocation(programDebugHandler, 0, "a_Position");
		GLES20.glLinkProgram(programDebugHandler);

        mPositionHandle = 0;//GLES20.glGetAttribLocation(programHandle, "a_Position");
		mColorHandle = 1;//GLES20.glGetAttribLocation(programHandle, "a_Color");
		mNormaleHandle = 2;//GLES20.glGetAttribLocation(programHandle, "a_Normale");

        fb = new int[1];
		GLES20.glGenFramebuffers(1, fb, 0);

		rb = new int[1];
		glGenRenderbuffers(1, rb, 0);
		glBindRenderbuffer(GL_RENDERBUFFER, rb[0]);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, 1080,1920);

		depthMap = new int[2];
		GLES20.glGenTextures(2, depthMap,0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap[0]);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, 1080, 1920, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		

// 		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap[1]);
// 		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, 1080, 1920, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_SHORT, null);
// // Set filtering
// 		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
// 		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
// 		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
// 		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
		//GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthMap[0], 0);
		
		
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
		aspect=(float)width/(float)height;
		GLES20.glViewport(0, 0, width, height);
	}

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
		//light_source to global matrix
		mMatrix VMatrix = new mMatrix(4)
			.MMultiply(new mMatrix(4).Rotate(60,0,1,0))
			.MMultiply(new mMatrix(4).Rotate(-60,1,0,0))
			.MMultiply(new mMatrix(4).Translate(0,0,-7));

		VMatrix = VMatrix.inverse();

		//camera_source to global matrix
		mMatrix VMatrix1 = new mMatrix(4)
			.MMultiply(new mMatrix(4).Rotate(0,1,0,0))
			.MMultiply(new mMatrix(4).Rotate(0,0,1,0))
			.MMultiply(new mMatrix(4).Translate(0,0,-7));

		VMatrix1 = VMatrix1.inverse();


		mMatrix PMatrix = new mMatrix(4)
		.Perspective(100,aspect,0.1f,100);
		
		//Create Depth mask texture
		
		GLES20.glUseProgram(programHandleShades);
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fb[0]);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, depthMap[0], 0);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rb[0]);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		for(Object3D obj:scene){
		 
			mMatrix AMMatrix = obj.Animate(time,obj.MMatrix);
			mMatrix MVPMatrix = new mMatrix(4)
			.MMultiply(PMatrix)
			.MMultiply(VMatrix)
			.MMultiply(AMMatrix);

			int MVPmatrixHandle = GLES20.glGetUniformLocation(programHandleShades,"u_MVPmatrix");
			GLES20.glUniformMatrix4fv(MVPmatrixHandle,1,false,MVPMatrix.arr,0);
				
			for(FloatBuffer face: obj.facesb){
				DrawFace(face);
			}
		}
		//GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthMap[0], 0);
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		
		//Show texture
		
		GLES20.glUseProgram(programDebugHandler);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		FloatBuffer b = ByteBuffer.allocateDirect(8*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		b.position(0);
		float[] ps = {-1,-1, 1,-1, -1,1, 1,1};
		b.put(ps).position(0);
		
		BitmapFactory factory = new BitmapFactory();
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inScaled = false;
		Bitmap img = factory.decodeResource(activity.getResources(),R.drawable.ic_launcher,opt);
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(img.getByteCount());
		img.copyPixelsToBuffer(buffer);
		
		buffer.position(0);
		
		//GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, img.getHeight(), img.getWidth(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap[0]);
		
		
		
		//GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,img,0);
		

		img.recycle();
		buffer.clear();
		
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

		
		
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 2*4, b);
        GLES20.glEnableVertexAttribArray(0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
		GLES20.glDisableVertexAttribArray(0);
		
		// //Main draw
		
		// GLES20.glUseProgram(programHandle);
		

        // GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		// GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthMap[0]);
		
		// Object3D[] scn = {scene[1]};
		// for(Object3D obj:scn){
		// //for(Object3D obj:scene){
		
		// 	mMatrix AMMatrix = obj.Animate(time,obj.MMatrix);
		// 	//Global frame to screen frame matrix
		// 	mMatrix MVPMatrix = new mMatrix(4)
		// 		.MMultiply(PMatrix)
		// 		.MMultiply(VMatrix)
		// 		.MMultiply(AMMatrix);

		// 	int MmatrixHandle = GLES20.glGetUniformLocation(programHandle,"u_Mmatrix");
		// 	GLES20.glUniformMatrix4fv(MmatrixHandle,1,false,AMMatrix.arr,0);

		// 	int VPmatrixHandle = GLES20.glGetUniformLocation(programHandle,"u_VPmatrix");
		// 	GLES20.glUniformMatrix4fv(VPmatrixHandle,1,false,PMatrix.MMultiply(VMatrix).arr,0);

		// 	int MVPmatrixHandle = GLES20.glGetUniformLocation(programHandle,"u_MVPmatrix");
		// 	GLES20.glUniformMatrix4fv(MVPmatrixHandle,1,false,MVPMatrix.arr,0);

		// 	float[] n = new float[]{0,0,0,1};
		// 	n = AMMatrix.inverse().MMultiply(VMatrix1.inverse()).VMultiply(n);
		// 	int LightHandle = GLES20.glGetUniformLocation(programHandle,"u_Light");
		// 	GLES20.glUniform3fv(LightHandle, 1, n, 0);

		// 	n = new float[]{0,0,0,1};
		// 	n = AMMatrix.inverse().MMultiply(VMatrix.inverse()).VMultiply(n);
		// 	int CameraHandle = GLES20.glGetUniformLocation(programHandle,"u_Camera");
		// 	GLES20.glUniform3fv(CameraHandle, 1, n, 0);

		// 	for(FloatBuffer face: obj.facesb){
		// 		DrawFace(face);
		// 	}
		// }
		
    }
	
	
	private void DrawFace(FloatBuffer aPlaneBuffer){
		aPlaneBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 10*4, aPlaneBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
		aPlaneBuffer.position(3);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 10*4, aPlaneBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
		aPlaneBuffer.position(7);
		GLES20.glVertexAttribPointer(mNormaleHandle, 3, GLES20.GL_FLOAT, false, 10*4, aPlaneBuffer);
		GLES20.glEnableVertexAttribArray(mNormaleHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, aPlaneBuffer.capacity()/10);
		//GLES20.glDrawArrays(GLES20.GL_POINTS, 0, aPlaneBuffer.capacity()/10);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mColorHandle);
		GLES20.glDisableVertexAttribArray(mNormaleHandle);
	}
	
	class mTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			time+=0.01;
		}
	}
}

