import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Titas on 2016-06-29.
 */
public class Animation_box extends Entity {
    Level01 level;
    SpriteSheet SS;
    int frame_count;
    int frame = 1;
    int timer = 1;
    float size_factor;
    org.newdawn.slick.Animation anim;


    public Animation_box(Level01 level, float x, float y, int width, int height) {
        super(x, y, width, height);
        this.level = level;
        this.collides = false;
        this.size_factor = 1.2f;
        init();



    }







    public void init() {
        super.init();
    }

    public void update(int delta) {
        this.KYS();
        if (this.SS == null){
            init();
        }
        super.update(delta);
        this.y -= 1 + 1*size_factor;
        this.anim.update(delta);


    }




    public void increment_frame() {
        if (this.frame < this.frame_count) {
            float width = this.width * size_factor;
            float height = this.height * size_factor;
            anim.draw(this.x - width/2, this.y - height/2, this.width*size_factor, this.height*size_factor);
            this.timer += 1;
        }
        if (this.timer % (int) ((Main.fps * 0.0005f) * anim.getDurations()[0]) == 0) {
            this.frame += 1;
            this.size_factor += 0.4f;
        }

    }

    public void render(Graphics g) {
        increment_frame();

    }

    public void KYS(){
        if (this.frame >= this.frame_count) {
            this.level.all.remove(this);
        }
    }

}



class Blood_box extends Animation_box {
    public Blood_box(Level01 level, float x, float y, int width, int height) {
        super(level, x, y, width, height);
        this.anim = this.level.Blood.anim;
        this.frame_count = this.level.Blood.anim.getFrameCount();
    }

    }






