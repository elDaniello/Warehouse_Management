package put.dkotynski.warehouse.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product_details)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val inputName = findViewById<EditText>(R.id.typeProductName)
        val inputLocation = findViewById<EditText>(R.id.typeLocation)
        val inputAmount = findViewById<EditText>(R.id.typeAmount)

        saveButton.setOnClickListener() {
//            Toast.makeText(applicationContext, "Data changes saved", Toast.LENGTH_LONG).show()

            if(inputName.length() > 0 && inputLocation.length() > 0 && inputAmount.length() > 0) {
                val intent = Intent(this, ProductDetailsActivity::class.java)
                this.onPause()
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Fill all fields", Toast.LENGTH_LONG).show()
            }
        }

        cancelButton.setOnClickListener(){
            Toast.makeText(applicationContext, "Editing data cancelled", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProductDetailsActivity::class.java)
            this.onPause()
            startActivity(intent)
        }

    }
}