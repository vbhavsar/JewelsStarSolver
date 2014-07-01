package jewels_star.solver;

import java.awt.*;
import java.awt.image.SampleModel;

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
}
