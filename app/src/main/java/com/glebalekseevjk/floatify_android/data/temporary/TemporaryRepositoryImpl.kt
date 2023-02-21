package com.glebalekseevjk.floatify_android.data.temporary

import com.glebalekseevjk.floatify_android.domain.repository.TemporaryRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TemporaryRepositoryImpl @Inject constructor(private val temporaryStorage: SharedPreferencesTemporaryStorage) :
    TemporaryRepository {
    override suspend fun getYandexTranslateSessionId(): String? = with(Dispatchers.IO) {
        return@with temporaryStorage.getYandexTranslateSessionId()
    }

    override suspend fun setYandexTranslateSessionId(sessionId: String) = with(Dispatchers.IO) {
        return@with temporaryStorage.setYandexTranslateSessionId(sessionId)
    }

    override suspend fun clear() = with(Dispatchers.IO) {
        temporaryStorage.clear()
    }

}