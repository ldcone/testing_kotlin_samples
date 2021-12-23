package com.example.testing_kotlin_samples.bookreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.testing_kotlin_samples.bookreview.model.Book
import com.example.testing_kotlin_samples.bookreview.model.Review
import com.example.testing_kotlin_samples.databinding.ActivityBookreviewDetailBinding

class DetailActivity:AppCompatActivity() {
    private lateinit var binding : ActivityBookreviewDetailBinding
    private lateinit var db:AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookreviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")
        binding.titleTV.text = model?.title.orEmpty()
        binding.descriptionTV.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread{
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread{
                binding.reviewEditText.setText(review.review.orEmpty())
            }
        }

        binding.saveButton.setOnClickListener {
            Thread{
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0,
                    binding.reviewEditText.text.toString())

                )
            }.start()
        }
    }
}