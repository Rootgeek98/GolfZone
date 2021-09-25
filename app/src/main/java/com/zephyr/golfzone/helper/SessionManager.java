package com.zephyr.golfzone.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zephyr.golfzone.model.User;

/**
 * Contains the code that controls or manages the user sessions in the app,
 * to determine whether the user is logged in or not.
 *
 * @author Bill Glinton
 * @version 1.0
 */

public class SessionManager {

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "GolfZone";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    // Shared Preferences Keys
    private static final String KEY_UNIQUE_USER_ID = "unique_user_id";

    private static final String KEY_FIRSTNAME = "firstname";

    private static final String KEY_LASTNAME = "lastname";

    private static final String KEY_EMAIL = "email";

    private static final String KEY_CREATED_AT = "created_at";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void storeUser(User user) {
        editor.putString(KEY_UNIQUE_USER_ID, user.getUnique_user_id());
        editor.putString(KEY_FIRSTNAME, user.getFirstname());
        editor.putString(KEY_LASTNAME, user.getLastname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_CREATED_AT, user.getCreated_at());
        editor.commit();

        Log.d(TAG, "User is stored in shared preferences. "
                + user.getUnique_user_id() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_UNIQUE_USER_ID, null) != null) {
            String unique_user_id, firstname, lastname, email, created_at;
            unique_user_id = pref.getString(KEY_UNIQUE_USER_ID, null);
            firstname = pref.getString(KEY_FIRSTNAME, null);
            lastname = pref.getString(KEY_LASTNAME, null);
            email = pref.getString(KEY_EMAIL, null);
            created_at = pref.getString(KEY_CREATED_AT, null);

            return new User(unique_user_id, firstname, lastname, email, created_at);
        }

        return null;
    }

    public void clear() {
        editor.clear();
        editor.commit();

        Log.d(TAG, "User is deleted from shared preferences.");
    }

}
