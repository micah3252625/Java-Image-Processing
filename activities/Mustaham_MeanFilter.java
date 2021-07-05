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


    public static int[][] getImageFile(BufferedImage img) {

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        // imageFile 2D array is to to store the Image file pixels
        int imageFile[][] = new int[width][height];

        // get the image Pixel
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imageFile[x][y] = img.getRGB(y, x);
            }
        }

        return imageFile;
    }

    public static int[][] ApplyMeanFilter(BufferedImage img) {
        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();


        int imageFile[][] = getImageFile(img);
        // for the output image
        int outputImage[][] = new int[width][height];

        // process wherein to apply mean filter
        for (int x = 1; x <= width - 2; x++) {
            for (int y = 1; y <= height - 2; y++) {
                //compute filter result for position (u,v)
                int sum = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if((y+(i)>=0 && x+(j)>=0 && y+(i) < width && x + (j) < height)) {
                            int pixel = imageFile[y + j][x + i];
                            int R = getRed(pixel) ;
                            int G = getGreen(pixel) ;
                            int B = getBlue(pixel);
                            int avg = (R + G + B) / 3;
                            sum += avg;
                        }
                    }
                }
                // get the mean
                int meanAvg = (sum / 9);
                outputImage[x][y] = meanAvg;
            }
        }
        return  outputImage;
    }
    public static void MeanFilter() {

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

        // call method apply mean filter
        int outputImage[][] = ApplyMeanFilter(img);

        // set image
        int pixelValue = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelValue = outputImage[x][y];
                pixelValue = (pixelValue << 16) | (pixelValue << 8) | pixelValue;
                img.setRGB(x, y, pixelValue);
            }
        }

        // write image
        try {
            WriteImage(img, "mean_filtered");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        MeanFilter();
    }
}
