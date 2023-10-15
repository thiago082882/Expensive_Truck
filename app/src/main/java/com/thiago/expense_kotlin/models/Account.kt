package com.thiago.expense_kotlin.models

class Account {
    var accountAmount: Double = 0.0
    var accountName: String = ""

    constructor() {}


    constructor(accountAmount: Double, accountName: String) {
        this.accountAmount = accountAmount
        this.accountName = accountName
    }
}
