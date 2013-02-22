/*******************************************************************************
 * Copyright ©2013 D.Winter
 * 
 * More in  http://www.cnblogs.com/dwinter
 * 
 ******************************************************************************/


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
/** 
 * @author D.Winter
 * http://www.cnblogs.com/dwinter/
 * Android 游戏引擎libgdx之Box2D Hello Box2D
 */
public class Demo00 extends Stage {

	private Box2DDebugRenderer renderer; // 测试用绘制器
	private World world;

	public Demo00() {
		super(48f, 80f, true);

		renderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -9.8f), true); // 标准重力场

		CircleShape circleShape = new CircleShape();// 创建一个圆
		circleShape.setRadius(3f); // 设置半径
		addShape(circleShape, new Vector2(16, 70), BodyType.DynamicBody);

		EdgeShape edgeShape = new EdgeShape();// 创建一个边
		edgeShape.set(new Vector2(5, 35), new Vector2(30, 20));// 设置2点
		addShape(edgeShape, new Vector2(1, 0), BodyType.StaticBody);
	}

	public void addShape(Shape shap, Vector2 pos, BodyType type) {
		BodyDef bd = new BodyDef();
		bd.position.set(pos.x, pos.y);
		bd.type = type;
		Body b = world.createBody(bd);
		b.createFixture(shap, 1f);
	}

	@Override
	public void draw() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);
		renderer.render(world, camera.combined);
		super.draw();
	}

	@Override
	public void dispose() {
		renderer.dispose();
		world.dispose();
		super.dispose();
	}
}
