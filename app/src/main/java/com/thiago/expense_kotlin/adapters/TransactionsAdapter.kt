package com.thiago.expense_kotlin.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thiago.expense_kotlin.R

import com.thiago.expense_kotlin.databinding.RowTransactionBinding
import com.thiago.expense_kotlin.models.Transaction
import com.thiago.expense_kotlin.utils.Constants
import com.thiago.expense_kotlin.utils.Helper
import com.thiago.expense_kotlin.views.activities.MainActivity
import io.realm.RealmResults

class TransactionsAdapter(
    private val context: Context,
    private val transactions: RealmResults<Transaction>
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.binding.transactionAmount.text = transaction!!.amount.toString()
        holder.binding.accountLbl.text = transaction!!.account
        holder.binding.transactionDate.text = Helper.formatDate(transaction.date)
        holder.binding.transactionCategory.text = transaction.category

        val transactionCategory = Constants.getCategoryDetails(transaction.category.toString())

        holder.binding.categoryIcon.setImageResource(transactionCategory!!.categoryImage)
        holder.binding.categoryIcon.backgroundTintList = context.getColorStateList(transactionCategory.categoryColor)

        holder.binding.accountLbl.backgroundTintList = context.getColorStateList(Constants.getAccountsColor(
            transaction.account.toString()
        ))

        if (transaction.type == Constants.INCOME) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor))
        } else if (transaction.type == Constants.EXPENSE) {
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor))
        }

        holder.itemView.setOnLongClickListener {
            val deleteDialog = AlertDialog.Builder(context).create()
            deleteDialog.setTitle("Delete Transaction")
            deleteDialog.setMessage("Are you sure to delete this transaction?")
            deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes") { _, _ ->
                (context as MainActivity).viewModel.deleteTransaction(transaction)
            }
            deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No") { _, _ ->
                deleteDialog.dismiss()
            }
            deleteDialog.show()
            false
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: RowTransactionBinding = RowTransactionBinding.bind(itemView)
    }
}