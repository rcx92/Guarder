package com.qq.guarder;

import android.util.Log;


public class Vector {
	public double x,y,z;
	public static double eps=Math.PI/4;
	public Vector(double xx,double yy,double zz){
		x=xx;y=yy;z=zz;
	}
	public double mod(){
		return Math.sqrt(x*x+y*y+z*z);
	}
	public double dian(Vector v){
		return x*v.x+y*v.y+z*v.z;
	}
	public double cos(Vector v){
		double dianji=dian(v);
		double temp=dianji/mod()/v.mod();
		temp=Math.min(1.0,temp);
		temp=Math.max(-1.0,temp);
		Log.i("save", "cos = "+Math.acos(temp));
		return Math.acos(temp);
	}
	public boolean isCross(Vector v){
		return Math.abs(cos(v)-Math.PI/2)<eps;
	}
	
}
