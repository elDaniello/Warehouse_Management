package put.dkotynski.warehouse.management

import android.annotation.SuppressLint
import android.content.Intent
import android.content.LocusId
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val apiURL = "http://localhost:80"
    private val client = OkHttpClient()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val searchButton = findViewById<Button>(R.id.searchButton)
        val generatingButton = findViewById<Button>(R.id.generatingButton)
        val inputTypeID = findViewById<EditText>(R.id.typeID)

//        TODO() disabling and enabling searching Button
//        searchButton.isEnabled = false

//        val client = OkHttpClient()


        searchButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Search button clicked", Toast.LENGTH_LONG).show()

            if(inputTypeID.length() > 0){
                val id = inputTypeID.text.toString()
                run(id)
//                MainScope().launch {
//                    val request: Request = Request.Builder()
//                        .url("$apiURL/item/$id")
//                        .build()
//
//                    val call: Call = client.newCall(request)
//                    val response: Response = call.execute()
//                }
//                val searchingProduct = input.text.toString()
//                searchButton.isEnabled

                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("id", id)
                this.onPause()
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "You have to type product ID", Toast.LENGTH_LONG).show()
            }
        }

        generatingButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Generate button clicked", Toast.LENGTH_LONG).show()
        }

    }

    fun run(id: String) {
        val request = Request.Builder()
            .url("$apiURL/items/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

//                    for ((name, location) in response.headers) {
//                        println("$name: $location")
//                    }
//
//                    println(response.body!!.string())
                }
            }
        })
    }
}



