package com.akgarg.whatsappuiclone.utils

import android.content.Context
import com.akgarg.whatsappuiclone.enums.SharedPreferenceConstants

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

            return sharedPreference.getInt(key, Int.MAX_VALUE)
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
    }

}