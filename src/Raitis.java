import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.util.List;

/**
 * Created by Titas on 2016-06-25.
 */
public class Raitis extends EnemyWalking {


    SpriteSheet ss_jumpingB;
    org.newdawn.slick.Animation a_jumpingB;

    SpriteSheet ss_runningB;
    Animation a_runningB;

    SpriteSheet ss_standingB;
    Animation a_standingB;

    Ball ball;

    boolean J_KEY_down;
    boolean ball_picked_up = false;



    float [] view_box = {Main.xSize / 3};


    boolean ball_in_hand = true;


    public Raitis(Level01 level, float x, float y, int width, int height) {
        super(level, x, y, width, height);
        melee = false;
        is_player = true;
        this.standing = true;
    }



    void load_animations() {
        try {

            this.ss_standing = new SpriteSheet("res/SS_standing.png", 480 , 864);
            this.a_standing = new Animation(ss_standing, new int[]{0,0,1,0}, new int[]{500, 600});
            this.ss_standingB = new SpriteSheet("res/SS_standingB.png", 384 , 864);
            this.a_standingB = new Animation(ss_standingB, new int[]{0,0,1,0}, new int[]{500, 600});

            this.ss_running = new SpriteSheet("res/SS_running.png", 544 , 800);
            this.a_running = new Animation(ss_running, new int [] {0,0, 1,0,2,0} , new int[] {100, 200, 100});
            this.ss_runningB = new SpriteSheet("res/SS_runningB.png", 446 , 800);
            this.a_runningB = new Animation(ss_runningB, new int [] {0,0, 1,0,2,0} , new int[] {100, 200, 100});

            this.ss_jumping = new SpriteSheet("res/SS_jumping.png", 480 , 864);
            this.a_jumping = new Animation(ss_jumping, new int [] {0,0} , new int[] {100});
            this.ss_jumpingB = new SpriteSheet("res/SS_jumpingB.png", 500 , 1024);
            this.a_jumpingB = new Animation(ss_jumpingB, new int [] {0,0} , new int[] {100});

        } catch(Exception e) {
            System.out.print(e);
        }
    }




    public void init() {
        super.init();
        load_animations();
        J_KEY_down = false;

    }
    // function to call in the level to update status of character


    public void move() {
        //overwritten so that it does not affect velocity
    }

    public void key_move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if (jumpkeydown == false && jump > 0) {
                v_y = -26;
                jump -= 1;
                jumpkeydown = true;
            }



        } else {
            jumpkeydown = false;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            v_x = 10;
            this.right = true;
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
            v_x = -10;
            this.right = false;
        }
        else {
            v_x = 0;
        }
    }



    public void make_ball() {
        // function used to make a ball in going in all 4 directions
        float speed = 25;
        int ball_size = 50;
        float spawn_x = this.x + this.v_x - ball_size - 1;
        float spawn_y = this.y + this.height/3;
        float spawn_vx = 0;
        float spawn_vy = 0;
        float acc_x = 0;
        float acc_y = 0;
        boolean released = false;


        if (Keyboard.isKeyDown(Keyboard.KEY_J) && ball_in_hand && !J_KEY_down) {
            released = true;
            J_KEY_down = true;
            if (this.right) {
                spawn_x = this.x + this.width + this.v_x + 1;
                spawn_vx = this.v_x + speed;
                spawn_vy = 0;
                acc_y = 0.05f;
                acc_x = -1;
            } else {
                spawn_vx = this.v_x - speed;
                spawn_vy = 0;
                acc_y = 0.05f;
                acc_x = 1;
            }
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_J)) {
            J_KEY_down = false;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
            released = true;
            if (this.right) {
                spawn_x = this.x + this.width + this.v_x + 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)){
                spawn_vy = speed + this.v_y * 0.4f;
                spawn_vx = this.v_x;
                acc_y = this.gravity;
            } else {
                spawn_vy = -1.2f*speed + this.v_y *0.4f;
                spawn_vx = this.v_x;
                acc_y = this.gravity;
            }
        }
        if (released && ball_in_hand) {
            this.ball = new Ball(this, spawn_x, spawn_y, spawn_vx, spawn_vy, ball_size, ball_size, 2, false);
            this.ball.a_x = acc_x;
            this.ball.a_y = acc_y;
            ball_in_hand = false;
            this.level.add_projectile(this.ball);
        }

    }


    public void change_pos(){
        this.x += v_x;
        this.y += v_y;
    }

    public void update_animations(int delta) {
        super.update_animations(delta);
        a_standingB.update(delta);
        a_runningB.update(delta);
        a_jumpingB.update(delta);
    }


    public void update(int delta) {

        this.make_ball();
        this.check_ball_in_hand();
        this.key_move();
        super.update(delta);
    }

    public void check_ball_in_hand() {
        // Specific behavior for the player's ball throw.
        if (ball_in_hand == false) {
            if (!this.level.projectiles.contains(this.ball) || this.ball.hit_creator){
                this.ball_in_hand = true;
                this.ball = null;
            }
        }
    }



    public void animate() throws SlickException {
        float pos_x;
        if (this.right) {
            pos_x = this.x;
        } else {
            pos_x = this.x - this.width;
        }
        Image currentFrame = new Image("res/blank.png");

        if (this.ball_in_hand) {
            if (this.standing) {
                currentFrame = this.a_standing.getCurrentFrame().getFlippedCopy(right ? false : true, false);
            } else if (this.running) {
                currentFrame = this.a_running.getCurrentFrame().getFlippedCopy(right ? false : true, false);
            } else if (this.jump < 2 || this.falling) {
                currentFrame = this.a_jumping.getCurrentFrame().getFlippedCopy(right ? false : true, false);
            }
        } else {
            if (this.standing) {
                currentFrame = this.a_standingB.getCurrentFrame().getFlippedCopy(right ? false : true, false);
            } else if (this.running){
                currentFrame = this.a_runningB.getCurrentFrame().getFlippedCopy(right ? false : true, false);
            } else if (this.jump < 2 || this.falling) {
                currentFrame = this.a_jumpingB.getCurrentFrame().getFlippedCopy(right ? false : true, false);
            }
        }
        if (this.hit) {
            if(this.frames_uncollidable_left == this.time_uncollidable * Main.fps -1 && this.health >= 0) {
                this.level.hits.play(1, 0.5f);
                if (health == 0) {
                    this.health -= 1;
                }
            }
            currentFrame.setAlpha(0.2f);

        }
        currentFrame.draw(pos_x, this.y, this.width * 2, this.height);

    }


    public void render(Graphics g) { // Here if you flip the animation
        try {
            this.animate();
        } catch (Exception e) {
            System.out.print(e);
        }

        super.render(g);


    }



    // side_collisions






    // Camera // TODO: have to change variables to generic ones.
    public void shift_world(List<? extends Entity> object, boolean shift_player) {
        float size_x = Main.xSize/2.3f;
        float size_y = Main.xSize/6;

        if (x <= size_x && v_x > 0 ||
                x + width >= Main.xSize - size_x && v_x < 0 ||
                (x  >= size_x && x + width <= Main.xSize - size_x)
                ) {
            if (shift_player) {
                x += v_x;
            }
        } else {
            for (int a = 0; a < object.size() ; a++) {
                object.get(a).x -= v_x;
            }
        }
        if (y >= size_y*0.8f && v_y < 0 ||
                y - height  <= Main.ySize - size_y * 2 && v_y > 0 ||
                (y <= size_y && y + height >= Main.ySize - size_y)
                ) {
            if (shift_player) {
                y += v_y;
            }
        } else {
            for (int a = 0; a < object.size() ; a++) {
                object.get(a).y -= v_y;
            }
        }
    }
}
