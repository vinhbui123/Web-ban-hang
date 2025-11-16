package vn.edu.hcmuaf.fit.Web_ban_hang.model;

public class Dimension {
    private int totalWeight;
    private int totalLength;
    private int totalWidth;
    private int totalHeight;

    public Dimension(int weight, int length, int width, int height) {
        this.totalWeight = weight;
        this.totalLength = length;
        this.totalWidth = width;
        this.totalHeight = height;
    }

    public void add(int weight, int length, int width, int height) {
        this.totalWeight += weight;
        this.totalLength += length;
        this.totalWidth += width;
        this.totalHeight += height;
    }

    // Getters
    public int getTotalWeight() {
        return totalWeight;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    // Setters
    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
    }

    public void setTotalHeight(int totalHeight) {
        this.totalHeight = totalHeight;
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "totalWeight=" + totalWeight +
                ", totalLength=" + totalLength +
                ", totalWidth=" + totalWidth +
                ", totalHeight=" + totalHeight +
                '}';
    }
}
