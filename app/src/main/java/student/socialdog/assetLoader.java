package student.socialdog;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class assetLoader {
    private static Context mContext = null;

    /**
     * Returns a orj.json.JSONObject from the app's context and a filename
     * @param context The app's context, most of the times, typing "this" from an Activity is OK
     * @param filename Name of the JSON file to load
     * @return JSONObject Return a JSONObject representation of the JSON file
     */
    public static JSONObject JSON(Context context,String filename) {
        String jsonString = null;
        try {
            InputStream is = context.getAssets().open(filename);
            mContext = context;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            return new JSONObject(jsonString);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns an ArrayList from a JSONObject
     * @param jsonObject JSONOject containing the desired JSONArray
     * @param arrayName Name of the desired JSONArray in jsonObject
     * @return ArrayList<JSONObject> Return a list of JSONObjects
     */
    public static ArrayList<JSONObject> getJSONArray(JSONObject jsonObject,String arrayName){
        ArrayList<JSONObject> arrayData = new ArrayList<>();
        JSONArray itemsList;
        try{
            itemsList = jsonObject.getJSONArray(arrayName);
            for(int i = 0 ; i < itemsList.length();i++){
                arrayData.add(itemsList.getJSONObject(i));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return arrayData;
    }

    /**
     * Returns an ArrayList of FriendAdapter.FriendsObject
     * @return ArrayList<FriendAdapter.FriendsObject> Return a list of FriendAdapter.FriendsObject
     */
    public static ArrayList<FriendAdapter.FriendsObject> getFriends(ArrayList<HashMap> friendsInDB){
        return getFriends(mContext,friendsInDB);
    }

    /**
     * Returns an ArrayList of FriendAdapter.FriendsObject
     * @param pcontext Context to use to load JSON file. Might be usefull if mContext from assetLoader isn't initialized yet
     * @return ArrayList<FriendAdapter.FriendsObject> Return a list of FriendAdapter.FriendsObject
     */

    public static ArrayList<FriendAdapter.FriendsObject> getFriends(Context pcontext,ArrayList<HashMap> friendsInDB) {
        ArrayList<FriendAdapter.FriendsObject> friendslist = new ArrayList<>();
        for(int i=0; i<friendsInDB.size(); i++) {
            try{
                Object lastWalk = friendsInDB.get(i).get("lastWalk");
                Object name = friendsInDB.get(i).get("name");
                Object ppic = friendsInDB.get(i).get("ppic");
                int imgResID = getResIDfromImageName(ppic.toString(),pcontext);
                friendslist.add(new FriendAdapter.FriendsObject((String) name,(String) lastWalk, imgResID));
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return friendslist;
    }

    /**
     * Returns ressource ID for an image as an "int"
     * @param imageName Name of the image ressource to load.
     * @return int Return the ressource ID of image named "imageName" as an "int"
     */
    public static int getResIDfromImageName(String imageName){
        int result = getResIDfromImageName(imageName,mContext);
        if(result == -1){
            Log.e("assetLoader","Context isn't loaded yet, assetLoader.JSON or getResIDfromImageName(String imageName,Context context) to load context !");
        }
        return -1;
    }

    /**
     * Returns ressource ID for an image as an "int"
     * @param imageName Name of the image ressource to load.
     * @param context Context from which to load the ressources
     * @return int Return the ressource ID of image named "imageName" as an "int"
     */
    private static int getResIDfromImageName(String imageName,Context context){
        if(context!=null){
            return context.getResources().getIdentifier(imageName , "drawable", context.getPackageName());
        }
        Log.e("assetLoader","Context parameter isn't correct.");
        return -1;
    }
}
