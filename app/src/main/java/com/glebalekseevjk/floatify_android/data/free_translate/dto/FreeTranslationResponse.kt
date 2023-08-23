package com.glebalekseevjk.floatify_android.data.free_translate.dto

import com.google.gson.annotations.SerializedName

data class FreeTranslationResponse(
    @SerializedName("source-language") val sourceLanguage: String,
    @SerializedName("source-text") val sourceText: String,
    @SerializedName("destination-language") val destinationLanguage: String,
    @SerializedName("destination-text") val destinationText: String,
    val pronunciation: Pronunciation,
    val translations: Translations?,
    val definitions: List<Definition>?,
    @SerializedName("see-also") val seeAlso: List<List<String>>?
)

data class Pronunciation(
    @SerializedName("source-text-phonetic") val sourceTextPhonetic: String,
    @SerializedName("source-text-audio") val sourceTextAudio: String,
    @SerializedName("destination-text-audio") val destinationTextAudio: String
)

data class Translations(
    @SerializedName("all-translations") val allTranslations: Any?,
    @SerializedName("possible-translations") val possibleTranslations: Any?,
    @SerializedName("possible-mistakes") val possibleMistakes: Any?
)

data class Definition(
    @SerializedName("part-of-speech") val partOfSpeech: String,
    val definition: String,
    val example: String,
    @SerializedName("other-examples") val otherExamples: List<String>,
    val synonyms: Map<String, List<String>>
)
