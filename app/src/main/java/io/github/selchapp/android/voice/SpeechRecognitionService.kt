package io.github.selchapp.android.voice

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.RecognizerIntent
import android.util.Log


/**
 * Created by rzetzsche on 01.10.17.
 */
class SpeechRecognitionService : Service(), RecognitionListener {
    var listener: RecognizeListener? = null

    interface RecognizeListener {
        fun wasRecognized()
    }

    inner class LocalBinder : Binder() {
        fun setListener(listener: RecognizeListener) {
            this@SpeechRecognitionService.listener = listener
        }
    }

    private lateinit var speechRecognizer: SpeechRecognizer
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
        speechRecognizer.setRecognitionListener(this)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizer.startListening(intent)
    }

    override fun onBind(p0: Intent?): IBinder {
        return LocalBinder()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
    }

    override fun onRmsChanged(p0: Float) {
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onPartialResults(p0: Bundle?) {
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(p0: Int) {
    }

    override fun onResults(bundle: Bundle) {
        val data: ArrayList<String> = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        for (i in 0..data.size - 1) {
            Log.d("lol", "result " + data[i])
        }
        if (listener != null) {
            listener!!.wasRecognized()
        }
    }
}