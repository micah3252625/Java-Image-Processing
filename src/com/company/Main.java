package com.company;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class Main {

    public static Cluster[] clusters;

    public static File newFile(String filename) {
        return (new File(filename));
    }

    public static BufferedImage ReadImage() throws IOException {
        File file = newFile("sourceImg/tulips.jpg");
        return (ImageIO.read(file));
    }

    public static boolean WriteImage(BufferedImage img, String id) throws IOException {
        File file = newFile("outputImg/output_" + id + ".jpg");
        return (ImageIO.write(img, "jpg", file));
    }

    public static Cluster findMinCluster(int RGB) {
        Cluster clusterObj = null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < clusters.length; i++) {
            int distance = clusters[i].getDistance(RGB);
            if (distance < min) {
                min = distance;
                clusterObj = clusters[i];
            }
        }
        return clusterObj;
    }

    public static Cluster[] createClusters(BufferedImage img, int kValue) {
        Cluster[] clusterResult = new Cluster[kValue];
        // points
        int x = 0, y = 0;
        // calculate point distance
        int distanceX = img.getWidth() / kValue;
        int distanceY = img.getHeight() / kValue;
        for (int i = 0; i < kValue; i++) {
            clusterResult[i] = new Cluster(i, img.getRGB(x, y));
            x += distanceX;
            y += distanceY;
        }
        return clusterResult;
    }

    public static void CalculateK_Means(int kValue) {
        // read or load image
        long start = System.currentTimeMillis();
        BufferedImage img = null;
        try {
            img = ReadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get image dimension
        int width = img.getWidth();
        int height = img.getHeight();

        // create a cluster
        clusters = createClusters(img, kValue);
        // create LUT
        int[] LUT = new int[width * height];
        Arrays.fill(LUT, -1);
        // at the first loop, all pixels will move their clusters
        boolean clusterState = true;
        // loop until all clusters are stable
        int loops = 0;

        while (clusterState) {
            clusterState = false;
            loops++;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = img.getRGB(x, y);
                    Cluster clusterObj = findMinCluster(pixel);
                    if (LUT[width * y + x] != clusterObj.id) {
                        // change clusterState to continue looping
                        clusterState = true;
                        LUT[width * y + x] = clusterObj.id;
                    }
                }
            }

            // updates and clears all cluster
            for (int i = 0; i < clusters.length; i++) {
                clusters[i].clear();
            }
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = img.getRGB(x, y);
                    int clusterID = LUT[width * y + x];
                    // add the pixel to the cluster
                    clusters[clusterID].appendPixel(pixel);
                }
            }
        }

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int clusterID = LUT[width * y + x];
                output.setRGB(x, y, clusters[clusterID].getGrayIntensity());
            }
        }

        long end = System.currentTimeMillis();
        long timeRange = (end - start);

        System.out.println("Clustered to " + kValue + " clusters in " + loops + " loops in " + timeRange + " ms.");

        // write image
        try {
            WriteImage(output, "K-MeansGrayIntensity");
            System.out.println("Success!");
        } catch (IOException e) {
            System.out.println("Unable to process image!");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        CalculateK_Means(3);
    }



}