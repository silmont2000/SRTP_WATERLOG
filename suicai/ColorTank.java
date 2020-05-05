package suicai;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorTank {
    private int count = 0;
    //    实际上是二维数组
    private List<List<Integer>> colorList = new ArrayList<List<Integer>>();

    ColorTank(int count) {
//        分配空间
        this.count = count;
        for (int i = 0; i <= this.count; i++) {
            colorList.add(new ArrayList<Integer>());
        }
    }

    void addToTank(int rgb) {
//        把输入灰度放到对应的油漆桶
        float gray = SuiCaiDemo.rgb2gray(rgb);
        int index = (int) ((gray / 255) * count);
        colorList.get(index).add(rgb);
    }

    int getColor() {
        int maxIndex = 0;
        int maxSize = 0;

//        找到对应最多颜色的油漆桶
        for (int i = 0; i < colorList.size(); i++) {
            List<Integer> temp = colorList.get(i);
            if (temp.size() > maxSize) {
                maxSize = temp.size();
                maxIndex = i;
            }
        }
//        maxIndex = colorList.size() - 1;
//        maxSize = colorList.get(maxIndex).size();

        List<Integer> maxRGBList = colorList.get(maxIndex);
        int allColorR = 0;
        int allColorG = 0;
        int allColorB = 0;
        for (Integer integer : maxRGBList) {
            Color c = new Color(integer);
//             创建具有指定组合的 RGB 值的不透明的 sRGB 颜色，
//             此 sRGB 值的 16-23 位表示红色分量，8-15 位表示绿色分量，0-7 位表示蓝色分量。
            allColorR += c.getRed();
            allColorG += c.getGreen();
            allColorB += c.getBlue();
        }

//        rgb各自取平均
        allColorR = allColorR / maxRGBList.size();
        allColorG = allColorG / maxRGBList.size();
        allColorB = allColorB / maxRGBList.size();
        return new Color(allColorR, allColorG, allColorB).getRGB();
    }
}