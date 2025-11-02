package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage{

    public Sprite(BufferedImage image){
        super(image != null ? image.getWidth() : 1, 
              image != null ? image.getHeight() : 1, 
              image != null ? image.getType() : BufferedImage.TYPE_INT_ARGB);
        
        if (image == null) {
            throw new IllegalArgumentException("Cannot create Sprite from null image");
        }
        
        Graphics2D g = this.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }
}
