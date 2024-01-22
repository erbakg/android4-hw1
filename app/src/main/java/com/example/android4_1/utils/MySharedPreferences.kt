package com.example.android4_1.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.android4_1.ui.note.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MySharedPreferences(val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(CONSTANTS.APP_DATA, Context.MODE_PRIVATE)

    fun saveNotes(list: List<Note>) {
        val convertedData = Gson().toJson(list)
        sharedPreferences
            .edit()
            .putString(CONSTANTS.NOTES_LIST, convertedData)
            .apply()
    }

    fun getSavedNotes(): List<Note>? {
        val savedNotes = sharedPreferences.getString(CONSTANTS.NOTES_LIST, "default")
        val type = object : TypeToken<List<Note>>() {}.type

        return if (savedNotes != "default") {
            Gson().fromJson(savedNotes, type);
        } else {
            null
        }
    }

    fun saveName(name: String) {
        sharedPreferences
            .edit()
            .putString(CONSTANTS.PROFILE_NAME, name)
            .apply()
    }

    fun getSavedName(): String? {
        val savedName = sharedPreferences.getString(CONSTANTS.PROFILE_NAME, "default")

        return if (savedName != "default") {
            savedName
        } else {
            null
        }
    }
    fun saveLogin(login: String) {
        sharedPreferences
            .edit()
            .putString(CONSTANTS.PROFILE_LOGIN, login)
            .apply()
    }

    fun getSavedLogin(): String? {
        val savedLogin = sharedPreferences.getString(CONSTANTS.PROFILE_LOGIN, "default")

        return if (savedLogin != "default") {
            savedLogin
        } else {
            null
        }
    }
    fun saveAvatar(url: String) {
        sharedPreferences
            .edit()
            .putString(CONSTANTS.PROFILE_AVATAR, url)
            .apply()
    }

    fun getSavedAvatar(): String? {
        val savedAvatar = sharedPreferences.getString(CONSTANTS.PROFILE_AVATAR, "default")

        return if (savedAvatar!= "default") {
            savedAvatar
        } else {
            null
        }
    }
}