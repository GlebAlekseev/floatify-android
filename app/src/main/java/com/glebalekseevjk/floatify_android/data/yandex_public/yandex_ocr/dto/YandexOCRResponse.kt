package com.glebalekseevjk.floatify_android.data.yandex_public.yandex_ocr.dto

import com.google.gson.annotations.SerializedName


data class YandexOCRResponse(
    val status: String,
    val data: Data,
    @SerializedName("request_id") val requestId: String
) {
    companion object {
        data class Data(
            val detectedLang: String,
            val rotate: Int,
            val blocks: List<Block>
        )

        data class Block(
            val angle: Int,
            val x: Int,
            val y: Int,
            val w: Int,
            val h: Int,
            val rx: Int,
            val ry: Int,
            val rw: Int,
            val rh: Int,
            val boxes: List<Box>,
        )

        data class Box(
            val x: Int,
            val y: Int,
            val w: Int,
            val h: Int,
            val backgroundColor: BackgroundColor,
            val textColor: TextColor,
            val polyCoefs: PolyCoefs,
            val text: String,
            val words: List<Word>,
        )

        data class BackgroundColor(
            val a: Int,
            val b: Int,
            val r: Int,
            val x: Int,
            val y: Int,
            val g: Int,
            @SerializedName("Points") val points: List<Point>,
        )

        data class Point(
            val x: Int,
            val y: Int,
        )

        data class TextColor(
            val a: Int,
            val b: Int,
            val r: Int,
            val x: Int,
            val y: Int,
            val g: Int,
            @SerializedName("Points") val points: List<Point>,
        )

        data class PolyCoefs(
            val a3: Double,
            val a2: Double,
            val a1: Double,
            val a0: Double,
            val fromXtoY: Boolean,
            val hh: Double,
        )

        data class Word(
            val x: Int,
            val y: Int,
            val w: Int,
            val h: Int,
            val polyCoefs: PolyCoefs,
            val text: String,
        )
    }
}

