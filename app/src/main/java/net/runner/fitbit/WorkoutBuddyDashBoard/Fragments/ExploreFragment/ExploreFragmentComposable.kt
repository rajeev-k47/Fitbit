package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.ExploreFragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.runner.fitbit.R
import net.runner.fitbit.ui.theme.background
import net.runner.fitbit.ui.theme.lightBlueText
import net.runner.fitbit.ui.theme.lightText

@Composable
fun ExploreFragmentComposable() {

    var query by rememberSaveable { mutableStateOf("") }

    var typeFilerGroupSelected by rememberSaveable { mutableStateOf(false) }
    var typeFilerPeopleSelected by rememberSaveable { mutableStateOf(true) }
    var selectedFilter by rememberSaveable { mutableStateOf("Related") }
    var tabitems by rememberSaveable { mutableStateOf(listOf("Related", "Near you")) }







    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp).padding(top = 5.dp),
        horizontalAlignment = Alignment.Start
    ){

        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text(text = "Search", color = lightBlueText, fontSize = 17.sp) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
            ,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.search), contentDescription = "", modifier = Modifier.size(25.dp), tint = lightText)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = lightText.copy(alpha = 0.1f),
                unfocusedContainerColor = lightText.copy(alpha = 0.1f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth()){

            Text(text = "Explore", color = Color.White, fontSize = 18.sp, modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterStart), fontWeight = FontWeight.Bold)
            Row (
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.End
            ){
                TypeFilter(typeFilerPeopleSelected,"People", painterResource(id = R.drawable.person)){
                    if(typeFilerGroupSelected && !typeFilerPeopleSelected){
                        typeFilerGroupSelected = false
                    }
                    typeFilerPeopleSelected = true

                }
                Spacer(modifier = Modifier.width(10.dp))
                TypeFilter(typeFilerGroupSelected,"Groups", painterResource(id = R.drawable.group_filter)){
                    if(!typeFilerGroupSelected && typeFilerPeopleSelected){
                        typeFilerPeopleSelected = false
                    }
                    typeFilerGroupSelected = true
                }
            }
        }


        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = if(selectedFilter == "Near you") 1 else 0,
                modifier = Modifier.background(background),
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    Box(
                        modifier = Modifier
                            .offset(y = 1.dp)
                            .tabIndicatorOffset(tabPositions[if (selectedFilter == "Near you") 1 else 0])
                            .height(3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(horizontal = 60.dp)
                            .background(color = Color.Red)
                    )
                },
                divider = {},
            ) {
                tabitems.forEachIndexed { index, item ->
                            FeedFilter(item, isSelected = item){
                                selectedFilter = it
                            }
                }
            }
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }


        ExploreContent(selectedFilter,typeFilerPeopleSelected)



    }





}