import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

/**
 * Created by Titas on 2016-06-25.
 */
public class Enemy extends Entity {

    String Name;


    int health = 4;
    boolean collides = true;
    boolean right = true;
    boolean left = false;
    boolean melee = true;
    boolean is_player = false;
    float a_x = 0;
    float a_y = 0;
    float gravity = 0;
    int damage = 1;
    float time_uncollidable = 2;
    int frames_uncollidable_left = 0;
    boolean hit = false;
    boolean active = false;
    Entity throw_object;
    Level01 level;
    float [] shifts = {0, 0};


    public Enemy(Level01 level, float x, float y, int width, int height) {
        super(x, y, width, height);
        this.level = level;
    }

    public void check_cols() {
        for (int i = 0; i < level.all.size(); i++) {
            this.isCollide(level.all.get(i));
        }
        this.isCollide(this.level.Raitis_char);
    }

    public void do_with_cols() {
        for (int i = 0; i < this.collisions.size(); i++) {
            if (collisions.get(i).collides) {
                this.sideCollide(this.collisions.get(i));

                //Enemy oritented things


                //Player oriented things
                player_hit_enemy(this.collisions.get(i));

            }
        }
    }

    public void check_remove() {
        if (this.x < this.level.Raitis_char.x) {
            float change_x = this.x - this.level.Raitis_char.x;
             if (Math.sqrt(change_x * change_x) > 2 * Main.xSize) {
                 this.KYS();
             }
        } else if (this.y > this.level.Referencepoint.y) {
            this.KYS();
        }
    }




    public void enemy_hit_projectile(Entity ent) {
        if (!this.is_player) {
            if(ent instanceof Ball) {
                Projectile proj = (Projectile) ent;
                this.health -= proj.damage;
                this.throw_object = proj;
            }
        }
    }



    public void player_hit_projectile(Entity ent) {
        if (this.is_player) {
            if (ent instanceof Projectile) {
                Projectile proj = (Projectile) ent;
                if (proj.hurts_player) {
                    this.health -=  proj.damage;
                    this.throw_object = proj;
                    this.set_inv_timer();
                }

            }
        }
    }

    public void player_hit_enemy(Entity ent) {
        if (this.is_player) {
            if (ent instanceof Enemy) {
                Enemy object = (Enemy) ent;
                if (object.melee) {
                    if (!this.hit) {
                        this.health -= object.damage;
                        this.throw_object = object;
                        this.set_inv_timer();
                    }
                }
            }
        }
    }


    public void KYS() {
        if (!this.is_player) {
            if (this.health < 1) {
                this.level.all.add(new Blood_box(this.level, this.x+this.width/2, this.y+this.height/2,  this.height, this.height));
                this.level.remove_enemies(this);
                this.level.splat.play(1, 50);
            }
        }
    }

    public void get_thrown(Entity enemy) {
        if (this.hit && enemy != null || !this.is_player && enemy != null) {
            float acc_y = 7;
            float acc_x = 20;
            this.v_y = 0;
            if (enemy.v_x > 0) {
                this.a_x = acc_x;
            } else if (enemy.v_x < 0) {
                this.a_x = -acc_x;
            } else if (this.x < enemy.x) {
                this.a_x = -acc_x;
            } else if ( this.x > enemy.x) {
                this.a_x = acc_x;
            }
            this.a_y -=acc_y;
            this.throw_object = null;
        }
    }


    public void accelerate(){
        this.v_x += this.a_x;
        this.v_y += this.a_y;
    }

    public void friction(float acc) {
        if (this.a_x > 0) {
            this.a_x -= acc;
        } else if(this.a_x < 0) {
            this.a_x += acc;
        }
        if (this.a_y > 0) {
            this.a_y -= acc;
        } else if (this.a_y < 0) {
            this.a_y += acc;
        }
        if (Math.abs(this.a_x) < acc) {
            this.a_x = 0;
        }
        if (Math.abs(this.a_y) < acc) {
            this.a_y = 0;
        }



    }

    public void set_inv_timer() {
        if (this.frames_uncollidable_left == 0) {
            this.frames_uncollidable_left = (int) this.time_uncollidable * Main.fps;
            this.hit = true;
        }
    }

    public void set_inv() {
        // method to set someone invulnerable when needed.
        // Likely after a damage change.
        if (this.frames_uncollidable_left > 0) {
            this.frames_uncollidable_left -= 1;
        } else {
            this.hit = false;
        }
    }

    public void check_active() {
        float change_x = this.x - this.level.Raitis_char.x;
        if (Math.sqrt(change_x * change_x) < Main.xSize) {
            this.active = true;

        }
    }

    public void update(int delta) {
        this.check_remove();
        if (this.active || this.is_player) {
            this.KYS();
            super.update(delta);
            check_cols();
            do_with_cols();
            set_inv();
            this.collisions.clear();

        } else {
            this.check_active();
        }
    }

    public void render(Graphics g) {
        if (this.active || is_player) {
            super.render(g);
        }
    }
}

class EnemyWalking extends Enemy {
    SpriteSheet ss_standing;
    Animation a_standing;
    Boolean standing = false;
    Boolean falling = false;

    SpriteSheet ss_running;
    Animation a_running;
    Boolean running = false;


    SpriteSheet ss_jumping;
    Animation a_jumping;

    boolean jumpkeydown;
    int jump = 2;

    public EnemyWalking(Level01 level, float x, float y, int width, int height) {
        super(level, x, y, width, height);
        this.level = level;
        this.gravity = this.level.gravity;


    }


    public void do_with_left_collide() {
        if (collidedLeft != null) {
            Entity pl;
            pl = collidedLeft;
            this.shifts[0] = pl.x+ pl.width - this.x;
            x = pl.x + pl.width;
            v_x = 0;
            collidedLeft = null;
        }
    }

    public void do_with_right_collide() {
        if (collidedRight != null) {
            Entity pl;
            pl = collidedRight;
            this.shifts[0] = pl.x - width - this.x;
            x = pl.x - width;
            v_x = 0;
            collidedRight = null;
        }
    }

    public void do_with_up_collide() {


        if (collidedUp != null) {
            if (this.v_x == 0) {
                this.standing = true;
                this.running = false;
            } else {
                this.standing = false;
                this.running = true;
            }
            Entity pl;
            jump = 2;
            pl = collidedUp;
            this.shifts[1] = pl.y - this.height - this.y;
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

    public void do_with_down_collide() {
        if (collidedDown != null) {
            Entity pl;
            pl = collidedDown;
            this.shifts[1] = pl.y + pl.height - this.y;
            y = pl.y + pl.height;
            this.v_y = 1;
            collidedDown = null;
        }
    }


    public void do_with_side_cols() {
        this.do_with_down_collide();
        this.do_with_left_collide();
        this.do_with_right_collide();
        this.do_with_up_collide();
    }

    public void move() {
        if (this.active || this.is_player) {
            this.x += this.v_x;
            this.y += this.v_y;
        }
    }

    public void experience_gravity() {
        this.v_y += this.level.gravity;
    }

    public void update_animations(int delta) {
        if (a_standing != null) {
            a_standing.update(delta);
        }
        if (a_running != null) {
            a_running.update(delta);
        }
        if (a_jumping != null) {
            a_jumping.update(delta);
        }

    }

    public void update(int delta) {
        super.update(delta);

        if (this.active || this.is_player) {

            this.experience_gravity();

            this.do_with_side_cols();

            this.move();
        }
        this.get_thrown(this.throw_object);
        this.accelerate();
        this.friction(1);
        this.update_animations(delta);

    }

}





