package id.calocallo.githubusersapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.calocallo.githubusersapp.adapter.UsersAdapter
import id.calocallo.githubusersapp.databinding.ActivityMainBinding
import id.calocallo.githubusersapp.model.UsersDto
import id.calocallo.githubusersapp.network.ApiClient
import id.calocallo.githubusersapp.utils.OnLoadMoreListener
import id.calocallo.githubusersapp.utils.RecyclerInfinityScroll

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: UsersAdapter
    private lateinit var viewModel: UsersViewModel
    lateinit var scrollListener: RecyclerInfinityScroll

    private var mList = mutableListOf<UsersDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //layout
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)

        val repository = RemoteRepository(ApiClient.service())
        val usersVMFactory = UsersVMFactory(repository)
        viewModel = ViewModelProvider(this, usersVMFactory).get(UsersViewModel::class.java)
        viewModel.listUsers()


        //getData
        viewModel.getDataUsers.observe(this, {
            mList = it
            adapter = UsersAdapter(mList as ArrayList<UsersDto>)
            adapter.notifyDataSetChanged()
            binding.rvUsers.adapter = adapter

            //clicklistener
            adapter.setOnClickItemListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.html_url)))
            }
        })

        //scrolllistener
        scrollListener = RecyclerInfinityScroll(LinearLayoutManager(this))
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })
        binding.rvUsers.addOnScrollListener(scrollListener)

        //search
        binding.svUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

    }

    private fun LoadMoreData() {
        adapter.addLoadingView()
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.removeLoadingView()
            adapter.addData(mList)
            scrollListener.setLoaded()
            binding.rvUsers.post {
                adapter.notifyDataSetChanged()
            }
        }, 1000)

    }
}