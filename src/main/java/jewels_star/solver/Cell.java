package jewels_star.solver;

import java.awt.*;
import java.awt.image.SampleModel;
import java.util.HashMap;
import java.util.Map;

/**
 * User: vishal
 * Date: 5/31/14
 * Time: 2:24 PM
 */
public class Cell {

    Point point;
    private SampleModel sampleModel;
    private int[] rgb;
    private String rgbHex;
    private Integer clazz;
    private int[][] samples;

    public Cell(int x, int y) {
        this.point = new Point(x, y);
    }

    public Cell(Point p) {
        this.point = p;
    }

    public SampleModel getSampleModel() {
        return sampleModel;
    }

    public void setSampleModel(SampleModel sampleModel) {
        this.sampleModel = sampleModel;
    }

    public int[] getRgb() {
        return rgb;
    }

    public void setRGB(int[] rgb) {
        this.rgb = rgb;
        this.rgbHex = Cell.getRGB(rgb[0], rgb[1], rgb[2]);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("{x: %d, y: %d, sampleModel: %s, rgb: %s}", point.x, point.y, sampleModel.toString(), getRgbHex()));
        return builder.toString();
    }

    public String getRgbHex() {
        return this.rgbHex;
    }

    public static String getRGB(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public Integer getClazz() {
        return clazz;
    }

    public void setClazz(Integer clazz) {
        this.clazz = clazz;
    }

    public int[][] getSamples() {
        return samples;
    }

    public void setSamples(int[][] samples) {
        this.samples = samples;
    }

    public void determineClazz(Map<String, Integer> rgbToClassMap) {

        // Build histogram

        Map<Integer, Integer> clazzCount = new HashMap<Integer, Integer>();

        if (samples != null) {
            for (int[] sample : samples) {
                int r = sample[0], g = sample[1], b = sample[2];
                final String rgbHex = Cell.getRGB(r, g, b);
                int sclazz = rgbToClassMap.get(rgbHex);

                final Integer clazzVal = clazzCount.get(sclazz);
                if (clazzVal == null) {
                    clazzCount.put(sclazz, 1);
                } else {
                    clazzCount.put(sclazz, clazzVal+1);
                }
            }

            int maxCount = 0;
            int maxClazz = -1;
            // See which count is highest
            for (Map.Entry<Integer, Integer> entry : clazzCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    maxClazz = entry.getKey();
                }
            }
            if (this.clazz != maxClazz) {
                System.out.printf("Cell [%d,%d] think it belongs to clazz [%d] but I believe it belongs to [%d]. Setting clazz to %d\n", this.point.x, this.point.y, this.clazz, maxClazz, maxClazz);
                this.clazz = maxClazz;
            }
        }
    }
}
