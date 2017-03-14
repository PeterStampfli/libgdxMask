package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by peter on 3/14/17.
 */

public class Piece extends SpriteWithShape implements Shape2D,AbstractPiece {

    public Piece(Texture texture,Shapes2D shapes){
        super(texture,shapes);
    }

    @Override
    public void render(SpriteBatch batch) {
        draw(batch);
    }

    @Override
    public boolean touchBegin(Vector2 position) {
        return false;
    }

    @Override
    public boolean touchDrag(Vector2 position, Vector2 deltaPosition) {
        translate(deltaPosition.x,deltaPosition.y);
        return true;
    }

    @Override
    public boolean touchEnd() {
        return false;
    }
}
