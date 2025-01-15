package net.runner.fitbit.WorkoutBuddyDashBoard.Fragments.CreateFragment

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createContent(modalStatus:Boolean,onDismiss:(Boolean)->Unit) {

    if(modalStatus){

        ModalBottomSheet(
            onDismissRequest = {
                onDismiss(false)
            },
            sheetState = rememberModalBottomSheetState(),
            scrimColor = Color.Black.copy(0.1f)
        ) {

        }
    }

}