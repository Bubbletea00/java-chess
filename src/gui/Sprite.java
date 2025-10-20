package gui;

import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage{

    public Sprite(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public Sprite(BufferedImage image){
        super(image.getWidth(), image.getHeight(), image.getType());
        this.getGraphics().drawImage(image, 0, 0, null);
    }

    public Sprite(Sprite sprite){
        super(sprite.getWidth(), sprite.getHeight(), sprite.getType());
        this.getGraphics().drawImage(sprite, 0, 0, null);
    }
}
