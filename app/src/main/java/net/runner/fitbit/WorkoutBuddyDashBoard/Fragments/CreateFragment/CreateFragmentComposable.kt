package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.launch
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createContent(modalStatus:Boolean,navController: NavController,onDismiss:(Boolean)->Unit) {

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val buttons = listOf("Create Post","Chat")
    if(modalStatus){

        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {

                    sheetState.hide()
                    onDismiss(false)
                }
            },
            sheetState = sheetState,
            scrimColor = Color.White.copy(0.05f),
            containerColor = background,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    buttons.forEach { label ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {
                                    if(label=="Create Post"){
                                        coroutineScope.launch {

                                            sheetState.hide()
                                            onDismiss(false)
                                        }

                                        navController.navigate("CreatePost")
                                    }
                                    else{
                                        navController.navigate("UserChats")
                                    }
                                },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .size(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = lightBlueText.copy(0.1f),
                                ),
                            ) {
                                Icon(painter = if(label=="Create Post") painterResource(id = R.drawable.create_post) else painterResource(id = R.drawable.chat), contentDescription = "", modifier = Modifier.size(26.dp), tint = lightText)
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = label, color = lightText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {

                            sheetState.hide()
                            onDismiss(false)
                        }
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lightBlueText.copy(0.1f),
                    ),
                ) {
                    Text("Close", color = lightText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))


            }


        }
    }

}