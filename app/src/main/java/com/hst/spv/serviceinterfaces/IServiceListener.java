package com.hst.spv.serviceinterfaces;

import org.json.JSONObject;

/**
 * Created by Admin on 15-09-2020.
 */

public interface IServiceListener {

    void onResponse(JSONObject response);

    void onError(String error);
}
