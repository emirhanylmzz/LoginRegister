package com.example.loginregister

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import org.json.JSONArray


class MainActivity : AppCompatActivity(), CustomAdapter.ItemClickListener {
    private var adapter: CustomAdapter? = null
    private lateinit var recyclerView : RecyclerView
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var userNames: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        define()
        getFromDatabase()
        setUpRecyclerView()

    }

    private fun define(){
        recyclerView = findViewById<RecyclerView>(R.id.userList)
        layoutManager = LinearLayoutManager(this)
        userNames = ArrayList()
    }


    private fun getFromDatabase(){
        val handler = Handler(Looper.getMainLooper())
        handler.post(Runnable {
            val fetchData =
                FetchData("http://192.168.1.23:8080/LoginRegister/readUsers.php")
            if (fetchData.startFetch()) {
                if (fetchData.onComplete()) {
                    val array = JSONArray(fetchData.result)
                    for (i in 0 until array.length()) {
                        val item = array.getJSONObject(i)
                        //Log.e("MyActivity", item.getString("id"))
                        val user = User(item.getString("id").toInt(), item.getString("fullname"),item.getString("username"), item.getString("email"))
                        userNames.add(user)
                    }
                    adapter?.notifyDataSetChanged()
                    Log.e("MyActivity", fetchData.result)
                    //End ProgressBar (Set visibility to GONE)
                }
            }
        })
    }

    private fun setUpRecyclerView()
    {
        recyclerView.layoutManager = layoutManager
        adapter = CustomAdapter(this, userNames)
        adapter!!.setClickListener(this)
        recyclerView.adapter = adapter

         /*val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )*/
        recyclerView.addItemDecoration(MyItemDecoration(20,32))
    }

    override fun onItemClick(view: View?, position: Int) {
        Toast.makeText(
            this,
            "You clicked " + adapter?.getItem(position).toString() + " on row number " + position,
            Toast.LENGTH_SHORT
        ).show()
    }

}