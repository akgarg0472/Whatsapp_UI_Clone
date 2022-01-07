package com.akgarg.whatsappuiclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.SingleChatRecyclerViewAdapter
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.ChatConstants
import com.akgarg.whatsappuiclone.constants.FirebaseConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.models.firebase.ChatMessage
import com.akgarg.whatsappuiclone.utils.SecurityUtils
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.akgarg.whatsappuiclone.utils.TimeUtils
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

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
    private var receiverUid: String? = null
    private var senderUid: String? = null

    private lateinit var messageCollectionRef: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_chat)

        window.statusBarColor = resources.getColor(R.color.tab_layout_bg_color, theme)
        actionBar = supportActionBar
        toolbar = findViewById(R.id.topChatPane)
        messageCollectionRef = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.FIREBASE_CHAT_MESSAGES_COLLECTION)

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

        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        receiverUid = intent.extras?.getString(ChatConstants.CHAT_PROFILE_UID)

        message.addTextChangedListener(this)
        sendMessageButton.setOnClickListener { sendMessageButtonClickHandler() }
        chatNameAndOnlineStatusContainer.setOnClickListener { showProfile() }
        backChat.setOnClickListener { finish() }
        updateToolbar()

        messageCollectionRef.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && value.metadata.hasPendingWrites()) {
                Log.d(
                    ApplicationLoggingConstants.CHAT_DATA_CHANGED.toString(),
                    "Listener"
                )

                fetAndUpdateChatDataForCurrentChat()
            }
        }
    }


    override fun onResume() {
        super.onResume()

        val bundle = intent.extras
        chatName.text = bundle?.getString(ChatConstants.CHAT_PROFILE_NAME)
        chatRecyclerViewAdapter = SingleChatRecyclerViewAdapter(this)
        chatRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        chatRecyclerView.adapter = chatRecyclerViewAdapter

        val profilePicture = bundle?.getString(ChatConstants.CHAT_PROFILE_PICTURE)
        if (profilePicture != null && profilePicture != "null") {
            chatProfilePicture.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            chatProfilePicture.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            chatProfilePicture.scaleType = ImageView.ScaleType.FIT_XY
            chatProfilePicture.imageTintMode = null
            Glide.with(this).load(profilePicture)
                .into(chatProfilePicture)
        }

        fetAndUpdateChatDataForCurrentChat()
    }


    private fun sendMessageButtonClickHandler() {
        if (message.text.isNotEmpty()) {
            val msg = message.text.toString()
            message.setText("")
            sendMessage(msg)
        }
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


    private fun sendMessage(message: String) {
        val messageObject = ChatMessage(
            message = SecurityUtils.encryptMessage(message, senderUid, receiverUid),
            chatMessageTime = TimeUtils.getMessageDateTime(),
            time = System.currentTimeMillis(),
            senderUid = senderUid,
            isMessageDelivered = false,
            receiverUid = receiverUid,
            isMessageSeen = false
        )

        uploadMessage(messageObject)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun uploadMessage(message: ChatMessage): String? {
        messageCollectionRef
            .add(message)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(
                        ApplicationLoggingConstants.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                        "Message stored in the database"
                    )
                } else {
                    Log.d(
                        ApplicationLoggingConstants.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                        it.exception?.message.toString()
                    )
                }
            }.addOnFailureListener {
                Log.d(
                    ApplicationLoggingConstants.FIREBASE_CHAT_MESSAGE_UPLOAD.toString(),
                    it.message.toString()
                )
            }

        return null
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun fetAndUpdateChatDataForCurrentChat() {
        val sender = senderUid
        val receiver = receiverUid

        messageCollectionRef
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val docs = it.documents
                val chats = arrayListOf<ChatMessage>()
                val chatIds = arrayListOf<String>()

                docs.forEach { documentSnapshot ->
                    val message = documentSnapshot.toObject<ChatMessage>()

                    if (message != null) {
                        if ((message.getSenderUid() == receiver && message.getReceiverUid() == sender)
                            || (message.getSenderUid() == sender && message.getReceiverUid() == receiver)
                        ) {
                            chats.add(message)
                            chatIds.add(documentSnapshot.id)
                        }
                    }
                }

                chatRecyclerViewAdapter.updateChatMessageDataset(chats)
                chatRecyclerViewAdapter.notifyDataSetChanged()
                updateMessagesReadStatus(chats, chatIds)
            }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateMessagesReadStatus(
        chats: ArrayList<ChatMessage>,
        chatIds: ArrayList<String>
    ) {
        val isReadReceiptEnabled = SharedPreferenceUtil.getBooleanPreference(
            this,
            SharedPreferenceConstants.REGISTERED_USER_IS_READ_RECEIPT_ENABLED
        )
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null && isReadReceiptEnabled) {
            chats.forEachIndexed { index, message ->
                if (message.getReceiverUid() == currentUserUid && !message.getIsMessageSeen()) {
                    val messageId = chatIds[index]
                    messageCollectionRef.document(messageId).update("isMessageSeen", true)
                }
            }

            chatRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

}