package com.harbornews.common.ads

interface AdsListener {
    fun onAdLoaded()
}

/** Process-wide ads coordinator. Screens attach while visible and must detach when they stop. */
class AdsManager {
    private var listener: AdsListener? = null

    fun attach(listener: AdsListener) {
        this.listener = listener
    }

    fun detach() {
        listener = null
    }

    fun notifyAdLoaded() {
        listener?.onAdLoaded()
    }
}
