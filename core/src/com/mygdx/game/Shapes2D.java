package com.mygdx.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by peter on 3/13/17.
 */

public class Shapes2D implements Shape2D {
    Array<Shape2D> shape2DArray =new Array<Shape2D>();

    @Override
    public boolean contains(float x, float y){
        boolean result=false;
        for (Shape2D shape: shape2DArray){
            result=result||shape.contains(x, y);
        }
        return result;
    }


    @Override
    public boolean contains(Vector2 point) {
        return contains(point.x,point.y);
    }

    public void add(Shape2D shape2D){
        shape2DArray.add(shape2D);
    }

    public void addPolygon(float[] verticesXY){
        add(new Polygon(verticesXY));
    }

    static public Polygon createPolygon(Array<Vector2> vertices){
        return new Polygon(Vector2Array.toFloats(vertices));
    }

    public void addPolygon(Array<Vector2> vertices){
        add(Shapes2D.createPolygon(vertices));
    }

    public void addCircle(float x,float y,float radius){
        add(new Circle(x,y,radius));
    }

    public void addRectangle(float x,float y,float width,float height){
        add(new Rectangle(x,y,width,height));
    }
}
