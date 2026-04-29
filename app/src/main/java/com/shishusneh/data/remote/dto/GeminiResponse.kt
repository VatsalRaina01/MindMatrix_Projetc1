package com.shishusneh.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<Candidate>? = null
) {
    @JsonClass(generateAdapter = true)
    data class Candidate(
        val content: Content? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Content(
            val parts: List<Part>? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class Part(
                val text: String? = null
            )
        }
    }
}
