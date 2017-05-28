import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Titas on 2016-06-25.
 */
public class Platform extends Entity {
    Image img;
    ImageHolder imgH;



    public Platform(float x, float y, int size_x, int size_y, ImageHolder imgH){
        super(x, y, size_x, size_y);
        this.imgH = imgH;
    }
    public void init() {
        super.init();
        this.img = this.imgH.img;
    }
    public void update(int delta) {
        super.update(delta);
    }
    public void render(Graphics g) {
        g.drawRect(this.x, this.y, this.width, this.height);
        this.img.draw(this.x, this.y, this.width, this.height);
    }

}


class Active_State_Platform extends Platform {
    boolean active;
    public Active_State_Platform(float x, float y, int size_x, int size_y, ImageHolder imgH){
        super(x, y, size_x, size_y, imgH);
        this.active = false;
    }
    public void init() {
        super.init();
        this.img = this.imgH.img;
    }

    public void update(int delta) {
        if (this.active) {
            super.update(delta);
        }
    }

    public void render(Graphics g) {
        if (this.active) {
            g.drawRect(this.x, this.y, this.width, this.height);
            this.img.draw(this.x, this.y, this.width, this.height);
        }
    }
}