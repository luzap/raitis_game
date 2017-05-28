/**
 * Created by Titas on 2016-06-07.
 */
import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class MenuBar {
    public boolean selected = false;
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException {
        g.drawString("WHATS UP", 59, 49);
        g.drawRect(49,59,100,100);
        g.drawOval(200, 130, 10,23);
    }

}
