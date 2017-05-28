import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

import java.util.Arrays;

/**
 * Created by Titas on 2016-06-29.
 */
public class Animation_holder {
    Animation anim;
    SpriteSheet SS;
    public Animation_holder(String link, int frames, int width, int height, int framelength){
        try {
            SS = new SpriteSheet(link, width, height);
        } catch (Exception e) {}
        frames = frames -1;
        int [] framelengths = new int [frames];
        int [] sprite_cut = new int[frames*2];
        for (int i = 0; i < sprite_cut.length; i++) {
            if (i%2 == 0){
                sprite_cut[i] = i/2;
                framelengths[i/2] = framelength;
            } else {
                sprite_cut[i] = 0;
            }
        }
        System.out.println(Arrays.toString(framelengths));
        this.anim = new Animation(this.SS, sprite_cut, framelengths);

    }
}
