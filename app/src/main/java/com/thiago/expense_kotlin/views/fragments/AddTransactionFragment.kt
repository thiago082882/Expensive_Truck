package com.thiago.expense_kotlin.views.fragments



import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.thiago.expense_kotlin.R
import com.thiago.expense_kotlin.adapters.AccountsAdapter
import com.thiago.expense_kotlin.adapters.CategoryAdapter
import com.thiago.expense_kotlin.databinding.FragmentAddTransactionBinding
import com.thiago.expense_kotlin.databinding.ListDialogBinding
import com.thiago.expense_kotlin.models.Account
import com.thiago.expense_kotlin.models.Category
import com.thiago.expense_kotlin.models.Transaction
import com.thiago.expense_kotlin.utils.Constants
import com.thiago.expense_kotlin.utils.Helper
import com.thiago.expense_kotlin.views.activities.MainActivity
import java.util.Calendar

class AddTransactionFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTransactionBinding
    private val transaction = Transaction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(inflater)

        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.background = requireContext().getDrawable(R.drawable.income_selector)
            binding.expenseBtn.background = requireContext().getDrawable(R.drawable.default_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor))

            transaction.type = Constants.INCOME
        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.background = requireContext().getDrawable(R.drawable.default_selector)
            binding.expenseBtn.background = requireContext().getDrawable(R.drawable.expense_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor))

            transaction.type = Constants.EXPENSE
        }

        binding.date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.YEAR, year)

                    val dateToShow = Helper.formatDate(calendar.time)

                    binding.date.text = Editable.Factory.getInstance().newEditable(dateToShow)

                    transaction.date = calendar.time
                    transaction.id = calendar.time.time
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        binding.category.setOnClickListener {
            val dialogBinding = ListDialogBinding.inflate(inflater)
            val categoryDialog = AlertDialog.Builder(requireContext()).create()
            categoryDialog.setView(dialogBinding.root)

            val categoryAdapter = CategoryAdapter(requireContext(), Constants.categories,
                object : CategoryAdapter.CategoryClickListener {
                    override fun onCategoryClicked(category: Category) {
                        binding.category.text = Editable.Factory.getInstance().newEditable(category.categoryName)
                        transaction.category = category.categoryName
                        categoryDialog.dismiss()
                    }
                }
            )


            dialogBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            dialogBinding.recyclerView.adapter = categoryAdapter

            categoryDialog.show()
        }

        binding.account.setOnClickListener {
            val dialogBinding = ListDialogBinding.inflate(inflater)
            val accountsDialog = AlertDialog.Builder(requireContext()).create()
            accountsDialog.setView(dialogBinding.root)

            val accounts = ArrayList<Account>()
            accounts.add(Account(0.0, "Cash"))
            accounts.add(Account(0.0, "Bank"))
            accounts.add(Account(0.0, "PayTM"))
            accounts.add(Account(0.0, "EasyPaisa"))
            accounts.add(Account(0.0, "Other"))

            val adapter = AccountsAdapter(requireContext(), accounts, object : AccountsAdapter.AccountsClickListener {
                override fun onAccountSelected(account: Account) {
                    binding.account.text = Editable.Factory.getInstance().newEditable(account.accountName)
                    transaction.account = account.accountName
                    accountsDialog.dismiss()
                }
            })



            dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            // dialogBinding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            dialogBinding.recyclerView.adapter = adapter

            accountsDialog.show()
        }

        binding.saveTransactionBtn.setOnClickListener {
            val amount = binding.amount.text.toString().toDouble()
            val note = binding.note.text.toString()

            if (transaction.type == Constants.EXPENSE) {
                transaction.amount = -amount
            } else {
                transaction.amount = amount
            }

            transaction.note = note

            (activity as MainActivity).viewModel.addTransaction(transaction)
            (activity as MainActivity).getTransactions()
            dismiss()
        }

        return binding.root
    }
}
