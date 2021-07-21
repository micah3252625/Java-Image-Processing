package com.company;

public class Cluster {
    int id, pixelCount;
    int red, green, blue;
    int Rs, Gs, Bs;

    public Cluster(int id, int rgb) {
        this.red = getRed(rgb);
        this.green = getGreen(rgb);
        this.blue = getBlue(rgb);
        this.id = id;
        appendPixel(rgb);
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
    public void clear() {
        this.red = this.green = this.blue = 0;
        this.Rs = this.Gs = this.Bs = 0;
        this.pixelCount = 0;
    }
    int getRGB() {
        int R = this.Rs / this.pixelCount;
        int G = this.Gs / this.pixelCount;
        int B = this.Bs / this.pixelCount;
        return (R << 16) | (G << 8) | B;
    }
    void appendPixel(int color) {
        this.Rs += getRed(color);
        this.Gs += getGreen(color);
        this.Bs += getBlue(color);
        this.pixelCount++;
        this.red = this.Rs / this.pixelCount;
        this.green = this.Gs / this.pixelCount;
        this.blue = this.Bs / this.pixelCount;

    }
    int getDistance(int color) {
        int rangeRed = Math.abs(this.red - getRed(color));
        int rangeGreen = Math.abs(this.green - getGreen(color));
        int rangeBlue = Math.abs(this.blue - getBlue(color));
        return (rangeRed + rangeGreen + rangeBlue) / 3;
    }

}
