package es.unizar.iaaa.pid.domain;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

public class BoundingBoxTest {

    @Test
    public void splitPositive() {
        BoundingBox bb = new BoundingBox(0.0, 0.0, 2.0, 2.0);
        List<BoundingBox> l = bb.split();
        assertThat("Length", l.size(), is(4));
        assertThat("First bounding box", l.get(0), samePropertyValuesAs(new BoundingBox(0.0, 0.0, 1.0, 1.0)));
        assertThat("Second bounding box", l.get(1), samePropertyValuesAs(new BoundingBox(1.0, 0.0, 2.0, 1.0)));
        assertThat("Third bounding box", l.get(2), samePropertyValuesAs(new BoundingBox(0.0, 1.0, 1.0, 2.0)));
        assertThat("Fourth bounding box", l.get(3), samePropertyValuesAs(new BoundingBox(1.0, 1.0, 2.0, 2.0)));
    }

    @Test
    public void splitCenter() {
        BoundingBox bb = new BoundingBox(-1.0, -1.0, 1.0, 1.0);
        List<BoundingBox> l = bb.split();
        assertThat("Length", l.size(), is(4));
        assertThat("First bounding box", l.get(0), samePropertyValuesAs(new BoundingBox(-1.0, -1.0, 0.0, 0.0)));
        assertThat("Second bounding box", l.get(1), samePropertyValuesAs(new BoundingBox(0.0, -1.0, 1.0, 0.0)));
        assertThat("Third bounding box", l.get(2), samePropertyValuesAs(new BoundingBox(-1.0, 0.0, 0.0, 1.0)));
        assertThat("Fourth bounding box", l.get(3), samePropertyValuesAs(new BoundingBox(0.0, 0.0, 1.0, 1.0)));
    }
}
