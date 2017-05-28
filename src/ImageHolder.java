import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Titas on 2016-07-01.
 */
public class ImageHolder {
    Image img;
    public ImageHolder(String src) {
        try {
            img = new Image(src);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}

class ImageHolderMultiple {
    List<Image> img_list = new ArrayList<>();
    public ImageHolderMultiple(String folder, String [] filenames) {
        for (String s: filenames) {
            String source = folder;
            source += s;
            try {
                Image img = new Image(source);
                img_list.add(img);
            } catch (Exception e) {
                System.out.println(e);
            }
        }


    }
}