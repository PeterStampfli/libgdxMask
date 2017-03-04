package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.FloatArray;

/**
 * Created by peter on 3/3/17.
 */

public class CircleToLines {
    static float maxDeltaAngle=0.1f;

    static public FloatArray basic(float centerX,float centerY,float radius,
                                float alpha,float beta){
        int nSegments=MathUtils.ceil(Math.abs(beta-alpha)/maxDeltaAngle);
        float deltaAngle=(beta-alpha)/nSegments;
        float angle=alpha;
        FloatArray result=new FloatArray();
        for (int i=0;i<=nSegments;i++){
            result.add(centerX+radius*MathUtils.cos(angle));
            result.add(centerY+radius*MathUtils.sin(angle));
            angle+=deltaAngle;
        }
        return result;
    }


    static public FloatArray aBCenter(float ax, float ay, float bx, float by,
                                      float centerX, float centerY, boolean clockwise){
        float alpha=MathUtils.atan2(ay-centerY,ax-centerX);
        float beta=MathUtils.atan2(by-centerY,bx-centerX);
        float radius=(float) Math.sqrt((ax-centerX)*(ax-centerX)+(ay-centerY)*(ay-centerY));
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
        return CircleToLines.basic(centerX,centerY,radius,alpha,beta);
    }

    static public FloatArray aBC(float ax,float ay,float bx,float by,float cx,float cy){
        float centerX,centerY;



        return new FloatArray();
    }
}

