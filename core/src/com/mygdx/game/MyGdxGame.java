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
import com.badlogic.gdx.math.Polyline;
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
	
	@Override
	public void create () {
		camera=new OrthographicCamera();
		batch = new SpriteBatch();
		shapeRenderer=new ShapeRenderer();
		FileHandle bad=Gdx.files.internal("badlogic.jpg");

		Pixmap badmap=new Pixmap(bad);
		Mask mask;
		mask=new Mask(100,100);

		Pixmap rgba=mask.createPixmap();
		rgba.setColor(Color.WHITE);
		rgba.fill();
		//mask.setLimits(1,48,1,48);
	//	mask.disc(20f,40f,10);
		//mask.fillLine(20f,20f,20,40,10);
		//mask.setLimits();

		//mask.fillPolygon(10,10,40,20,30,49,-10,20);
		//mask.fillPolygon(40,20,140,40,30,249,30,49);
		//mask.invertLimits();

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
		mask.noLimits();
		Polygon polygon= Shapes2D.createPolygon(triangle);
		Circle circle=new Circle(10,30,5);
		Rectangle rectangle=new Rectangle(0,0,80,30);
		Shapes2D shapes=new Shapes2D();
		//shapes.add(polygon);
		//shapes.add(circle);
		shapes.add(rectangle);
		//mask.fillCircle(circle);
		//mask.invertWithinLimits();
		mask.fill(shapes);
		//mask.fillRect(rectangle);
		//mask.fillRect(1,1,9,19);

		mask.setPixmapAlpha(rgba);

		//rgba=mask.cutFromPixmap(badmap,10,50);
		mask.setPixmapAlpha(rgba);

		img = new Texture(rgba);
		//img = new Texture(badmap);
		rgba.dispose();

		img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		//img.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		sprite=new SpriteWithShape(img,shapes);

		Vector2[] vertices=new Vector2[]{a,b,t};
		//shapes.addPolygon(triangle);

		sprite.setX(100);
		sprite.setY(200);
		//sprite.setRotation(80);

		Polyline polyline=new Polyline();
		mask.fill(polyline);
	}

	@Override
	public void resize(int width,int height) {
		camera.setToOrtho(false);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sprite.setColor(Color.WHITE);
		Vector3 spacePositionOfTouch=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
		//       Logger.log("Spacex "+spacePositionOfTouch);
		camera.unproject(spacePositionOfTouch);

		if (sprite.contains(spacePositionOfTouch.x,spacePositionOfTouch.y)){
			sprite.setColor(Color.GREEN);
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//batch.draw(img, 0, 0,400,400);
		sprite.draw(batch);
		batch.end();
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.YELLOW);
		shapeRenderer.circle(sprite.getX(),sprite.getY(),5);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.circle(sprite.getX()+sprite.getOriginX(),sprite.getY()+sprite.getOriginY(),7);

		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
