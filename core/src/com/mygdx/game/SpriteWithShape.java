package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


/**
 * Created by peter on 3/12/17.
 */

public class SpriteWithShape extends Sprite implements Shape2D {
    private Array<Shape2D> shapes=new Array<Shape2D>();

    public SpriteWithShape(Texture texture){
        super(texture);
    }

    @Override
    public boolean contains(float x,float y){
        x-=getX();
        y-=getY();
        boolean result=false;
        for (Shape2D shape:shapes){
            result=result||shape.contains(x, y);
        }
        return result;
    }

    @Override
    public boolean contains(Vector2 point) {
        return contains(point.x,point.y);
    }

    public void addShape(Shape2D shape2D){
        shapes.add(shape2D);
    }

    public void addPolygonShape(float[] verticesXY){
        shapes.add(new Polygon(verticesXY));
    }

    public void addPolygonShape(Array<Vector2> vertices){
        shapes.add(new Polygon(Vector2Array.toFloats(vertices)));
    }
}
