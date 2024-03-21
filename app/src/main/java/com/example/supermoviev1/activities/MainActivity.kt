package com.example.supermoviev1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superheroes.R
import com.example.superheroes.databinding.ActivityMainBinding
import com.example.supermoviev1.adapters.SuperMovieAdapter
import com.example.supermoviev1.data.SuperMovieServiceApi
import com.example.supermoviev1.data.Supermovie
import com.example.supermoviev1.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: SuperMovieAdapter
    private var supermovieList: List<Supermovie> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SuperMovieAdapter() {
            onItemClickListener(it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        binding.progress.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.emptyPlaceholder.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        initSearchView(menu?.findItem(R.id.menu_search))

        return true
    }

    private fun initSearchView(searchItem: MenuItem?) {
        if (searchItem != null) {
            var searchView = searchItem.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchSupermovie(query!!)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun onItemClickListener(position: Int) {
        val superhero: Supermovie = supermovieList[position]

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, superhero.id)
        intent.putExtra(DetailActivity.EXTRA_NAME, superhero.name)
        intent.putExtra(DetailActivity.EXTRA_IMAGE, superhero.image.url)
        startActivity(intent)
    }

    private fun searchSupermovie(query: String) {
        binding.progress.visibility = View.VISIBLE

        val service: SuperMovieServiceApi = RetrofitProvider.getRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.searchByName(query)

            runOnUiThread {
                binding.progress.visibility = View.GONE

                if (response.body() != null) {
                    Log.i("HTTP", "respuesta correcta :)")
                    supermovieList = response.body()?.results.orEmpty()
                    adapter.updateItems(supermovieList)

                    if (supermovieList.isNotEmpty()) {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.emptyPlaceholder.visibility = View.GONE
                    } else {
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyPlaceholder.visibility = View.VISIBLE
                    }
                } else {
                    Log.i("HTTP", "respuesta erronea :(")
                    Toast.makeText(
                        this@MainActivity,
                        "Hubo un error inesperado, vuelva a intentarlo más tarde",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
