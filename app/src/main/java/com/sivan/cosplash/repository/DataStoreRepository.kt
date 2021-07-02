package com.sivan.cosplash.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.sivan.cosplash.datastore.data.FilterOptions
import com.sivan.cosplash.repository.DataStoreRepository.PreferenceKeys.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    val preference = context.dataStore
    val preferencesFlow = preference.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val query = preferences[PreferenceKeys.query] ?: ""

            val sort_by = preferences[PreferenceKeys.sort_by] ?: ""
            val color = preferences[PreferenceKeys.color] ?: ""
            val orientation = preferences[PreferenceKeys.orientation] ?: ""
            val content_filter = preferences[PreferenceKeys.content_filter] ?: ""

            FilterOptions(query, sort_by, color, orientation, content_filter)
        }

//    suspend fun updateFilterOptions(filterOptions: FilterOptions) {
//        preference.edit { preference ->
//            preference[PreferenceKeys.query] = filterOptions.query
//            preference[PreferenceKeys.sort_by] = filterOptions.sort_by
//
//            preference[PreferenceKeys.color] = filterOptions.color
//            preference[PreferenceKeys.orientation] = filterOptions.orientation
//
//            preference[PreferenceKeys.content_filter] = filterOptions.content_filter
//
//        }
//    }

    suspend fun updateQuery(query_text : String) {
        preference.edit { preference ->
            preference[PreferenceKeys.query] = query_text
        }
    }

    suspend fun updateSortBy(text: String) {
        preference.edit { preference ->
            preference[PreferenceKeys.sort_by] = text
        }
    }

    suspend fun updateColor(text: String) {
        preference.edit { preference ->
            preference[PreferenceKeys.color] = text
        }
    }

    suspend fun updateOrientation(text: String) {
        preference.edit { preference ->
            preference[PreferenceKeys.orientation] = text
        }
    }

    suspend fun updateContentFilter(text: String) {
        preference.edit { preference ->
            preference[PreferenceKeys.content_filter] = text
        }
    }

    private object PreferenceKeys {
        const val PREFERENCE_NAME = "cosplash_preference"
        val query = stringPreferencesKey("query_key")
        val sort_by = stringPreferencesKey("sortby_key")
        val color = stringPreferencesKey("color_key")
        val orientation = stringPreferencesKey("orientation_key")
        val content_filter = stringPreferencesKey("contentfilter_key")
    }
}