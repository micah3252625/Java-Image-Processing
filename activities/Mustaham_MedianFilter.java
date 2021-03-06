package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Main {
    public static File newFile(String filename) {
        return (new File(filename));
    }
    public static BufferedImage ReadImage() throws IOException {
        File file = newFile("sourceImg/lenna_noisy_input.png");
        return (ImageIO.read(file));
    }
    public static boolean WriteImage(BufferedImage img, String id) throws IOException {
        File file = newFile("outputImg/output_" + id + ".png");
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
    public static void MedianFilter(String filename) {

        // read or load image
        BufferedImage img = null;

        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage outputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int []neighborhood = new int[9];

        // process wherein to apply mean filter
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                neighborhood[0] = img.getRGB(x, y) & 255;
                neighborhood[1] = img.getRGB(x - 1, y) & 255;
                neighborhood[2] = img.getRGB(x + 1, y) & 255;
                neighborhood[3] = img.getRGB(x, y - 1) & 255;
                neighborhood[4] = img.getRGB(x - 1, y - 1) & 255;
                neighborhood[5] = img.getRGB(x + 1, y - 1) & 255;
                neighborhood[6] = img.getRGB(x, y + 1) & 255;
                neighborhood[7] = img.getRGB(x - 1, y + 1) & 255;
                neighborhood[8] = img.getRGB(x + 1, y + 1) & 255;
                Arrays.sort(neighborhood);
                int median_filter = neighborhood[neighborhood.length / 2];
                int pixel = (median_filter << 16) | (median_filter << 8) | median_filter;
                outputImg.setRGB(x, y, pixel);
            }
        }

        // write image
        try {
            WriteImage(outputImg, filename);
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MedianFilter( "median_filter");
    }
}
