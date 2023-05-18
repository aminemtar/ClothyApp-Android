package com.example.clothy.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clothy.clothyandroid.services.MatchResponse
import com.clothy.clothyandroid.services.OutfitResponse
import com.example.clothy.Adapter.MessageAdapter
import com.example.clothy.MainActivity
import com.example.clothy.Model.MatchItem
import com.example.clothy.Model.RetrofitClient
import com.example.clothy.Model.UserResponse
import com.example.clothy.R
import com.example.clothy.Service.MatchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MatchFragment : Fragment() {
    private lateinit var rootLayout: View
    private lateinit var nbMSG: TextView
    private lateinit var edtSearch :SearchView
    private var originalList = mutableListOf<UserResponse.User>()

    var matchs : List<MatchResponse.Match>? = null
    object MessageItemHolder {
        var messageItemList: List<MatchItem>? = null
    }
    private lateinit var recyclerView:RecyclerView
    val TAG = MainActivity::class.java.simpleName
    var messageList = ArrayList<MatchItem>()
    var mAdapter: MessageAdapter? = null
    val msg = mutableListOf<String>()
    val c = mutableListOf<Int>()
    val msn = mutableListOf<String>()
    val idd = mutableListOf<String>()
    val idR = mutableListOf<String>()
    val ii = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_match, container, false)
edtSearch=rootLayout.findViewById(R.id.edtSearch)
        recyclerView = rootLayout.findViewById(R.id.recycler_view_messages)
        messageList = ArrayList()
        mAdapter = MessageAdapter(messageList ?: ArrayList())
        Log.e("hahah", messageList.toString())
        nbMSG= rootLayout.findViewById(R.id.text_count_messsage)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter

        val retro = RetrofitClient().getInstance().create(MatchService::class.java)
        retro.getmatch().enqueue(object : Callback<List<MatchResponse.Match>> {

            override fun onResponse(
                call: Call<List<MatchResponse.Match>>,
                response: Response<List<MatchResponse.Match>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()
                    // user?.get(0)?.Etat?.let { Log.e("type", it.toString()) }
                    if (user != null) {
                        idR.clear()
                        idd.clear()
                        msg.clear()
                        c.clear()
                        msn.clear()
                        ii.clear()
                        for (userr in user) {
                            println(userr.userr?.email.toString())
                            idd.add(userr.Id.toString())
                            msg.add(userr.userr?.firstname.toString())
                            c.add(user.size)
                            msn.add(userr.userr?.lastname.toString())
                            idR.add((userr.userr?.id.toString()))
                            ii.add(RetrofitClient().BASE_URLL+userr.userr?.image.toString())
                            println(userr.userr?.image.toString())
                            matchs = listOf(userr)
                            prepareMessageList()
                            println("lqithom")
                            mAdapter?.notifyDataSetChanged()

                        }
                        MessageItemHolder.messageItemList = messageList


                    }
                }

            }

            override fun onFailure(call: Call<List<MatchResponse.Match>>, t: Throwable) {
                // Handle failure
                Log.e("Error", "error")
            }
        })

        edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String): Boolean = true

            override fun onQueryTextChange(name: String): Boolean {
                val filteredList = originalList.filter { outfit ->
                    outfit.firstname!!.contains(name, ignoreCase = true)
                    outfit.lastname!!.contains(name, ignoreCase = true)
                }
                mAdapter!!.notifyDataSetChanged()
                return true
            }
        })


        return rootLayout
    }


    private fun prepareMessageList() {
        val rand = Random()
        val id = rand.nextInt(100)
        var i: Int
        i = 0
        println(msg.size)
        while (i < msg.size) {
            val message = MatchItem(
                idd[i],
                idR[i],
                msg[i],
                msn[i],
                c[i],
                ii[i]
            )
            (messageList).add(message)
            i++
            Log.e("hahah", messageList.toString())
            nbMSG.text = messageList.size.toString()
        }
    }
}