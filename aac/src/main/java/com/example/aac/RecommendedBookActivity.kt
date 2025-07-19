package com.example.aac

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aac.RecommendedBookAdapter
import com.example.aac.databinding.ActivityRecommendedBookBinding
import com.example.aac.model.RecommendedBook
import com.example.aac.model.ResponseData
import com.example.aac.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendedBookBinding
    private lateinit var adapter: RecommendedBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendedBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "추천 도서 목록"

        adapter = RecommendedBookAdapter(mutableListOf())
        binding.recyclerRecommendedBooks.layoutManager = LinearLayoutManager(this)
        binding.recyclerRecommendedBooks.setHasFixedSize(true)
        binding.recyclerRecommendedBooks.adapter = adapter

        val service = RetrofitInstance.api
        val call = service.getRecommendedBooks(
            serviceKey = "da3acf77-23ed-4aab-955f-9346c8ed159c",
            numOfRows = 20,
            pageNo = 1
        )

        Log.d("API_URL", call.request().toString())

        call.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {
                    val items = response.body()?.body?.items?.itemList
                    if (items.isNullOrEmpty()) {
                        Toast.makeText(this@RecommendedBookActivity, "추천 도서가 없습니다.", Toast.LENGTH_SHORT).show()
                        adapter.updateBooks(dummyBooks())
                    } else {
                        adapter.updateBooks(items)
                    }
                } else {
                    Log.e("API_ERROR", "응답 오류: ${response.code()}")
                    Toast.makeText(this@RecommendedBookActivity, "서버 응답 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                    adapter.updateBooks(dummyBooks())
                }
            }

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.e("API_FAIL", "API 요청 실패", t)

                Toast.makeText(
                    this@RecommendedBookActivity,
                    "API 요청 실패: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()

                adapter.updateBooks(dummyBooks())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 더미 데이터 함수 테스트용
    private fun dummyBooks(): List<RecommendedBook> {
        return listOf(
            RecommendedBook(
                title = "홍길동전",
                creator = "홍길동",
                regDate = "2024-01-01",
                subjectCategory = "문학",
                description = "이 책은 전래동화 홍길동전에 대한 이야기입니다."
            ),
            RecommendedBook(
                title = "양파의 꿈",
                creator = "김영희",
                regDate = "2024-02-15",
                subjectCategory = "과학",
                description = "이 책은 양파에 속하지 못한 아이들에 대한 이야기입니다.."
            )
        )
    }
}


