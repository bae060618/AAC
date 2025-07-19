package com.example.aac

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aac.db.BookDao
import com.example.aac.model.Book
import com.example.aac.model.RecommendedBook

class RecommendedBookAdapter(private val books: MutableList<RecommendedBook>) :
    RecyclerView.Adapter<RecommendedBookAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textTitle)
        val creator: TextView = view.findViewById(R.id.textCreator)
        val regDate: TextView = view.findViewById(R.id.textRegDate)
        val category: TextView = view.findViewById(R.id.textCategory)
        val description: TextView = view.findViewById(R.id.textDescription)
        val btnSave: Button = view.findViewById(R.id.btnSave)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommended_book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.title.text = book.title
        holder.creator.text = "저자: ${book.creator}"
        holder.regDate.text = "등록일: ${book.regDate}"
        holder.category.text = "분류: ${book.subjectCategory}"
        holder.description.text = "설명: ${book.description}"

        holder.btnSave.setOnClickListener {
            val dao = BookDao(holder.itemView.context)
            dao.insertBook(
                title = book.title ?: "제목 없음",
                author = book.creator ?: "저자 미상",
                publisher = "출판사 없음",
                category = book.subjectCategory ?: "카테고리 없음",
                description = book.description ?: "설명 없음"
            )
            Toast.makeText(holder.itemView.context, "${book.title} 저장 완료!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<RecommendedBook>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}
