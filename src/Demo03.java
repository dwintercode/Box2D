import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author D.Winter
 * @link http://www.cnblogs.com/dwinter/archive/2013/02/19/libgdx-box2d-04.html
 * @title Android 游戏引擎libgdx之Box2D基础教程——关节篇(Joints)
 */
public class Demo03 extends Stage {
	private World world;
	private Body bodyA, bodyB;
	private Box2DDebugRenderer renderer; // 测试用绘制器

	public Demo03() {
		super(48f, 80f, true);
		world = new World(new Vector2(0, -9.8f), true);
		renderer = new Box2DDebugRenderer();

		// Create bodyA
		BodyDef bodyDefA = new BodyDef();
		bodyDefA.type = BodyType.DynamicBody;
		bodyDefA.position.set(30f, 75f);
		bodyA = world.createBody(bodyDefA);
		PolygonShape polygonShapeA = new PolygonShape();
		polygonShapeA.setAsBox(3f, 6f);
		bodyA.createFixture(polygonShapeA, 1f);

		// Create bodyB
		BodyDef bodyDefB = new BodyDef();
		bodyDefB.type = BodyType.DynamicBody;
		bodyDefB.position.set(15f, 50f);
		bodyB = world.createBody(bodyDefB);
		PolygonShape polygonShapeB = new PolygonShape();
		polygonShapeB.setAsBox(3f, 3f);
		bodyB.createFixture(polygonShapeB, 1f);

		// Create edge
		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(new Vector2(20, 40), new Vector2(40,40));
		BodyDef bd = new BodyDef();
		bd.position.set(1, 0);
		bd.type = BodyType.StaticBody;
		Body b = world.createBody(bd);
		b.createFixture(edgeShape, 1f);

		// 创建距离关节定义
		DistanceJointDef jointDef = new DistanceJointDef();
		// 锚点分别取各物体的中点
		jointDef.initialize(bodyA, bodyB, bodyA.getWorldCenter(),
				bodyB.getWorldCenter());
		// 我们要监测碰撞
		jointDef.collideConnected = true;
		// 设置阻尼与频率，值越小，弹簧的效果越明显
		//jointDef.dampingRatio = 0.1f;
		//jointDef.frequencyHz = 0.6f;
		

		// 创建距离关节定义
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(bodyA, bodyB, bodyA.getWorldCenter());
		revoluteJointDef.lowerAngle = -0.5f;
		revoluteJointDef.upperAngle = 0.25f;
		revoluteJointDef.enableLimit = true;
		// 由世界创建关节
		world.createJoint(revoluteJointDef);
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
