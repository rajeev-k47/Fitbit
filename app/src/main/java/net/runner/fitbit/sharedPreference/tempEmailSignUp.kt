package net.runner.fitbit.sharedPreference

import android.content.Context

fun tempEmailSignUp(email: String,context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("temp_email_signup", email)
    editor.apply()
}
fun getTempEmail(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("temp_email_signup", null)
}
fun removeTempEmail(context: Context) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.remove("temp_email_signup")
    editor.apply()
}