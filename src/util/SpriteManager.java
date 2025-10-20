package util;

import gui.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private final Map<String, Sprite> sprites = new HashMap<>();

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
                    Sprite sprite = scaledImage(image, scaling);
//                    sprites.put(name, image.getScaledInstance(scaling, scaling, Image.SCALE_AREA_AVERAGING));
                    sprites.put(name, sprite);
                } catch (Exception e){
                    System.out.println("Error loading sprite: " + name + " at path: " + path);
                }



            }
        }
    }

    private Sprite scaledImage(BufferedImage image, int scaling) {
        BufferedImage scaled = new BufferedImage(scaling, scaling, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();

        // Enable high-quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(image, 0, 0, scaling, scaling, null);
        g2d.dispose();

        return new Sprite(scaled);
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

