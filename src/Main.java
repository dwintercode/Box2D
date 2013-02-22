import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Main {

	public static void main(String[] argv) {
		new LwjglApplication(new DemoGame(), "DWINTER_GAME", 144, 240, true);
	}
}