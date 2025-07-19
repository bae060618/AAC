package com.example.aac

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryCardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_emotion_card, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val cardItems = listOf(
            CardItem(R.drawable.ic_happy, "감정"),
            CardItem(R.drawable.ic_hand, "신체"),
        )

        adapter = CardAdapter(cardItems) { selected ->
            if (selected.label == "감정") {

                val intent = Intent(requireContext(), EmotionSelectionActivity::class.java)
                startActivity(intent)
            } else if (selected.label == "신체") {
                val intent = Intent(requireContext(), BodySelectionActivity::class.java)
                startActivity(intent)
            }else {
            }
        }

        recyclerView.adapter = adapter
        return view
    }
}
