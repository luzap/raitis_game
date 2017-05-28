import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.logging.Level;

/**
 * Created by Titas on 2016-07-02.
 */
public class BackGroundTexture extends Entity {
    Image img;
    ImageHolder imgH;
    public BackGroundTexture(float x, float y, int width, int height, Image img){
        super(x, y, width, height);
        this.collides = false;
        this.img = img;
    }

    public void init(){
        super.init();
    }

    public void render(Graphics g) {
        this.img.draw(this.x, this.y, this.width, this.height);
    }
}
