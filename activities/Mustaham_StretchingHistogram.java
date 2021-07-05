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
    public static boolean WriteImage(BufferedImage img, String id) throws IOException {
        File file = newFile("output_" + id + ".png");
        return (ImageIO.write(img, "png", file));
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
    public static int truncate(int value) {
        if (value < 0)
            value = 0;
        if (value > 255)
            value = 255;
        return value;
    }

    public static void HistogramStretching() {

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

        int darkestIntensity = 0;
        int brightestIntensity = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = truncate(getRed(pixel));
                int G = truncate(getGreen(pixel));
                int B = truncate(getBlue(pixel));

                int avg = (R + G + B) / 3;

                hist[avg]++;

                // calculate intensities
                if ((x == 0) && (y == 0)) {
                    darkestIntensity = avg;
                    brightestIntensity = avg;
                }
                else {
                    if (avg < darkestIntensity)
                        darkestIntensity = avg;
                    if (avg > brightestIntensity)
                        brightestIntensity = avg;
                }
            }
        }

        int fMax = brightestIntensity;
        int fMin = darkestIntensity;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = getRed(pixel) ;
                int G = getGreen(pixel) ;
                int B = getBlue(pixel);

                int avg = (R + G + B) / 3;
                int contrast = ((avg - fMin) * 255 / (fMax - fMin));
                pixel = (contrast << 16) | (contrast << 8) | contrast;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "stretched");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        HistogramStretching();
    }
}
