package com.thiago.expense_kotlin.views.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.android.material.tabs.TabLayout
import com.thiago.expense_kotlin.R
import com.thiago.expense_kotlin.databinding.FragmentStatsBinding
import com.thiago.expense_kotlin.utils.Constants
import com.thiago.expense_kotlin.utils.Helper
import com.thiago.expense_kotlin.viewmodels.MainViewModel
import java.util.Calendar

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var calendar: Calendar


    /*
    0 = Daily
    1 = Monthly
    */

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        calendar = Calendar.getInstance()
        updateDate()

        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.background = requireContext().getDrawable(R.drawable.income_selector)
            binding.expenseBtn.background = requireContext().getDrawable(R.drawable.default_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor))

            Constants.SELECTED_STATS_TYPE = Constants.INCOME
            updateDate()
        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.background = requireContext().getDrawable(R.drawable.default_selector)
            binding.expenseBtn.background = requireContext().getDrawable(R.drawable.expense_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor))

            Constants.SELECTED_STATS_TYPE = Constants.EXPENSE
            updateDate()
        }

        binding.nextDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1)
            } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1)
            }
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1)
            } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1)
            }
            updateDate()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "Monthly") {
                    Constants.SELECTED_TAB_STATS = 1
                    updateDate()
                } else if (tab.text == "Daily") {
                    Constants.SELECTED_TAB_STATS = 0
                    updateDate()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val pie = AnyChart.pie()

        viewModel.categoriesTransactions.observe(viewLifecycleOwner, Observer { transactions ->
            if (transactions.size > 0) {
                binding.emptyState.visibility = View.GONE
                binding.anyChart.visibility = View.VISIBLE

                val data = ArrayList<DataEntry>()
                val categoryMap = HashMap<String, Double>()

                for (transaction in transactions) {
                    val category: String = transaction.category
                    val amount: Double = transaction.amount
                    if (categoryMap.containsKey(category)) {
                        var currentTotal = categoryMap[category]!!
                        currentTotal += Math.abs(amount)
                        categoryMap[category] = currentTotal
                    } else {
                        categoryMap[category] = Math.abs(amount)
                    }
                }


                for ((key, value) in categoryMap) {
                    data.add(ValueDataEntry(key, value))
                }
                pie.data(data)
            } else {
                binding.emptyState.visibility = View.VISIBLE
                binding.anyChart.visibility = View.GONE
            }
        })

        viewModel.getTransactions(calendar, Constants.SELECTED_STATS_TYPE)

        binding.anyChart.setChart(pie)

        return binding.root
    }

    private fun updateDate() {
        if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            binding.currentDate.text = Helper.formatDate(calendar.time)
        } else if (Constants.SELECTED_TAB_STATS == Constants.MONTHLY) {
            binding.currentDate.text = Helper.formatDateByMonth(calendar.time)
        }
        viewModel.getTransactions(calendar, Constants.SELECTED_STATS_TYPE)
    }
}
