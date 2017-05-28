import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

/**
 * Created by Titas on 2016-07-04.
 */
public class HealthBar {
    int health_total;
    int health;
    Enemy enemy;
    java.awt.Font font;
    TrueTypeFont fonts;

    public HealthBar(Enemy enemy){
        this.health_total = this.health = enemy.health;
        this.enemy = enemy;
        this.font = new java.awt.Font("Helvetica", java.awt.Font.BOLD, 50);
        this.fonts = new TrueTypeFont(font, false);
    }



    public void render(Graphics g) {
        this.health = enemy.health;
        float width = Main.xSize/3;
        float height = 100;
        float x = Main.xSize/2 - width/2;
        float y = 0;
        g.setColor(Color.red);
        g.fillRect(x, y, width, height);
        g.setColor(Color.green);
        g.fillRect(x, y, width*health/health_total, height);
        g.setColor(Color.black);
        this.fonts.drawString(x+ this.fonts.getWidth("Lithuanian")/2 -40, y+this.fonts.getHeight("Lithuanian")/2,"Lithuanian");
    }
}
