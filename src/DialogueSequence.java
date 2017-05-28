import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.util.*;


public class DialogueSequence {
    private final TrueTypeFont font;
    List<String> dialogue_list = new ArrayList<>();
    Iterator<String> dialogue_lines;

    final int chars_per_line = 40;
    private int string_pos = 0;
    private int character = 1;
    private int char_rate = 7;
    boolean done = false;
    float x;
    float y;

    String line;
    Image box; // The box that encapsulates text. Can be changed to Rect
    Image person; // The person doing the talking


    public DialogueSequence(TrueTypeFont font, String dialogue, float x, float y) throws SlickException {
        this.font = font;
        this.box = new Image("img/background.png");
        List<String> dialogue_words = new ArrayList<>(Arrays.asList(dialogue.split("(<=\\G.{40})")));
        dialogue_lines = dialogue_words.iterator();
        line = dialogue_lines.next();
        this.x = x;
        this.y = y;
    }



    public boolean render(int iter) {
        if (iter != (dialogue_list.size() * char_rate * chars_per_line)) {
            if (iter % (char_rate * line.length()) == 0) { // For some reason this is called twice.
                try {
                    line = dialogue_lines.next();
                } catch (NoSuchElementException ex) {}
                character = 0;
                this.done = true;
                //System.out.println("CALLED");
            }
            if (iter % char_rate == 0) {
                character += 1;
            }
            this.font.drawString(x, y, line.substring(0, character));
            return false;
        }else {
            return true;
        }
    }
}