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
        File file = newFile("lenna.png");
        return (ImageIO.read(file));
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

    public static void ArrayHistogram() {

        int hist[] = new int[256];

        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // initialized all counters to zeros
        for (int i = 0; i < 256; i++) {
            hist[i] = 0;
        }

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        //
        int darkestIntensity = 0;
        int brightestIntensity = 0;
        int frequentIntensity = 0;
        int frequency = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = getRed(pixel) ;
                int G = getGreen(pixel) ;
                int B = getBlue(pixel);

                int avg = (R + G + B) / 3;

                hist[avg]++;

                // calculate intensities
                if ((x == 0) && (y == 0)) {
                    darkestIntensity = avg;
                    brightestIntensity = avg;
                    frequency = hist[avg];
                }
                else {
                    if (avg < darkestIntensity)
                        darkestIntensity = avg;
                    if (avg > brightestIntensity)
                        brightestIntensity = avg;
                    if (hist[avg] > frequency) {
                        frequency = hist[avg];
                        frequentIntensity = avg;
                    }
                }
                // set new RGB
                pixel =  (R << 16) | (G << 8) | B;
                img.setRGB(x, y, pixel);
            }
        }

        // display sc1
        for (int i = 0; i < 256; i++) {
            System.out.println("No.of pixels with a grayscale intensity of " + i + ": " + hist[i]);
        }

        // display sc2
        System.out.println("\ndarkest intensity: " + darkestIntensity);
        System.out.println("brightest intensity: " + brightestIntensity);
        System.out.println("most frequent intensity: " + frequentIntensity);


    }
    public static void main(String[] args) {
        ArrayHistogram();
    }
}
