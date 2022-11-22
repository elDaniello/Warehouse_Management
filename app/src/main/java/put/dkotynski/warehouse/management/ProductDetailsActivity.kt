package put.dkotynski.warehouse.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val editButton = findViewById<Button>(R.id.editButton)
        val orderButton = findViewById<Button>(R.id.orderButton)

        editButton.setOnClickListener() {
            Toast.makeText(applicationContext, "Edit button clicked", Toast.LENGTH_LONG).show()

            val intent = Intent(this, EditProductDetailsActivity::class.java)
            this.onPause()
            startActivity(intent)
        }

        orderButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Order button clicked", Toast.LENGTH_LONG).show()
        }
    }
}