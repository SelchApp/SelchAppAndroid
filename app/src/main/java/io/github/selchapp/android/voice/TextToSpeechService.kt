package io.github.selchapp.android.voice

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech


/**
 * Created by rzetzsche on 01.10.17.
 */
class TextToSpeechService : IntentService("TTS"), TextToSpeech.OnInitListener {
    var isReady: Boolean = false
    override fun onHandleIntent(intent: Intent) {
        val text: CharSequence? = intent.extras?.getCharSequence("TEXT")
        if (text != null)
            if (isReady)
                tts.speak(text, TextToSpeech.QUEUE_FLUSH
                        , null, System.currentTimeMillis().toString())
    }

    lateinit var tts: TextToSpeech
    override fun onInit(status: Int) {
        isReady = true;
    }

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(applicationContext, this)
    }
}