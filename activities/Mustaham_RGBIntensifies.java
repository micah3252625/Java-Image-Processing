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
    // function to separate the red channel
    public static int getAlpha(int pixel) {
        return (pixel >> 24) & 255;
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
    public static void GreenConversion() {
        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = 0;
        int height = 0;
        if (img != null) {
            width = img.getWidth();
            height = img.getHeight();
        }
        System.out.println("Image Dimension: " + width + " x " + height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int g = getGreen(pixel);
                int a = getAlpha(pixel);

                // set new RGB
                pixel = (a << 24) | (0 << 16) | (g << 8) | 0;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "green");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void BlueConversion() {
        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = 0;
        int height = 0;
        if (img != null) {
            width = img.getWidth();
            height = img.getHeight();
        }
        System.out.println("Image Dimension: " + width + " x " + height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int b = getBlue(pixel);
                int a = getAlpha(pixel);

                // set new RGB
                pixel = (a << 24) | (0 << 16) | (0 << 8) | b;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "blue");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void RedConversion() {
        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = 0;
        int height = 0;
        if (img != null) {
            width = img.getWidth();
            height = img.getHeight();
        }
        System.out.println("Image Dimension: " + width + " x " + height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int r = getRed(pixel);
                int a = getAlpha(pixel);

                // set new RGB
                pixel = (a << 24) | (r << 16) | (0 << 8) | 0;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "red");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        GreenConversion();
        BlueConversion();
        RedConversion();
    }
}
