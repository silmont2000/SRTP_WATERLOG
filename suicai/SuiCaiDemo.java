package suicai;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import filter.Smooth;
import filter.Waterlog;

import javax.imageio.ImageIO;

public class SuiCaiDemo {

    public static BufferedImage readImage(String imageName) {
        //读取本地图片
        File imageFile = new File(imageName);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }

    public static void writeImage(BufferedImage bi, String imageName) {
        //保存本地图片
        File skinImageOut = new File(imageName);
        try {
            ImageIO.write(bi, "png", skinImageOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int rgb2gray(int rgb) {
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0xff00) >> 8;
        int b = (rgb & 0xff);
        int gray = (int) (r * 0.3 + g * 0.59 + b * 0.11);
        return gray;
    }

    public static void MYtry() {
        double k = Math.pow(Math.E, (-1 - 2));
        System.out.println(k);
    }

    public static void main(String[] args) {
//        MYtry();
        //参数设置
        int radius = 5;//半径
        int tank = 10;//油漆桶
        String savePath = "C:\\Users\\Administrator\\Pictures\\";
        String openPath = "C:\\Users\\Administrator\\Pictures\\fll.jpg";
        BufferedImage colorImage = readImage(openPath);//原图
        int width = colorImage.getWidth();
        int height = colorImage.getHeight();
        //输出图    TYPE_INT_RGB表示将8位RGB颜色分量打包为整数像素的图像。
        BufferedImage outRes = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage outRes1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.println("图片上载完毕");

        Waterlog waterlog = new Waterlog(colorImage);
        outRes = waterlog.Waterlog_action();
        System.out.println("膨胀处理完毕");

        for (int y = 1; y < height - 1; ++y) {
            for (int x = 1; x < width - 1; ++x) {
                ColorTank ctank = new ColorTank(tank);

//                在一定大小的方块内进行颜色平均
                for (int tx = x - radius; tx < x + radius; tx++) {
                    for (int ty = y - radius; ty < y + radius; ty++) {
                        if (tx > 0 && tx < width && ty > 0 && ty < height) {
//                            ctank.addToTank(colorImage.getRGB(tx, ty));
                            ctank.addToTank(outRes.getRGB(tx, ty));
                        }
                    }
                }//内矩阵循环

//                更新要写的图片信息，每个点都这样跑一遍
                outRes1.setRGB(x, y, ctank.getColor());
//				System.out.println("x:"+x+"y:"+y);
            }
        }
        System.out.println("油画感处理完毕");


        Deepen_edge Edge = new Deepen_edge(outRes1);
        Edge.OSTU_set_Threshold(50);
//        outRes1 = Edge.deepen_action();
        Edge.deepen_action();
        System.out.println("细节处理完毕");

        Smooth smooth = new Smooth(outRes1);
//        outRes1 = smooth.smooth_action();
        smooth.smooth_action();
        System.out.println("平滑处理完毕");

        outRes1 = smooth.smooth_action();
        System.out.println("复平滑处理完毕");

        writeImage(outRes1, savePath + System.currentTimeMillis() + ".jpg");
//        writeImage(outRes1, savePath + System.currentTimeMillis() + ".jpg");
        System.out.println(savePath + System.currentTimeMillis() + ".jpg");
        System.out.println("****success****");
    }

}