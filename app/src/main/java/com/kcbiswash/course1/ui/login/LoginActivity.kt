package com.kcbiswash.course1.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kcbiswash.course1.data.Result
import com.kcbiswash.course1.databinding.ActivityLoginBinding
import com.kcbiswash.course1.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    /** Set to true after we launch Dashboard so we know to clear on return. */
    private var launchedDashboard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Campus dropdown (no default value)
        val campuses = listOf("br", "sydney", "footscray")
        (binding.etCampus as? AutoCompleteTextView)?.apply {
            setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, campuses))
            // Close dropdown when user hits Done
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) { dismissDropDown(); true } else false
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text?.toString()?.trim().orEmpty()
            val password = binding.etPassword.text?.toString()?.trim().orEmpty()
            val campus = binding.etCampus.text?.toString()?.trim()?.lowercase(Locale.ROOT).orEmpty()
            if (!validateInputs(username, password, campus)) return@setOnClickListener
            viewModel.doLogin(campus, username, password)
        }

        // Observe login state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is Result.Loading -> showLoading(true, "Logging in…")
                        is Result.Success -> {
                            showLoading(false, "Success")
                            if (!launchedDashboard) {
                                launchedDashboard = true
                                startActivity(
                                    Intent(this@LoginActivity, DashboardActivity::class.java)
                                        .putExtra("keypass", state.data)
                                )
                            }
                        }
                        is Result.Error -> showLoading(false, "Login failed: ${state.message}")
                        else -> showLoading(false, null) // Idle or any initial state
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // If we had launched Dashboard and user came back, reset the form + state
        if (launchedDashboard) {
            launchedDashboard = false
            clearForm()
            // Reset VM so it doesn't immediately emit Success again
            viewModel.clearState()
        }
    }

    private fun validateInputs(u: String, p: String, c: String): Boolean {
        var ok = true
        if (u.isBlank()) { binding.etUsername.error = "Enter your first name"; ok = false } else binding.etUsername.error = null
        if (p.isBlank()) { binding.etPassword.error = "Enter student ID (without 's')"; ok = false } else binding.etPassword.error = null
        if (c.isBlank()) { binding.etCampus.error = "Choose br / sydney / footscray"; ok = false } else binding.etCampus.error = null
        return ok
    }

    private fun showLoading(loading: Boolean, message: String?) {
        binding.progress.isVisible = loading
        binding.btnLogin.isEnabled = !loading
        binding.etUsername.isEnabled = !loading
        binding.etPassword.isEnabled = !loading
        binding.etCampus.isEnabled = !loading
        binding.tvStatus.text = message.orEmpty()
    }

    /** Wipes all inputs, errors, status, and re-enables fields. */
    private fun clearForm() {
        binding.etUsername.setText("")
        binding.etPassword.setText("")
        (binding.etCampus as? AutoCompleteTextView)?.setText("", false)

        binding.etUsername.error = null
        binding.etPassword.error = null
        binding.etCampus.error = null

        binding.progress.isVisible = false
        binding.btnLogin.isEnabled = true
        binding.etUsername.isEnabled = true
        binding.etPassword.isEnabled = true
        binding.etCampus.isEnabled = true
        binding.tvStatus.text = ""

        // Clear focus so keyboard doesn't pop up on return
        binding.root.clearFocus()
    }
}
