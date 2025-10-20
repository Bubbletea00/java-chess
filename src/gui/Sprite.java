package gui;

import java.awt.image.BufferedImage;

public class Sprite extends BufferedImage{

    public Sprite(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public Sprite(BufferedImage image){
        super(image.getWidth(), image.getHeight(), image.getType());
    }

}
