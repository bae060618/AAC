package com.example.aac

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BodySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aac_selection)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAac)
        val selectedLayout = findViewById<LinearLayout>(R.id.selectedSentenceLayout)
        selectedLayout.visibility = View.VISIBLE

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val emotionList = listOf(
            CardItem(R.drawable.ic_hand, "손"),
            CardItem(R.drawable.ic_foot, "발"),
            CardItem(R.drawable.ic_ear, "귀"),
            CardItem(R.drawable.ic_mouth, "입")
        )

        val adapter = CardAdapter(emotionList) { selectedCard ->
            Toast.makeText(this, "${selectedCard.label} 선택됨", Toast.LENGTH_SHORT).show()

            for (i in 0 until selectedLayout.childCount) {
                val child = selectedLayout.getChildAt(i) as LinearLayout
                val label = (child.getChildAt(0) as? TextView)?.text
                if (label == selectedCard.label) return@CardAdapter
            }

            val container = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundResource(R.drawable.background_selected)
                setPadding(24, 12, 24, 12)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 0, 16, 0)
                }
            }

            val textView = TextView(this).apply {
                text = selectedCard.label
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }

            val closeBtn = ImageView(this).apply {
                setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                setPadding(16, 0, 0, 0)
                setOnClickListener {
                    selectedLayout.removeView(container)
                }
            }

            container.addView(textView)
            container.addView(closeBtn)
            selectedLayout.addView(container)
        }

        recyclerView.adapter = adapter
    }
}