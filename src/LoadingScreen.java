/**
 * Created by Titas on 2016-07-01.
 */
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;


public class LoadingScreen extends BasicGameState {
    private LoadingThread loader;
    public List<GameState> gameStateList = new ArrayList<>();
    Image img;
    public void LoadingScreen() {
    }



    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        this.loader = new LoadingThread(this, gc, sbg);
        this.img = new Image("Loading.jpg");
        this.loader.start();

    }


    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
        this.img.draw(0,0,Main.xSize, Main.ySize);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        if (this.loader.loaded){
            this.loader = null;


            for (int i = 0; i < this.gameStateList.size(); i++) {
                try {
                    sbg.addState(gameStateList.get(i));
                    this.gameStateList.get(i).init(gc, sbg);
                } catch (Exception e) {
                System.out.print(e);
                }}
            sbg.enterState(0);
    }}

    @Override
    public int getID() {return -2;}
}

