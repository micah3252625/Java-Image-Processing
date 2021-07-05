package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    public static File newFile(String filename) {
        return (new File(filename));
    }
    public static BufferedImage ReadImage() throws IOException {
        File file = newFile("chameleon.jpg");
        return (ImageIO.read(file));
    }
    public static boolean WriteImage(BufferedImage img, String id) throws IOException {
        File file = newFile("output_" + id + ".jpg");
        return (ImageIO.write(img, "jpg", file));
    }
    public static int getRed(int pixel) {
        return (pixel & (255 << 16)) >> 16;
    }
    // function to separate the green channel
    public static int getGreen(int pixel) {
        return (pixel & (255 << 8)) >> 8;
    }
    // function to separate the blue channel
    public static int getBlue(int pixel) {
        return pixel & 255;
    }
    // this function is to keep RGB value in the valid range
    public static int truncate(int value) {
        if (value < 0)
            value = 0;
        if (value > 255)
            value = 255;
        return value;
    }
    public static void ImageNegative() {
        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);

                // subtract 255 to RGB value
                int R = 255 - getRed(pixel);
                int G = 255 - getGreen(pixel);
                int B = 255 - getBlue(pixel);

                // set new RGB
                pixel =  (R << 16) | (G << 8) | B;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "negative");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ImageNegative();
    }
}
