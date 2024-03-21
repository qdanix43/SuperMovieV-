package com.example.supermoviev1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.supermoviev1.R
import com.example.supermoviev1.data.SuperMovieServiceApi
import com.example.supermoviev1.data.Supermovie
import com.example.supermoviev1.databinding.ActivityDetailBinding
import com.example.supermoviev1.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "SUPERMOVIE_Search"
        const val EXTRA_NAME = "SUPERMOVIE_totalResults"
        const val EXTRA_IMAGE = "SUPERMOVIE_Response"
    }

    private lateinit var binding: ActivityDetailBinding
    private var supermovieId: String? = null
    private lateinit var supermovie: Supermovie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()

        supermovieId = intent.getStringExtra(EXTRA_ID)
        val name = intent.getStringExtra(EXTRA_NAME)
        val image = intent.getStringExtra(EXTRA_IMAGE)

        binding.toolbarLayout.title = name
        Picasso.get().load(image).into(binding.photoImageView)

        findSupermovieById(supermovieId!!)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        binding.content.titleTextView.text = supermovie.title
        // If you want to display other properties, make sure they are defined in the Supermovie class
        // binding.content.publisherTextView.text = supermovie.publisher
        // val alignmentColor = if (supermovie.alignment == "good") {
        //     R.color.good_color
        // } else {
        //     R.color.evil_color
        // }
    }

    private fun findSupermovieById(id: String) {
        binding.content.progress.visibility = View.VISIBLE

        val service: SuperMovieServiceApi = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.findById(id)

            runOnUiThread {
                binding.content.progress.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.i("HTTP", "respuesta correcta :)")
                    supermovie = response.body()!!
                    loadData()
                } else {
                    Log.i("HTTP", "respuesta erronea :(")
                }
            }
        }
    }
}
