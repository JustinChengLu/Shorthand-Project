package justin.shorthand.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.json.JSONException;

import justin.shorthand.Shorthand;
import justin.shorthand.sample.bean.Test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String json = "[{\"id\":1},{\"id\":2},{\"id\":3},{\"id\":4},{\"id\":5}]";
//        try {
//            List<TestBean> testBean = Shorthand.parseList(json,TestBean.class);
//            Log.d("justin","justin="+testBean);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String json = " {\n" +
                "            \"city\": \"天津\",\n" +
                "            \"date_y\": \"2014年03月21日\",\n" +
                "            \"week\": \"星期五\",\n" +
                "            \"temperature\": \"8℃~20℃\",\t/*今日温度*/\n" +
                "            \"weather\": \"晴转霾\",\t/*今日天气*/\n" +
                "            \"weather_id\": {\t/*天气唯一标识*/\n" +
                "                \"fa\": \"00\",\t/*天气标识00：晴*/\n" +
                "                \"fb\": \"53\"\t/*天气标识53：霾 如果fa不等于fb，说明是组合天气*/\n" +
                "            }" +
                "}";

        try {
            Test test = Shorthand.parseObject(json, Test.class);
            Log.d("justin", "justin=" + test);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                shorthand();
            }
        }).start();
    }

    private void shorthand() {
        String json = " {\n" +
                "            \"city\": \"天津\",\n" +
                "            \"date_y\": \"2014年03月21日\",\n" +
                "            \"week\": \"星期五\",\n" +
                "            \"temperature\": \"8℃~20℃\",\t/*今日温度*/\n" +
                "            \"weather\": \"晴转霾\",\t/*今日天气*/\n" +
                "            \"weather_id\": {\t/*天气唯一标识*/\n" +
                "                \"fa\": \"00\",\t/*天气标识00：晴*/\n" +
                "                \"fb\": \"53\"\t/*天气标识53：霾 如果fa不等于fb，说明是组合天气*/\n" +
                "            }" +
                "}";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            try {
                Test test = Shorthand.parseObject(json, Test.class);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        long use = end - start;
        Log.d("justin", "justin Shorthand use=" + use);
        start = end;
        Gson gson = new Gson();
        for (int i = 0; i < 10000; i++) {
            Test test = gson.fromJson(json, Test.class);
        }
        end = System.currentTimeMillis();
        use = end - start;
        start = end;
        Log.d("justin", "justin Gson use=" + use);

        for (int i = 0; i < 10000; i++) {
            Test test = JSON.parseObject(json, Test.class);
        }
        end = System.currentTimeMillis();
        use = end - start;
        start = end;
        Log.d("justin", "justin fastjson use=" + use);

    }

}
