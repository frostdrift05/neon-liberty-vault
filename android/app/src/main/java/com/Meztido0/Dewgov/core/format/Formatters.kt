package com.Meztido0.Dewgov.core.format

import kotlin.math.abs

fun formatChips(value: Int): String {
    val digits = abs(value).toString()
    val grouped = StringBuilder()
    val length = digits.length
    for (i in 0 until length) {
        if (i > 0 && (length - i) % 3 == 0) grouped.append(',')
        grouped.append(digits[i])
    }
    return if (value < 0) "-$grouped" else grouped.toString()
}

fun formatStreak(streakX10: Int): String {
    val whole = streakX10 / 10
    val fraction = streakX10 % 10
    return "$whole.$fraction"
}

fun formatSigned(value: Int): String = if (value > 0) "+$value" else value.toString()
