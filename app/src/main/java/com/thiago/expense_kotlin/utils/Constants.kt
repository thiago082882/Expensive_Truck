package com.thiago.expense_kotlin.utils

import com.thiago.expense_kotlin.R
import com.thiago.expense_kotlin.models.Category


object Constants {
    const val INCOME = "INCOME"
    const val EXPENSE = "EXPENSE"

    val categories = ArrayList<Category>()

    const val DAILY = 0
    const val MONTHLY = 1
    const val CALENDAR = 2
    const val SUMMARY = 3
    const val NOTES = 4

    var SELECTED_TAB = DAILY
    var SELECTED_TAB_STATS = DAILY
    var SELECTED_STATS_TYPE = INCOME

    fun setCategories() {
        categories.add(Category("Salary", R.drawable.ic_salary, R.color.category1))
        categories.add(Category("Business", R.drawable.ic_business, R.color.category2))
        categories.add(Category("Investment", R.drawable.ic_investment, R.color.category3))
        categories.add(Category("Loan", R.drawable.ic_loan, R.color.category4))
        categories.add(Category("Rent", R.drawable.ic_rent, R.color.category5))
        categories.add(Category("Other", R.drawable.ic_other, R.color.category6))
    }

    fun getCategoryDetails(categoryName: String): Category? {
        for (cat in categories) {
            if (cat.categoryName == categoryName) {
                return cat
            }
        }
        return null
    }

    fun getAccountsColor(accountName: String): Int {
        return when (accountName) {
            "Bank" -> R.color.bank_color
            "Cash" -> R.color.cash_color
            "Card" -> R.color.card_color
            else -> R.color.default_color
        }
    }
}
