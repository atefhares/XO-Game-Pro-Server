package com.itijavafinalprojectteam8.controller.operations.json;

import com.itijavafinalprojectteam8.others.Constants;
import org.json.JSONObject;

import java.util.Vector;

public class JsonOperations {

    public static String getRequestType(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject.optString(Constants.JsonKeys.KEY_REQUEST_TYPE);
    }

    public static Vector<String> getSignUpData(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        Vector<String> data = new Vector<>(3);
        data.add(jsonObject.optString(Constants.JsonKeys.KEY_USER_NAME));
        data.add(jsonObject.optString(Constants.JsonKeys.KEY_USER_EMAIL));
        data.add(jsonObject.optString(Constants.JsonKeys.KEY_USER_PASSWORD));
        return data;
    }

    public static Vector<String> getSignInData(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        Vector<String> data = new Vector<>(2);
        data.add(jsonObject.optString(Constants.JsonKeys.KEY_USER_EMAIL));
        data.add(jsonObject.optString(Constants.JsonKeys.KEY_USER_PASSWORD));
        return data;
    }

    public static String getSignUpConfirmationResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_UP);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_SUCCESS);
        return jsonObject.toString();
    }

    public static String getSignUpErrorResponse(String errorMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_UP);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_ERROR);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, errorMsg);
        return jsonObject.toString();
    }

    public static String getSignInConfirmationResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.ConnectionTypes.TYPE_SIGN_IN, Constants.ResponseCodes.RESPONSE_SUCCESS);
        return jsonObject.toString();
    }

    public static String getSignInErrorResponse(String errorMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_IN);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_ERROR);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, errorMsg);
        return jsonObject.toString();
    }
}
