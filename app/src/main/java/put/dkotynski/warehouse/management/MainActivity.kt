package put.dkotynski.warehouse.management

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchButton)
        val generatingButton = findViewById<Button>(R.id.generatingButton)
        val input = findViewById<EditText>(R.id.typeID)

//        TODO() disabling and enabling searching Button
//        searchButton.isEnabled = false

        searchButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Search button clicked", Toast.LENGTH_LONG).show()
            if(input.length() > 0){
//                val searchingProduct = input.text.toString()
//                searchButton.isEnabled
                val intent = Intent(this, ProductDetailsActivity::class.java)
                this.onPause()
                startActivity(intent)
            }
        }

        generatingButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Generate button clicked", Toast.LENGTH_LONG).show()
        }

    }


}