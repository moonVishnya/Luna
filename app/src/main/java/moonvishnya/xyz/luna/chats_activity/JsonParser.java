package moonvishnya.xyz.luna.chats_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {


    public List<HashMap<String, Object>> parse(JSONObject jObject) {

        JSONArray jMessages = null;
        try {
            jMessages = jObject.getJSONArray("Messages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getMessages(jMessages);
    }

    private List<HashMap<String, Object>> getMessages(JSONArray jMessages) {
        List<HashMap<String, Object>> messagesList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> message = null;

        for (int i = 0; i < jMessages.length(); i++) {
            try {
                message = getMessage((JSONObject) jMessages.get(i));
                messagesList.add(message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return messagesList;
    }



    private HashMap<String, Object> getMessage(JSONObject jMessage) {
        HashMap<String, Object> message = new HashMap<String, Object>();

        String name = "";
        String time = "";
        String content = "";
         String cover = "";
        try {
            name = jMessage.getString("author");
            time = jMessage.getString("time");
            content = jMessage.getString("content");
            cover = jMessage.getString("profilePhoto");

            message.put("cover", cover);
            message.put("name", name);
            message.put("time", time);
            message.put("content", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }



}
