package com.akgarg.whatsappuiclone.utils

@Suppress("unused")
class SecurityUtils private constructor() {

    companion object {

        fun encryptMessage(message: String, senderUid: String?, receiverUid: String?): String {
            val sum = getSum(senderUid, receiverUid)

            val sb = StringBuilder()
            for (ch in message.toCharArray()) {
                if (isEmoji(ch)) {
                    sb.append(ch)
                } else {
                    val newInt = (ch.code + sum) % 256
                    sb.append(newInt.toChar())
                }
            }
            return sb.toString()
        }


        fun decryptMessage(message: String, senderUid: String?, receiverUid: String?): String {
            val sum = getSum(senderUid, receiverUid)

            val sb = StringBuilder()
            for (ch in message.toCharArray()) {
                val newInt = (ch.code + 256 - sum) % 256

                if (isEmoji(newInt.toChar())) {
                    sb.append(ch)
                } else {
                    sb.append(newInt.toChar())
                }
            }

            return sb.toString()
        }


        private fun getSum(idOne: String?, idTwo: String?): Int {
            var sum = 0

            if (idOne != null) {
                for (c in idOne.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sum += c.toString().toInt()
                    }
                }
            }

            if (idTwo != null) {
                for (c in idTwo.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sum += c.toString().toInt()
                    }
                }
            }

            return sum
        }


        private fun isEmoji(ch: Char): Boolean {
            return Character.getNumericValue(ch) == -1
        }

    }
}