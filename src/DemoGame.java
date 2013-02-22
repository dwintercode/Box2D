import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class DemoGame extends Game {

	Demo03 demo = null;

	@Override
	public void create() {
		demo = new Demo03();
		Gdx.input.setInputProcessor(demo);
	}

	@Override
	public void render() {
		demo.draw();
	}

	@Override
	public void dispose() {
		demo.dispose();
		super.dispose();
	}
}
