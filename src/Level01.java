/**
 * Created by Titas on 2016-06-07.
 */
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import sun.awt.resources.awt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



// This is where we define the first state Play
public class Level01 extends BasicGameState {

    Boolean in_animation = true;
    Boolean in_boss_fight = false;
    float gravity = 1;

    Boolean win = false;

    Raitis Raitis_char = new Raitis(this, 600, 780, 50, 100);


    List<Platform> PlatformList = new ArrayList<>();
    List<Projectile> projectiles = new ArrayList<>();
    List<Enemy> enemies = new ArrayList<>();
    List<Entity> all = new ArrayList<>();
    List<Animations> animationsList = new ArrayList<>();
    List<BackGroundTexture> backgroundList = new ArrayList<>();
    Image background;

    // Lists holding all of the locations of object:
    List<List<Integer>> platform_coords;
    List<List<Integer>> brick_coords;
    List<List<Integer>> w_Gopniks;
    List<List<Integer>> s_Gopniks;
    List<List<Integer>> backs;

    Sound splat;
    Sound hits;
    Music Hardbass;

    // two image holders for the basic platforms used in the level.
    ImageHolder imgH_basicplat;
    ImageHolder imgH_brickplat;
    ImageHolderMultiple imgH_backwall;
    ImageHolder imgH_heart;
    Animation_holder Blood;

    DialogueSequence dialogueSequence;
    int Dialogue_iter = 0;


    Platform Referencepoint;
    HUD hud;
    Boss boss;

    public Level01() {

    }

    public void init(GameContainer gc, StateBasedGame sbg)

            throws SlickException {
        try
        {
            background = new Image("res/Background_01.png");
        } catch(Exception e) {}
        this.imgH_basicplat = new ImageHolder("res/pavement.jpg");
        this.imgH_brickplat = new ImageHolder("res/brick.jpg");
        this.imgH_backwall = new ImageHolderMultiple("res/backs/",new String [] {"back_wall.png", "old_brick.jpg", "fence.png", "blank.png"});
        this.imgH_heart = new ImageHolder("res/Heart.png");
        Blood = new Animation_holder("res/BloodDie2.png", 12, 1024, 1024, 125);
        platform_coords = new ReadFile("maps/Platform_map.txt").return_data();
        brick_coords = new ReadFile("maps/Wall_map.txt").return_data();
        w_Gopniks = new ReadFile("maps/w_Gopnik_map.txt").return_data();
        s_Gopniks = new ReadFile("maps/s_Gopnik_map.txt").return_data();
        backs = new ReadFile("maps/back_map.txt").return_data();

        this.splat = new Sound("res/audio/splat.ogg");
        this.hits = new Sound("res/audio/hit.ogg");
        this.Hardbass = new Music("res/audio/Hardbass.ogg");


        // Platforms
    }

    public void reset_world() {
        int initial_shift_x = 200;
        PlatformList.clear();
        all.clear();
        Raitis_char = null;
        enemies.clear();
        projectiles.clear();
        animationsList.clear();
        backgroundList.clear();





        for (List<Integer> iter : backs) {
            Image img;
            if(iter.get(0) <3600){
                img = imgH_backwall.img_list.get(1);
            } else if (iter.get(0) < 6450) {
                img = imgH_backwall.img_list.get(0);
            } else if (iter.get(0) < 12000) {
                img = imgH_backwall.img_list.get(1);
            } else if (iter.get(0) < 14500){
                img = imgH_backwall.img_list.get(2);
            } else if (iter.get(0) < 15900) {
                img = imgH_backwall.img_list.get(0);
            }else if (iter.get(0) < 20000) {
                img = imgH_backwall.img_list.get(1);
            } else {
                img = imgH_backwall.img_list.get(3);
            }
            this.add_background_image(new BackGroundTexture(iter.get(0)-initial_shift_x, iter.get(1),50, 50, img));
        }

        try {
            Image img = new Image("Basket.png");
            Image img2 = new Image("raitis.png");
            Image img3 = new Image("pekuss.png");
            this.add_background_image(new BackGroundTexture(13000-initial_shift_x, 500, 200, 400, img));
            this.add_background_image(new BackGroundTexture(25450-initial_shift_x, 200, 1000, 300, img2 ));
            this.add_background_image(new BackGroundTexture(25700-initial_shift_x, 600, 1000, 300, img3 ));
        } catch (Exception e) {
            System.out.print("ASDASDAS");
        }

        this.add_eventbox(new AnimationEventBox(this, 600, 780, 200, 1000, new float[] {200, 0, 4}, "The Gopniks are after you! .... Run!"));
        this.add_eventbox(new BossEventBox(this, 25510-initial_shift_x, 0, 200, 1000));



        for (int i = 0; i < 6; i++) {
            Active_State_Platform platform = new Active_State_Platform(25400 - initial_shift_x, 650+50*i, 50, 50, this.imgH_brickplat);
            this.add_platfrom(platform);
        }

        for (List<Integer> iter : platform_coords) {
            this.add_platfrom(new Platform(iter.get(0)-initial_shift_x, iter.get(1), 50, 50, this.imgH_basicplat));
        }
        for (List<Integer> iter : brick_coords) {
            this.add_platfrom(new Platform(iter.get(0)-initial_shift_x, iter.get(1), 50, 50, this.imgH_brickplat));
        }
        for (List<Integer> iter : w_Gopniks) {
            this.add_enemy(new Gopnik(this, iter.get(0)-initial_shift_x, iter.get(1)-1, 80, 100));
        }
        for (List<Integer> iter : s_Gopniks) {
            this.add_enemy(new ThrowingGopnik(this, iter.get(0)-initial_shift_x, iter.get(1)-1, 60, 90));
        }

        this.Raitis_char = new Raitis(this, 600, 780, 50, 100);

        // Reference point creation

        this.Referencepoint = new Platform(initial_shift_x, 10000, 0, 0, imgH_basicplat);
        this.add_platfrom(this.Referencepoint);






        // Events






        for (int i = 0; i < backgroundList.size(); i++) {
            backgroundList.get(i).init();
        }
        for (int i = 0 ; i < all.size(); i++){
            all.get(i).init();
        }
        Raitis_char.init();

        // HUD
        this.hud = new HUD(this, 30, this.imgH_heart, 50);

        // To take care of the player not having the ball at the beginning of the level.
        Raitis_char.ball_in_hand = false;
        Raitis_char.ball = new Ball(Raitis_char, 12500, 800, 0, 0, 50, 50, 1, false);
        this.add_projectile(Raitis_char.ball);
    }


    public void check_Raitis_y() {
        if (Raitis_char.y > Referencepoint.y) {
            Raitis_char.health = 0;
        }
    }

    public void add_global_move_animation(float x, float y, float time) {
        this.in_animation = true;
        this.animationsList.add(new Animations (Raitis_char, x, y, time));
        for (int i = 0 ; i < all.size(); i++){
            this.animationsList.add(new Animations (all.get(i), x, y, time));
        }

        try {

        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e);
        }
    }

     public void add_platfrom (Platform platform) {
         this.PlatformList.add(platform);
         this.all.add(platform);
     }

    public void add_background_image(BackGroundTexture img){
        this.backgroundList.add(img);
        this.all.add(img);
    }

    public void add_eventbox (EventBox box) {

        this.all.add(box);
    }

    public void add_projectile (Projectile projectile) {
        this.projectiles.add(projectile);
        this.all.add(projectile);
    }

    public void add_enemy (Enemy enemy) {
        this.enemies.add(enemy);
        this.all.add(enemy);
    }

    public void remove_projectile (Projectile projectile) {
        this.projectiles.remove(projectile);
        this.all.remove(projectile);
        projectile = null;
    }

    public void remove_enemies (Enemy enemy) {
        this.enemies.remove(enemy);
        this.all.remove(enemy);
    }


    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
        this.background.draw(0, 0, Main.xSize, Main.ySize);

            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).x+all.get(i).width > 0 && all.get(i).x < Main.xSize){
                all.get(i).render(g);
            }}

        Raitis_char.render(g);
        this.hud.render(g);

        if (this.dialogueSequence != null) {
            this.dialogueSequence.render(Dialogue_iter);
            this.Dialogue_iter ++;
            if (this.dialogueSequence.done) {
                this.dialogueSequence = null;
            }
        }
    }

    public void check_raitis_alive(StateBasedGame sbg){
        check_Raitis_y();
        if (this.Raitis_char.health <= 0) {
            this.all.add(new Blood_box(this, 0, 0, Main.xSize, Main.ySize));
            this.Hardbass.stop();
            sbg.enterState(-3, new FadeOutTransition(Color.red, 1000), new FadeInTransition(Color.black, 500));
        }


    }



    public void start_boss_battle(){
        this.in_boss_fight = true;
        Raitis_char.v_y = 0;
        Raitis_char.v_x =0;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i) instanceof Active_State_Platform) {
                Active_State_Platform plat = (Active_State_Platform) all.get(i);
                plat.active = true;
            }}
            this.boss = new Boss(this, 300, 200, 200, 200);
            for (int i = 0; i < enemies.size(); i++) {
                enemies.remove(i);
                i-=1;
            }
            this.add_enemy(boss);
            boss.init();
        Raitis_char.x += 20;
        Raitis_char.y -= 20;
    }



    public void update(GameContainer gc, StateBasedGame sbg, int delta)
            throws SlickException {
        check_raitis_alive(sbg);

        if (this.win) {
            this.Hardbass.stop();
            sbg.enterState(-4, new FadeOutTransition(Color.black, 500), new FadeInTransition(Color.black, 500));
        }
        if (!this.Hardbass.playing()) {
            this.Hardbass.play(1, 0.2f);
        }
        float shift_x = this.Raitis_char.x - this.Referencepoint.x;
        this.Hardbass.setVolume(0.05f + 0.3f * +shift_x/26000);
        if (!in_animation) {
            Raitis_char.update(delta);

            for (int i = 0; i < all.size(); i++) {
                all.get(i).update(delta);
            }

            if (!this.in_boss_fight) {
                Raitis_char.shift_world(all, true);
            } else {
                Raitis_char.change_pos();
                this.check_boss_fight();

            }
            ball_pick_up_event();
        } else {
            Raitis_char.animate();
            Raitis_char.update_animations(delta);
            if (animationsList.size() == 0) {
                this.in_animation = false;
                this.Raitis_char.v_x = 0;
                this.Raitis_char.v_y = -10;


            }
            for (int i = 0; i < animationsList.size(); i++) {
                this.animationsList.get(i).Animate();
                if (this.animationsList.get(i).done) {
                    this.animationsList.remove(i);
                    i-=1;
                }
            }

        }

        System.out.println(this.Raitis_char.health);



    }


    public void check_boss_fight()  {
        if (Raitis_char.x + Raitis_char.width < 0) {
            Raitis_char.x = 150;
        } else if (Raitis_char.x > Main.xSize) {
            Raitis_char.x = Main.xSize - 150 - Raitis_char.width;
        }
        if (Raitis_char.y > Main.ySize) {
            Raitis_char.y = Main.ySize - Raitis_char.height - 100;
        } else if (Raitis_char.y < 0) {
            Raitis_char.y = 100;
        }

    }

    public void ball_pick_up_event() {
        if (this.Raitis_char.ball_in_hand == true && !(this.Raitis_char.ball_picked_up)) {
            this.Raitis_char.ball_picked_up = true;
            try {
                java.awt.Font font = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 50);
                TrueTypeFont fonts= new TrueTypeFont(font, false);
                this.dialogueSequence = new DialogueSequence(fonts, "use  J  and  K to throw the ball a", 400, Main.ySize/2);
            } catch (Exception e) {}
        }
    }

    public int getID() {
        return 1;
    }
}



