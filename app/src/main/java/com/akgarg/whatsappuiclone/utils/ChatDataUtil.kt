package com.akgarg.whatsappuiclone.utils

import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.models.ChatDataModel

class ChatDataUtil {

    companion object {

        fun getChatData(): ArrayList<ChatDataModel> {
            val chatDataList = ArrayList<ChatDataModel>()

            chatDataList.add(
                ChatDataModel(
                    "Rishabh",
                    "Hi bro",
                    "Just now",
                    R.mipmap.ic_launcher_round,
                    isMessageSend = true,
                    isMessageDelivered = true,
                    isMessageSeen = true
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Sachin Tendulkar",
                    "You need more practice, there is some problem with your front foot",
                    "11:18 pm",
                    R.drawable.sachin_tendulkar,
                    isMessageSend = false,
                    isMessageDelivered = false,
                    isMessageSeen = false
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Virat Kohli",
                    "Waiting for your firing 🔥 century 🏏",
                    "04:45 pm",
                    R.drawable.virat_kohli,
                    isMessageSend = true,
                    isMessageDelivered = true,
                    isMessageSeen = true
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Rinney",
                    "Hi, Can you please help me to solve this question?",
                    "03:57 pm",
                    R.drawable.user,
                    isMessageSend = true,
                    isMessageDelivered = true,
                    isMessageSeen = false
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "MS Dhoni",
                    "Hello sir, Big fan of you ❤️",
                    "03:15 pm",
                    R.drawable.ms_dhoni,
                    isMessageSend = true,
                    isMessageDelivered = false,
                    isMessageSeen = false
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Rohit Sharma",
                    "Hey boi, Hey, what's up?",
                    "09:23 am",
                    R.drawable.rohit_sharma,
                    isMessageSend = false,
                    isMessageDelivered = false,
                    isMessageSeen = false
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Rishabh 2",
                    "Hi, How are you?",
                    "Yesterday",
                    R.drawable.user,
                    isMessageSend = true,
                    isMessageDelivered = true,
                    isMessageSeen = false
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Shubham",
                    "Hey, let's catch up at PizzaHut",
                    "Yesterday",
                    R.drawable.shubham,
                    isMessageSend = true,
                    isMessageDelivered = true,
                    isMessageSeen = false
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Paras",
                    "Let's go for a ride on sunday",
                    "17-12-2021",
                    R.drawable.user,
                    isMessageSend = true,
                    isMessageDelivered = true,
                    isMessageSeen = true
                )
            )

            chatDataList.add(
                ChatDataModel(
                    "Akash",
                    "I'm at my office, I'll call you later",
                    "16-12-2021",
                    R.drawable.user,
                    isMessageSend = false,
                    isMessageDelivered = false,
                    isMessageSeen = false
                )
            )

            return chatDataList
        }


        fun getMessageStatusTick(data: ChatDataModel): Int {
            return if (!data.getIsMessageDelivered()) {
                R.drawable.single_tick
            } else if (data.getIsMessageDelivered() && data.getIsMessageSeen()) {
                R.drawable.blue_tick
            } else {
                R.drawable.double_tick
            }
        }

    }

}