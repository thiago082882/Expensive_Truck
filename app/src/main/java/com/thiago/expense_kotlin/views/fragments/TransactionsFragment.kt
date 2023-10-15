package com.thiago.expense_kotlin.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.thiago.expense_kotlin.adapters.TransactionsAdapter
import com.thiago.expense_kotlin.databinding.FragmentTransactionsBinding
import com.thiago.expense_kotlin.utils.Constants
import com.thiago.expense_kotlin.utils.Helper
import com.thiago.expense_kotlin.viewmodels.MainViewModel
import java.util.Calendar
import io.realm.RealmResults

class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private lateinit var calendar: Calendar

    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
    */

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        calendar = Calendar.getInstance()
        updateDate()

        binding.nextDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1)
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1)
            }
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1)
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1)
            }
            updateDate()
        }

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment().show(parentFragmentManager, null)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "Monthly") {
                    Constants.SELECTED_TAB = 1
                    updateDate()
                } else if (tab.text == "Daily") {
                    Constants.SELECTED_TAB = 0
                    updateDate()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.transactionsList.layoutManager = LinearLayoutManager(context)

        viewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
            val transactionsAdapter = TransactionsAdapter(requireActivity(), transactions)
            binding.transactionsList.adapter = transactionsAdapter
            if (transactions.size > 0) {
                binding.emptyState.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.VISIBLE
            }
        })

        viewModel.totalIncome.observe(viewLifecycleOwner, Observer { income ->
            binding.incomeLbl.text = income.toString()
        })

        viewModel.totalExpense.observe(viewLifecycleOwner, Observer { expense ->
            binding.expenseLbl.text = expense.toString()
        })

        viewModel.totalAmount.observe(viewLifecycleOwner, Observer { amount ->
            binding.totalLbl.text = amount.toString()
        })
        viewModel.getTransactions(calendar)

        return binding.root
    }

    private fun updateDate() {
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            binding.currentDate.text = Helper.formatDate(calendar.time)
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            binding.currentDate.text = Helper.formatDateByMonth(calendar.time)
        }
        viewModel.getTransactions(calendar)
    }
}