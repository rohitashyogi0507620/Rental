package com.example.mapwork.utils

import android.content.Context;
import android.content.SharedPreferences;


class SessionManager {

    lateinit var componentName: String
    lateinit var context: Context
    lateinit var sharedPreferences: SharedPreferences

    fun setContext(componentName: String, context: Context) {
        this.componentName = componentName
        this.context = context
    }

    fun saveData(query: String?, value: String?) {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(query, value)
        editor.apply()
    }

    fun saveBooleanData(query: String?, value: Boolean) {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(query, value)
        editor.apply()
    }

    fun getData(query: String?): String? {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(query, null)
    }

    fun getBooleanData(query: String?): Boolean {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(query, false)
    }

    fun saveIsFirstTime(query: String?, value: Boolean) {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(query, value)
        editor.apply()
    }

    fun getIsFirstTime(query: String?): Boolean {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(query, true)
    }


    fun saveIsFirstTimeTour(query: String?, value: Boolean) {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(query, value)
        editor.apply()
    }

    fun getIsFirstTimeTour(query: String?): Boolean {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(query, true)
    }

    fun clearSharedPreferences() {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    fun clearUserData() {
        sharedPreferences = context!!.getSharedPreferences(componentName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
