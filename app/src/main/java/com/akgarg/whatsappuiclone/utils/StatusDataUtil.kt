package com.akgarg.whatsappuiclone.utils

import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.models.StatusDataModel

class StatusDataUtil {

    companion object {

        fun getRecentStatusData(): ArrayList<StatusDataModel> {
            val recentStatusDataList = ArrayList<StatusDataModel>()

            recentStatusDataList.add(
                StatusDataModel(
                    "Java ❤️",
                    "Just Now",
                    R.drawable.java
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Rishabh",
                    "Today, 10:35 pm",
                    R.mipmap.ic_launcher_round
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Rinney",
                    "Today, 3:35 pm",
                    R.drawable.user
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Shubham",
                    "Today, 1:10 am",
                    R.drawable.shubham
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Akash",
                    "Today, 1:08 am",
                    R.drawable.user
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "MS Dhoni",
                    "Today, 12:01 am",
                    R.drawable.ms_dhoni
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Paras",
                    "Yesterday, 9:59 pm",
                    R.drawable.user
                )
            )

            return recentStatusDataList
        }

    }

}