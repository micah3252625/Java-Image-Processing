package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


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
    public static ArrayList<int[]> ImageHistogram(BufferedImage img) {

        // histogram for RGBs
        int RedHistogram[] = new int[256];
        int GreenHistogram[] = new int[256];
        int BlueHistogram[] = new int[256];

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = getRed(pixel) ;
                int G = getGreen(pixel) ;
                int B = getBlue(pixel);
                RedHistogram[R]++;
                GreenHistogram[G]++;
                BlueHistogram[B]++;
            }
        }

        ArrayList<int[]> histogram = new ArrayList<int[]>();
        histogram.add(RedHistogram);
        histogram.add(GreenHistogram);
        histogram.add(BlueHistogram);

        return histogram;
    }
    public static ArrayList<int[]> HistogramTable(BufferedImage img) {

        // Get an image histogram
        ArrayList<int[]> imageHistogram = ImageHistogram(img);

        // create the table for RGB channels
        ArrayList<int[]> imageTable = new ArrayList<int[]>();

        // histogram for RGBs
        int RedHistogram[] = new int[256];
        int GreenHistogram[] = new int[256];
        int BlueHistogram[] = new int[256];

        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        // calculate scale factor
        float scale = (float) (255.0 / (width * height));

        for (int i = 0; i < 256; i++) {
            sumRed += imageHistogram.get(0)[i];
            int redValue = (int) (sumRed * scale);
            RedHistogram[i] = (redValue > 255) ? 255 : redValue;

            sumGreen += imageHistogram.get(1)[i];
            int greenValue = (int) (sumGreen * scale);
            GreenHistogram[i] = (greenValue > 255) ? 255 : greenValue;

            sumBlue += imageHistogram.get(2)[i];
            int blueValue = (int) (sumBlue * scale);
            BlueHistogram[i] = (blueValue > 255) ? 255 : blueValue;
        }

        imageTable.add(RedHistogram);
        imageTable.add(GreenHistogram);
        imageTable.add(BlueHistogram);

        return imageTable;

    }

    public static void HistogramEqualization() {
        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<int[]> histogramTable = HistogramTable(img);

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = getRed(pixel) ;
                int G = getGreen(pixel) ;
                int B = getBlue(pixel);
                R = histogramTable.get(0)[R];
                G = histogramTable.get(1)[G];
                B = histogramTable.get(2)[B];

                pixel =  (R << 16) | (G << 8) | B;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "coloredEqualized");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HistogramEqualization();
    }
}
