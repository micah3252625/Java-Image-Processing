package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {


    public static File newFile(String filename) {
        return (new File(filename));
    }
    public static BufferedImage ReadImage() throws IOException {
        File file = newFile("sourceImg/coin.jpg");
        return (ImageIO.read(file));
    }
    public static boolean WriteImage(BufferedImage img, String id) throws IOException {
        File file = newFile("outputImg/output_" + id + ".jpg");
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
    public static int setPixel(int val) { return ((val << 16) | (val << 8) | val); }
    public static BufferedImage getGrayScaleImg(BufferedImage img) {

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

    public static void ShrinkEdgeDetection(final int T) {

        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get and set the grayscale image
        BufferedImage grayImg = getGrayScaleImg(img);

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        // conversion of grayscale image to binary image
        BufferedImage binaryImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int [][]Binary = new int[width][height];

        // process threshold
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Binary[x][y] = (grayImg.getRGB(x, y) & 255) > T ? 0 : 1;
                binaryImg.setRGB(x, y, (Binary[x][y] == 0 ? setPixel(255) : setPixel(0)));
            }
        }

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 8 path-neighbors
        int []neighborhood = new int[8];

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int a0 = (binaryImg.getRGB(x, y) & 255) == 0 ? 1 : 0;
                neighborhood[0] = (binaryImg.getRGB(x + 1, y) & 255) == 0 ? 1 : 0;
                neighborhood[1] = (binaryImg.getRGB(x - 1, y) & 255) == 0 ? 1 : 0;
                neighborhood[2] = (binaryImg.getRGB(x, y + 1) & 255) == 0 ? 1 : 0;
                neighborhood[3] = (binaryImg.getRGB(x, y - 1) & 255) == 0 ? 1 : 0;
                neighborhood[4] = (binaryImg.getRGB(x + 1, y + 1) & 255) == 0 ? 1 : 0;
                neighborhood[5] = (binaryImg.getRGB(x + 1, y - 1) & 255) == 0 ? 1 : 0;
                neighborhood[6] = (binaryImg.getRGB(x - 1, y + 1) & 255) == 0 ? 1 : 0;
                neighborhood[7] = (binaryImg.getRGB(x - 1, y - 1) & 255) == 0 ? 1 : 0;
                int sigma = 0;
                for (int i = 0; i < neighborhood.length; i++)
                    sigma += neighborhood[i];
                // formula

                if (sigma < 8)
                    output.setRGB(x, y, setPixel(255));
                    if (sigma == 8)
                        output.setRGB(x, y, setPixel(255));
                else if (a0 == 0)
                    output.setRGB(x, y, setPixel(255));
                else
                    output.setRGB(x, y, setPixel(0));
            }
        }

        // write image
        try {
            WriteImage(binaryImg, "threshold_102");
            WriteImage(output, "shrunk_edge");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ShrinkEdgeDetection(102);
    }
}
