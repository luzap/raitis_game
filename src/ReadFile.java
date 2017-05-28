import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ReadFile {
    private List<List<Integer>> coords = new ArrayList<>();
    private String filename;

    public ReadFile(String file) {
        filename = file;
    }

    public List<List<Integer>> return_data() {
        try {
            Scanner input = new Scanner(new File(filename));
            while (input.hasNext()) {
                String[] string_coords = input.next().split(",",2);
                List<Integer> int_coords = new ArrayList<>();
                for (String item : string_coords) {
                    int_coords.add(Integer.parseInt(item));
                }
                coords.add(int_coords);
            }
            input.close();
            return coords;
        } catch (FileNotFoundException no_file) {
            System.out.println(no_file);
            System.out.println("File not found;");
            coords.add(null);
            return coords;
        }
    }

}

