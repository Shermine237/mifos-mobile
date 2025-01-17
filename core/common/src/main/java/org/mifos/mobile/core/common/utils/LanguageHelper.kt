package org.mifos.mobile.core.common.utils

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import org.mifos.mobile.core.common.R
import java.util.*

/**
 * Created by dilpreet on 02/10/17.
 */
object LanguageHelper {
    // https://gunhansancar.com/change-language-programmatically-in-android/
    fun onAttach(context: Context): Context? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return if (preferences.getBoolean(context.getString(R.string.default_system_language), true)) {
            if(!context.resources.getStringArray(R.array.languages_value).contains(Locale.getDefault().language)){
                setLocale(context, "en")
            }else{
                setLocale(context, Locale.getDefault().language)
            }
        } else {
            val lang = getPersistedData(context, Locale.getDefault().language)
            lang?.let { setLocale(context, it) }
        }
    }

    @JvmStatic
    fun onAttach(context: Context, defaultLanguage: String): Context? {
        val lang = getPersistedData(context, defaultLanguage)
        return lang?.let { setLocale(context, it) }
    }

    fun setLocale(context: Context?, language: String): Context? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context!!, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(context.getString(R.string.language_type), defaultLanguage)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context?, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context?.resources
        val configuration = resources?.configuration
        configuration?.locale = locale
        configuration?.setLayoutDirection(locale)
        resources?.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}
