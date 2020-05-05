package filter;

import java.awt.*;
import java.awt.image.BufferedImage;

import suicai.Deepen_edge;

public class Infiltration {
    BufferedImage image;
    int h, w;
    int process_i, process_j;
    boolean direction;

    public Infiltration(BufferedImage input, int i, int j, boolean dir) {
        image = input;
        h = image.getHeight();
        w = image.getWidth();
        process_i = i;
        process_j = j;
        direction = dir;
    }

    public void infiltration_action() {
        double index = 1.05;
//        todo
        int[] derta_x = {1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4};
        int[] derta_y = {0, -1, 1, 0, -1, -2, 2, 1, 0, -1, -2, -3, 1, 0, -1};
        int total_points = derta_x.length;
        for (int i = 0; i < total_points; i++) {
            int ii;
            if (direction) {
                ii = process_i + derta_x[i];
            } else {
                ii = process_i - derta_x[i];
            }
            int jj = process_j + derta_y[i];

            if (ii < 0) ii = 0;
            else if (ii >= w) ii = w - 1;
            if (jj < 0) jj = 0;
            else if (jj >= h) jj = h - 1;

            double distance = Math.sqrt(Math.pow(Math.abs(derta_x[i]) + Math.abs(derta_y[i]), 2));
            double percent_original = Math.pow(index, -distance);
            double percent_other = 1 - percent_original;

            Color tmp1 = new Color(image.getRGB(process_i, process_j));
            Color tmp2 = new Color(image.getRGB(ii, jj));

            int R = Math.min((int) (tmp1.getRed() * percent_original + tmp2.getRed() * percent_other),
                    tmp1.getRed());
            int G = Math.min((int) (tmp1.getGreen() * percent_original + tmp2.getGreen() * percent_other),
                    tmp1.getGreen());
            int B = Math.min((int) (tmp1.getBlue() * percent_original + tmp2.getBlue() * percent_other),
                    tmp1.getBlue());

            int rgb = ((0xff) << 24) | ((Deepen_edge.clamp(R) & 0xff) << 16)
                    | ((Deepen_edge.clamp(G) & 0xff) << 8) | ((Deepen_edge.clamp(B) & 0xff));

            image.setRGB(process_i, process_j, rgb);
        }
    }
}
