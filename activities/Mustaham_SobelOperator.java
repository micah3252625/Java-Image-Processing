package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    // Kernels of different kind of operators
    public static final int[][] SobelHoriKernel = {
            { -1, 0, 1 },
            { -2, 0, 2 },
            { -1, 0, 1 }
    };
    public static final int[][] SobelVertKernel = {
            { -1, -2, -1 },
            {  0,  0,  0 },
            {  1,  2,  1 }
    };
    public static final int[][] LaplacianKernel = {
            { 0,  1,  0 },
            { 1, -4,  1 },
            { 0,  1,  0 }
    };
    public static final int[][] PrewittVertKernel = {
            { -1, -1, -1 },
            {  1,  1,  1 },
            {  0,  0,  0 }
    };
    public static final int[][] PrewittHoriKernel = {
            { -1, 0, 1 },
            { -1, 0, 1 },
            { -1, 0, 1 },
    };
    public static File newFile(String filename) {
        return (new File(filename));
    }
    public static BufferedImage ReadImage() throws IOException {
        File file = newFile("sourceImg/lenna.png");
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
    public static int truncate(int value) {
        if (value < 0)
            value = 0;
        if (value > 255)
            value = 255;
        return value;
    }
    public static BufferedImage getGrayScaleImg(BufferedImage img) { // method that gets that process the grayscale image

        // initialize new Buffered Image
        BufferedImage grayImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        // get the pixel of image and process it to grayscale
        for (int w = 0; w < img.getWidth(); w++) {
            for (int h = 0; h < img.getHeight(); h++) {
                int grayPixel = img.getRGB(w, h);
                int R = getRed(grayPixel) ;
                int G = getGreen(grayPixel) ;
                int B = getBlue(grayPixel);
                int avg = (R + G + B) / 3;
                grayPixel = (avg << 16) | (avg << 8) | avg;
                grayImg.setRGB(w, h, grayPixel);
            }
        }
        return grayImg; // return grayscale image
    }
    public static void Edges(int [][]kernel, String filename) {

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

        BufferedImage grayImg = getGrayScaleImg(img);  // call to get the grayscale image
        BufferedImage outputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int filter = 0;
        // convolution process
        for (int x = 1; x <= width - 2; x++) {
            for (int y = 1; y <= height - 2; y++) {
                int sum = 0;
                for (int i = 0, a = -1; i < kernel.length; i++, a++) {
                    for (int j = 0, b = -1; j < kernel.length; j++, b++) {
                        sum += ((grayImg.getRGB(x + a, y + b) & 255) * kernel[i][j]);
                    }
                }

                filter = truncate(sum);
                int pixel = (filter << 16) | (filter << 8) | filter;
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
        Edges(SobelHoriKernel, "sobel_horizontal");
        Edges(SobelVertKernel, "sobel_vertical");
    }
}
