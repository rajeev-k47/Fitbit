package net.runner.fitbit.GoogleAPis

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

fun fetchDistanceMatrix(
    personEmail:String,
    origins: String,
    destinations: String,
    apiKey: String,
    onSuccess: (distance: String,duration: String,email: String) -> Unit,
    onError: (Exception) -> Unit
) {
    val client = OkHttpClient()
    val url = "https://maps.googleapis.com/maps/api/distancematrix/json" +
            "?origins=$origins"+
            "&destinations=$destinations"+
            "&key=$apiKey"

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            onError(e)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            response.body?.let { responseBody ->
                if (response.isSuccessful) {
                    val responseJson = JSONObject(responseBody.string())
                    Log.d("gre",responseJson.toString())
                    try {
                        val rows = responseJson.getJSONArray("rows")
                        val elements = rows.getJSONObject(0).getJSONArray("elements")
                        val element = elements.getJSONObject(0)
                        val distance = element.getJSONObject("distance").getString("text")
                        val duration = element.getJSONObject("duration").getString("text")
                        onSuccess(distance, duration,personEmail)
                    } catch (e: Exception) {
                        onError(e)
                    }
                } else {
                    onError(IOException("Failed: ${response.message}"))
                }
            } ?: onError(IOException("Response body is null"))
        }
    })
}
