package es.unizar.iaaa.pid.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Embeddable
public class BoundingBox implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "min_x")
    private Double minX;

    @Column(name = "min_y")
    private Double minY;

    @Column(name = "max_x")
    private Double maxX;

    @Column(name = "max_y")
    private Double maxY;

    public BoundingBox(Double minX, Double minY, Double maxX, Double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public BoundingBox() {
    }

    public Double getMinX() {
        return minX;
    }

    public BoundingBox minX(Double minX) {
        this.minX = minX;
        return this;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Double getMinY() {
        return minY;
    }

    public BoundingBox minY(Double minY) {
        this.minY = minY;
        return this;
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Double getMaxX() {
        return maxX;
    }

    public BoundingBox maxX(Double maxX) {
        this.maxX = maxX;
        return this;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMaxY() {
        return maxY;
    }

    public BoundingBox maxY(Double maxY) {
        this.maxY = maxY;
        return this;
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public List<BoundingBox> split() {
        Double disX = (maxX - minX)/2.0;
        Double disY = (maxY - minY)/2.0;

        return Arrays.asList(
            new BoundingBox(minX, minY, minX+disX, minY+disY),
            new BoundingBox(minX+disX, minY, maxX, minY+disY),
            new BoundingBox(minX, minY+disY, minX+disX, maxY),
            new BoundingBox(minX+disX, minY+disY, maxX, maxY));
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
            "minX='" + minX + '\'' +
            ", minY='" + minY + '\'' +
            ", maxX=" + maxX + '\'' +
            ", maxY=" + maxY + '\'' +
            "}";
    }

}
