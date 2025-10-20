package util;

import gui.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private final Map<String, Image> sprites = new HashMap<>();

    private int scaling = 64;
    
    public SpriteManager(){
        loadSprites();
    }

    public SpriteManager(int scaling){
        this.scaling = scaling;
        loadSprites();
    }

    private void loadSprites() {
        String[] colors = {"white", "black"};
        String[] pieces = {"bishop", "king", "knight", "pawn", "queen", "rook"};

        for(String color : colors){
            for(String piece : pieces){
                String name = color + "_" + piece;
                String path = "res/assets/" + name + ".png";


                try {
                    BufferedImage image =  ImageIO.read(new FileInputStream(path));
                    sprites.put(name, image.getScaledInstance(scaling, scaling, Image.SCALE_AREA_AVERAGING));

                } catch (Exception e){
                    System.out.println("Error loading sprite: " + name + " at path: " + path);
                }



            }
        }
    }

    public Image get(String name){
        return sprites.get(name);
    }

    public int getScaling(){
        return scaling;
    }

    public void setScaling(int scaling){
        this.scaling = scaling;
    }
}

