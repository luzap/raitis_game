import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Titas on 2016-07-03.
 */
public class HUD {
    Image img;
    int distance_between_hearts;
    Level01 level;
    int heart_dim;
    int lives_max;

    public HUD(Level01 level, int distance, ImageHolder imgH_heart, int heart_dim){
        distance_between_hearts = distance;
        this.img = imgH_heart.img;
        this.level = level;
        this.heart_dim = heart_dim;
        this.lives_max = this.level.Raitis_char.health;
    }

    public void render(Graphics g){

        Rectangle rect = new Rectangle(Main.xSize - 1.05f*lives_max * (distance_between_hearts + heart_dim),
                0, (heart_dim+distance_between_hearts)* lives_max,
                heart_dim *1.2f);
        g.setColor(Color.darkGray);
        g.fill(rect);
        int pos_x = Main.xSize;
        for (int i = 0; i<level.Raitis_char.health; i++){
            pos_x -= distance_between_hearts;
            this.img.draw(pos_x - heart_dim, 5, heart_dim, heart_dim);
            pos_x -= heart_dim;
        }

    }



}
