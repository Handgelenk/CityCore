package net.nerux;

import org.json.simple.JSONObject;

/**
 * Created by Ben on 23.05.17.
 */
public interface Serializable {
    static JSONObject serialize() {
        JSONObject ob = new JSONObject();
        return ob;
    }
}
