

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
/** 
 * @author D.Winter
 * http://www.cnblogs.com/dwinter/
 * Android 游戏引擎libgdx之Box2D 案例实践——弹球
 */
public class Demo01 extends Stage {
	private World world;
	private Texture ball;
	private Body body;

	public Demo01() {
		super(48f, 80f, true);

		world = new World(new Vector2(0, -30f), true);
		ball = new Texture(Gdx.files.internal("assets/Ball.jpg"));

		// Create edges around the entire screen
		BodyDef bd = new BodyDef();
		bd.position.set(2, 2);
		Body groundBody = world.createBody(bd);		
		EdgeShape edge = new EdgeShape();
		edge.set(new Vector2(0f, 0f), new Vector2(48f, 0f));
		FixtureDef boxShapeDef = new FixtureDef();
		boxShapeDef.shape = edge;
		groundBody.createFixture(boxShapeDef);

		// Create ball body and shape
		BodyDef ballBodyDef = new BodyDef();
		ballBodyDef.type = BodyType.DynamicBody;
		ballBodyDef.position.set(24f, 60f);
		body = world.createBody(ballBodyDef);
		body.setUserData("ball");
		CircleShape circle = new CircleShape();
		circle.setRadius(2f);
		FixtureDef ballShapeDef = new FixtureDef();
		ballShapeDef.shape = circle;
		ballShapeDef.density = 1.0f;
		ballShapeDef.friction = 0.2f;
		ballShapeDef.restitution = 0.8f;
		body.createFixture(ballShapeDef);
	}

	@Override
	public void draw() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		world.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);
		batch.begin();
		batch.draw(ball, body.getPosition().x, body.getPosition().y, 5f, 5f);
		batch.end();
		super.draw();
	}

	@Override
	public void dispose() {
		// renderer.dispose();
		world.dispose();
		super.dispose();
	}
}
