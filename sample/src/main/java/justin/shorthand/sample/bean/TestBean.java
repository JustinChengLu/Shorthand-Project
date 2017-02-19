package justin.shorthand.sample.bean;

import java.util.List;

import justin.shorthand.annotations.JsonAlias;
import justin.shorthand.annotations.JsonObject;

/**
 * @author justin on 2017/02/16 10:32
 * @version V1.0
 */
@JsonObject
public class TestBean {
    @JsonAlias("id")
    private Integer index;

    private boolean te = false;

    private TestBean2 testBean2;

    private List<TestBean3> list;
    private List<String> json;

    public List<String> getJson() {
        return json;
    }

    public void setJson(List<String> json) {
        this.json = json;
    }

    public List<TestBean3> getList() {
        return list;
    }

    public void setList(List<TestBean3> list) {
        this.list = list;
    }

    public TestBean2 getTestBean2() {
        return testBean2;
    }

    public void setTestBean2(TestBean2 testBean2) {
        this.testBean2 = testBean2;
    }

    public boolean isTe() {
        return te;
    }

    public void setTe(boolean te) {
        this.te = te;
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
