package com.thiago.expense_kotlin.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.thiago.expense_kotlin.models.Transaction
import com.thiago.expense_kotlin.utils.Constants
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val transactions = MutableLiveData<RealmResults<Transaction>>()
    val categoriesTransactions = MutableLiveData<RealmResults<Transaction>>()
    val totalIncome = MutableLiveData<Double>()
    val totalExpense = MutableLiveData<Double>()
    val totalAmount = MutableLiveData<Double>()
    private var realm: Realm? = null
    private var calendar: Calendar? = null

    init {
        Realm.init(application)
        setupDatabase()
    }

    fun getTransactions(calendar: Calendar, type: String) {
        this.calendar = calendar
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var newTransactions: RealmResults<Transaction>? = null
        if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            newTransactions = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", calendar.time)
                .lessThan("date", Date(calendar.time.time + 24 * 60 * 60 * 1000))
                .equalTo("type", type)
                .findAll()
        } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH, 0)
            val startTime = calendar.time
            calendar.add(Calendar.MONTH, 1)
            val endTime = calendar.time
            newTransactions = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", startTime)
                .lessThan("date", endTime)
                .equalTo("type", type)
                .findAll()
        }
        categoriesTransactions.value = newTransactions
    }

    fun getTransactions(calendar: Calendar) {
        this.calendar = calendar
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var income = 0.0
        var expense = 0.0
        var total = 0.0
        var newTransactions: RealmResults<Transaction>? = null
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            newTransactions = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", calendar.time)
                .lessThan("date", Date(calendar.time.time + 24 * 60 * 60 * 1000))
                .findAll()
            income = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", calendar.time)
                .lessThan("date", Date(calendar.time.time + 24 * 60 * 60 * 1000))
                .equalTo("type", Constants.INCOME)
                .sum("amount")
                .toDouble()
            expense = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", calendar.time)
                .lessThan("date", Date(calendar.time.time + 24 * 60 * 60 * 1000))
                .equalTo("type", Constants.EXPENSE)
                .sum("amount")
                .toDouble()
            total = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", calendar.time)
                .lessThan("date", Date(calendar.time.time + 24 * 60 * 60 * 1000))
                .sum("amount")
                .toDouble()
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH, 0)
            val startTime = calendar.time
            calendar.add(Calendar.MONTH, 1)
            val endTime = calendar.time
            newTransactions = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", startTime)
                .lessThan("date", endTime)
                .findAll()
            income = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", startTime)
                .lessThan("date", endTime)
                .equalTo("type", Constants.INCOME)
                .sum("amount")
                .toDouble()
            expense = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", startTime)
                .lessThan("date", endTime)
                .equalTo("type", Constants.EXPENSE)
                .sum("amount")
                .toDouble()
            total = realm!!.where(Transaction::class.java)
                .greaterThanOrEqualTo("date", startTime)
                .lessThan("date", endTime)
                .sum("amount")
                .toDouble()
        }
        totalIncome.value = income
        totalExpense.value = expense
        totalAmount.value = total
        transactions.value = newTransactions
    }

    fun addTransaction(transaction: Transaction) {
        realm!!.beginTransaction()
        realm!!.copyToRealmOrUpdate(transaction)
        realm!!.commitTransaction()
    }

    fun deleteTransaction(transaction: Transaction) {
        realm!!.beginTransaction()
        transaction.deleteFromRealm()
        realm!!.commitTransaction()
        getTransactions(calendar!!)
    }

    fun addTransactions() {
        realm!!.beginTransaction()
        realm!!.copyToRealmOrUpdate(
            Transaction(
                Constants.INCOME,
                "Business",
                "Cash",
                "Some note here",
                Date(),
                500.0,
                Date().time
            )
        )
        realm!!.copyToRealmOrUpdate(
            Transaction(
                Constants.EXPENSE,
                "Investment",
                "Bank",
                "Some note here",
                Date(),
                -900.0,
                Date().time
            )
        )
        realm!!.copyToRealmOrUpdate(
            Transaction(
                Constants.INCOME,
                "Rent",
                "Other",
                "Some note here",
                Date(),
                500.0,
                Date().time
            )
        )
        realm!!.copyToRealmOrUpdate(
            Transaction(
                Constants.INCOME,
                "Business",
                "Card",
                "Some note here",
                Date(),
                500.0,
                Date().time
            )
        )
        realm!!.commitTransaction()
    }

    private fun setupDatabase() {
        realm = Realm.getDefaultInstance()
    }
}
