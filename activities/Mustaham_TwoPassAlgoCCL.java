package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Main {

    private static int[][] labeledBinaryImage;
    public static File newFile(String filename) {
        return (new File(filename));
    }
    public static BufferedImage ReadImage() throws IOException {
        File file = newFile("sourceImg/lightsabers.jpg");
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
    public static int[][] getBinaryImg(BufferedImage img, int T) {
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
        return Binary;
    }
    public static ArrayList<Integer> prior_neighbors(int[][] labels, int x, int y) {
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i < 0 || y + j < 0 || x + i > labels.length - 1 || y + j > labels[0].length - 1)
                    continue;
                else {
                    if (x + i == 0 && x + j == 0)
                        continue;
                    if (labels[x + i][y + j] != 0)
                        neighbors.add(labels[x + i][y + j]);
                }
            }
        }
        return neighbors;
    }
    public static <T> ArrayList<T> union(ArrayList<T> list1, ArrayList<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }
    public static void ClassComponent(int[][] binaryImg, int width, int height) {

        ArrayList<ArrayList<Integer>> linked = new ArrayList<ArrayList<Integer>>();
        int[][] labels= new int[binaryImg.length][binaryImg[0].length];
        int nextLabel = 0;

        // FIRST PASS
        // initialize labels to all 0s
        for (int i = 0; i < binaryImg.length; i++) {
            for (int j = 0; j < binaryImg.length; j++) {
                labels[i][j] = 0;
            }
        }

        for (int i = 0; i < binaryImg.length; i++) {
            for (int j = 0; j < binaryImg[0].length; j++) {
                if (binaryImg[i][j] != 0) {
                    ArrayList<Integer> neighbors = prior_neighbors(labels, i, j);
                    if (neighbors.size() == 0) {
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        temp.add(nextLabel);
                        linked.add(nextLabel, temp);
                        labels[i][j] = nextLabel;
                        nextLabel++;
                    }
                    else {
                        labels[i][j] = width * height;
                        for (int n : neighbors) {
                            if (n < labels[i][j])
                                labels[i][j] = n;
                        }
                        for (int n : neighbors) {
                            linked.set(n, union(linked.get(n), neighbors));
                        }
                    }
                }
            }
        }

        // SECOND PASS
        for (int i = 0; i < binaryImg.length; i++) {
            for (int j = 0; j < binaryImg[0].length; j++) {
                ArrayList<Integer> EquivalenceLabels = linked.get(labels[i][j]);
                labels[i][j] = width * height;
                for (int label : EquivalenceLabels) {
                    if (label < labels[i][j])
                        labels[i][j] = label;
                }

            }
        }

        /*


        int[][] allLabels = new int[width][height];
        for (int i = 0; i < binaryImg.length; i++) {
            for (int j = 0; j < binaryImg[0].length; j++) {
                if (labels[i][j] != 0){
                    allLabels[i][j] = labels[i][j];
                    System.out.println(allLabels[i][j]);
                }
            }
        }

        for (int i = 0; i < binaryImg.length; i++) {
            for (int j = 0; j < binaryImg[0].length; j++) {
                if (labels[i][j] != 0){
                    count++;
                }
            }
        }

        int colors[] = new int[count];
        Random random = new Random(0);
        for (int i=0; i<count; i++) {
            colors[i] = 0xFF000000 | random.nextInt();
        }
    */
        int count = 0;

        for (int i = 0; i < binaryImg.length; i++) {
            for (int j = 0; j < binaryImg[0].length; j++) {
                count++;
            }
        }
        int colors[] = new int[count];
        Random random = new Random(0);
        for (int i = 0; i < count; i++) {
            colors[i] = 0xFF000000 | random.nextInt();
        }

        labeledBinaryImage = labels;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = labeledBinaryImage[x][y];
                output.setRGB(x, y, colors[pixel]);
            }
        }

        // write image
        try {
            WriteImage(output, "lightsabers2ndPass");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
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

        int[][] binaryImage = getBinaryImg(img, 200);

        ClassComponent(binaryImage, width, height);

    }
}
