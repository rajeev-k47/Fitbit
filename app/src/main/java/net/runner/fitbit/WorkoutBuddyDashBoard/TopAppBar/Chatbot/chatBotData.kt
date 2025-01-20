package net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar.Chatbot

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import net.runner.fitbit.BuildConfig
import net.runner.fitbit.Database.getUserDataSuspendable
import java.time.LocalDate

val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash",
    apiKey = BuildConfig.GENERATIVE_API_KEY
)

suspend fun response(query:String,onResponse:(String)->Unit){

        val data = getUserDataSuspendable()
        val goalType = data?.get("goalType")
        val inputContent = content {
            text("Query => $query")
            text("Date => ${LocalDate.now()}")
            text(
                "You are a fitness and nutrition expert.Your goal is to help people with their fitness and diet plans.\n" +
                        "Answer in the context of gym workouts and healthy eating. This is the extra data provided for user's goals and preferences \n" +
                        "Data:${goalType}"+
                        "\n"+
                        "User: $query\n")
        }

        val response = generativeModel.generateContent(inputContent)
        onResponse(response.text?.trimIndent()!!)
}