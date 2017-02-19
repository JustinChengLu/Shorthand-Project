package justin.shorthand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author justin on 2017/02/16 10:29
 * @version V1.0
 */
@SuppressWarnings("unchecked")
public class Shorthand {
    private static HashMap<Class<?>, IJsonParse> mParses = new HashMap<>();

    public static <T> T parseObject(String json, Class<T> tClass) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return parseObject(jsonObject, tClass);
    }

    public static <T> T parseObject(JSONObject json, Class<T> tClass) throws JSONException {
        IJsonParse parse = getParse(tClass);
        if (parse == null) {
            throw new JSONException("can not find json parse");
        }
        return (T) parse.parse(json);
    }

    public static <T> List<T> parseList(String json, Class<T> tClass) throws JSONException {
        JSONArray jsonObject = new JSONArray(json);
        return parseList(jsonObject, tClass);
    }

    public static <T> List<T> parseList(JSONArray jsonArray, Class<T> tClass) throws JSONException {
        if (tClass.isPrimitive() || tClass.equals(String.class)) {
            return parsePrimitiveList(jsonArray, tClass);
        }
        IJsonParse parse = getParse(tClass);
        if (parse == null) {
            throw new JSONException("can not find json parse");
        }
        int size = jsonArray.length();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add((T) parse.parse(jsonObject));
        }
        return list;
    }

    private static <T> List<T> parsePrimitiveList(JSONArray jsonArray, Class<T> tClass) throws JSONException {
        int size = jsonArray.length();

        switch (tClass.getSimpleName()) {
            case "Integer": {
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(jsonArray.optInt(i));
                }
                return (List<T>) list;
            }
            case "Boolean": {
                List<Boolean> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(jsonArray.optBoolean(i));
                }
                return (List<T>) list;
            }
            case "Double": {
                List<Double> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(jsonArray.optDouble(i));
                }
                return (List<T>) list;
            }
            case "Long": {
                List<Long> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(jsonArray.optLong(i));
                }
                return (List<T>) list;
            }
            case "String": {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(jsonArray.optString(i));
                }
                return (List<T>) list;
            }
        }

        return null;
    }

    private static synchronized <T> IJsonParse getParse(Class<T> tClass) {
        IJsonParse parse = mParses.get(tClass);
        if (parse == null) {
            try {
                Class<IJsonParse> jsonParseClass = (Class<IJsonParse>) Class.forName(tClass.getName() + "$Parser");
                parse = jsonParseClass.newInstance();
                mParses.put(tClass, parse);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        return parse;
    }

}
