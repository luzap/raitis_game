import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;

public class Menu extends BasicGameState {
    // Font initialization
    private Font load_font;
    TrueTypeFont font;
    Image img;
    //Sounds
    Sound button;
    Sound background;

    // Button positioning
    private String[] labels = new String[]{"New Game", "Exit"};
    private MainButton[] menu = new MainButton[labels.length];

    //Selected item
    private int selected = 0;

    public Menu() {}

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        background = new Sound("audio/background.ogg");
        this.img = new Image("img/background.png");
        button  = new Sound("audio/switch.ogg");
        load_font = new Font("Helvetica", Font.BOLD, 20);
        font = new TrueTypeFont(load_font, false);

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        //Load and draw background

        img.draw(0, 0, Main.xSize, Main.ySize);

        //Positions Buttons
        float[] pos = new float[]{(float) Main.xSize / 3, (float) Main.ySize / (menu.length + 2)};
        for (int iter = 0; iter < menu.length; iter++) {
            menu[iter] = new MainButton(g, font, labels[iter], pos[0], pos[1] * (iter + 1));
        }
        menu[selected].render(g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        if (background.playing() == false) {
            background.loop(1.0f, 0.2f);
        }
        System.out.println(sbg.getStateCount());
        if (sbg.getCurrentState() == this){
        if (input.isKeyPressed(input.KEY_DOWN) || input.isKeyPressed(input.KEY_S)) {
            selected += 1;
            selected = wrap_around(selected);
            button.play();
        } else if (input.isKeyPressed(input.KEY_UP) || input.isKeyPressed(input.KEY_W)) {
            selected -= 1;
            selected = wrap_around(selected);
            button.play();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            button.play();
            switch (selected) {
                case 0:
                    background.stop();
                    Level01 level = (Level01) sbg.getState(1);
                    level.reset_world();
                    sbg.enterState(Main.play);
                    break;
                case 1:
                    gc.exit();
            }
        }
    }}

    private int wrap_around(int selected) {
        switch (selected) {
            case -1:
                return 1;
            case 2:
                return 0;
            default:
                return selected;
        }
    }

    @Override
    public int getID() {
        return 0;
    }
}

