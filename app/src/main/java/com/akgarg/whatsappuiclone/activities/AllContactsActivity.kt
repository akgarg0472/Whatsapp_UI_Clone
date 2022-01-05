package com.akgarg.whatsappuiclone.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.adapters.AllContactsRecyclerViewAdapter
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.ChatConstants
import com.akgarg.whatsappuiclone.constants.FirebaseConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.interfaces.IContactClick
import com.akgarg.whatsappuiclone.models.firebase.User
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class AllContactsActivity : AppCompatActivity(), IContactClick {

    private lateinit var toolbar: Toolbar
    private lateinit var allContactsRecyclerView: RecyclerView
    private lateinit var contactRvAdapter: AllContactsRecyclerViewAdapter
    private lateinit var refreshProgressBar: MenuItem
    private lateinit var contactsCountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_contacts)

        toolbar = findViewById(R.id.allChatsToolbar)
        allContactsRecyclerView = findViewById(R.id.allContactsRecyclerView)
        contactsCountTextView = findViewById(R.id.contactsCountTextView)
    }


    override fun onResume() {
        super.onResume()
        updateToolbar()

        contactRvAdapter = AllContactsRecyclerViewAdapter(this, this)
        allContactsRecyclerView.layoutManager = LinearLayoutManager(this)
        allContactsRecyclerView.adapter = contactRvAdapter
        updateTotalContactCount()
        refreshContactsList(false)
    }


    private fun updateTotalContactCount() {
        contactsCountTextView.text = getString(
            R.string.all_contacts_count, SharedPreferenceUtil.getIntegerPreference(
                this,
                SharedPreferenceConstants.CONTACTS_COUNT
            )
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.all_contacts_menu, menu)

        val menuItem = menu?.findItem(R.id.refreshContactProgressBarMenuItem)
        if (menuItem != null) {
            refreshProgressBar = menuItem
        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.refreshContact) {
            refreshContactsList(true)
        }

        return true
    }


    private fun updateToolbar() {
        window.statusBarColor = resources.getColor(R.color.tab_layout_bg_color, theme)
        window.navigationBarColor = resources.getColor(R.color.chat_profile_info_bg, theme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun refreshContactsList(isManuallyRefreshed: Boolean) {
        Log.d(
            ApplicationLoggingConstants.CONTACTS_TAG.toString(),
            "Starting refreshing contacts list"
        )
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            val usersCollection = FirebaseFirestore.getInstance()
                .collection(FirebaseConstants.FIREBASE_USERS_COLLECTION)

            if (isManuallyRefreshed) {
                refreshProgressBar.isVisible = true
            }

            usersCollection.whereNotEqualTo(FieldPath.documentId(), currentUserUid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result.documents

                        SharedPreferenceUtil.setIntegerPreference(
                            this,
                            SharedPreferenceConstants.CONTACTS_COUNT,
                            result.size
                        )
                        contactRvAdapter.updateContactDataSet(result)
                        contactRvAdapter.notifyDataSetChanged()

                        if (isManuallyRefreshed) {
                            refreshProgressBar.isVisible = false
                        }

                        updateTotalContactCount()
                    }
                    Toast.makeText(this, "Your contacts list has been updated", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener {
                    if (isManuallyRefreshed) {
                        refreshProgressBar.isVisible = false
                    }

                    Toast.makeText(this, "Error refreshing contacts list", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }


    override fun onItemClicked(user: User?) {
        if (user != null) {
            val singleChatActivityIntent = Intent(this, SingleChatActivity::class.java)

            val bundle = Bundle()
            Log.d(
                ApplicationLoggingConstants.SECURITY_LOGS.toString(),
                "Contact item click: ${user.getUid()}"
            )
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
            finish()
        }
    }


}