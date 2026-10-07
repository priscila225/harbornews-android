package com.harbornews.news.ui

import android.content.Context

class ReadLaterStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isSaved(id: String): Boolean = prefs.getStringSet(KEY_IDS, emptySet()).orEmpty().contains(id)

    /** Returns true when the article is saved after the toggle. */
    fun toggle(id: String): Boolean {
        val ids = prefs.getStringSet(KEY_IDS, emptySet()).orEmpty().toMutableSet()
        val saved = ids.add(id)
        if (!saved) ids.remove(id)
        prefs.edit().putStringSet(KEY_IDS, ids).apply()
        return saved
    }

    private companion object {
        const val PREFS_NAME = "read_later"
        const val KEY_IDS = "ids"
    }
}
