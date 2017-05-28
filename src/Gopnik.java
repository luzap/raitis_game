import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by Titas on 2016-06-27.
 */
public class Gopnik extends EnemyWalking {
    int jump_timer = 0;
    boolean time_to_jump = false;
    public Gopnik(Level01 level, float x, float y, int width, int height) {
        super(level, x, y, width, height);
    }

    public void update(int delta) {
        if (this.active) {
            move_behavior();
        }
        super.update(delta);



    }

    public void init() {
        super.init();
        load_animations();

    }

    public void load_animations() {
        try {
            this.ss_jumping = new SpriteSheet("res/Dancing_gopnik.png", 250, 492);
        } catch (Exception e){}
        this.a_jumping = new Animation(ss_jumping, new int [] {0,0} , new int[] {100});
    }

    public void render(Graphics g) {
        super.render(g);
        this.a_jumping.getCurrentFrame().getFlippedCopy(right ? false : true, false).draw(this.x, this.y, this.width, this.height);
    }

    public void do_with_up_collide() {
        if (collidedUp != null) {
            this.jump_timer += 1;
            if (this.v_x == 0) {
                this.standing = true;
                this.running = false;
            } else {
                this.standing = false;
                this.running = true;
            }
            Entity pl;
            pl = collidedUp;
            y = pl.y - this.height;
            this.v_y = 0;
            collidedUp = null;
            this.falling = false;
        } else {
            this.falling = true;
            this.running = false;
            this.standing = false;
        }


    }

    public void move_behavior() {
        if (this.jump_timer > Main.fps/2) {
            v_y = -20;
            this.jump_timer = 0;
        }
        if (falling) {

        Raitis raitis = this.level.Raitis_char ;
        if (raitis.x > this.x) {
            this.v_x = 7;
            this.right = true;
        }
        else {
            this.right = false;
            this.v_x = -7;
        }
    } else {
            v_x = 0;
        }

    }

}

class ThrowingGopnik extends EnemyWalking {
    float throw_timer;
    public ThrowingGopnik(Level01 level, float x, float y, int width, int height){
        super(level, x, y, width, height);
        throw_timer = 3;
    }

    public void init(){
        super.init();
        this.load_animations();
    }

    public void render(Graphics g){
        super.render(g);
        this.a_standing.getCurrentFrame().getFlippedCopy(!right, false).draw(this.x, this.y, this.width, this.height);
    }

    public void update(int delta){
        super.update(delta);
        this.throw_bottle(3);

    }
    public void load_animations() {
        try {
            this.ss_standing = new SpriteSheet("res/Squating_Gopnik.png", 266, 390);
        } catch (Exception e){

        }
        this.a_standing = new Animation(ss_standing, new int [] {0,0} , new int[] {100});
    }


    public void generate_velocities(Enemy target, float speed){
        float change_x = target.x-this.x;
        float change_y = target.y-this.y;
        float change_t = Math.abs(change_x) + Math.abs(change_y);
        float v_x = change_x/change_t  *  speed;
        float v_y = change_y/change_t  * speed;
        if (v_y < 0) {
            v_y *=2;
        }
        this.level.add_projectile(new Bottle_projectile(this, this.x, this.y, v_x, v_y, 20, 46, 1, true));
    }


    public void throw_bottle(float time){
        Raitis raitis = this.level.Raitis_char ;
        if (raitis.x > this.x) {
            this.right = true;
        }
        else {
            this.right = false;
        }
        if (this.throw_timer <= 0) {
            this.throw_timer = Main.fps * time;
            this.generate_velocities(raitis, 15);
        }
        this.throw_timer -= 1;

    }

}