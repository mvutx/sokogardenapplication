package com.example.sokogarden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.loopj.android.http.RequestParams

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)


//        find view by use of their id
        val txtname = findViewById<TextView>(R.id.txtProductName)
        val txtcost = findViewById<TextView>(R.id.txtProductCost)
        val imgProduct = findViewById<ImageView>(R.id.imgProduct)

//        retrieve the data passed from previous Activity(Mainactivity)
        val name = intent.getStringExtra("product_name")
        val cost = intent.getIntExtra("product_cost",0)
        val product_photo = intent.getStringExtra("product_photo")

//        updatethe textview with the data passed on previous activity
        txtname.text = name
        txtcost.text = " Ksh $cost"

//        specify the image url
        val imageUrl = "https://kbenkamotho.alwaysdata.net/static/images/${product_photo}"

        Glide.with(this)
            .load(imageUrl )
            .placeholder(R.drawable.ic_launcher_background) // Make sure you have a placeholder image
            .into(imgProduct)

//        find the paynow button by use of their ids
        val phone = findViewById<EditText>(R.id.phone)
        val btnPay = findViewById<Button>(R.id.pay)

//        setclickonlistener on the button
        btnPay.setOnClickListener {
//            specify the endpoint for making payment
            val api = "https://kbenkamotho.alwaysdata.net/api/mpesa_payment"

//            create a requestparam
            val data = RequestParams()

//            insert data to request params
            data.put("amount", cost)
            data.put("phone", phone.text.toString().trim())

//            import the helper
            val helper = ApiHelper(applicationContext)

//            access the post function inside helper class
            helper.post(api, data)
//            clear phone number fromedittext
            phone.text.clear()
        }
    }
}