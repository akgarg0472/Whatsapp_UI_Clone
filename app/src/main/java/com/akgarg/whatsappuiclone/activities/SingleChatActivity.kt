package com.akgarg.whatsappuiclone.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SingleChatActivity : AppCompatActivity(), TextWatcher {

    private lateinit var backChat: LinearLayout
    private lateinit var chatMessageAttachmentIcon: ImageView
    private lateinit var chatMessageCurrencyIcon: ImageView
    private lateinit var chatMessageCameraIcon: ImageView
    private lateinit var chatName: TextView
    private lateinit var chatProfilePicture: ImageView

    private lateinit var message: EditText
    private lateinit var sendMessageButton: FloatingActionButton
    private lateinit var recordVoiceButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.tab_layout_bg_color, theme)

        backChat = findViewById(R.id.backToAllChats)
        message = findViewById(R.id.newMessageEditText)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        recordVoiceButton = findViewById(R.id.recordVoiceButton)
        chatMessageAttachmentIcon = findViewById(R.id.chatMessageAttachmentIcon)
        chatMessageCurrencyIcon = findViewById(R.id.chatMessageCurrencyIcon)
        chatMessageCameraIcon = findViewById(R.id.chatMessageCameraIcon)
        chatName = findViewById(R.id.chatName)
        chatProfilePicture = findViewById(R.id.chatProfilePicture)

        message.addTextChangedListener(this)

        sendMessageButton.setOnClickListener { sendMessageButtonClickHandler() }

        backChat.setOnClickListener { finish() }
    }


    override fun onStart() {
        super.onStart()

        val bundle = intent.extras
        chatName.text = bundle?.getString(ApplicationConstants.CHAT_PROFILE_NAME)
        Glide.with(this).load(bundle?.getInt(ApplicationConstants.CHAT_PROFILE_PICTURE))
            .into(chatProfilePicture)
    }


    private fun sendMessageButtonClickHandler() {
        if (message.text.isNotEmpty()) {
            val msg = message.text.toString()
            message.setText("")
            sendMessage(msg)
        }
    }


    private fun sendMessage(message: String) {
        Toast.makeText(this, "Message '$message' sent", Toast.LENGTH_SHORT).show()
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (message.text.isEmpty()) {
            sendMessageButton.visibility = View.VISIBLE
            recordVoiceButton.visibility = View.GONE
            chatMessageCameraIcon.visibility = View.GONE
            chatMessageCurrencyIcon.visibility = View.GONE
            changeMarginEndOfAttachmentIcon(4F, 8F)
        }
    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }


    override fun afterTextChanged(s: Editable?) {
        if (s.toString().isEmpty()) {
            recordVoiceButton.visibility = View.VISIBLE
            sendMessageButton.visibility = View.GONE
            chatMessageCameraIcon.visibility = View.VISIBLE
            chatMessageCurrencyIcon.visibility = View.VISIBLE
            changeMarginEndOfAttachmentIcon(0F, 16F)
        }
    }


    private fun changeMarginEndOfAttachmentIcon(marginStart: Float, marginEnd: Float) {
        val start = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginStart,
            resources.displayMetrics
        )

        val end = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginEnd,
            resources.displayMetrics
        )

        val layoutParams = chatMessageAttachmentIcon.layoutParams as LinearLayout.LayoutParams
        layoutParams.marginEnd = end.toInt()
        layoutParams.marginStart = start.toInt()
        chatMessageAttachmentIcon.layoutParams = layoutParams
    }

}