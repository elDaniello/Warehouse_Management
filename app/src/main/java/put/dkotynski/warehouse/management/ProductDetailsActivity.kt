package put.dkotynski.warehouse.management

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class MyItem(val ean: String, val id: Int, val name: String, val location: String, val quantity: Int)

class ProductDetailsActivity : AppCompatActivity() {
    private val apiURL = "http://10.0.2.2:80"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
//        if (Build.VERSION.SDK_INT > 9) {
//            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
//        }

        val editButton = findViewById<Button>(R.id.editButton)
        val orderButton = findViewById<Button>(R.id.orderButton)
        val productIdView = findViewById<TextView>(R.id.productIdView)
        val productNameView = findViewById<TextView>(R.id.productView)
        val statusView = findViewById<TextView>(R.id.statusView)
        val locationView = findViewById<TextView>(R.id.locationView)
        val amountView = findViewById<TextView>(R.id.amountView)

        val productId = intent.getStringExtra("id")
        productIdView.text = productId

//        robocze
        productNameView.text = "cokolwiek"
        amountView.text = "3"
        locationView.text = "gdziekolwiek"
        if(amountView.text.toString().toInt() == 0)
            statusView.text = "lack of product"
        else if ((amountView.text.toString().toInt() < 5))
            statusView.text = "last items"
        else statusView.text = "available"

        val productName = productNameView.text.toString()
        val amount = amountView.text.toString().toInt()
        val location = locationView.text.toString()

        MainScope().launch {
            val client = OkHttpClient()

            val request: Request = Request.Builder()
                .url("$apiURL/item/$productId")
                .build()
            val response: Response = client.newCall(request).execute()
            println(response.body)
            val gson = Gson()

            var item = gson.fromJson(response.body?.string(), MyItem::class.java)
            println(item)
        }

        editButton.setOnClickListener() {
            Toast.makeText(applicationContext, "Edit button clicked", Toast.LENGTH_LONG).show()
            val intent = Intent(this, EditProductDetailsActivity::class.java)
            intent.putExtra("id", productId)
            intent.putExtra("name", productName)
            intent.putExtra("location", location)
            intent.putExtra("amount", amount)
            this.onPause()
            startActivity(intent)
        }

        orderButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Order button clicked", Toast.LENGTH_LONG).show()
        }
    }
}