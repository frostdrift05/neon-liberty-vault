package com.Meztido0.Dewgov.core.ui

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.domain.model.JobType

fun jobIconRes(type: JobType): Int = when (type) {
    JobType.WHEELS -> R.drawable.ic_wheels
    JobType.WIRES -> R.drawable.ic_wires
    JobType.MUSCLE -> R.drawable.ic_muscle
    JobType.WORDS -> R.drawable.ic_words
    JobType.VAULT -> R.drawable.ic_lock
}

fun jobLabelRes(type: JobType): Int = when (type) {
    JobType.WHEELS -> R.string.job_wheels
    JobType.WIRES -> R.string.job_wires
    JobType.MUSCLE -> R.string.job_muscle
    JobType.WORDS -> R.string.job_words
    JobType.VAULT -> R.string.job_vault
}
