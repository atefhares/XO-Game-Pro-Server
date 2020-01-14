package com.itijavafinalprojectteam8.controller.operations.json;

import com.itijavafinalprojectteam8.model.Player;
import com.itijavafinalprojectteam8.others.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public static String getSignUpConfirmationResponse(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_UP);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_SUCCESS);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, getPlayerJson(player));
        return jsonObject.toString();
    }

    private static String getPlayerJson(Player player) {
        JSONObject playerJson = new JSONObject();
        playerJson.put("id", player.id);
        playerJson.put("email", player.email);
        playerJson.put("name", player.name);
        playerJson.put("status", player.status);
        playerJson.put("points", player.points);
        return playerJson.toString();
    }

    public static String getSignUpErrorResponse(String errorMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_UP);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_ERROR);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, errorMsg);
        return jsonObject.toString();
    }

    public static String getSignInConfirmationResponse(Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_IN);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_SUCCESS);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, getPlayerJson(player));
        return jsonObject.toString();
    }

    public static String getSignInErrorResponse(String errorMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SIGN_IN);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_ERROR);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, errorMsg);
        return jsonObject.toString();
    }

    public static String getPlayersListJson(ArrayList<Player> allPlayers) {
        JSONArray objects = new JSONArray();
        for (Player p : allPlayers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.JsonKeys.KEY_USER_EMAIL, p.email);
            jsonObject.put(Constants.JsonKeys.KEY_USER_NAME, p.name);
            jsonObject.put(Constants.JsonKeys.KEY_USER_STATUS, p.status);
            jsonObject.put(Constants.JsonKeys.KEY_USER_POINTS, p.points);
            objects.put(jsonObject);
        }
        return objects.toString();
    }

    public static String createAllPlayersJsonString(String allPlayersJson) {
        JSONArray allPlayers = new JSONArray(allPlayersJson);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_GET_ALL_PLAYERS);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_SUCCESS);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, allPlayers);
        return jsonObject.toString();
    }
}
