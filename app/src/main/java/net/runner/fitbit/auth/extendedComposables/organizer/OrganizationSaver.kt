package net.runner.fitbit.auth.extendedComposables.organizer

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope

val OrganizerGroupDataSaver =Saver<OrganizerGroupData, Map<String, String>>(
    save ={data ->
        mapOf(
            "organizationName" to data.organizationName,
            "organizerMobile" to data.organizerMobile,
            "organizationAddress" to data.organizationAddress,
            "organizationCity" to data.organizationCity
        )
    },
    restore ={map ->
        OrganizerGroupData(
            organizationName =map["organizationName"] ?:"",
            organizerMobile =map["organizerMobile"] ?:"",
            organizationAddress =map["organizationAddress"] ?:"",
            organizationCity =map["organizationCity"] ?:""
        )
    }
)
