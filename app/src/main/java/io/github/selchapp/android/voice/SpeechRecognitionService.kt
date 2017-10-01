package io.github.selchapp.android.voice

import android.app.Service
import android.content.Intent
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
        TODO()
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
        var str = String()
        val data: ArrayList<String> = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        for (i in 0..data.size - 1) {
            Log.d("lol", "result " + data[i])
        }
        val intent = Intent(applicationContext, TextToSpeechService::class.java)
        intent.putExtra("TEXT", "Und dann kommt dir der Mock hoch")
        startService(intent)
    }
}