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

    private static JSONObject getPlayerJson(Player player) {
        JSONObject playerJson = new JSONObject();
        playerJson.put(Constants.JsonKeys.KEY_USER_EMAIL, player.email);
        playerJson.put(Constants.JsonKeys.KEY_USER_NAME, player.name);
        playerJson.put(Constants.JsonKeys.KEY_USER_STATUS, player.status);
        playerJson.put(Constants.JsonKeys.KEY_USER_POINTS, player.points);
        return playerJson;
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

    public static String getOtherPlayerEmail(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject.optString(Constants.JsonKeys.KEY_USER_EMAIL);
    }

    public static String getSendInvitationJson(String email) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SEND_INVITATION);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_SUCCESS);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, email);
        return jsonObject.toString();
    }

    public static String getSendInvitationError(String errorMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_SEND_INVITATION);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_ERROR);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, errorMsg);
        return jsonObject.toString();
    }

    public static String getInvitationResponseJson(String email, boolean result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_INVITATION_RESULT);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_CODE, Constants.ResponseCodes.RESPONSE_SUCCESS);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put(Constants.JsonKeys.KEY_USER_EMAIL, email);
        jsonObject1.put(Constants.JsonKeys.KEY_INVITATION_RESULT, result);
        jsonObject.put(Constants.JsonKeys.KEY_RESPONSE_MSG, jsonObject1);

        return jsonObject.toString();
    }

    public static boolean parseInvitationResult(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getBoolean(Constants.JsonKeys.KEY_INVITATION_RESULT);
    }

    public static int getGameCord(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        return jsonObject.optInt(Constants.JsonKeys.KEY_GAME_CORD);
    }

    public static String getGameCommunicationJson(String email, int cord) {
        JSONObject object = new JSONObject();
        object.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_GAME);
        //object.put(Constants.JsonKeys.KEY_USER_EMAIL, email);
        object.put(Constants.JsonKeys.KEY_GAME_CORD, cord);
        return object.toString();
    }

    public static String parseGameStateStr(String jsonStr) {
        JSONObject object = new JSONObject(jsonStr);
        return object.getString(Constants.JsonKeys.KEY_GAME_STATE);
    }

    public static String createGamePausedJson() {
        JSONObject object = new JSONObject();
        object.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_PAUSE_GAME);
        return object.toString();
    }

    public static String createThereIsOldGameJson(String otherPlayerEmail, String gameState) {
        JSONObject object = new JSONObject();
        object.put(Constants.JsonKeys.KEY_RESPONSE_TYPE, Constants.ConnectionTypes.TYPE_RESUME_GAME);
        object.put(Constants.JsonKeys.KEY_USER_EMAIL, otherPlayerEmail);
        object.put(Constants.JsonKeys.KEY_GAME_STATE, gameState);
        return object.toString();
    }
}
