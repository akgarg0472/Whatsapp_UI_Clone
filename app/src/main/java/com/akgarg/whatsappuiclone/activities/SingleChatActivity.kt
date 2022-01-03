package com.akgarg.whatsappuiclone.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.SingleChatRecyclerViewAdapter
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SingleChatActivity : AppCompatActivity(), TextWatcher {

    private var actionBar: ActionBar? = null
    private lateinit var toolbar: Toolbar
    private lateinit var backChat: LinearLayout
    private lateinit var chatNameAndOnlineStatusContainer: LinearLayout
    private lateinit var chatMessageAttachmentIcon: ImageView
    private lateinit var chatMessageCurrencyIcon: ImageView
    private lateinit var chatMessageCameraIcon: ImageView
    private lateinit var chatProfilePicture: ImageView
    private lateinit var chatName: TextView
    private lateinit var message: EditText
    private lateinit var sendMessageButton: FloatingActionButton
    private lateinit var recordVoiceButton: FloatingActionButton

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatRecyclerViewAdapter: SingleChatRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)

        window.statusBarColor = resources.getColor(R.color.tab_layout_bg_color, theme)
        actionBar = supportActionBar
        toolbar = findViewById(R.id.topChatPane)

        backChat = findViewById(R.id.backToAllChats)
        message = findViewById(R.id.newMessageEditText)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        recordVoiceButton = findViewById(R.id.recordVoiceButton)
        chatMessageAttachmentIcon = findViewById(R.id.chatMessageAttachmentIcon)
        chatMessageCurrencyIcon = findViewById(R.id.chatMessageCurrencyIcon)
        chatMessageCameraIcon = findViewById(R.id.chatMessageCameraIcon)
        chatName = findViewById(R.id.chatName)
        chatProfilePicture = findViewById(R.id.chatProfilePicture)
        chatNameAndOnlineStatusContainer = findViewById(R.id.chatNameAndOnlineStatusContainer)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatRecyclerViewAdapter = SingleChatRecyclerViewAdapter(this, null)

        message.addTextChangedListener(this)
        sendMessageButton.setOnClickListener { sendMessageButtonClickHandler() }
        chatNameAndOnlineStatusContainer.setOnClickListener { showProfile() }
        backChat.setOnClickListener { finish() }
        updateToolbar()
    }


    override fun onStart() {
        super.onStart()

        val bundle = intent.extras
        chatName.text = bundle?.getString(ApplicationConstants.CHAT_PROFILE_NAME)

//        chatName.doOnPreDraw {
//            Log.d("LINE_COUNT", chatName.lineCount.toString())
//        }
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        chatRecyclerView.layoutManager = linearLayoutManager
        chatRecyclerView.adapter = chatRecyclerViewAdapter

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_options_menu, menu)
        val moreMenuItem = menu?.findItem(R.id.chatMoreMenuItem)
        moreMenuItem?.subMenu?.clearHeader()
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.chatViewContactMenuItem -> showProfile()
        }

        return true
    }


    private fun showProfile() {
        val profileIntent = Intent(this, ChatProfileInfo::class.java)
        val extras = intent.extras
        if (extras != null) {
            profileIntent.putExtras(extras)
        }
        startActivity(profileIntent)
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


    private fun updateToolbar() {
        setSupportActionBar(toolbar)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(false)

        actionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )
    }

}