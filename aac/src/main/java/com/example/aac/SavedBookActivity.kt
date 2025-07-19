package com.example.aac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aac.databinding.ActivitySavedBookBinding
import com.example.aac.db.BookDao
import com.example.aac.model.Book
import android.util.Log

class SavedBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedBookBinding
    private lateinit var adapter: SavedBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = BookDao(this)
        val savedBooks: MutableList<Book> = dao.getAllBooks().toMutableList()

        Log.d("SavedBookActivity", "불러온 책 수: ${savedBooks.size}")
        for (book in savedBooks) {
            Log.d("SavedBookActivity", "책 제목: ${book.title}, 저자: ${book.author}")
        }

        adapter = SavedBookAdapter(savedBooks)
        binding.recyclerSavedBooks.layoutManager = LinearLayoutManager(this)
        binding.recyclerSavedBooks.adapter = adapter
    }
}

