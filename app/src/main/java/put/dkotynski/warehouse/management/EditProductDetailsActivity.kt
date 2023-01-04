package put.dkotynski.warehouse.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


class EditProductDetailsActivity : AppCompatActivity() {
    private val apiURL = "http://10.0.2.2:80"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product_details)

        data class MyItem(val ean: String, val id: Int, val name: String, val location: String, val quantity: Int)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val productIdView = findViewById<TextView>(R.id.productIdViewEdit)
        val inputName = findViewById<EditText>(R.id.typeProductName)
        val inputLocation = findViewById<EditText>(R.id.typeLocation)
        val inputAmount = findViewById<EditText>(R.id.typeAmount)

        val productId = intent.getStringExtra("id")

        val client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("$apiURL/items/$productId")
            .build()
        val response: Response = client.newCall(request).execute()
//        println(response.body)

        val gson = Gson()
        var item = gson.fromJson(response.body?.string(), MyItem::class.java)

//        val productName = intent.getStringExtra("name")
//        val amount = intent.getIntExtra("amount", 0)
//        val location = intent.getStringExtra("location")
//
        productIdView.text = item.ean
        inputName.setText(item.name)
        inputAmount.setText(Integer.toString(item.quantity))
        inputLocation.setText(item.location)

        saveButton.setOnClickListener() {
//            Toast.makeText(applicationContext, "Data changes saved", Toast.LENGTH_LONG).show()

            if(inputName.length() > 0 && inputLocation.length() > 0 && inputAmount.length() > 0) {

                val changedName = inputName.text.toString()
                val changedLocation = inputLocation.text.toString()
                val changedAmount = inputAmount.text.toString().toInt()

                val gson = Gson()
                var json = gson.toJson(MyItem(item.ean, item.id, changedName, changedLocation, changedAmount))

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = json.toRequestBody(mediaType)

                val requestChange: Request = Request.Builder()
                    .url("$apiURL/items/$productId")
                    .put(requestBody)
                    .build()
                val responseChange: Response = client.newCall(requestChange).execute()

                if (responseChange.code==200){
                    val intent = Intent(this, ProductDetailsActivity::class.java)

                    intent.putExtra("id", productId)
                    this.onPause()
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Smth went wrong", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(applicationContext, "Fill all fields", Toast.LENGTH_LONG).show()
            }
        }

        cancelButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Editing data cancelled", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("id", productId)
            this.onPause()
            startActivity(intent)
        }

    }
}