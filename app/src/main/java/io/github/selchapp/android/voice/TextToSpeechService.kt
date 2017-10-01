package io.github.selchapp.android.voice

import android.app.IntentService
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.*


/**
 * Created by rzetzsche on 01.10.17.
 */
class TextToSpeechService : IntentService("TTS"), TextToSpeech.OnInitListener {
    var isReady: Boolean = false
    var text: String = ""
    override fun onHandleIntent(intent: Intent) {
        val text: CharSequence? = intent.getStringExtra("TEXT")
        if (text != null)
            if (isReady)
                speak(text)
            else this.text = text.toString()
    }

    private fun speak(text: CharSequence?) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH
                , null, System.currentTimeMillis().toString())
    }

    lateinit var tts: TextToSpeech
    override fun onInit(status: Int) {
        tts.setLanguage(Locale.US)
        isReady = true;
        if (!text.isEmpty()) {
            speak(text)
            text = ""
        }
    }

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(applicationContext, this)
    }
}