package com.example.testing_kotlin_samples.bookreview

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testing_kotlin_samples.R
import com.example.testing_kotlin_samples.bookreview.adapter.bookAdapter
import com.example.testing_kotlin_samples.bookreview.api.BookService
import com.example.testing_kotlin_samples.bookreview.model.BestSellerDto
import com.example.testing_kotlin_samples.bookreview.model.SearchBookDto
import com.example.testing_kotlin_samples.databinding.ActivityBookreviewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity:AppCompatActivity() {
    private lateinit var binding: ActivityBookreviewBinding
    private lateinit var bookAdapter: bookAdapter
    private lateinit var bookService: BookService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookreviewBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBookRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interparkAPIkey))
            .enqueue(object : Callback<BestSellerDto>{
                override fun onResponse(call: Call<BestSellerDto>, response: Response<BestSellerDto>
                ) {
                    if(response.isSuccessful.not()){
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG,it.toString())
                        it.books.forEach{book ->
                            Log.d(TAG,book.toString())
                        }
                        bookAdapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {

                }

            })

        binding.searchEditText.setOnKeyListener{v, KeyCode, event->
            if(KeyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interparkAPIkey),keyword)
            .enqueue(object : Callback<SearchBookDto>{
                override fun onResponse(call: Call<SearchBookDto>, response: Response<SearchBookDto>
                ) {
                    if(response.isSuccessful.not()){
                        return
                    }
                    bookAdapter.submitList(response.body()?.books.orEmpty())
//                    response.body()?.let {
//                        Log.d(TAG,it.toString())
//                        it.books.forEach{book ->
//                            Log.d(TAG,book.toString())
//                        }
//                        bookAdapter.submitList(it.books)
//                    }
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {

                }

            })


    }

    private fun initBookRecyclerView() {
        bookAdapter = bookAdapter()
        binding.bookRecyclerView.layoutManager =LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = bookAdapter
    }

    companion object{
        private const val TAG = "MainActivity"

    }
}