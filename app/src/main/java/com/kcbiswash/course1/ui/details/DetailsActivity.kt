package com.kcbiswash.course1.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.kcbiswash.course1.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar back (Up)
        binding.toolbar.setNavigationOnClickListener {
            finish() // back to Dashboard
        }

        val jsonString = intent.getStringExtra("entity")
        if (jsonString.isNullOrBlank()) {
            binding.tvTitle.text = "No data"
            binding.tvAll.text = "Missing entity payload"
            return
        }

        try {
            val obj = JsonParser.parseString(jsonString).asJsonObject
            bindJson(obj)
        } catch (e: Exception) {
            // Fallback: show raw string so we never crash
            binding.tvTitle.text = "Details"
            binding.tvAll.text = jsonString
        }
    }

    private fun bindJson(obj: JsonObject) {
        // Use a nice title if present
        val title = obj.entrySet().firstOrNull {
            it.key.equals("courseCode", true) ||
                    it.key.equals("title", true) ||
                    it.key.equals("name", true)
        }?.value?.toReadable() ?: "Details"
        binding.tvTitle.text = title

        // Show all fields including description
        val body = buildString {
            for ((k, v) in obj.entrySet()) {
                append(k).append(": ").append(v.toReadable()).append('\n')
            }
        }.trimEnd()
        binding.tvAll.text = body
    }

    private fun JsonElement.toReadable(): String = when {
        isJsonNull -> ""
        isJsonPrimitive -> asJsonPrimitive.toString().trim('"')
        isJsonArray || isJsonObject -> toString()
        else -> toString()
    }
}

