package justin.shorthand;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author justin on 2017/02/16 10:43
 * @version V1.0
 */
public interface IJsonParse {
    Object parse(JSONObject jsonObject) throws JSONException;
}
