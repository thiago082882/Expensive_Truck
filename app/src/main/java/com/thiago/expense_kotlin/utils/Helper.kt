package com.thiago.expense_kotlin.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Helper {
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy")
        return dateFormat.format(date!!)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateByMonth(date: Date?): String {
        val dateFormat = SimpleDateFormat("MMMM, yyyy")
        return dateFormat.format(date!!)
    }
}