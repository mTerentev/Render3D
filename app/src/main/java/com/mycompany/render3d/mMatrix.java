package com.mycompany.render3d;
import android.util.*;
import javax.xml.transform.*;

public class mMatrix
{
	float[] arr;
	int dim;
	mMatrix(int dim){
		this.dim = dim;
		arr = new float[dim*dim];
		for(float element:arr){element=0;}
		for(int i=0;i<dim;i++){
			arr[i+dim*i]=1;
		}
	}
	float get(int a, int b){
		return arr[b+dim*a];
	}
	void set(int a, int b, float c){
		arr[b+dim*a]=c;
	}
	mMatrix add(mMatrix mat){
		for(int i=0; i<dim*dim; i++){
			this.arr[i]+=mat.arr[i];
		}
		return this;
	}
	mMatrix mul(float a){
		for(int i=0; i<dim*dim; i++){
			this.arr[i]*=a;
		}
		return this;
	}
	mMatrix MMultiply(mMatrix mat){
		mMatrix result = new mMatrix(dim);
		for(int i=0; i<dim; i++){
			for(int j=0; j<dim; j++){
				float sum=0;
				for(int k=0;k<dim;k++){
					sum+=this.get(k,i)*mat.get(j,k);
				}
				result.set(j,i,sum);
			}
		}
		return result;
	}
	mMatrix Translate(float x, float y, float z){
		this.set(3,0,x);
		this.set(3,1,y);
		this.set(3,2,z);
		return this;
	}
	mMatrix Scale(float x, float y, float z){
		this.set(0,0,x);
		this.set(1,1,y);
		this.set(2,2,z);
		return this;
	}
	mMatrix Rotate(float angle, float x, float y, float z){
		angle*=(float)Math.PI/180f;
		mMatrix K = new mMatrix(4);
		K.arr=new float[]{
			0,-z, y, 0,
			z, 0,-x, 0,
			-y, x, 0, 0,
			0, 0, 0, 0
		};
		mMatrix KK = K.MMultiply(K);
		this.add(K.mul((float)Math.sin(angle))).add(KK.mul(1f-(float)Math.cos(angle)));
		return this;
	}
	mMatrix Perspective(float fovy, float aspect, float n, float f){
		fovy*=(float)Math.PI/180f;
		this.set(0,0,1f/aspect/(float)Math.tan(fovy/2f));
		this.set(1,1,1f/(float)Math.tan(fovy/2f));
		this.set(2,2,(f+n)/(f-n));
		this.set(2,3,1f);
		this.set(3,2,-2f*f*n/(f-n));
		this.set(3,3,0);
		return this;
	}
	float[] VMultiply(float[] vec){
		float[] res = new float[vec.length];
		for(int i = 0; i<vec.length;i++){
			float sum=0;
			for(int j = 0; j<vec.length;j++){
				sum+=vec[j]*this.get(j,i);
			}
			res[i]=sum;
		}
		return res;
	}
	mMatrix minor(int x, int y){
		mMatrix result = new mMatrix(this.dim-1);
		int j1=0;
		for(int j=0;j<this.dim-1;j++){
			int i1=0;
			for(int i=0;i<this.dim-1;i++){
				if(i1==x){i1++;}
				if(j1==y){j1++;}
				result.set(i,j,this.get(i1,j1));
				i1++;
			}
			j1++;
		}
		return result;
	}
	
	float det(){
		float sum = 0;
		if(this.dim == 1){
			sum = this.get(0,0);
		}
		else{
			for(int j=0;j<this.dim;j++){
				sum+=this.get(0,j)*(float)Math.pow(-1,j)*this.minor(0,j).det();
			}
		}
		return sum;
	}
	
	mMatrix transpose(){
		mMatrix result = new mMatrix(this.dim);
		for(int j=0;j<this.dim;j++){
			for(int i=0;i<this.dim;i++){
				result.set(i,j,this.get(j,i));
			}
		}
		return result;
	}
	
	mMatrix inverse(){
		mMatrix result = new mMatrix(this.dim);
		float d=this.det();
		for(int j=0;j<this.dim;j++){
			for(int i=0;i<this.dim;i++){
				result.set(i,j,(float)Math.pow(-1,i+j)*this.minor(i,j).det()/d);
			}
		}
		return result.transpose();
	}
	
	void print(String tag){
		for(int j=0;j<this.dim;j++){
			String row="";
			for(int i=0;i<this.dim;i++){
				row+=Float.toString(this.get(i,j))+" ";
			}
			Log.v(tag,row+"\n");
		}
		Log.v(tag,"\n");
	}
}
