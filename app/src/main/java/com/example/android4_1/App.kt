package com.example.android4_1

import android.app.Application
import com.example.android4_1.data.NoteManager
import com.example.android4_1.utils.MySharedPreferences

class App : Application() {

    var mySharedPreferense: MySharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        mySharedPreferense = MySharedPreferences(this)
        NoteManager.init(this)

    }

    companion object {

    }
}