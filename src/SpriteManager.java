import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpriteManager {
    private final Map<String, ImageIcon> sprites = new HashMap<>();

    private int scaling = 64;
    
    public SpriteManager(){
        loadSprites();
    }

    private void loadSprites() {
        //todo replace Sting arrays with enums?
        String[] colors = {"white", "black"};
        String[] pieces = {"bishop", "king", "knight", "pawn", "queen", "rook"};

        for(String color : colors){
            for(String piece : pieces){
                String name = color + "_" + piece;

//                System.out.println("name: " + "/assets/" + name + ".png");
//                System.out.println("getClass().getResource(\"/assets/\" + name + \".png\") = " + getClass().getResource("/assets/" + name + ".png"));

                ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/" + name + ".png")));
                Image scaled = icon.getImage().getScaledInstance(scaling, scaling, Image.SCALE_AREA_AVERAGING);
                sprites.put(name, new ImageIcon(scaled));

            }
        }
    }

    public ImageIcon get(String name){
        return sprites.get(name);
    }

    public int getScaling(){
        return scaling;
    }

    public void setScaling(int scaling){
        this.scaling = scaling;
    }
}

