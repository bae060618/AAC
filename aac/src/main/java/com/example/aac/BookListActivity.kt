package com.example.aac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aac.db.BookDao

class BookListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dao = BookDao(this)
        val books = dao.getAllBooks()

        adapter = BookListAdapter(books)
        recyclerView.adapter = adapter
    }
}

