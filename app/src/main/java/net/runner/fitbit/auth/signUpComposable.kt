package net.runner.fitbit.auth

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.runner.fitbit.Firebase.fcmTokenSave
import net.runner.fitbit.R
import net.runner.fitbit.auth.authFunctions.EmailLinkAuth
import net.runner.fitbit.auth.authFunctions.gOauthClient
import net.runner.fitbit.auth.database.checkIfAccountExists
import net.runner.fitbit.auth.database.getfcmToken
import net.runner.fitbit.sharedPreference.tempEmailSignUp
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun SignUpComposable(navController: NavController,activity: Activity) {
    val auth = FirebaseAuth.getInstance()
    var accountstatus by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var wrongEmailWarning by rememberSaveable {
        mutableStateOf(false)
    }

    DisposableEffect(auth) {
        val authListener = FirebaseAuth.AuthStateListener{ auth ->
            val user = auth.currentUser
            if (user != null) {
                checkIfAccountExists(user.email.toString()){status,type->
                    accountstatus=status
                    Log.d("TAG","$accountstatus")
                    if(accountstatus){
                        fcmTokenSave(true)
                        if(type=="Workoutbuddy"){

                            navController.navigate("dashBoardBuddy"){

                                popUpTo("signUpScreen") { inclusive =true }
                            }
                        }else{
                            navController.navigate("dashBoardOrganizer"){
                                popUpTo("signUpScreen") { inclusive =true }
                            }
                        }
                    }else{
                        navController.navigate("userDetails") {
                            popUpTo("signUpScreen"){inclusive = true }
                        }
                    }
                }
            }
        }
        auth.addAuthStateListener(authListener)
        onDispose {
            auth.removeAuthStateListener(authListener)
        }
    }
    var emailText by rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)

        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = R.drawable.logo1), contentDescription = "" , modifier = Modifier.size(340.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(0.8f)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Sign up",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "Once you sign up, your personal feed will be ready to explore.",
                        color = lightText,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            ,
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        onClick = {
                            println("scope")
                            coroutineScope.launch(Dispatchers.Main) {

                                gOauthClient(activity)
                            }
                        }

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google_default",Modifier.size(22.dp))
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(text = "Google", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
                        }
                    }
                    Spacer(modifier = Modifier.size(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(modifier = Modifier.weight(0.4f)){
                            HorizontalDivider(thickness = 1.1.dp, color = lightText.copy(alpha = 0.3f))
                        }
                        Box(modifier = Modifier.weight(0.1f)){
                            Text(text = "or", fontWeight = FontWeight.Bold , color = Color.White, fontSize = 16.sp, modifier = Modifier.align(Alignment.Center))
                        }
                        Box(modifier = Modifier.weight(0.4f)){
                            HorizontalDivider(thickness = 1.1.dp, color = lightText.copy(alpha = 0.4f))
                        }
                    }
                    Spacer(modifier = Modifier.size(15.dp))
                        TextField(
                            value = emailText,
                            onValueChange = {text-> emailText = text},
                            placeholder = {
                            Text(text = "Email", color = lightBlueText, fontSize = 17.sp)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = lightText.copy(alpha = 0.1f),
                                unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                                focusedIndicatorColor = Color.Red.copy(alpha = 0.5f),
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                            leadingIcon = {
                                Icon(painter = painterResource(id = R.drawable.mail), contentDescription = "", modifier = Modifier.size(24.dp), tint = lightBlueText)
                            },
                            supportingText = {
                                if(wrongEmailWarning){

                                    Row (
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Icon(imageVector = Icons.Filled.Warning, contentDescription = "", tint = Color.Red, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(text = "Enter a vaild email", color = Color.Red, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            },
                            trailingIcon ={
                                Button(
                                    onClick = {
                                        if(Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                                            wrongEmailWarning = false
                                            coroutineScope.launch{
                                                EmailLinkAuth(emailText)
                                                tempEmailSignUp(emailText,context)
                                            }
                                            FancyToast.makeText(context,"Email sent !!",FancyToast.LENGTH_LONG,FancyToast.DEFAULT,false).show()
                                        }else{
                                            wrongEmailWarning = true
                                        }

                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = lightText.copy(alpha = 0.2f),
                                    ),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(40.dp)
                                ) {
                                    Icon(painter = painterResource(id = R.drawable.arrow_right), contentDescription = "signUp", modifier = Modifier.size(22.dp), tint = lightBlueText)
                                }
                            }
                        )
                    Spacer(modifier = Modifier.size(75.dp))
//                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.1.dp, color = lightText.copy(alpha = 0.3f))
//                    Spacer(modifier = Modifier.size(15.dp))
//
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ){
//                        Text(text = "Already using Fitbit? ", color = lightText, fontSize = 16.sp)
//                        Text(text = "Log in", color = Color.White, fontSize = 16.sp, modifier = Modifier.clickable {
////                            navController.navigate("login")
//                        },
//                            textDecoration = TextDecoration.Underline
//                            )
//                    }

                }
            }
        }
    }
}