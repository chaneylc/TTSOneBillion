package chaneylc.bug.ttsonebillion

import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import java.util.*

class TextToSpeechHelper(private val context: Context, private val init: (() -> Unit)? = null): TextToSpeech.OnInitListener {

    companion object {

        val TAG = TextToSpeech::class.simpleName

        val availableLocales = setOf(Locale.US, Locale.FRENCH,
            Locale.GERMAN, Locale.ITALIAN, Locale("spa", "ESP"))

        /**
         * This intent should be called to check TTS presence.
         * A successful result will return CHECK_VOICE_DATA_PASS result code.
         * Otherwise, users should use the install tts intent.
         */
        val checkTtsPresenceIntent = Intent().apply {
            action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        }

        /**
         * Navigates user to play store to download TTS
         */
        val installTtsIntent = Intent().apply {
            action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
        }
    }

    val client: TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {

        when(status) {

            TextToSpeech.SUCCESS -> {

                Log.d(TAG, "Success initializing TTS.")

                init?.invoke()
            }

            TextToSpeech.ERROR -> {

                Log.d(TAG, "Error initializing TTS.")
            }

            else -> {

                Log.d(TAG, "Initializing TTS. $status")

            }
        }
    }

    fun speak(text: String) {

        Log.d(TAG, "Speaking $text")

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                client.speak(text, TextToSpeech.QUEUE_FLUSH, null, "test")

            } else {

                client.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun setLanguage(lang: Locale) {

        Log.d(TAG, "Set language $lang")

        if (client.isLanguageAvailable(lang) in setOf(
                TextToSpeech.LANG_COUNTRY_AVAILABLE,
                TextToSpeech.LANG_AVAILABLE)) {
            client.language = lang
        }
    }

    fun setVoice(voice: Voice) {

        Log.d(TAG, "Set voice $voice")

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                client.voice = voice

            }

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }

    fun close() {

        Log.d(TAG, "Closing")

        try {

            client.shutdown()

        } catch (e: Exception) {

            e.printStackTrace()

        }
    }
}