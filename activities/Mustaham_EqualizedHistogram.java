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

    public static int[] ComputeHistogram() {
        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int histogram[] = new int[256];

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = getRed(pixel) ;
                int G = getGreen(pixel) ;
                int B = getBlue(pixel);
                int avg = (R + G + B) / 3;
                histogram[avg]++;
            }
        }
        return histogram;
    }
    public static int[] ComputeCDFHistogram() {
        // call ComputeHistogram method to get the histogram
        int hist[] = ComputeHistogram();
        int cdfHist[] = new int[256];

        // CDF histogram calculation
        for (int i = 0; i < cdfHist.length; i++)
            cdfHist[i] = (i == 0 ?  hist[i] :  hist[i] + cdfHist[i - 1]);

        return cdfHist;
    }
    public static void EqualizeHistogram() {

        // read or load image
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // call ComputeCDFHistogram method to get the CDF histogram
        int cdfHist[] = ComputeCDFHistogram();

        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        int totalPixel = width * height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                int R = getRed(pixel) ;
                int G = getGreen(pixel) ;
                int B = getBlue(pixel);
                int avg = (R + G + B) / 3;

                int scale = truncate((255 * cdfHist[avg]) / totalPixel);

                int equalizedIntensity = scale;

                pixel =  (equalizedIntensity << 16) | (equalizedIntensity << 8) | equalizedIntensity;
                img.setRGB(x, y, pixel);
            }
        }

        try {
            WriteImage(img, "equalized");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        EqualizeHistogram();
    }
}
