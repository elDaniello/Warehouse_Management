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
import com.google.firebase.firestore.util.Assert
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val apiURL = "http://10.0.2.2:80"
    private val client = OkHttpClient()


    data class MyItem(val ean: String, val id: Int, val name: String, val location: String, val quantity: Int)

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
                MainScope().launch {

                    val request = Request.Builder()
                        .url("$apiURL/items/$id")

                        .build()

                    val response = client.newCall(request).execute();

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



                    if (response.code==200)
                    {
                        print(response)
                        val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                        intent.putExtra("id", id)
//                        this@MainActivity.onPause()
                        val gson = Gson()
                        var item = gson.fromJson(response.body?.string(), MyItem::class.java)
//                        Assert. assertEquals(item.id, 1)
//                        Assert.assertEquals(item.ean, "5900014002180")
//                        Assert.assertEquals(item.name, "piwko harnas jasne pelne 500ml puszka")
//                        Assert.assertEquals(item.location, "P4 R69 L1488")
//                        Assert.assertEquals(item.quantity, 0)
//                        intent.putExtra("" ) //TODO

                        println(item)
                        print(response)
                        this@MainActivity.onPause()
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(applicationContext, "Server error", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "You have to type product ID", Toast.LENGTH_LONG).show()
            }
        }

        generatingButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Generate button clicked", Toast.LENGTH_LONG).show()
        }

    }

    fun run(id: String) {

    }
    }




