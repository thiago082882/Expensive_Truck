package com.thiago.expense_kotlin.views.activities



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.thiago.expense_kotlin.R
import com.thiago.expense_kotlin.databinding.ActivityMainBinding
import com.thiago.expense_kotlin.utils.Constants
import com.thiago.expense_kotlin.viewmodels.MainViewModel
import com.thiago.expense_kotlin.views.fragments.StatsFragment
import com.thiago.expense_kotlin.views.fragments.TransactionsFragment
import java.text.SimpleDateFormat
import java.util.*
import io.realm.Realm
import io.realm.RealmResults

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val calendar = Calendar.getInstance()

    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
    */

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "Transactions"

        Constants.setCategories()

        val calendar = Calendar.getInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, TransactionsFragment())
        transaction.commit()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val transaction = supportFragmentManager.beginTransaction()
            if (item.itemId == R.id.transactions) {
                supportFragmentManager.popBackStack()
            } else if (item.itemId == R.id.stats) {
                transaction.replace(R.id.content, StatsFragment())
                transaction.addToBackStack(null)
            }
            transaction.commit()
            true
        }
    }

    fun getTransactions() {
        viewModel.getTransactions(calendar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
