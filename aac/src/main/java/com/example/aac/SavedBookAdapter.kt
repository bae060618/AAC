package com.example.aac

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aac.model.Book
import android.widget.Button
import android.widget.Toast
import com.example.aac.db.BookDao

class SavedBookAdapter(private val books: MutableList<Book>) :
    RecyclerView.Adapter<SavedBookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textTitle)
        val author: TextView = view.findViewById(R.id.textAuthor)
        val publisher: TextView = view.findViewById(R.id.textPublisher)
        val category: TextView = view.findViewById(R.id.textCategory)
        val description: TextView = view.findViewById(R.id.textDescription)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.title.text = book.title
        holder.author.text = "저자: ${book.author}"
        holder.publisher.text = "출판사: ${book.publisher}"
        holder.category.text = "분류: ${book.category}"
        holder.description.text = "설명: ${book.description}"

        holder.btnDelete.setOnClickListener {
            val dao = BookDao(holder.itemView.context)
            val deleted = dao.deleteBook(book.title, book.author)
            if (deleted > 0) {
                books.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, books.size)
                Toast.makeText(holder.itemView.context, "삭제 완료", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(holder.itemView.context, "삭제 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = books.size
}



