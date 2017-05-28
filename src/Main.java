/**
 * Created by Titas on 2016-06-07.
 */
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import java.util.ArrayList;
import java.util.List;

public class Main extends StateBasedGame {
    public static final String gamename = "Raitis GAME";
    // Define all the game states here
    public static final int load = -2;
    public static final int menu = 0;
    public static final int play = 1;
    public static final int high = 2;

    // Window Size setup
    public static final int xSize = 1280;
    public static final int ySize= 960;
    public static final int fps = 60;

    public Main(String gamename) {
        super(gamename);
        }


    public void initStatesList(GameContainer gc) throws SlickException {
        this.addState(new LoadingScreen());
        this.enterState(-2);
    }



    public static void main(String[] args){
        AppGameContainer appgc;
        try{
            appgc = new AppGameContainer(new Main(gamename));
            appgc.setDisplayMode(xSize, ySize, false);
            appgc.setTargetFrameRate(fps);
            appgc.enableSharedContext();
            appgc.start();
        }catch(SlickException e){
            e.printStackTrace();
        }
    }
}
