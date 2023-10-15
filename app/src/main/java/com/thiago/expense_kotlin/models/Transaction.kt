package com.thiago.expense_kotlin.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class Transaction : RealmObject {
    var type: String? = null
    var category: String = ""
    var account: String? = null
    var note: String? = null
    var date: Date? = null
    var amount = 0.0

    @PrimaryKey
    var id: Long = 0

    constructor()
    constructor(
        type: String?,
        category: String,
        account: String?,
        note: String?,
        date: Date?,
        amount: Double,
        id: Long
    ) {
        this.type = type
        this.category = category
        this.account = account
        this.note = note
        this.date = date
        this.amount = amount
        this.id = id
    }
}