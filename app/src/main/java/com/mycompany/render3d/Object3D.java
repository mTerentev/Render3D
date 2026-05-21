package com.mycompany.render3d;

import android.opengl.*;
import android.renderscript.*;
import android.util.*;
import java.nio.*;
import java.util.*;

public class Object3D implements iAnimate
{
	mMatrix MMatrix;
	FloatBuffer[] facesb;
	iAnimate ianimate;
	
	public Object3D(float[][] faces, iAnimate ianimate){
		MMatrix = new mMatrix(4);
		this.ianimate = ianimate;
		facesb = new FloatBuffer[faces.length];
		for(int i=0;i<faces.length;i++){
			facesb[i]=ByteBuffer.allocateDirect(faces[i].length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
			facesb[i].put(faces[i]).position(0);
		}
	}
	
	Object3D Move(float x, float y, float z){
		MMatrix = MMatrix.MMultiply(new mMatrix(4).Translate(x,y,z));
		return this;
	}
	
	Object3D Rotate(float angle, float x, float y, float z){
		MMatrix = MMatrix.MMultiply(new mMatrix(4).Rotate(angle,x,y,z));
		return this;
	}
	
	@Override
	public mMatrix Animate(float time, mMatrix MMatrix)
	{
		return ianimate.Animate(time, MMatrix);
	}
}
