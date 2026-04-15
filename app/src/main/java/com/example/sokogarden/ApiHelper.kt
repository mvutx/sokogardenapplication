package com.example.sokogarden

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject

class ApiHelper(var context: Context) {

    // POST (generic)
    fun post(api: String, params: RequestParams) {
        Toast.makeText(context, "Please wait for response", Toast.LENGTH_LONG).show()
        val client = AsyncHttpClient(true, 80, 443)

        client.post(api, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {
                Toast.makeText(context, "Response: $response", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(context, "Error: $responseString", Toast.LENGTH_LONG).show()
            }
        })
    }

    // 🔐 LOGIN (UPDATED - CLEAN CALLBACK VERSION)
    fun post_login(
        api: String,
        params: RequestParams,
        callback: (JSONObject?) -> Unit
    ) {

        Toast.makeText(context, "Please wait for response", Toast.LENGTH_LONG).show()
        val client = AsyncHttpClient(true, 80, 443)

        client.post(api, params, object : JsonHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONObject?
            ) {

                val message = response?.optString("message")

                if (message == "Login successful") {

                    val user = response.optJSONObject("user")
                    val username = user?.optString("username") ?: ""
                    val email = user?.optString("email") ?: ""

                    // Save session
                    val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putString("username", username)
                        .putString("email", email)
                        .apply()

                    Toast.makeText(context, "Welcome $username", Toast.LENGTH_LONG).show()

                    // return success to Signin
                    callback(response)

                } else {
                    Toast.makeText(context, "$message", Toast.LENGTH_LONG).show()
                    callback(null)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(context, "Error: $responseString", Toast.LENGTH_LONG).show()
                callback(null)
            }
        })
    }

    // LOAD PRODUCTS
    fun loadProducts(url: String, recyclerView: RecyclerView, progressBar: ProgressBar? = null) {
        progressBar?.visibility = View.VISIBLE

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        val client = AsyncHttpClient(true, 80, 443)

        client.get(context, url, null, "application/json", object : JsonHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONArray
            ) {
                progressBar?.visibility = View.GONE

                val productList = ProductAdapter.fromJsonArray(response)
                recyclerView.adapter = ProductAdapter(productList)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                progressBar?.visibility = View.GONE
                Toast.makeText(context, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // GET
    fun get(api: String, callBack: CallBack) {
        val client = AsyncHttpClient(true, 80, 443)

        client.get(context, api, null, "application/json",
            object : JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray
                ) {
                    callBack.onSuccess(response)
                }

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    callBack.onSuccess(response)
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?
                ) {
                    callBack.onFailure(responseString)
                }
            })
    }

    // PUT
    fun put(api: String, jsonData: JSONObject) {
        Toast.makeText(context, "Please Wait for response", Toast.LENGTH_LONG).show()
        val client = AsyncHttpClient(true, 80, 443)
        val body = StringEntity(jsonData.toString())

        client.put(context, api, body, "application/json",
            object : JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(context, "Response $response", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    Toast.makeText(context, "Error Occurred $throwable", Toast.LENGTH_LONG).show()
                }
            })
    }

    // DELETE
    fun delete(api: String, jsonData: JSONObject) {
        Toast.makeText(context, "Please Wait for response", Toast.LENGTH_LONG).show()
        val client = AsyncHttpClient(true, 80, 443)
        val body = StringEntity(jsonData.toString())

        client.delete(context, api, body, "application/json",
            object : JsonHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(context, "Response $response", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    Toast.makeText(context, "Error Occurred $throwable", Toast.LENGTH_LONG).show()
                }
            })
    }

    // CALLBACK INTERFACE
    interface CallBack {
        fun onSuccess(result: JSONArray?)
        fun onSuccess(result: JSONObject?)
        fun onFailure(result: String?)
    }
}