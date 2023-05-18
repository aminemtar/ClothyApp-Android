package com.example.clothy.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.clothy.R
import com.example.clothy.databinding.FragmentCategoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val nav by lazy{ findNavController() }


    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)

        // TODO


        binding.relativeMen.setOnClickListener { val intent = Intent(context, SwipeActivity::class.java)
            intent.putExtra("type", "outwear")
            startActivity(intent)}
        binding.shoes.setOnClickListener { val intent = Intent(context, SwipeActivity::class.java)
            intent.putExtra("type", "shoes")
            startActivity(intent)}
        binding.relativeWomen.setOnClickListener { val intent = Intent(context, SwipeActivity::class.java)
            intent.putExtra("type", "jeans")
            startActivity(intent)}
        binding.hat.setOnClickListener { val intent = Intent(context, SwipeActivity::class.java)
            intent.putExtra("type", "hat")
            startActivity(intent)}
        binding.tshirt.setOnClickListener { val intent = Intent(context, SwipeActivity::class.java)
            intent.putExtra("type", "t-shirt")
            startActivity(intent)}



        return binding.root
    }
    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.host, fragment)
            .commit()
    }
}
