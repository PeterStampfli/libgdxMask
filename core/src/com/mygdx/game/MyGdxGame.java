package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Texture bad;
	TextureRegion region;
	SpriteWithShape sprite;
	OrthographicCamera camera;
	ShapeRenderer shapeRenderer;
	UserInteraction userInteraction;
	Piece piece;
	PieceInteraction pieceInteraction;
	
	@Override
	public void create () {
		camera=new OrthographicCamera();
		batch = new SpriteBatch();
		shapeRenderer=new ShapeRenderer();
		userInteraction=new UserInteraction(camera);
		FileHandle bad=Gdx.files.internal("badlogic.jpg");

		Pixmap badmap=new Pixmap(bad);
		Mask mask;
		mask=new Mask(100,100);

		Pixmap rgba=mask.createPixmap();
		rgba.setColor(Color.WHITE);
		rgba.fill();

		//mask.fillLine(2,2,49,40,6);
		DotsAndLines dotsAndLines=new DotsAndLines();
		//dotsAndLines.addLines(15,15f,100,15f,80,80,70,90,20,30);
		Vector2 a=new Vector2(10,10);
		Vector2 b=new Vector2(80,10);
		Vector2 t=new Vector2(90,90);
		Array<Vector2> triangle=new Array<Vector2>();

		//dotsAndLines.addArcABTangent(a,b,t,true);
		//dotsAndLines.addArcABSomeCenter(a,b,t,true);
		dotsAndLines.addArcABC(a,b,t);
		//dotsAndLines.addBasicArc(50,50,20,0,2.3f,true);
		//dotsAndLines.drawDotsAndLines(mask,6);
		//dotsAndLines.fillPolygon(mask);
		//mask.fillLine(200,10,10,100,10);
		triangle.add(a);
		triangle.add(b);
		triangle.add(t);
		//mask.fillPolygon(triangle);
		//mask.noLimits();
		Polygon polygon= Shapes2D.createPolygon(triangle);
		Circle circle=new Circle(10,30,5);
		Rectangle rectangle=new Rectangle(1,1,98,98);
		Shapes2D shapes=new Shapes2D();
		//shape2DArray.add(polygon);
		//shape2DArray.add(circle);
		shapes.add(rectangle);
		//mask.fillCircle(circle);
		//mask.invertWithinLimits();
		mask.fill(shapes);
		//mask.fillRect(rectangle);
		//mask.fillRect(1,1,9,19);

		mask.setPixmapAlpha(rgba);

		//rgba=mask.cutFromPixmap(badmap,10,50);

		img = new Texture(rgba);
		rgba.dispose();

		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		//img.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		sprite=new SpriteWithShape(img,shapes);

		sprite.setX(100);
		sprite.setY(200);
		//sprite.setRotation(80);
		 piece=new Piece(img,shapes);
		Pieces pieces=new Pieces();
		pieces.add(piece);
		pieceInteraction=new PieceInteraction(pieces,userInteraction);

	}

	@Override
	public void resize(int width,int height) {
		camera.setToOrtho(false);
		userInteraction.resize(width, height);
	}

	@Override
	public void render () {
		Basic.setContinuousRendering(false);
		pieceInteraction.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sprite.setColor(Color.WHITE);
		Vector3 spacePositionOfTouch=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
		//       L.log("Spacex "+spacePositionOfTouch);
		camera.unproject(spacePositionOfTouch);
		piece.setColor(Color.WHITE);
		if (piece.contains(spacePositionOfTouch.x,spacePositionOfTouch.y)){
			piece.setColor(Color.GREEN);
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//batch.draw(img, 0, 0,400,400);
		//sprite.draw(batch);
		pieceInteraction.render(batch);
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
