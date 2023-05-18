package com.example.clothy.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clothy.clothyandroid.services.OutfitResponse
import com.clothy.clothyandroid.services.OutfitService
import com.example.clothy.Adapter.ProductAdapter
import com.example.clothy.Model.MyApplication
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.R
import com.example.clothy.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList



class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    var outfitlist = ArrayList<OutfitResponse.Outfit>()
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var edtSearch :SearchView
    private var originalList = mutableListOf<OutfitResponse.Outfit>()
    private var filteredList = mutableListOf<OutfitResponse.Outfit>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.rv // move this line after inflating the layout
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter= ProductAdapter(outfitlist)
        recyclerView.adapter = adapter
       edtSearch= binding.edtSearch
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val retro = RetrofitClient().getInstance().create(OutfitService::class.java)
        retro.getUserOutfit().enqueue(object : Callback<List<OutfitResponse.Outfit>> {
            override fun onResponse(
                call: Call<List<OutfitResponse.Outfit>>,
                response: Response<List<OutfitResponse.Outfit>>
            ) {
                if (response.isSuccessful) {
                    val outfit = response.body()
                    if (outfit != null) {
                        outfitlist.clear() // Clear the existing list
                        outfitlist.addAll(outfit)
                        println(outfit)
                        setupRecyclerViewAdapter()
                        originalList.clear()
                        originalList =
                            outfitlist.toList() as MutableList<OutfitResponse.Outfit>
                        adapter.notifyDataSetChanged()
                    }
                }
                else{
                    Log.e("Error", "error")
                }
            }
            override fun onFailure(call: Call<List<OutfitResponse.Outfit>>, t: Throwable) {
                // Handle failure
            }
        })

        edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String): Boolean = true

            override fun onQueryTextChange(name: String): Boolean {
                val filteredList = originalList.filter { outfit ->
                    outfit.type!!.contains(name, ignoreCase = true)
                }
                adapter.submitList(filteredList)
                return true
            }
        })
        return binding.root
    }


    private fun setupRecyclerViewAdapter() {
        recyclerView = requireView().findViewById(R.id.rv)
        recyclerView.layoutManager = GridLayoutManager(MyApplication.getInstance(), 2)
        adapter = ProductAdapter(outfitlist)
        recyclerView.adapter = adapter
        adapter.submitList(outfitlist)

    }
}