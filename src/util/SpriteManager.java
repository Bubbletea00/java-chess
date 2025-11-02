package util;

import gui.Sprite;
import model.Pieces;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private static final Map<Pieces, Sprite> sprites = new HashMap<>();
    private final Map<Pieces, ImageIcon> pieceIcons = new HashMap<>();

    private int scaling = 64;
    
    public SpriteManager(){
        loadSprites();
        loadIcons();
    }

    public SpriteManager(int scaling){
        this.scaling = scaling;
        loadSprites();
        loadIcons();
    }

    private void loadSprites() {

        for(Pieces piece : Pieces.values()){
            String name = piece.name().toLowerCase();
            if(name.equals("empty")) continue;
            String path = "res/assets/" + name + ".png";

            try {
                BufferedImage image =  ImageIO.read(new FileInputStream(path));
                Sprite sprite = scaledImage(image, scaling);
//                    sprites.put(name, image.getScaledInstance(scaling, scaling, Image.SCALE_AREA_AVERAGING));
//                System.out.println("loaded sprite for " + piece);
                sprites.put(piece, sprite);
            } catch (Exception e){
                System.err.println("Error loading sprite: " + name + " at path: " + path);
            }
        }

    }

    private void loadIcons(){
        for(Pieces piece : Pieces.values()){
            String name = piece.name().toLowerCase();
            if(name.equals("empty")) continue;
            String path = "res/assets/" + name + ".png";

            try{
                BufferedImage image = ImageIO.read(new File(path));
                ImageIcon icon = new ImageIcon(image.getScaledInstance(scaling, scaling, Image.SCALE_AREA_AVERAGING));
//                System.out.println("loaded icon for " + piece);
                pieceIcons.put(piece, icon);
            } catch (IOException e) {
                System.err.println("Error loading sprite: " + name + " at path: " + path);
                throw new RuntimeException(e);

            }
        }
    }

    private Sprite scaledImage(BufferedImage image, int scaling) {
        BufferedImage scaled = new BufferedImage(scaling, scaling, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(image, 0, 0, scaling, scaling, null);
        g2d.dispose();

        return new Sprite(scaled);
    }


    public Sprite getSprite(Pieces piece){
        return sprites.get(piece);
    }

    public Sprite get(Pieces piece){
        return getSprite(piece);
    }

    public ImageIcon getIcon(Pieces piece){
        return pieceIcons.get(piece);
    }
}

