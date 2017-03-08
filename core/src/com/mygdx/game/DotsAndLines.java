package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by peter on 3/3/17.
 */

public class DotsAndLines {
    Array<Vector2> points;
    Array<Line> lines;
    float epsilon;
    float maxDeltaAngle=0.1f;



    public DotsAndLines(float epsilon){
        this.epsilon=epsilon;
        points=new Array<Vector2>();
        lines=new Array<Line>();
    }

    public DotsAndLines(){
        this(0.01f);
    }

    public void drawDotsAndLines(Mask mask,float width){
        float halfWidth=0.5f*width;
        for (Vector2 point:points){
            mask.disc(point,halfWidth);
        }
        for (Line line:lines){
            mask.fillLine(line.a,line.b,halfWidth);
        }
    }

    public void fillShape(Mask mask){
        mask.fillShape(points);
    }

    private class Line{
        Vector2 a;
        Vector2 b;

        public Line(Vector2 a,Vector2 b){
            this.a=a;
            this.b=b;
        }
    }

    private void addPoint(Vector2 p){
        int length=points.size;
        for (int i=0;i<length;i++){
            if (points.get(i).epsilonEquals(p,epsilon)) return ;
        }
        points.add(p);
    }

    public void addLineTo(Vector2 p){
        lines.add(new Line(points.peek(),p));
        addPoint(p);
    }

    public void addLine(Vector2 a,Vector2 b){
        addPoint(a);
        addPoint(b);
        lines.add(new Line(a,b));
    }

    public void addLines(Array<Vector2> points){
        int length=points.size-1;
        for (int i=0;i<length;i+=2){
            addLine(points.get(i),points.get(i+1));
        }
    }

    public void addLines(float... coordinates){
        int length=coordinates.length-2;
        for (int i=0;i<length;i+=2){
            addLine(new Vector2(coordinates[i],coordinates[i+1]),new Vector2(coordinates[i+2],coordinates[i+3]));
        }
    }

    // creating circle points around center(X,Y) with radius
    //  from angle alpha to beta
    //  counterClockwise determines sense (independent of cut at angle=+/-PI)
    public void addBasicArc(Vector2 center, float radius,
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
        Gdx.app.log("nseg",""+nSegments);
        float deltaAngle=(beta-alpha)/nSegments;
        float angle=alpha;
        Vector2 lastPoint=new Vector2(center.x+radius*MathUtils.cos(alpha),
                                        center.y+radius*MathUtils.sin(alpha));
        Gdx.app.log("Startpoint",""+lastPoint);
        Vector2 nextPoint;
        for (int i=0;i<=nSegments;i++){
            nextPoint=new Vector2(center.x+radius*MathUtils.cos(angle),
                                    center.y+radius*MathUtils.sin(angle));
            angle+=deltaAngle;
            addLine(lastPoint,nextPoint);
            lastPoint=nextPoint;
        }
    }

    public void addArcABSomeCenter(Vector2 a,Vector2 b,Vector2 someCenter,boolean counterclockwise){
        Vector2 unitACenter=new Vector2(someCenter).sub(a);
        unitACenter.scl(1f/unitACenter.len());
        Vector2 aB=new Vector2(b).sub(a);
        float radius=0.5f*aB.len2()/aB.dot(unitACenter);
        Vector2 center=new Vector2(a).mulAdd(unitACenter,radius);
        Gdx.app.log("center",""+center);
        float alpha=MathUtils.atan2(a.y-center.y,a.x-center.x);
        Gdx.app.log("alpha",""+alpha);
        Gdx.app.log("radius",""+radius);
        float beta=MathUtils.atan2(b.y-center.y,b.x-center.x);
        addBasicArc(center,Math.abs(radius),alpha,beta,counterclockwise);
    }

    public void addArcABTangent(Vector2 a,Vector2 b,Vector2 tangentPoint,boolean counterclockwise){
        Vector2 someCenter=new Vector2(tangentPoint).sub(a).rotate90(0).add(a);
        Gdx.app.log("",""+someCenter);
        addArcABSomeCenter(a,b,someCenter,counterclockwise);
    }

}
