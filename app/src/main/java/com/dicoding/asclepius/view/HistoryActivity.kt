package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "History"

        val historyAdapter = HistoryAdapter()

        historyViewModel.getAllHistory().observe(this) {
            historyAdapter.setData(it)
            if (it.isEmpty()) {
                binding.tvNoHistory.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.tvNoHistory.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE
            }
        }

        with(binding.rvHistory) {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            setHasFixedSize(true)
            adapter = historyAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all) {
            val builder = AlertDialog.Builder(this)
            builder.apply {
                setPositiveButton("Yes") { _, _ ->
                    historyViewModel.deleteAllHistory()
                    Toast.makeText(
                        this@HistoryActivity,
                        getString(R.string.success_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setNegativeButton("No") { _, _ -> }
                setTitle(getString(R.string.delete_all))
                setMessage(getString(R.string.delete_desc))
                create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}