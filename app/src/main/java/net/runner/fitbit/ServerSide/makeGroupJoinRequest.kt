package net.runner.fitbit.ServerSide

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

fun makeGroupJoinRequest(userId:String,groupId:String) {
    val client = OkHttpClient()
    println(userId)
    println(groupId)
    val requestBody = JSONObject().apply {
        put("uid", userId)
        put("groupId", groupId)
    }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

    val request = Request.Builder()
        .url("https://fitbit-api-server.vercel.app/api/")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                println("Response: $responseBody")
            } else {
                println("Error: ${response}")
            }
        }
    })
}
