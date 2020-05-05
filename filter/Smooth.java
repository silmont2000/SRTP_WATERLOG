package filter;

import java.awt.*;
import java.awt.image.BufferedImage;

import suicai.Deepen_edge;

public class Smooth {

    int sigma_clr;
    int sigma_spa;
    double wd, wr;
    BufferedImage image;
    int h, w;
    int d;

    public Smooth(BufferedImage input) {
        image = input;
        h = image.getHeight();
        w = image.getWidth();
        d = 7;
        sigma_clr = 10;
        sigma_spa = 10;
        System.out.println(h + "," + w);
    }

    public BufferedImage smooth_action() {
        double e = Math.E;//自然常数e的近似值
        int[] total_RGB = new int[3];
        double[] total_w = new double[3];
//        BufferedImage re = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                total_w[0] = total_w[1] = total_w[2] = 0;
                total_RGB[0] = total_RGB[1] = total_RGB[2] = 0;
//                System.out.println("1");
                for (int m = -d / 2; m <= d / 2; ++m) {
                    for (int k = -d / 2; k <= d / 2; ++k) {
//                        System.out.print(m + "," + k + "   ");
                        int ii = i + m, jj = j + k;
                        if (ii < 0) ii = 0;
                        else if (ii >= w) ii = w - 1;
                        if (jj < 0) jj = 0;
                        else if (jj >= h) jj = h - 1;
                        Color tmp1 = new Color(image.getRGB(i, j));
                        Color tmp2 = new Color(image.getRGB(ii, jj));

                        wd = (m * m + k * k) / (2 * sigma_spa * sigma_spa);

                        double denominator = 2 * sigma_clr * sigma_clr;
                        wr = Math.pow(tmp1.getRed() - tmp2.getRed(), 2) / denominator;
                        total_RGB[0] += Math.pow(e, (-wd - wr)) * tmp2.getRed();
                        total_w[0] += Math.pow(e, (-wd - wr));

                        wr = Math.pow(tmp1.getGreen() - tmp2.getGreen(), 2) / denominator;
                        total_RGB[1] += Math.pow(e, (-wd - wr)) * tmp2.getGreen();
                        total_w[1] += Math.pow(e, (-wd - wr));

                        wr = Math.pow(tmp1.getBlue() - tmp2.getBlue(), 2) / denominator;
                        total_RGB[2] += Math.pow(e, (-wd - wr)) * tmp2.getBlue();
                        total_w[2] += Math.pow(e, (-wd - wr));
                    }
                }

                for (int l = 0; l < 3; ++l) {
//                            System.out.println(total_RGB[0] + "," + total_RGB[1] + "," + total_RGB[2]);
//                            System.out.println(total_w[0] + "," + total_w[1] + "," + total_w[2]);
                    total_RGB[l] /= total_w[l];
                }
//                for (int l = 0; l < 3; ++l) {
//                    System.out.println(total_RGB[0] + "," + total_RGB[1] + "," + total_RGB[2]);
//                }

                int rgb = ((0xff) << 24) | ((Deepen_edge.clamp(total_RGB[0]) & 0xff) << 16)
                        | ((Deepen_edge.clamp(total_RGB[1]) & 0xff) << 8) | ((Deepen_edge.clamp(total_RGB[2]) & 0xff));

                image.setRGB(i, j, rgb);
            }
        }
        return image;
    }
}
