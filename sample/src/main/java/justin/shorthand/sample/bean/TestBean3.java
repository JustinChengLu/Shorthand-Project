package justin.shorthand.sample.bean;

import justin.shorthand.annotations.Ignore;
import justin.shorthand.annotations.JsonAlias;
import justin.shorthand.annotations.JsonObject;

/**
 * @author justin on 2017/02/16 10:32
 * @version V1.0
 */
@JsonObject
public class TestBean3 {
    @JsonAlias("id")
    private int index;

    @Ignore
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "index=" + index +
                '}';
    }
}
