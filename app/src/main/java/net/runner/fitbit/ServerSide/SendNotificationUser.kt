package net.runner.fitbit.ServerSide

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

fun SendNotificationUser(userId:String,toId:String,Message:String,onResponse:(String)->Unit){
    val client = OkHttpClient()
    val requestBody = JSONObject().apply {
        put("uid", userId)
            .put("toId",toId)
            .put("Message",Message)
    }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

    val request = Request.Builder()
        .url("https://fitbit-api-server.vercel.app/api/SendChatNotificationUser/")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                onResponse(responseBody.toString())
                println("Response: $responseBody")
            } else {
                println("Error: ${response}")
            }
        }
    })


}