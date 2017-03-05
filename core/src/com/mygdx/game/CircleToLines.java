package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by peter on 3/3/17.
 */

public class CircleToLines {
    static float maxDeltaAngle=0.1f;

    // creating circle points around center(X,Y) with radius
    //  from angle alpha to beta
    //  counterClockwise determines sense (independent of cut at angle=+/-PI)
    static public Array<Vector2> basic(float centerX, float centerY, float radius,
                                       float alpha, float beta, boolean counterClockwise) {
        if (counterClockwise) {
            if (beta < alpha) {
                beta += MathUtils.PI2;
            }
        } else {
            if (beta > alpha) {
                beta -= MathUtils.PI2;
            }
        }
        int nSegments=MathUtils.ceil(Math.abs(beta-alpha)/maxDeltaAngle);
        float deltaAngle=(beta-alpha)/nSegments;
        float angle=alpha;
        Array<Vector2> result=new Array<Vector2>();
        for (int i=0;i<=nSegments;i++){
            result.add(new Vector2(centerX+radius*MathUtils.cos(angle),
                    centerY+radius*MathUtils.sin(angle)));
            angle+=deltaAngle;
        }
        return result;
    }

    static public Array<Vector2> aBCenter(float ax, float ay, float bx, float by,
                                      float centerX, float centerY, boolean counterClockwise){
        float alpha=MathUtils.atan2(ay-centerY,ax-centerX);
        float beta=MathUtils.atan2(by-centerY,bx-centerX);
        float radius=(float) Math.sqrt((ax-centerX)*(ax-centerX)+(ay-centerY)*(ay-centerY));
        return CircleToLines.basic(centerX,centerY,radius,alpha,beta,counterClockwise);
    }

    static public Array<Vector2> aBC(float ax,float ay,float bx,float by,float cx,float cy){
        float a2mb2=(ax-bx)*(ax+bx)+(ay-by)*(ay+by);
        float a2mc2=(ax-cx)*(ax+cx)+(ay-cy)*(ay+cy);
        Gdx.app.log("a2mb2",""+a2mb2);
        Gdx.app.log("a2mc2",""+a2mc2);
        float den=2*((by-ay)*(cx-ax)-(cy-ay)*(bx-ax));
        Gdx.app.log("",""+den);
        float centerX=((cy-ay)*a2mb2-(by-ay)*a2mc2)/den;
        float centerY=-((cx-ax)*a2mb2-(bx-ax)*a2mc2)/den;
        Gdx.app.log("center",centerX+" "+centerY);
        float alpha=MathUtils.atan2(ay-centerY,ax-centerX);
        float beta=MathUtils.atan2(by-centerY,bx-centerX);
        float gamma=MathUtils.atan2(cy-centerY,cx-centerX);
        Gdx.app.log("alpha",""+alpha);
        Gdx.app.log("beta",""+beta);
        Gdx.app.log("gamma",""+gamma);
        float radius=(float)Math.sqrt((ax-centerX)*(ax-centerX)+(ay-centerY)*(ay-centerY));
        boolean counterClockwise=((alpha<beta)&&(beta<gamma))||((beta<gamma)&&(gamma<alpha))||((gamma<alpha)&&(alpha<beta));

        return CircleToLines.basic(centerX,centerY,radius,alpha,gamma,counterClockwise);
    }
}

