import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class High extends BasicGameState {
    Music back;

    public void High() {}


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        back = new Music("audio/highscore.ogg");
    }


    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        if (back.playing() == false) {
            back.loop();
        }
    }

    @Override
    public int getID() {return 2;}
}
