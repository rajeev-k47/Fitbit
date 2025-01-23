package net.runner.fitbit.WorkoutBuddyDashBoard.TopAppBar.Chatbot

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText


@Composable
fun ChatbotPanel(navController: NavController) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(background)
        .statusBarsPadding().navigationBarsPadding()){

                ChatbotComposable()
    }

}
@Parcelize
data class listMessages(val type:String,val message:String): Parcelable

@Composable
fun ChatbotComposable() {

    val coroutineScope = rememberCoroutineScope()
    var query by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    var listMessages by rememberSaveable {
        mutableStateOf(
            mutableListOf(
                listMessages("0", "Well! How can I help you Today ?")
            )
        )
    }


    Box(modifier = Modifier
        .fillMaxSize()
    )
        {
        val scrollState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp),
                state = scrollState
            ) {
                items(listMessages.size) { index ->
                    val currentMessage = listMessages[index]
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = if (currentMessage.type == "0") {
                            Arrangement.Start
                        } else {
                            Arrangement.End
                        }
                    ){
                        if (currentMessage.type == "0") {
                            Icon(
                                painter = painterResource(id = R.drawable.ai),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .padding(end = 5.dp, top = 5.dp)
                                    .size(35.dp)
                            )
                        }
                        Text(
                            parseBoldText(currentMessage.message),
                            modifier = Modifier
                                .padding(2.dp)
                                .background(
                                    if (currentMessage.type == "0")
                                        lightText
                                    else
                                        MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .padding(12.dp)
                                .widthIn(min = 50.dp, max = 300.dp),
                            color = if (currentMessage.type == "0")
                                Color.Black
                            else
                                MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (currentMessage.type == "1") {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier
                                    .padding(start = 5.dp, top = 5.dp)
                                    .size(30.dp)
                            )
                        }

                    }
                }
            }

            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text(text = "Ask a question...", color = lightBlueText, fontSize = 17.sp) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(22.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send // Action to trigger "Send"
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (query.isNotBlank()) {
                                listMessages =
                                    listMessages
                                        .toMutableList()
                                        .apply {
                                            add(listMessages("1", query))
                                            add(listMessages("0", "Thinking..."))
                                        }
                                val indexOfThinkingMessage = listMessages.size - 1
                                query = ""
                                keyboardController?.hide()
                                coroutineScope.launch {
                                    response(listMessages[listMessages.size - 2].message) {
                                        listMessages = listMessages
                                            .toMutableList()
                                            .apply {
                                                set(indexOfThinkingMessage, listMessages("0", it))
                                            }

                                    }


                                }

                        }
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = lightText.copy(alpha = 0.1f),
                    unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                    focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ai),
                        contentDescription = "",
                        modifier = Modifier.size(25.dp),
                        tint = lightBlueText
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (query.isNotBlank()) {
                                listMessages =
                                    listMessages
                                        .toMutableList()
                                        .apply {
                                            add(listMessages("1", query))
                                            add(listMessages("0", "Thinking..."))
                                        }
                                val indexOfThinkingMessage = listMessages.size - 1
                                query = ""
                                keyboardController?.hide()

                                coroutineScope.launch {
                                    response(listMessages[listMessages.size - 2].message) {
                                        listMessages = listMessages
                                            .toMutableList()
                                            .apply {
                                                set(indexOfThinkingMessage, listMessages("0", it))
                                            }

                                    }


                                }
                            }
                        },
                        Modifier.size(35.dp)
                            .clip(CircleShape)
                    ) {

                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = lightBlueText

                        )
                    }
                }
            )
        }


    }


fun parseBoldText(input: String): AnnotatedString {
    val builder = AnnotatedString.Builder()

    var bold = false
    input.split("**").forEachIndexed { index, part ->
        if (index % 2 == 0) {
            builder.append(part)
        } else {
            builder.withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                builder.append(part)
            }
        }
    }

    return builder.toAnnotatedString()
}