package com.akgarg.whatsappuiclone.utils

import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.model.StatusDataModel

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
                    "Shubham",
                    "Today, 1:10 am",
                    R.drawable.shubham
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Akash",
                    "Today, 1:08 am",
                    R.drawable.akash
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
                    R.drawable.paras
                )
            )

            recentStatusDataList.add(
                StatusDataModel(
                    "Rishabh",
                    "Yesterday, 3:12 pm",
                    R.drawable.rishabh
                )
            )

            return recentStatusDataList
        }

    }

}