package com.glebalekseevjk.floatify_android.data.temporary

import com.glebalekseevjk.floatify_android.domain.repository.TemporaryRepository
import javax.inject.Inject

class TemporaryRepositoryImpl @Inject constructor(private val temporaryStorage: SharedPreferencesTemporaryStorage) :
    TemporaryRepository {
    override fun getYandexTranslateSessionId(): String? =
        temporaryStorage.getYandexTranslateSessionId()

    override fun setYandexTranslateSessionId(sessionId: String?) =
        temporaryStorage.setYandexTranslateSessionId(sessionId)

    override fun clear() = temporaryStorage.clear()
}