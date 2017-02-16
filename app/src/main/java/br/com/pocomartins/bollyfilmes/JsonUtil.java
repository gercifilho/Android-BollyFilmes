package br.com.pocomartins.bollyfilmes;

/*{
    "page":1,
    "results":[],
    "total_results":19640,
    "total_pages":982
}*/

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poço Martins on 2/16/2017.
 */

public class JsonUtil {

    public static List<ItemFilme> fromJsonToList(String json) {
        List<ItemFilme> list = new ArrayList<>();
        try {
            JSONObject jsonBase = new JSONObject(json);
            JSONArray results = jsonBase.getJSONArray("results");

            for(int i = 0; i < results.length();i++) {
                JSONObject filmeObject = results.getJSONObject(i);
                ItemFilme itemFilme = new ItemFilme(filmeObject);
                list.add(itemFilme);

             }

            } catch (Exception e) {
              e.printStackTrace();
        }

        return list;

    }
}
