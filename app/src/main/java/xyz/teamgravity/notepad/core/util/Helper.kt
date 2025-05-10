package xyz.teamgravity.notepad.core.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import xyz.teamgravity.coresdkandroid.android.safelyStartActivity
import xyz.teamgravity.coresdkandroid.connect.ConnectUtil
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.core.constant.ConnectionConst

object Helper {

    private fun getShareAppText(context: Context): String {
        return context.getString(R.string.share_app, ConnectUtil.getAppPlayStorePageUrl(context))
    }

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Shares the app text with other apps.
     */
    fun shareApp(context: Context) {
        ConnectUtil.shareText(
            context = context,
            text = getShareAppText(context),
            chooserTitle = context.getString(R.string.choose)
        )
    }

    /**
     * Navigates the user to show source code on Github.
     */
    fun viewSourceCode(context: Context) {
        context.safelyStartActivity(Intent(Intent.ACTION_VIEW, ConnectionConst.GITHUB_SOURCE_CODE.toUri()))
    }

    /**
     * Navigates the user to connect us via Telegram.
     */
    fun connectViaTelegram(context: Context) {
        context.safelyStartActivity(Intent(Intent.ACTION_VIEW, ConnectionConst.SUPPORT_TELEGRAM.toUri()))
    }

    /**
     * Navigates the user to connect us via Email.
     */
    fun connectViaEmail(context: Context) {
        ConnectUtil.connectViaEmail(
            context = context,
            email = ConnectionConst.SUPPORT_MAIL,
            subject = context.getString(R.string.improvement),
            chooserTitle = context.getString(R.string.choose)
        )
    }

    /**
     * Shares note as a text.
     */
    fun shareNote(
        context: Context,
        note: String
    ) {
        ConnectUtil.shareText(
            context = context,
            text = note,
            chooserTitle = context.getString(R.string.choose)
        )
    }
}