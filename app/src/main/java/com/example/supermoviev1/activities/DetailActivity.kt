package com.example.supermoviev1.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.example.superheroes.R
import com.example.superheroes.databinding.ActivityDetailBinding
import com.example.supermoviev1.data.SuperMovieServiceApi
import com.example.supermoviev1.data.Supermovie
import com.example.supermoviev1.utils.RetrofitProvider
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "SUPERMOVIE_ID"
        const val EXTRA_NAME = "SUPERMOVIE_NAME"
        const val EXTRA_IMAGE = "SUPERMOVIE_IMAGE"
    }

    private var supermovieid:String? = null
    private lateinit var supermovie: Supermovie


    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()

        supermovieid = intent.getStringExtra(EXTRA_ID)
        val name = intent.getStringExtra(EXTRA_NAME)
        val image = intent.getStringExtra(EXTRA_IMAGE)

        binding.toolbarLayout.title = name
        Picasso.get().load(image).into(binding.photoImageView)

        findSupermovieById(supermovieid!!)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)

        // Show back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        //val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.toolbar_layout)
        //collapsingToolbar.title = "Your Title"
    }

    private fun loadData () {
        binding.toolbarLayout.title = supermovie.name

        // Biography
        binding.content.realNameTextView.text = supermovie.biography.realName
        binding.content.publisherTextView.text = supermovie.biography.publisher
        val alignmentColor = if (supermovie.biography.alignment == "good") {
            R.color.good_color
        } else {
            R.color.evil_color
        }

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

    private fun findSupermovieById(id: String) {
        binding.content.progress.visibility = View.VISIBLE

        val service: SuperMovieServiceApi = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            // Llamada en segundo plano
            val response = service.findById(id)

            runOnUiThread {
                // Modificar UI
                binding.content.progress.visibility = View.GONE
                if (response.body() != null) {
                    Log.i("HTTP", "respuesta correcta :)")
                    supermovie = response.body()!!
                    loadData()
                } else {
                    Log.i("HTTP", "respuesta erronea :(")
                }
            }
        }
    }

