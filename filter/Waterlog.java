package filter;

import suicai.ColorTank;

import java.awt.*;
import java.awt.image.BufferedImage;

import static suicai.Deepen_edge.clamp;

public class Waterlog {
    BufferedImage image;
    BufferedImage re;
    int h, w;
    int radius = 4;
    int process_i, process_j;

    public Waterlog(BufferedImage input) {
        image = input;
//        re = output;
        h = image.getHeight();
        w = image.getWidth();
//        process_i = i;
//        process_j = j;
        re = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage Waterlog_action() {
        Result result = new Result();
        for (int process_j = 1; process_j < h - 1; ++process_j) {
            for (int process_i = 1; process_i < w - 1; ++process_i) {
        result.R = result.G = result.B = 0;
//                result.print();
        for (int tx = process_i - radius; tx < process_i + radius; tx++) {
            for (int ty = process_j - radius; ty < process_j + radius; ty++) {
                if (tx > 0 && tx < w && ty > 0 && ty < h) {
                    int tmp = image.getRGB(tx, ty);
                    result.set_max_rgb(tmp);
                }
            }
        }
//                result.print();
//                System.out.println();
        re.setRGB(process_i, process_j, result.getRGB());
            }
        }
        return re;
    }

    public static int rgb2yuv(int rgb) {
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0xff00) >> 8;
        int b = (rgb & 0xff);
        int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);
        return gray;
    }
}

class Result {
    int R, G, B;

    public int getRGB() {
        int rgb = (0xff << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8)
                | ((clamp(B) & 0xff));
        return rgb;
    }

    public void set_max_rgb(int tmp) {
        int Rr = ((tmp >> 16) & 0xff);
        int Gg = ((tmp >> 8) & 0xff);
        int Bb = (tmp & 0xff);

        R = Math.max(R, Rr);
        G = Math.max(G, Gg);
        B = Math.max(B, Bb);

//        R = Math.min(R, Rr);
//        G = Math.min(G, Gg);
//        B = Math.min(B, Bb);
    }

    public void print() {
        System.out.println("RGB:" + R + "," + G + "," + B);
    }
}