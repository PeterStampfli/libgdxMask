package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by peter on 3/14/17.
 */

// a single piece or a collection of pieces

public interface AbstractPiece {

    void render(SpriteBatch batch);

    // return true if element is selected by given position
    boolean contains(Vector2 position);

    // begin-touch action, return true if something changed, call requestRendering, this is safer
    boolean touchBegin(Vector2 position);

    // do drag action, return true if something changed
    boolean touchDrag(Vector2 position,Vector2 deltaPosition);

    // end of touch
    boolean touchEnd();


}
