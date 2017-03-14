package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by peter on 11/26/16.
 */

public class PieceInteraction {

    private UserInteraction userInteraction;
    private AbstractPiece piece;
    private boolean elementIsSelected;                                 // piece is really selected
    // if there was touch in previous call, we need this because we do polling
    private boolean wasTouching;
    //  positions of last touch and new touch
    private Vector2 oldTouchPosition;
    private Vector2 newTouchPosition;
    //  touchPosition of touch as average (dragging) and displacement
    private Vector2 touchPosition;
    private Vector2 deltaTouchPosition;


    /**
     * create a touch handler
     *
     * @param piece       object,its rendering, selection and moving methods get called,
     * @param userInteraction object for reading touch touchPosition, depends on camera
     */
    public PieceInteraction(AbstractPiece piece, UserInteraction userInteraction) {
        this.piece = piece;
        elementIsSelected = false;
        wasTouching = false;
        this.userInteraction = userInteraction;
        oldTouchPosition = new Vector2();
        newTouchPosition = new Vector2();
        touchPosition = new Vector2();
        deltaTouchPosition = new Vector2();
    }

    private void updatePosition() {
        touchPosition.set(oldTouchPosition).add(newTouchPosition).scl(0.5f);
        deltaTouchPosition.set(newTouchPosition).sub(oldTouchPosition);
    }

    private boolean touchBeginSelection() {
        userInteraction.readTouch(newTouchPosition);
        oldTouchPosition.set(newTouchPosition);
        updatePosition();
        return piece.contains(touchPosition);
    }

    private boolean touchBeginAction() {
        return piece.touchBegin(touchPosition);
    }

    private boolean touchDrag() {
        oldTouchPosition.set(newTouchPosition);
        userInteraction.readTouch(newTouchPosition);
        updatePosition();
        return piece.touchDrag(touchPosition, deltaTouchPosition);
    }

    private boolean touchEnd() {
        oldTouchPosition.set(newTouchPosition);
        updatePosition();
        return piece.touchEnd();
    }

    /**
     * update touch data and call events
     * make sure all touchPosition data is well defined, even when not used
     */
    public void update() {
        // get touch event, for mouse only if the left button is pressed
        boolean isTouching = Gdx.input.isTouched() && input.isButtonPressed(Input.Buttons.LEFT);
        boolean somethingHappened = false;

        if (!wasTouching && isTouching) {
            elementIsSelected = touchBeginSelection();
            if (elementIsSelected) {
                somethingHappened = touchBeginAction();
            }
        }

        if (wasTouching && isTouching) {
            if (elementIsSelected) {
                somethingHappened = touchDrag();
            }
        }

        if (wasTouching && !isTouching) {
            if (elementIsSelected) {
                somethingHappened = touchEnd();
            }
            elementIsSelected = false;
        }

        wasTouching = isTouching;

        if (somethingHappened) {
            Basic.requestRendering();
        }
    }

    /**
     * render the piece(collection)
     * @param batch
     */
    public void render(SpriteBatch batch){
        piece.render(batch);
    }

}
