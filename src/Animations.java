/**
 * Created by Titas on 2016-06-22.
 */
import java.lang.Math;

public class Animations {

    float change_x;
    float change_y;
    double per_frame;
    int frames;
    double dx;
    double dy;
    int current_frame = 0;
    boolean done = false;
    Entity object;

    public Animations(Entity object, float x, float y, double time) {
        this.object = object;
        change_x = x;
        change_y = y;
        frames = (int) time * Main.fps;
        per_frame  = Main.fps * time;
        dx = change_x/per_frame;
        dy = change_y/per_frame;
    }

    void Animate() {
        if (this.current_frame < frames){
            object.x += dx;
            object.y += dy;
            object.rect.setLocation(object.x, object.y);
            current_frame += 1;
        }
        if (current_frame == frames) {
            this.done = true;
        }
    }
}
