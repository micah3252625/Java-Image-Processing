package com.company;

public class Cluster {
    int id, pixelCount;
    int gray;
    int GRAYS;

    public Cluster(int id, int colorVal) {
        this.gray = getGrayScale(colorVal);
        this.id = id;
        appendPixel(colorVal);
    }
    int getRed(int pixel) {
        return (pixel & (255 << 16)) >> 16;
    }
    int getGreen(int pixel) {
        return (pixel & (255 << 8)) >> 8;
    }
    int getBlue(int pixel) {
        return pixel & 255;
    }
    int getGrayScale(int grayValue) {
        return (getRed(grayValue) + getGreen(grayValue) + getBlue(grayValue)) / 3;
    }
    public void clear() {
        this.gray = this.GRAYS = 0;
        this.pixelCount = 0;
    }
    int getGrayIntensity() {
        int grayIntensity = this.GRAYS / this.pixelCount;
        return (grayIntensity << 16) | (grayIntensity << 8) | grayIntensity;
    }
    void appendPixel(int color) {
        this.GRAYS += getGrayScale(color);
        this.pixelCount++;
        this.gray = this.GRAYS / this.pixelCount;
    }
    int getDistance(int color) {
        int rangeGray = Math.abs(this.gray - getGrayScale(color));
        return rangeGray / 3 ;
    }

}
