package com.akgarg.whatsappuiclone.utils

import android.content.Context
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants

@Suppress("unused")
class SharedPreferenceUtil {

    companion object {

        fun getStringPreference(context: Context, key: String): String? {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            return sharedPreference.getString(key, null)
        }


        fun getIntegerPreference(context: Context, key: String): Int {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            return sharedPreference.getInt(key, 0)
        }


        fun getBooleanPreference(context: Context, key: String): Boolean {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            return sharedPreference.getBoolean(key, false)
        }


        fun setStringPreference(context: Context, key: String, value: String) {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreference.edit()
            editor.putString(key, value)
            editor.apply()
        }


        fun setBooleanPreference(context: Context, key: String, value: Boolean) {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreference.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }


        fun setIntegerPreference(context: Context, key: String, value: Int) {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreference.edit()
            editor.putInt(key, value)
            editor.apply()
        }


        fun clearAllSharedPreferences(context: Context) {
            val sharedPreference = context.getSharedPreferences(
                SharedPreferenceConstants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE
            )

            val editor = sharedPreference.edit()
            editor.clear()
            editor.apply()
        }

    }

}