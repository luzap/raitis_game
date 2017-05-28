/**
 * Created by Titas on 2016-07-02.
 */
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;


public class LoseScreen extends BasicGameState {
    Music back;
    boolean pressed = false;
    boolean pressed_before = false;
    Image img;
    public void LoseScreen() {}


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        back = new Music("audio/highscore.ogg");
        img = new Image("res/Losescreen.png");
    }



    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        this.img.draw(0, 0, Main.xSize, Main.ySize);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        this.pressed = false;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            this.pressed = true;

        }
            if (this.pressed_before && !this.pressed){
                try {
                    GameState g = sbg.getState(-2);
                    g.init(gc, sbg);
                } catch (Exception e) {
                }
                sbg.enterState(-2, new FadeOutTransition(Color.black, 500), new FadeInTransition(Color.red, 500));
            }

            this.pressed_before = this.pressed;
        }


    @Override
    public int getID() {return -3;}
}

