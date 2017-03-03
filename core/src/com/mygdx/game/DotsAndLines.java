package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

/**
 * Created by peter on 3/3/17.
 */

public class DotsAndLines {
    Array<Vector2> points;
    IntArray lineEndA,lineEndB;
    float halfWidth;
    float epsilon;

    public DotsAndLines(float width,float epsilon){
        halfWidth=0.5f*width;
        this.epsilon=epsilon;
        points=new Array<Vector2>();
        lineEndA=new IntArray();
        lineEndB=new IntArray();
    }

    public DotsAndLines(float width){
        this(width,0.01f);
    }

    private int addPoint(float x,float y){
        int length=points.size;
        for (int i=0;i<length;i++){
            if (points.get(i).epsilonEquals(x,y,epsilon)) return i;
        }
        points.add(new Vector2(x,y));
        return length;
    }

    public void addLine(float x1,float y1,float x2,float y2){
        lineEndA.add(addPoint(x1,y1));
        lineEndB.add(addPoint(x2,y2));
    }

    public void addLines(float... coordinates){
        int length=coordinates.length-2;
        for (int i=0;i<length;i+=2){
            addLine(coordinates[i],coordinates[i+1],coordinates[i+2],coordinates[i+3]);
        }
    }

    public void mask(Mask mask){
        for (Vector2 point:points){
            mask.disc(point.x,point.y,halfWidth);
        }
        Vector2 a,b;
        int length=lineEndA.size;
        for (int i=0;i<length;i++){
            a=points.get(lineEndA.get(i));
            b=points.get(lineEndB.get(i));
            mask.fillLine(a.x,a.y,b.x,b.y,halfWidth);
        }
    }
}
