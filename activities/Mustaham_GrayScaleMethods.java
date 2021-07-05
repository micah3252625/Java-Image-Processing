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
    public static void AverageMethod() {
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
                int R = getRed(pixel);
                int G = getGreen(pixel);
                int B = getBlue(pixel);

                int average = (R + G + B) / 3;

                // set new RGB
                pixel =  (average << 16) | (average << 8) | average;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "average");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void LuminosityMethod() {
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

                int R = getRed(pixel);
                int G = getGreen(pixel);
                int B = getBlue(pixel);

                // calculate new RGB
                int luminosity = (int) (0.21 * R + 0.72 * G + 0.07 * B);

                luminosity = (luminosity > 255) ? 255 : luminosity;

                // set new RGB
                pixel =  (luminosity << 16) | (luminosity << 8) | luminosity;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "luminosity");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void LightnessMethod() {
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

                int R = getRed(pixel);
                int G = getGreen(pixel);
                int B = getBlue(pixel);

                // calculate max and min of RGB
                int getRGBMax = Math.max(R, (Math.max(G, B)));
                int getRGBMin = Math.min(R, (Math.min(G, B)));

                // calculate new RGB
                int lightness = (int) (0.5 *(getRGBMax + getRGBMin));
                // set new RGB
                pixel =  (lightness << 16) | (lightness << 8) | lightness;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "lightness");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        AverageMethod();
        LightnessMethod();
        LuminosityMethod();
    }
}
