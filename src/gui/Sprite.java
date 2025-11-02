package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage{

    public Sprite(BufferedImage image){
        super(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = this.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
}
