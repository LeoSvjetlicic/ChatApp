package ls.android.chatapp.common

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreferencesHelper {
    private lateinit var encryptedSharedPreferences: SharedPreferences
    private const val SECURE_SHARED_PREFERENCES_FILE_NAME = "secureSharedPreferences"
    private const val SECURE_SHARED_PREFERENCES_EMAIL = "secureSharedPreferencesEmail"
    private const val SECURE_SHARED_PREFERENCES_PASSWORD = "secureSharedPreferencesPassword"

    fun init(context: Context) {
        val masterKey: MasterKey =
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        encryptedSharedPreferences =
            EncryptedSharedPreferences.create(
                context,
                SECURE_SHARED_PREFERENCES_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
            )
    }

    fun getEmailFromSharedPrefs(): String? {
        return encryptedSharedPreferences.getString(SECURE_SHARED_PREFERENCES_EMAIL, null)
    }

    fun saveEmailToSharedPrefs(email: String) {
        encryptedSharedPreferences.edit()?.putString(SECURE_SHARED_PREFERENCES_EMAIL, email)?.apply()
    }

    fun getPasswordFromSharedPrefs(): String? {
        return encryptedSharedPreferences.getString(SECURE_SHARED_PREFERENCES_PASSWORD, null)
    }

    fun savePasswordToSharedPrefs(password: String) {
        encryptedSharedPreferences.edit()?.putString(SECURE_SHARED_PREFERENCES_PASSWORD, password)?.apply()
    }
}