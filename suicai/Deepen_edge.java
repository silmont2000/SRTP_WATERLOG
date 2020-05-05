package suicai;

import filter.Infiltration;
import filter.Waterlog;

import java.awt.image.BufferedImage;
import java.util.Random;

import static suicai.SuiCaiDemo.rgb2gray;

public class Deepen_edge {
    BufferedImage image;
    BufferedImage re_image;
    int h;
    int w;
    int threshold;
    int m_threshold = 3;
    int param = -7;
    int count;

    Deepen_edge(BufferedImage input) {
        image = input;
        re_image = input;
        h = image.getHeight();
        w = image.getWidth();
        System.out.println(h + "," + w);
        count = 0;
    }

    void OSTU_set_Threshold(int Threshold) {
        threshold = Threshold;
    }

    public void deepen_action() {

//        for (int i = 1; i < w - 1; i++) {
//            for (int j = 1; j < h - 1; j++) {
//                penetration(i, j);
////                texture(i, j);
//            }
//        }

        int gradient = 0;
        int gradient_r, gradient_g, gradient_b;
//        Color cl;
        int Laplacian[] = {0, 1, 0,
                1, -4, 1,
                0, 1, 0};
        int x_order[] = {1, 1, 1, 0, 0, 0, -1, -1, -1};
        int y_order[] = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                gradient = 0;
                for (int k = 0; k < 9; k++) {
//                    if (j > 840) {
//                        System.out.println("原来的位置：" + i + "," + j + "k是" + k);
//                        System.out.println("现在的位置：" + (i + x_order[k]) + "," + (j + y_order[k]));
//                    }
                    int tmp = image.getRGB(i + x_order[k], j + y_order[k]);
                    float gray = rgb2gray(tmp);
                    gradient += gray * Laplacian[k];
//                    System.out.println(Math.abs(gradient));
                }

                if (Math.abs(gradient) > threshold) {
//                    if (Math.abs(gradient) > threshold + 5)
                    for (int k = 0; k < 9; k++) {
                        int rgb = image.getRGB(i + x_order[k], j + y_order[k]);
                        int R = ((rgb >> 16) & 0xff) + param;
                        int G = ((rgb >> 8) & 0xff) + param;
                        int B = (rgb & 0xff) + param;
                        rgb = (0xff << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8)
                                | ((clamp(B) & 0xff));
                        re_image.setRGB(i + x_order[k], j + y_order[k], rgb);
                    }
//                    else {
//                        for (int k = 0; k < 9; k++) {
//                            int rgb = (0xff << 24) | (0xff << 16) | (0xff << 8) | (0xff);
//                            re_image.setRGB(i + x_order[k], j + y_order[k], rgb);
//                        }
//                    }
                } else if (Math.abs(gradient) < m_threshold) {
                    for (int k = 0; k < 9; k++) {
                        int rgb = image.getRGB(i + x_order[k], j + y_order[k]);
                        int R = ((rgb >> 16) & 0xff) - param;
                        int G = ((rgb >> 8) & 0xff) - param;
                        int B = (rgb & 0xff) - param;
                        rgb = (0xff << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8)
                                | ((clamp(B) & 0xff));
                        re_image.setRGB(i + x_order[k], j + y_order[k], rgb);
                    }
                } else if (Math.abs(gradient) < (threshold - m_threshold) / 5 + m_threshold) {
//                    boolean dir = false;
//                    int gray = 0;
//                    int[] dir_derta_x = {
//                            -1, -1, -1,
//                            -2, -2, -2,
//                            1, 1, 1,
//                            2, 2, 2};
//                    int[] dir_derta_y = {
//                            1, 0, -1,
//                            1, 0, -1,
//                            1, 0, -1,
//                            1, 0, -1};
//
//                    for (int k = 0; k < 8; k++) {
//                        int ii = i + dir_derta_x[k];
//                        int jj = j + dir_derta_y[k];
//
//                        if (ii < 0) ii = 0;
//                        else if (ii >= w) ii = w - 1;
//                        if (jj < 0) jj = 0;
//                        else if (jj >= h) jj = h - 1;
//                        gray += (dir_derta_x[k] > 0 ? -1 : 1) * rgb2gray(image.getRGB(ii, jj));
//                    }
//
//                    dir = (gray >= 0);
//
//                    Infiltration infiltration = new Infiltration(image, i, j, dir);
//                    infiltration.infiltration_action();
//                    count++;

//                    Waterlog tmp = new Waterlog(image, image, i, j);
//                    tmp.Waterlog_action();
//                    texture(i, j);
                }
            }
        }

//        if (Math.abs(gradient) > (m_threshold + threshold) / 3 + m_threshold
//                && Math.abs(gradient) > (m_threshold + threshold) / 3 * 2 + m_threshold)
//        return re_image;
        System.out.println("count:" + count);
    }

    private void penetration(int i, int j) {
        int derta_x = randInt(-1, 1);
        int derta_y = randInt(-1, 1);
        if (i + derta_x >= w || i + derta_x < 0) derta_x = 0;
        re_image.setRGB(i + derta_x, j + derta_y, image.getRGB(i, j));
    }

    private void texture(int i, int j) {
        int derta_x = randInt(-5, 5);
        int derta_y = randInt(-2, 2);
        if (i + derta_x >= w || i + derta_x < 0) derta_x = 0;
        if (j + derta_y >= h || j + derta_y < 0) derta_y = 0;
//
        int rgb1 = image.getRGB(i, j);
        int rgb2 = image.getRGB(i + derta_x, j + derta_y);
//                    Math.round(Math.abs());
        int R = Math.round(Math.abs(((rgb1 >> 16) & 0xff) - ((rgb2 >> 16) & 0xff))) + ((rgb1 >> 16) & 0xff);
        int G = Math.round(Math.abs(((rgb1 >> 8) & 0xff) - ((rgb2 >> 8) & 0xff))) + ((rgb1 >> 8) & 0xff);
        int B = Math.round(Math.abs((rgb1 & 0xff) - (rgb2 & 0xff))) + (rgb1 & 0xff);
        rgb1 = ((clamp(255) & 0xff) << 24) | ((clamp(R) & 0xff) << 16) | ((clamp(G) & 0xff) << 8)
                | ((clamp(B) & 0xff));
        re_image.setRGB(i + derta_x, j + derta_y, rgb1);
//        re_image.setRGB(i + derta_x, j + derta_y, image.getRGB(i, j));
    }

    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int clamp(int rgb) {
//        return 0;
        if (rgb > 255)
            return 255;
        return Math.max(rgb, 0);
    }

}