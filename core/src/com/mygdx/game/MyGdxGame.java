package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		Mask mask=new Mask(50,50);
		mask.setLimits(1,48,1,48);
		//mask.disc(20.5f,20,25);
		mask.setLimits();

		mask.fill();
		mask.resetShapeVertices();

		mask.addShapeVertex(10,10).addShapeVertex(40,15).addShapeVertex(25,45);
		mask.makeShapeLines();

		img = mask.imageWhite();

		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		img.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.draw(img, img.getWidth(), img.getHeight(),4*img.getWidth(),4*img.getHeight());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
