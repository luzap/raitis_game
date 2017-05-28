import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;


public class MainButton{;
    private int[] size = new int[] {(int) (Main.xSize * 0.3), (int) (Main.ySize * 0.1)};
    private Rectangle rect;
    TrueTypeFont font;

    public MainButton(Graphics g, TrueTypeFont font, String button_text, float xpos, float ypos) {
        // Shape rendering and placing
        this.rect = new Rectangle(xpos, ypos, size[0], size[1]);
        this.font = font;

        // Initializing font
        this.font.drawString(this.rect.getCenter()[0] - font.getWidth(button_text)/2,
                this.rect.getCenter()[1] - font.getHeight(button_text)/2, button_text);
        g.setColor(Color.white);
    }

    public void render(Graphics g) {g.draw(rect);}
}