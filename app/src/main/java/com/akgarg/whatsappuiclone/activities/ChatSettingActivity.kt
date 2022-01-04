package com.akgarg.whatsappuiclone.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.forEach
import com.akgarg.whatsappuiclone.R
import com.akgarg.whatsappuiclone.constants.ApplicationConstants
import com.akgarg.whatsappuiclone.constants.ApplicationLoggingConstants
import com.akgarg.whatsappuiclone.constants.SharedPreferenceConstants
import com.akgarg.whatsappuiclone.utils.SharedPreferenceUtil
import java.util.*

class ChatSettingActivity : AppCompatActivity() {

    private lateinit var themeContainer: RelativeLayout
    private lateinit var enterKeySendContainer: RelativeLayout
    private lateinit var mediaVisibilityContainer: RelativeLayout
    private lateinit var keepChatsArchiveContainer: RelativeLayout
    private lateinit var fontSizeContainer: RelativeLayout
    private lateinit var appLanguageContainer: RelativeLayout

    private lateinit var currentThemeTextView: TextView
    private lateinit var currentChatFontSizeTextView: TextView
    private lateinit var chatSettingAppLanguageTextView: TextView

    private lateinit var enterKeySendSwitch: SwitchCompat
    private lateinit var mediaVisibilitySwitch: SwitchCompat
    private lateinit var keepChatsArchiveSwitch: SwitchCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chats"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.tab_layout_bg_color,
                    theme
                )
            )
        )

        themeContainer = findViewById(R.id.chatSettingsPageThemeContainer)
        enterKeySendContainer = findViewById(R.id.chatSettingsEnterKeySendContainer)
        mediaVisibilityContainer = findViewById(R.id.chatSettingsMediaVisibilityContainer)
        keepChatsArchiveContainer = findViewById(R.id.chatSettingsKeepChatsArchiveContainer)
        fontSizeContainer = findViewById(R.id.chatSettingsFontSizeContainer)
        appLanguageContainer = findViewById(R.id.chatSettingsPageAppLanguageContainer)
        currentThemeTextView = findViewById(R.id.currentThemeTextView)
        currentChatFontSizeTextView = findViewById(R.id.currentChatFontSizeTextView)
        chatSettingAppLanguageTextView = findViewById(R.id.chatSettingAppLanguageText)
        enterKeySendSwitch = findViewById(R.id.enterKeyIsSendSwitch)
        mediaVisibilitySwitch = findViewById(R.id.mediaVisibilitySwitch)
        keepChatsArchiveSwitch = findViewById(R.id.keepChatsArchiveSwitch)

        themeContainer.setOnClickListener { themeContainerClickHandler() }
        enterKeySendContainer.setOnClickListener { enterKeySendContainerClickHandler() }
        mediaVisibilityContainer.setOnClickListener { mediaVisibilityContainerClickHandler() }
        keepChatsArchiveContainer.setOnClickListener { keepChatsArchiveContainerClickHandler() }
        fontSizeContainer.setOnClickListener { fontSizeContainerClickHandler() }
        appLanguageContainer.setOnClickListener { appLanguageContainerClickHandler() }

        enterKeySendSwitch.setOnClickListener { enterKeySendSwitchClickHandler() }
        mediaVisibilitySwitch.setOnClickListener { mediaVisibilitySwitchClickHandler() }
        keepChatsArchiveSwitch.setOnClickListener { keepChatsArchiveSwitchClickHandler() }
    }


    @SuppressLint("InflateParams")
    private fun appLanguageContainerClickHandler() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.appThemeChooserDialogTheme)
        alertDialogBuilder.setTitle("App Language")

        val deviceLanguage = Locale.getDefault().displayLanguage
        val view = layoutInflater.inflate(R.layout.app_language_selector_dialog_layout, null)
        view.findViewById<RadioButton>(R.id.deviceLanguage).text =
            getString(R.string.lan_device, deviceLanguage)
        val radioGroup = view.findViewById<RadioGroup>(R.id.appLanguageRadioGroup)
        radioGroup.check(getCurrentAppLanguageId())

        alertDialogBuilder.setView(view)
        val alertDialog = alertDialogBuilder.create()

        radioGroup.setOnCheckedChangeListener { group, _ ->
            group.forEach { rb ->
                val radioButton = rb as RadioButton
                if (radioGroup.checkedRadioButtonId == radioButton.id) {
                    chatSettingAppLanguageTextView.text = radioButton.text
                    SharedPreferenceUtil.setStringPreference(
                        this,
                        SharedPreferenceConstants.CURRENT_APP_LANGUAGE,
                        radioButton.text.toString()
                    )
                    updateCurrentAppLanguageText()
                }
            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


    private fun getCurrentAppLanguageId(): Int {
        val currentAppLanguage = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.CURRENT_APP_LANGUAGE
        )

        return when (currentAppLanguage) {
            getString(R.string.lan_english) -> R.id.englishLanguage
            getString(R.string.lan_hindi) -> R.id.hindiLanguage
            getString(R.string.lan_gujarati) -> R.id.gujaratiLanguage
            getString(R.string.lan_marathi) -> R.id.marathiLanguage
            getString(R.string.lan_punjabi) -> R.id.punjabiLanguage
            else -> R.id.deviceLanguage
        }
    }


    override fun onResume() {
        super.onResume()
        chatSettingAppLanguageTextView.text =
            getString(R.string.lan_device, Locale.getDefault().displayLanguage)
        updateCurrentThemeText()
        updateAllSwitches()
        updateCurrentChatFontSizeText()
        updateCurrentAppLanguageText()
    }


    private fun updateCurrentAppLanguageText() {
        val currentAppLanguage = SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.CURRENT_APP_LANGUAGE
        )

        if (currentAppLanguage != null) {
            chatSettingAppLanguageTextView.text = currentAppLanguage
        } else {
            println(Locale.getDefault().displayLanguage)
            chatSettingAppLanguageTextView.text =
                getString(R.string.lan_device, Locale.getDefault().displayLanguage)
        }
    }


    private fun keepChatsArchiveSwitchClickHandler() {
        if (keepChatsArchiveSwitch.isChecked) {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.KEEP_CHATS_ARCHIVED,
                true
            )
        } else {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.KEEP_CHATS_ARCHIVED,
                false
            )
        }
    }


    private fun mediaVisibilitySwitchClickHandler() {
        if (mediaVisibilitySwitch.isChecked) {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.MEDIA_VISIBILITY,
                true
            )
        } else {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.MEDIA_VISIBILITY,
                false
            )
        }
    }


    private fun enterKeySendSwitchClickHandler() {
        if (enterKeySendSwitch.isChecked) {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.ENTER_KEY_IS_SEND,
                true
            )
        } else {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.ENTER_KEY_IS_SEND,
                false
            )
        }
    }


    private fun fontSizeContainerClickHandler() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.appThemeChooserDialogTheme)
        alertDialogBuilder.setTitle("Font size")

        val view = layoutInflater.inflate(R.layout.font_size_selector_dialog_layout, null)
        alertDialogBuilder.setView(view)
        val alertDialog = alertDialogBuilder.create()

        val radioGroup: RadioGroup = view.findViewById(R.id.chatFontSizeRadioGroup)
        radioGroup.check(getCurrentChatFontSizeId())

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            Log.d(
                ApplicationLoggingConstants.RADIO_BUTTON_CLICK_CHANGE.toString(),
                "Radio Group Changed"
            )
            updateChatFontSize(checkedId)
            updateCurrentChatFontSizeText()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


    private fun updateChatFontSize(checkedId: Int) {
        val fontSize: String = when (checkedId) {
            R.id.chatFontSizeSmall -> SharedPreferenceConstants.SMALL_CHAT_FONT_SIZE
            R.id.chatFontSizeLarge -> SharedPreferenceConstants.LARGE_CHAT_FONT_SIZE
            else -> SharedPreferenceConstants.MEDIUM_CHAT_FONT_SIZE
        }

        SharedPreferenceUtil.setStringPreference(
            this,
            SharedPreferenceConstants.CHAT_FONT_SIZE,
            fontSize
        )
    }


    private fun getCurrentChatFontSizeId(): Int {
        return when (SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.CHAT_FONT_SIZE
        )) {
            SharedPreferenceConstants.SMALL_CHAT_FONT_SIZE -> R.id.chatFontSizeSmall
            SharedPreferenceConstants.LARGE_CHAT_FONT_SIZE -> R.id.chatFontSizeLarge
            else -> R.id.chatFontSizeMedium
        }
    }


    private fun updateCurrentChatFontSizeText() {
        when (SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.CHAT_FONT_SIZE
        )) {
            SharedPreferenceConstants.SMALL_CHAT_FONT_SIZE -> currentChatFontSizeTextView.text =
                ApplicationConstants.CHAT_FONT_SMALL
            SharedPreferenceConstants.LARGE_CHAT_FONT_SIZE -> currentChatFontSizeTextView.text =
                ApplicationConstants.CHAT_FONT_LARGE
            else -> currentChatFontSizeTextView.text =
                ApplicationConstants.CHAT_FONT_MEDIUM
        }
    }


    private fun updateAllSwitches() {
        val enterSend = SharedPreferenceUtil.getBooleanPreference(
            this,
            SharedPreferenceConstants.ENTER_KEY_IS_SEND
        )
        val mediaVisibility = SharedPreferenceUtil.getBooleanPreference(
            this,
            SharedPreferenceConstants.MEDIA_VISIBILITY
        )
        val keepChatsArchived = SharedPreferenceUtil.getBooleanPreference(
            this,
            SharedPreferenceConstants.KEEP_CHATS_ARCHIVED
        )

        enterKeySendSwitch.isChecked = enterSend
        mediaVisibilitySwitch.isChecked = mediaVisibility
        keepChatsArchiveSwitch.isChecked = keepChatsArchived
    }


    private fun keepChatsArchiveContainerClickHandler() {
        if (keepChatsArchiveSwitch.isChecked) {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.KEEP_CHATS_ARCHIVED,
                false
            )
            keepChatsArchiveSwitch.isChecked = false
        } else {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.KEEP_CHATS_ARCHIVED,
                true
            )
            keepChatsArchiveSwitch.isChecked = true
        }
    }


    private fun mediaVisibilityContainerClickHandler() {
        if (mediaVisibilitySwitch.isChecked) {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.MEDIA_VISIBILITY,
                false
            )
            mediaVisibilitySwitch.isChecked = false
        } else {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.MEDIA_VISIBILITY,
                true
            )
            mediaVisibilitySwitch.isChecked = true
        }
    }


    private fun enterKeySendContainerClickHandler() {
        if (enterKeySendSwitch.isChecked) {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.ENTER_KEY_IS_SEND,
                false
            )
            enterKeySendSwitch.isChecked = false
        } else {
            SharedPreferenceUtil.setBooleanPreference(
                this,
                SharedPreferenceConstants.ENTER_KEY_IS_SEND,
                true
            )
            enterKeySendSwitch.isChecked = true
        }
    }


    private fun updateCurrentThemeText() {
        when (SharedPreferenceUtil.getStringPreference(this, SharedPreferenceConstants.APP_THEME)) {
            SharedPreferenceConstants.THEME_DARK -> currentThemeTextView.text =
                ApplicationConstants.THEME_DARK
            SharedPreferenceConstants.THEME_LIGHT -> currentThemeTextView.text =
                ApplicationConstants.THEME_LIGHT
            SharedPreferenceConstants.THEME_SYSTEM -> currentThemeTextView.text =
                ApplicationConstants.THEME_SYSTEM
            else -> currentThemeTextView.text =
                ApplicationConstants.THEME_SYSTEM
        }
    }


    @SuppressLint("InflateParams")
    private fun themeContainerClickHandler() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.appThemeChooserDialogTheme)
        val view = layoutInflater.inflate(R.layout.app_theme_selector_dialog_layout, null)
        alertDialogBuilder.setView(view)
        val radioGroup: RadioGroup = view.findViewById(R.id.appThemeRadioGroup)
        radioGroup.check(getCurrentThemeId())

        alertDialogBuilder
            .setTitle("Choose theme")
            .setPositiveButton("OK") { dialog, _ ->
                handleAppThemeChange(radioGroup.checkedRadioButtonId)
                dialog.dismiss()
            }
            .setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#4DAC9C"))
        }
        alertDialog.show()
    }


    private fun getCurrentThemeId(): Int {
        return when (SharedPreferenceUtil.getStringPreference(
            this,
            SharedPreferenceConstants.APP_THEME
        )) {
            SharedPreferenceConstants.THEME_DARK -> R.id.appThemeDark
            SharedPreferenceConstants.THEME_LIGHT -> R.id.appThemeLight
            else -> R.id.appThemeSystemDefault
        }
    }


    private fun handleAppThemeChange(checkedRadioButtonId: Int) {
        when (checkedRadioButtonId) {
            R.id.appThemeLight -> {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.APP_THEME,
                    SharedPreferenceConstants.THEME_LIGHT
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            R.id.appThemeDark -> {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.APP_THEME,
                    SharedPreferenceConstants.THEME_DARK
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            R.id.appThemeSystemDefault -> {
                SharedPreferenceUtil.setStringPreference(
                    this,
                    SharedPreferenceConstants.APP_THEME,
                    SharedPreferenceConstants.THEME_SYSTEM
                )
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }

        updateCurrentThemeText()
    }

}