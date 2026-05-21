package com.mycompany.render3d;

public class MeshCreator
{
	float pi = (float) Math.PI;
	MeshCreator(){}
	
	float[][] sphere(int Nx, int Ny, float radius){
		float[][] faces = new float[1][Nx*Ny*40];
		for(int k=0;k<4;k++){
			int dx=0;
			int dy=0;
			switch(k){
				case 0: dy=1; break;
				case 2: dx=1; dy=1; break;
				case 3: dx=1; break;
				
			}
			for(int y=0;y<Ny;y++){
				float alp = (float)Math.PI/Ny*(y+dx);
				float radius1 = radius*(float)Math.sin(alp);
				float cz = radius*(float)Math.cos(alp);
				
				float alp1 = (float)Math.PI/Ny*(y+0.5f);
				float radius11 = radius*(float)Math.sin(alp1);
				float cz1 = radius*(float)Math.cos(alp1);
				for(int x=0;x<Nx;x++){
					
					float beta = 2*(float)Math.PI/Nx*(x+dy);
					float cx = radius1*(float)Math.cos(beta);
					float cy = radius1*(float)Math.sin(beta);

					float beta1 = 2*(float)Math.PI/Nx*(x+0.5f);
					float cx1 = radius11*(float)Math.cos(beta1);
					float cy1 = radius11*(float)Math.sin(beta1);
					faces[0][10*(4*(Ny*x+y)+k)+0] = cx;
					faces[0][10*(4*(Ny*x+y)+k)+1] = cy;
					faces[0][10*(4*(Ny*x+y)+k)+2] = cz;
					faces[0][10*(4*(Ny*x+y)+k)+3] = 1;
					faces[0][10*(4*(Ny*x+y)+k)+4] = 0.5f;
					faces[0][10*(4*(Ny*x+y)+k)+5] = 0;
					faces[0][10*(4*(Ny*x+y)+k)+6] = 1;
					faces[0][10*(4*(Ny*x+y)+k)+7] = cx;
					faces[0][10*(4*(Ny*x+y)+k)+8] = cy;
					faces[0][10*(4*(Ny*x+y)+k)+9] = cz;
				}
			}
		}
		return faces;
	}
	
	float[][] tor(int Nx, int Ny, float radius, float width){
		float[][] faces = new float[1][Nx*Ny*40];
		for(int k=0;k<4;k++){
			int dx=0;
			int dy=0;
			switch(k){
				case 0: dy=1; break;
				case 2: dx=1; dy=1; break;
				case 3: dx=1; break;
			}
			for(int y=0;y<Ny;y++){
				float alp = 2f*pi*(float)(y+dy)/(float)Ny;
				float radius1 = radius+(float)Math.cos(alp)*width;
				float cz = width*(float)Math.sin(alp);
				
				float alp1 = 2f*pi*(float)(y+0.5f)/(float)Ny;
				float radius11 = radius+(float)Math.cos(alp1)*width;
				float cz1 = width*(float)Math.sin(alp1);
				for(int x=0;x<Nx;x++){
					float beta = 2f*pi*(float)(x+dx)/(float)Nx;
					float cx = radius1*(float)Math.cos(beta);
					float cy = radius1*(float)Math.sin(beta);
					
					float beta1 = 2f*pi*(float)(x+0.5f)/(float)Nx;
					float cx1 = radius11*(float)Math.cos(beta1);
					float cy1 = radius11*(float)Math.sin(beta1);
					faces[0][10*(4*(Ny*x+y)+k)+0] = cx;
					faces[0][10*(4*(Ny*x+y)+k)+1] = cy;
					faces[0][10*(4*(Ny*x+y)+k)+2] = cz;
					faces[0][10*(4*(Ny*x+y)+k)+3] = 1;
					faces[0][10*(4*(Ny*x+y)+k)+4] = 0.5f;
					faces[0][10*(4*(Ny*x+y)+k)+5] = 0;
					faces[0][10*(4*(Ny*x+y)+k)+6] = 1;
					
					faces[0][10*(4*(Ny*x+y)+k)+7] = cx-radius*(float)Math.cos(beta);
					faces[0][10*(4*(Ny*x+y)+k)+8] = cy-radius*(float)Math.sin(beta);
					faces[0][10*(4*(Ny*x+y)+k)+9] = cz;
					/*
					faces[0][10*(4*(Ny*x+y)+k)+7] = cx1-radius*(float)Math.cos(beta1);
					faces[0][10*(4*(Ny*x+y)+k)+8] = cy1-radius*(float)Math.sin(beta1);
					faces[0][10*(4*(Ny*x+y)+k)+9] = cz1;
					*/
				}
			}
		}
		return faces;
	}
	
	float[][] TestCube(){
		return new float[][]
		{
		{//back
			-1,-1, 1,
			0, 1, 0, 0.5f,
			0, 0, 1,

			-1, 1, 1,
			0, 1, 0, 0.5f,
			0, 0, 1,

			1, -1, 1,
			0, 1, 0, 0.5f,
			0, 0, 1,

			1,1, 1,
			0, 1, 0, 0.5f,
			0, 0, 1
		},
		{//front
			-1,-1, -1,
			0, 0, 1, 0.5f,
			0, 0, -1,

			1,-1, -1,
			0, 0, 1, 0.5f,
			0, 0, -1,

			-1, 1, -1,
			0, 0, 1, 0.5f,
			0, 0, -1,

			1, 1, -1,
			0, 0, 1, 0.5f,
			0, 0, -1
		},
		{//right
			1,-1, -1,
			1, 0.5f, 0, 0.5f,
			1, 0, 0,

			1, -1, 1,
			1, 0.5f, 0, 0.5f,
			1, 0, 0,

			1, 1, -1,
			1, 0.5f, 0, 0.5f,
			1, 0, 0,

			1, 1, 1,
			1, 0.5f, 0, 0.5f,
			1, 0, 0
		},
		{//left
			-1,-1,-1,
			1, 0, 0, 0.5f,
			-1, 0, 0,

			-1,-1, 1,
			1, 0, 0, 0.5f,
			-1, 0, 0,

			-1, 1, -1,
			1, 0, 0, 0.5f,
			-1, 0, 0,

			-1, 1, 1,
			1, 0, 0, 0.5f,
			-1, 0, 0
		},
		{//top
			-1, 1,-1,
			1, 1, 1, 0.5f,
			0, 1, 0,

			-1, 1, 1,
			1, 1, 1, 0.5f,
			0, 1, 0,

			1, 1,-1,
			1, 1, 1, 0.5f,
			0, 1, 0,

			1, 1, 1,
			1, 1, 1, 0.5f,
			0, 1, 0
		},
		{//bottom
			-1,-1,-1,
			1, 1, 0, 0.5f,
			0,-1, 0,

			-1,-1, 1,
			1, 1, 0, 0.5f,
			0,-1, 0,

			1,-1,-1,
			1, 1, 0, 0.5f,
			0,-1, 0,

			1,-1, 1,
			1, 1, 0, 0.5f,
			0,-1, 0
		}
		};
	}
	
	float[][] plane(){
		return new float[][]{
			{//top
				-100, 0,-100,
				0.8f, 0.8f, 0.8f, 1,
				0, 1, 0,

				-100, 0, 100,
				0.8f, 0.8f, 0.8f, 1,
				0, 1, 0,

				100, 0,-100,
				0.8f, 0.8f, 0.8f, 1,
				0, 1, 0,

				100, 0, 100,
				0.8f, 0.8f, 0.8f, 1,
				0, 1, 0
			},
		};
	}
}
