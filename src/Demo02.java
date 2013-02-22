import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Logger;

/**
 * @author D.Winter
 * @link http://www.cnblogs.com/dwinter/archive/2013/02/19/libgdx-box2d-03.html
 * @title Android 游戏引擎libgdx之Box2D 案例实践——打砖块(一)
 */
public class Demo02 extends Stage {
	// 144, 240
	protected final static float RATIO = 3f;
	protected final static float STAGE_W = 48f;
	protected final static float STAGE_H = 80f;

	private World world;
	private Texture tr_ball, tr_paddle;
	private Body ball, paddle, groundBody;
	private Box2DDebugRenderer renderer; // 测试用绘制器
	private Fixture fixPaddle;
	private Logger logger;
	private MouseJoint mouseJoint;

	public Demo02() {
		super(STAGE_W, STAGE_H, true);
		logger = new Logger("demo");

		world = new World(new Vector2(0, 0f), true);
		renderer = new Box2DDebugRenderer();

		tr_ball = new Texture(Gdx.files.internal("assets/Ball.jpg"));
		tr_paddle = new Texture(Gdx.files.internal("assets/Paddle.jpg"));

		// Create edges around the entire screen
		BodyDef bd = new BodyDef();
		bd.position.set(0, 0);
		groundBody = world.createBody(bd);
		EdgeShape edge = new EdgeShape();
		FixtureDef boxShapeDef = new FixtureDef();
		boxShapeDef.shape = edge;
		edge.set(new Vector2(0f, 0f), new Vector2(STAGE_W, 0f));
		groundBody.createFixture(boxShapeDef);
		edge.set(new Vector2(0f, 0f), new Vector2(0f, STAGE_H));
		groundBody.createFixture(boxShapeDef);
		edge.set(new Vector2(STAGE_W, 0f), new Vector2(STAGE_W, STAGE_H));
		groundBody.createFixture(boxShapeDef);
		edge.set(new Vector2(STAGE_W, STAGE_H), new Vector2(0f, STAGE_H));
		groundBody.createFixture(boxShapeDef);

		// Create ball body and shape
		BodyDef ballBodyDef = new BodyDef();
		ballBodyDef.type = BodyType.DynamicBody;
		ballBodyDef.position.set(STAGE_W / 2, STAGE_H / 2);
		ball = world.createBody(ballBodyDef);
		CircleShape circle = new CircleShape();
		circle.setRadius(2f);
		FixtureDef ballShapeDef = new FixtureDef();
		ballShapeDef.shape = circle;
		ballShapeDef.density = 1.0f;
		ballShapeDef.friction = 0f;
		ballShapeDef.restitution = 1.0f;
		ball.createFixture(ballShapeDef);
		Vector2 force = new Vector2(500f, -500f);
		ball.applyLinearImpulse(force, ballBodyDef.position);

		// Create paddle body and shape
		BodyDef paddleBodyDef = new BodyDef();
		paddleBodyDef.type = BodyType.DynamicBody;
		paddleBodyDef.position.set(STAGE_W / 2, 10f);
		paddle = world.createBody(paddleBodyDef);
		PolygonShape paddleShape = new PolygonShape();
		paddleShape.setAsBox(8f, 2f);
		FixtureDef paddleShapeDef = new FixtureDef();
		paddleShapeDef.shape = paddleShape;
		paddleShapeDef.density = 10f;
		paddleShapeDef.friction = 0.4f;
		paddleShapeDef.restitution = 0.1f;
		fixPaddle = paddle.createFixture(paddleShapeDef);

		// 声明移动关节的定义
		PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
		// 设置一个约束向量,只能沿着x轴移动
		Vector2 axis = new Vector2(1f, 0f);
		// 碰撞监测
		prismaticJointDef.collideConnected = true;
		// 初始化移动关节的定义
		prismaticJointDef.initialize(groundBody, paddle,
				paddle.getWorldCenter(), axis);
		// 生成移动关节的定义
		world.createJoint(prismaticJointDef);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// mouseJoint是个变量，只有它失效了才去重新生成
		if (mouseJoint == null) {
			// 这里将真实世界的坐标转换为游戏内的坐标
			// 注意libgdx的坐标系以屏幕左下角为(0,0)
			float yy = STAGE_H - (y / RATIO);
			float xx = x / RATIO;
			// 首先判断当前触点是否在砖块范围内
			if (fixPaddle.testPoint(xx, yy)) {
				// 我们来定义一个鼠标关节的声明
				MouseJointDef mouseJointDef = new MouseJointDef();
				mouseJointDef.bodyA = groundBody;// 设为物理世界的边界
				mouseJointDef.bodyB = paddle;// 我们想要拖动的物体
				// 是否检测2个物体间的碰撞，不检测会怎么样？砖块会飞离屏幕！
				mouseJointDef.collideConnected = true;
				// 最大力设为砖块质量的1000倍
				// 设小点会怎么样？砖块响应你的速度会很慢很慢
				mouseJointDef.maxForce = 1000.0f * paddle.getMass();
				// 好了，让世界生成它吧
				mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);
				// 传入目的地坐标
				mouseJoint.setTarget(new Vector2(x, STAGE_H - y));
			}
		}
		return super.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int p) {
		if (mouseJoint != null) {
			// 更新目的地坐标
			mouseJoint.setTarget(new Vector2(x, STAGE_H - y));
		}
		return super.touchDragged(x, y, p);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (mouseJoint != null) {
			// 当触角离开屏幕时，销毁关节
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
		return super.touchUp(x, y, pointer, button);
	}

	@Override
	public void draw() {
		// 获得小球的当前速度
		Vector2 velocity = ball.getLinearVelocity();
		float speed = velocity.len();
		// 当大于某一个值时，我们设置它的阻尼，来起到减速的效果，具体数值自行调整
		if (speed > 100f) {
			ball.setLinearDamping(0.1f);
		} else if (speed < 100f) {
			ball.setLinearDamping(0f);
		}

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.step(Gdx.app.getGraphics().getDeltaTime(), 8, 8);
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
