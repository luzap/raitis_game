import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Titas on 2016-07-03.
 */
public class Boss extends Enemy {
    float throw_timer;
    int frames;
    int current_frame;
    List<Image> images = new ArrayList<>();
    float [][] Locations;
    int current_location;
    int destination;
    int wait_timer;
    int attack_timer;
    int action;
    float action_timers [];
    int health_before;
    int hit_timer;
    boolean ready_to_pause;
    boolean ready_to_move;
    boolean ready_to_attack;
    boolean moved;

    Image current_image;
    HealthBar healthBar;

    float center_x;
    float center_y;

    public Boss (Level01 level, float x, float y, int width, int height) {
        super(level, x, y, width, height);
    }



    public void check_hit(){
        if (this.hit_timer != 0) {
            this.hit_timer -= 1;
            this.health = this.health_before;
            System.out.print("HEALTH");
            System.out.print(health);
            this.hit = false;
        }
        if(this.rect.intersects(this.level.Raitis_char.rect)) {
            this.hit = true;
            this.hit_timer = 60;
        }
        this.health_before = this.health;
    }




    public void init() {
        super.init();
        this.Locations = new float[][] {{100, 150}, {950, 150},{100, 700},{950, 700}, {550, 400}};
        this.current_location = 4;
        this.x = Locations[current_location][0];
        this.y = Locations[current_location][1];
        this.action_timers = new float[] {0.1f, 0.5f, 0.65f};
        this.ready_to_move = false;
        this.ready_to_pause = true;
        this.ready_to_attack = false;
        try {
            Image img1 = new Image("res/boss_casual.png");
            Image img2 = new Image("res/boss_attack.png");
            this.images.add(img1);
            this.images.add(img2);
        } catch (Exception e) {
        }
        this.health = this.health_before = 100;
        this.healthBar = new HealthBar(this);
    }

    public void render(Graphics g) {
        this.current_image.draw(this.x, this.y, this.width, this.height);
        healthBar.render(g);
    }

    public void KYS() {
        if (!this.is_player) {
            if (this.health < 1) {
                this.level.all.add(new Blood_box(this.level, this.x+this.width/2, this.y+this.height/2,  this.height, this.height));
                this.level.remove_enemies(this);
                this.level.splat.play(1, 50);
                this.level.win = true;
            }
        }
    }


    public void update(int delta) {
        super.update(delta);
        this.check_state();
        this.check_hit();
        boolean is_pause = is_pause();
        boolean is_moving = is_moving();
        boolean is_attacking = is_attacking();

        if (is_moving) {
            this.check_arrive();
            move();
            System.out.println("UPDATE IS CALLED");
        } else if (is_pause){
            System.out.println("Pause IS CALLED");
        } else if (is_attacking) {
            System.out.println("ATTACKING IS CALLED");
            this.attack();
        }
    }



    public void move(){
        this.x += v_x;
        this.y += v_y;
    }



    public void check_state(){
        System.out.println(this.ready_to_attack);
        System.out.println(this.ready_to_pause);
        System.out.println(this.ready_to_move);
        if (this.ready_to_attack){
            this.ready_to_attack = false;
            this.setAttack_timer(3);
            this.current_image = this.images.get(1);

        } else if (this.ready_to_pause) {
            this.ready_to_pause = false;
            this.pause(1);
            this.current_image = this.images.get(0);

        } else if (this.ready_to_move) {
            this.choose_destination();
            this.ready_to_move = false;
            this.current_image = this.images.get(0);
        }
        }




    void check_arrive(){
        double x_check = this.x - this.Locations[destination][0];
        double y_check = this.y - this.Locations[destination][1];
        if (Math.sqrt(Math.pow(x_check,2) + Math.pow(y_check,2)) < 10) {
            this.current_location = this.destination;
            this.x = Locations[current_location][0];
            this.y = Locations[current_location][1];
            this.v_x = 0;
            this.v_y = 0;
            this.moved = true;
            this.ready_to_pause = true;
            this.ready_to_attack = false;

        }
    }

    void setAttack_timer(int secs){
        this.attack_timer = secs * Main.fps;
    }

    void pause(int secs){
        this.wait_timer = secs * Main.fps;
    }

    boolean is_moving(){
        boolean is_moving;
        if (!this.moved) {
            is_moving = true;
        } else {

            is_moving = false;
        }

        return is_moving;

    }

    boolean is_pause(){
        boolean a;
        if (this.wait_timer > 0) {
            this.wait_timer -= 1;
            a = true;
        } else if (this.wait_timer == 0) {
            a = false;
            System.out.println(this.wait_timer);
            this.ready_to_attack = true;
            this.wait_timer -= 1;
        } else {
            a = false;
        }
        return a;
    }



    boolean is_attacking(){
        boolean attacking;
        if (this.attack_timer > 0) {
            this.attack_timer -= 1;
            attacking = true;
        } else if (this.attack_timer == 0) {
            attacking = false;
            this.ready_to_move = true;
            this.moved = false;
            this.attack_timer -= 1;
        } else {
            attacking = false;
        }
        return attacking;
    }


    void attack() {
        System.out.println(this.attack_timer);
        if (this.current_location == 0 || this.current_location == 1) {
            if (this.attack_timer % (int)(this.action_timers[0]*Main.fps) == 0 ) {
                attack12();
            }

        }else if (this.current_location == 2 || this.current_location == 3) {
            if (this.attack_timer % (int)(this.action_timers[1]*Main.fps) == 0 ) {
                attack34();
            }
        } else {
            if (this.attack_timer % (int)(this.action_timers[2]*Main.fps) == 0 ) {
                attack5();
            }
        }
    }

    void attack12() {
        generate_velocities_p(this.level.Raitis_char, 15);
    }

    void attack34() {
        float starting_angle;
        float v_x;
        float v_y;
        float speed = 10;
        if(this.current_location == 2){
            starting_angle = 2.98f*3.1415f/2;
            for (int i = 0; i< 11; i++) {
                v_x = speed * (float) Math.cos(starting_angle) ;
                v_y = speed * (float) Math.sin(starting_angle) ;
                this.add_projectile(v_x, v_y);
                starting_angle += 3.1415/18;
            }
        } else {
            starting_angle = 3.02f*3.1415f/2;
            for (int i = 0; i< 11; i++) {
                v_x = speed * (float) Math.cos(starting_angle) ;
                v_y = speed * (float) Math.sin(starting_angle) ;
                this.add_projectile(v_x, v_y);
                starting_angle -= 3.1415/18;
        }
    }
    }

    void attack5() {
        generate_circular_p();
    }



    public void add_projectile(float v_x, float v_y){
        this.level.add_projectile(new Projectile (this, this.x+width/2, this.y+height/2+20, v_x, v_y, 30, 30, 1, true));
    }

    public void generate_circular_p() {
        float starting_angle = (float) (Math.random()*2*3.1415);
        float angle;
        double speed = 10;
        for (int i =0; i < 8; i++) {
            angle = starting_angle + i * 3.1415f/4;
            float v_y = (float) (speed * Math.sin(angle));
            float v_x = (float) (speed * Math.cos(angle));
            add_projectile(v_x, v_y);
        }
    }

    public void generate_velocities_p(Enemy target, float speed){
        float change_x = target.x-this.x;
        float change_y = target.y-this.y;
        float change_t = Math.abs(change_x) + Math.abs(change_y);
        float v_x = change_x/change_t  *  speed;
        float v_y = change_y/change_t  * speed;
        add_projectile(v_x, v_y);
    }

    public void generate_velocities(float [] location, float speed) {
        float change_x = location[0] - this.x;
        float change_y = location[1] - this.y;
        float change_t = Math.abs(change_x) + Math.abs(change_y);
        this.v_x = change_x / change_t * speed;
        this.v_y = change_y / change_t * speed;
    }

    public void choose_destination(){
        Random ran = new Random();
        int x = 0 + (int)(Math.random() * 5);
        this.destination = x;
        this.generate_velocities(Locations[x], 5);
        System.out.print("Destination");
        System.out.println(this.destination);
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
            this.generate_velocities_p(raitis, 15);
        }
        this.throw_timer -= 1;

    }

}
