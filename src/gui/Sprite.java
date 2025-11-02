package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage{

    public Sprite(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public Sprite(BufferedImage image){
        super(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = this.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

//    public Sprite(Sprite sprite){
//        super(sprite.getWidth(), sprite.getHeight(), sprite.getType());
//    }
}
