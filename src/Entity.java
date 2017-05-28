import com.sun.org.apache.xpath.internal.operations.Bool;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.Image;
import org.newdawn.slick.Animation;


import java.util.*;
import java.util.concurrent.Exchanger;

/**
 * Created by Titas on 2016-06-10.
 */
public class Entity {
    // the basic factors
    float x;
    float y;
    int width;
    int height;
    float v_x;
    float v_y;
    // boolean to determine whether an object collides
    boolean collides = true;

    Entity collidedRight;
    Entity collidedLeft;
    Entity collidedUp;
    Entity collidedDown;
    List<Entity> collisions= new ArrayList<>();
    Animations Animation;
    Rectangle rect;



    public void render(Graphics g){
    }

    public void update(int delta) {
        if (this.rect != null) {
            this.rect.setBounds(this.x, this.y, this.width, this.height);
        }

    }

    public void init() {
        this.rect = new Rectangle(this.x, this.y, this.width, this.height);
    }

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        v_x = 0;
        v_y =0;
        collidedUp = null;
        collidedDown = null;
        collidedLeft = null;
        collidedLeft = null;

    }




        // Todo find a a way to implement collision of multiple blocks (seems to work, but confirm)
    public Boolean isCollide(Entity object) {
        float o1x = this.rect.getX();
        float o2x = object.rect.getX();
        float o1y = this.y;
        float o2y = object.y;
        float o1xa = o1x + v_x;
        float o2xa = o2x + object.v_x;
        float o1ya = o1y + v_y;
        float o2ya = o2y + object.v_y;
        int o1h = this.height;
        int o2h = object.height;
        int o1w = this.width;
        int o2w = object.width;
        boolean value = false;

        if (object == this) {
            value = false;
        }
        else if(o1xa <= o2xa + o2w &&
                o1xa + o1w >= o2xa &&
                o2ya <= o1ya + o1h  &&
                o2ya+ o2h >= o1ya) {
            collisions.add(object);
            value = true;
        }


        return value;

        }






    public void sideCollide(Entity object){
        float o1x = x;
        float o2x = object.x;
        float o1y = y;
        float o2y = object.y;
        int o2w = object.width;
        int o2h = object.height;
        float[] o1NE = {o1x + width , o1y};
        float[] o1NW = {o1x, o1y};
        float[] o1SW = {o1x, o1y + height};
        float[] o1SE = {o1x + width, o1y + height};
        float[] o2NE = {o2x + o2w, o2y};
        float[] o2NW = {o2x, o2y};
        float[] o2SW = {o2x, o2y + o2h};
        float[] o2SE = {o2x + o2w, o2y + o2h};
        if (o1NW[0] > o2SE[0]) {
            System.out.println("YEYSEYYSEYESYEYSYEYSEYSEYYSEYSESEY");
        }
        if (o1SW[1] <= o2NW[1] && o1NE[0] > o2NW[0] && o1NW[0] < o2SE[0]){
            collidedUp = object;
            System.out.println("up");
        } else if (o1NE[1] >= o2SE[1] && o1NE[0] > o2NW[0] && o1NW[0] < o2SE[0]) {
            collidedDown = object;
            System.out.println("DOWN");
        } else if (o1SE[1] > o2NW[1] && o1NE[1] < o2SE[1]) {
            if (x >= o2x) {
                collidedLeft = object;
                System.out.println("RIGHT");
            } else if (x <= o2x+o2w) {
                collidedRight = object;
                System.out.println("LEFT");
            }
        }
    }

    public void make_non_collidable() {
        this.collides = false;
    }
}













