import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Titas on 2016-07-01.
 */
public class LoadingThread extends Thread {
    LoadingScreen loadingScreen;
    GameContainer gc;
    StateBasedGame sbg;
    Graphics g;
    List<BasicGameState> gameStateList = new ArrayList<>();
    boolean loaded = false;
    public LoadingThread(LoadingScreen loadingScreen, GameContainer gc, StateBasedGame sbg ){
        gameStateList.add(new Menu());
        gameStateList.add(new Level01());
        gameStateList.add(new High());
        gameStateList.add(new LoseScreen());
        gameStateList.add(new WinScreen());

        this.gc = gc;
        this.sbg = sbg;
        this.loadingScreen = loadingScreen;
    }

    public void run(){
        if (!this.loaded) {
            if (this.sbg.getStateCount() >= 1) {
                for (int i = 0; i < gameStateList.size(); i++) {

                    this.loadingScreen.gameStateList.add(this.gameStateList.get(i));
                }
            } else {
                try {

                } catch (Exception e) {}
            }


            this.loaded = true;
        }
    }
}
