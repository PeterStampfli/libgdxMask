package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by peter on 3/3/17.
 */

public class CircleToLines {
    static float maxDeltaAngle=0.1f;


    static public float[] aBCenter(float ax,float ay,float bx,float by,
                                   float centerX,float centerY,boolean clockwise){
        float alpha=MathUtils.atan2(ay-centerY,ax-centerX);
        float beta=MathUtils.atan2(by-centerY,bx-centerX);
        float radius=(float) Math.sqrt((ax-centerX)*(ax-centerX)+(ay-centerY)*(ay-centerY));
        int nSegments;
        if (clockwise){
            if (beta<alpha){
                beta+=MathUtils.PI2;
            }
        }
        else {
            if (beta>alpha){
                beta-=MathUtils.PI2;
            }
        }
        nSegments=MathUtils.ceil(Math.abs(beta-alpha)/maxDeltaAngle);
        float deltaAngle=(beta-alpha)/nSegments;
        float angle=alpha;
        float[] result=new float[2*nSegments+2];
        result[0]=ax;
        result[1]=ay;
        for (int i=1;i<=nSegments;i++){
            angle+=deltaAngle;
            result[2*i]=radius*MathUtils.cos(angle);
            result[2*i+1]=radius*MathUtils.sin(angle);
        }



        return result;
    }
}
