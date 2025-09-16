package com.kcbiswash.course1.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.kcbiswash.course1.data.Result
import com.kcbiswash.course1.databinding.ActivityDashboardBinding
import com.kcbiswash.course1.ui.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    private val items = mutableListOf<JsonObject>()
    private lateinit var adapter: EntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar back (Up)
        binding.toolbar.setNavigationOnClickListener {
            finish() // go back to Login
        }

        // RecyclerView
        adapter = EntityAdapter(items) { item -> openDetails(item) }
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter

        val keypass = intent.getStringExtra("keypass")
        if (keypass.isNullOrBlank()) {
            showLoading(false, "Missing keypass from Login")
            return
        }

        // Observe state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is Result.Loading -> showLoading(true, "Loading…")
                        is Result.Success -> {
                            items.clear()
                            items.addAll(state.data.entities)
                            adapter.notifyDataSetChanged()
                            showLoading(false, "Total: ${state.data.entityTotal}")
                        }
                        is Result.Error -> showLoading(false, "Error: ${state.message}")
                        else -> showLoading(false, null) // Idle or others
                    }
                }
            }
        }

        // Load after observers are attached
        viewModel.load(keypass)
    }

    private fun openDetails(item: JsonObject) {
        startActivity(
            Intent(this, DetailsActivity::class.java)
                .putExtra("entity", item.toString())
        )
    }

    private fun showLoading(loading: Boolean, status: String?) {
        binding.rv.isVisible = !loading
        binding.tvStatus.text = status.orEmpty()
    }
}

