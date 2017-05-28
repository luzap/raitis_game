import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Titas on 2016-06-25.
 */
public class Projectile extends Entity {
    SpriteSheet SS;
    org.newdawn.slick.Animation anim;

    float a_x;
    float a_y;
    int damage;
    double distance_moved = 0;
    double max_distance = 1500;
    Enemy creator;
    Level01 level;
    boolean hurts_player;
    float gravity;


    public Projectile(Enemy creator, float x, float y, float v_x, float v_y, int width, int height, int damage, boolean hurts_player){
        super(x, y, width, height);
        this.v_x = v_x;
        this.v_y = v_y;
        this.hurts_player = hurts_player;
        this.damage = damage;
        this.creator = creator;
        this.level = this.creator.level;
        this.collides = false;
        this.init();
        this.gravity = this.level.gravity;

    }

    public void experience_gravity() {
        this.v_y += this.gravity;
    }

    public void distance_check() {
        double change = Math.sqrt(this.v_x * this.v_x + this.v_y * this.v_y);
        distance_moved += change;
        if (distance_moved > max_distance) {
            this.level.remove_projectile(this);
        }
    }

    public void damage(Enemy object) {
        // defines how the projectile hurts the enemy
        // can be overwritten for other effects
        object.health -= damage;
    }


    public void check_cols() {
        // function to check all of the objects that collide with the projectile
        for (int i = 0; i < this.level.all.size(); i++) {
            this.isCollide(level.all.get(i));
        }
        this.isCollide(this.level.Raitis_char);

    }




    public void do_with_cols() {
        //function that defines what is done with the collision list;
            for (int i = 0; i < this.collisions.size(); i++) {
                if (!this.hurts_player) {
                    if (this.collisions.get(i) instanceof Enemy) {
                        this.damage((Enemy) this.collisions.get(i));
                        this.level.remove_projectile(this);

                    }
                }else {
                    if (this.collisions.get(i) instanceof Raitis) {
                        Enemy e = (Enemy) this.collisions.get(i);
                        if (e.collides && !e.hit) {
                            e.player_hit_projectile(this);
                            this.level.remove_projectile(this);
                        }
                    }
                }

            }
    }

    public void set_up_animation() {
        if (SS == null) {
            try {
                this.SS = new SpriteSheet("res/SS_ball.png", 256, 256);
                this.anim = new Animation(this.SS, new int[]{0, 0, 1, 0, 2, 0, 3, 0}, new int[]{100, 100, 100, 100});
            } catch (Exception e) {

            }

        }
    }

    public void accelerate() {
        this.v_x += this.a_x;
        this.v_y += this.a_y;
    }

    public void move() {
        this.x += this.v_x;
        this.y += this.v_y;
    }

    public void update(int delta) {
        super.update(delta);

        this.set_up_animation();
        this.check_cols();
        this.do_with_cols();
        this.distance_check();
        this.accelerate();
        this.move();


        this.anim.update(delta);

        this.collisions.clear();
    }

    public void render(Graphics g){

        if (anim != null) {
            anim.draw(this.x, this.y, this.width, this.height);
            g.drawRect(this.x, this.y, this.width, this.height);
        }
    }

}


// Colliding class


class Projectile_Collides extends Projectile {
    float bounce_fraction = 1;
    boolean hit_creator = false;
    public Projectile_Collides(Enemy creator, float x, float y, float v_x, float v_y, int width, int height, int damage, boolean hurts_player){
        super(creator, x, y, v_x, v_y, width, height, damage, hurts_player);
        this.collides = true;
    }

    public void flip() {
        // function which allows a bouncing projectile to flip velocities.
        if (this.collidedLeft != null || this.collidedRight != null) {
            this.v_x = -this.bounce_fraction * this.v_x;
            this.v_y = this.bounce_fraction * this.v_y;
        } else if (this.collidedDown != null || this.collidedUp != null) {
            this.v_x = this.bounce_fraction * this.v_x;
            this.v_y = -this.bounce_fraction * this.v_y;
        }


    }

    public void do_with_cols() {
        // we want the projectile to only flip once per frame
        // so we make a time_to_flip variable
        boolean time_to_flip = false;
        if (this.hurts_player) {
            for (int i = 0; i < this.collisions.size(); i++) {
                if (this.collisions.get(i).collides) {
                    this.sideCollide(this.collisions.get(i));
                    if (!time_to_flip) {
                        time_to_flip = true;
                    }
                    if (this.collisions.get(i) instanceof Raitis) {
                        Raitis raitis = (Raitis) this.collisions.get(i);
                        if (raitis.collides) {
                            this.damage(raitis);
                            this.level.remove_projectile(this);
                        } else {
                            time_to_flip = false;
                        }

                    }


                }

            }
        } else {
            for (int i = 0; i < this.collisions.size(); i++) {
                if (this.collisions.get(i) == this.creator) {
                    this.hit_creator =true;
                }
                if (this.collisions.get(i).collides && this.collisions.get(i) != this.creator) {
                    time_to_flip = true;
                    this.sideCollide(this.collisions.get(i));
                    if (this.collisions.get(i) instanceof Enemy) {
                        Enemy e = (Enemy)this.collisions.get(i);
                        this.damage(e);
                    }
                }
            }
        }
        if (time_to_flip) {
            this.flip();
        }
    }

    public void update(int delta) {
        super.update(delta);
    }

}

class Ball extends Projectile_Collides {
    int max_hits;
    int hits;
    public Ball(Enemy creator, float x, float y, float v_x, float v_y, int width, int height, int damage,boolean hurts_player){
        super(creator, x, y, v_x, v_y, width, height, damage, hurts_player);
        this.damage = 0;
        this.max_distance = 1600;
        this.hurts_player = false;
        this.damage = 4;
        this.max_hits = 3;  // max number of damage ticks that can be done.
        this.hits = 0;
    }

    public void update(int delta) {
        super.update(delta);
    }

    public void do_with_cols () {
        boolean time_to_flip = false;
        for (int i = 0; i < this.collisions.size(); i++) {
            if (this.collisions.get(i) == this.creator) {
                this.hit_creator = true;
            }
            if (this.collisions.get(i).collides && this.collisions.get(i) != this.creator) {
                time_to_flip = true;
                this.sideCollide(this.collisions.get(i));
                if (this.collisions.get(i) instanceof Enemy && hits <= max_hits) {
                    Enemy e = (Enemy) this.collisions.get(i);
                    //this.level.all.add(new Animation_box(this.level, e.x + e.width/2, e.y + e.height/2, e.width, e.height ));
                    e.enemy_hit_projectile(this);
                    hits+=1;
                }
                if (this.collisions.get(i) instanceof Projectile && ((Projectile) this.collisions.get(i)).hurts_player) {
                    Projectile proj = (Projectile) this.collisions.get(i);
                    this.level.remove_projectile(proj);
                    proj.distance_moved = 10000;
                    time_to_flip = false;
                }
            }
        }

        if (time_to_flip) {
            this.flip();
        }
        if (this.hit_creator) {
            this.level.remove_projectile(this);
        }
    }




}



class Bottle_projectile extends Projectile {
    public Bottle_projectile(Enemy creator, float x, float y, float v_x, float v_y, int width, int height, int damage,boolean hurts_player){
        super(creator, x, y, v_x, v_y, width, height, damage, hurts_player);
        this.gravity = 0.2f;
    }

    public void set_up_animation() {
        if (SS == null) {
            try {
                this.SS = new SpriteSheet("res/beer.png", 256, 576);
                this.anim = new Animation(this.SS, new int[]{0, 0, 1, 0, 2, 0, 3, 0}, new int[]{100, 100, 100, 100});
            } catch (Exception e) {

            }

        }
    }

    public void update(int delta) {
        this.experience_gravity();
        super.update(delta);
    }


}
