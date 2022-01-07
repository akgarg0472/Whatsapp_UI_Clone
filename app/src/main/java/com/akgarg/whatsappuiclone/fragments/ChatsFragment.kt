package com.akgarg.whatsappuiclone.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.activities.SingleChatActivity
import com.akgarg.whatsappuiclone.adapters.ChatRecyclerViewAdapter
import com.akgarg.whatsappuiclone.constants.ChatConstants
import com.akgarg.whatsappuiclone.constants.FirebaseConstants
import com.akgarg.whatsappuiclone.interfaces.IChatClick
import com.akgarg.whatsappuiclone.models.firebase.ChatMessage
import com.akgarg.whatsappuiclone.models.firebase.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class ChatsFragment : Fragment(), IChatClick, EventListener<QuerySnapshot> {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatRecyclerViewAdapter

    private lateinit var messageCollectionRef: CollectionReference
    private lateinit var userCollectionRef: CollectionReference
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        currentUser = FirebaseAuth.getInstance().currentUser
        messageCollectionRef = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.FIREBASE_CHAT_MESSAGES_COLLECTION)
        userCollectionRef = FirebaseFirestore.getInstance()
            .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)

        messageCollectionRef.addSnapshotListener(this)

        return view
    }


    override fun onResume() {
        super.onResume()

        adapter = ChatRecyclerViewAdapter(requireContext(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        setMessagesStateToDelivered()
        getExistingChatUsersForCurrentUser()
    }


    override fun onItemClicked(user: User) {
        val singleChatActivityIntent = Intent(context, SingleChatActivity::class.java)

        val bundle = Bundle()
        bundle.putString(ChatConstants.CHAT_PROFILE_NAME, user.getName())
        bundle.putString(ChatConstants.CHAT_PROFILE_UID, user.getUid())
        bundle.putString(ChatConstants.CHAT_PROFILE_PICTURE, user.getProfilePictureUrl())
        bundle.putString(ChatConstants.CHAT_PROFILE_COUNTRY_CODE, user.getCountryCode())
        bundle.putString(ChatConstants.CHAT_PROFILE_PHONE_NUMBER, user.getMobileNumber())
        bundle.putString(ChatConstants.CHAT_PROFILE_LAST_SEEN, user.getLastSeen())
        bundle.putBoolean(
            ChatConstants.CHAT_PROFILE_IS_LAST_SEEN_VISIBLE,
            user.getIsLastSeenVisible()
        )
        bundle.putString(ChatConstants.CHAT_PROFILE_STATUS, user.getProfileStatus())
        bundle.putString(
            ChatConstants.CHAT_PROFILE_STATUS_UPDATED_ON,
            user.getStatusUpdatedOn()
        )

        singleChatActivityIntent.putExtras(bundle)
        startActivity(singleChatActivityIntent)
    }


    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            return
        }

        if (value != null && value.metadata.hasPendingWrites()) {
            getExistingChatUsersForCurrentUser()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getExistingChatUsersForCurrentUser() {
        val chatUsers: ArrayList<User> = arrayListOf()

        userCollectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                val allUsers = querySnapshot.documents

                allUsers.forEach { document ->
                    val user = document.toObject<User>()

                    if (user != null) {
                        if (user.getUid() != currentUser?.uid) {
                            chatUsers.add(user)
                        }
                    }
                }

                adapter.updateChatDataSet(chatUsers)
                adapter.notifyDataSetChanged()
            }
    }


    private fun setMessagesStateToDelivered() {
        val currentUserUid = currentUser?.uid
        if (currentUserUid != null) {
            messageCollectionRef.whereEqualTo("receiverUid", currentUserUid).get()
                .addOnSuccessListener {
                    val docs = it.documents

                    docs.forEach { documentSnapshot ->
                        val message = documentSnapshot.toObject<ChatMessage>()

                        if (message != null) {
                            if (!message.getIsMessageDelivered()) {
                                messageCollectionRef.document(documentSnapshot.id)
                                    .update("isMessageDelivered", true)
                            }
                        }
                    }
                }
        }
    }


}