package com.example.aac.db

import android.content.ContentValues
import android.content.Context
import com.example.aac.model.Book

class BookDao(context: Context) {
    private val dbHelper = BookDBHelper(context)

    fun insertBook(
        title: String,
        author: String?,
        publisher: String?,
        category: String?,
        description: String?
    ) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("author", author)
            put("publisher", publisher)
            put("category", category)
            put("description", description)
        }
        db.insert("books", null, values)
        db.close()
    }

    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM books", null)

        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val author = cursor.getString(cursor.getColumnIndexOrThrow("author"))
                val publisher = cursor.getString(cursor.getColumnIndexOrThrow("publisher"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))

                books.add(Book(title, author, publisher, category, description))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return books
    }

    fun deleteBook(title: String, author: String): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete("books", "title = ? AND author = ?", arrayOf(title, author))
        db.close()
        return result
    }
}

