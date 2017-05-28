import org.newdawn.slick.TrueTypeFont;

/**
 * Created by Titas on 2016-07-02.
 */
public class EventBox extends Entity {
    Level01 level;
    public EventBox(Level01 level, float x, float y, int width, int height){
        super(x, y, width, height);
        this.level = level;
        this.collides = false;
    }

    public void update(int delta) {
        super.update(delta);
        if (this.isCollide(this.level.Raitis_char)) {
            this.call_event();
            this.level.all.remove(this);
        }

    }

    public void call_event(){

    }

}

class AnimationEventBox extends EventBox {
    float[] lst;
    public AnimationEventBox(Level01 level, float x, float y, int width, int height, float[] lst, String str){
        super(level, x, y, width, height);
        this.lst = lst;
        java.awt.Font font = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 50);
        TrueTypeFont fonts= new TrueTypeFont(font, false);
        try {
            this.level.dialogueSequence = new DialogueSequence(fonts, str, 200, Main.ySize/2);
        } catch (Exception e) {

        }
    }
    public void call_event(){
        this.level.add_global_move_animation(lst[0], lst[1], lst[2]);
    }

}


class BossEventBox extends EventBox {
    public BossEventBox(Level01 level, float x, float y, int width, int height){
        super(level, x, y, width, height);
    }
    public void call_event(){
        Raitis raitis = this.level.Raitis_char;
        raitis.v_x = 0;
        raitis.v_y =0;
        this.level.add_global_move_animation(-raitis.x + raitis.width , -raitis.y - raitis.height + Main.ySize -40 - raitis.v_y , 1);
        this.level.start_boss_battle();


    }

}