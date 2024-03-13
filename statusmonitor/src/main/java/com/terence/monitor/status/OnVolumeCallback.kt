package com.terence.monitor.status

import android.view.KeyEvent

/**
 *   Created by Terence.J.Tang on 2024/3/12
 */
interface OnVolumeCallback {

    /**
     * @param streamType
     * [android.media.AudioManager.STREAM_MUSIC]
     * [android.media.AudioManager.STREAM_ALARM]
     * [android.media.AudioManager.STREAM_RING]
     * [android.media.AudioManager.STREAM_NOTIFICATION]
     * [android.media.AudioManager.STREAM_SYSTEM]
     * [android.media.AudioManager.STREAM_VOICE_CALL]
     * [android.media.AudioManager.STREAM_DTMF]
     * [android.media.AudioManager.STREAM_ACCESSIBILITY]
     *
     * @param volume volume value
     */
    fun onVolumeChanged(streamType: Int, volume: Int)
}