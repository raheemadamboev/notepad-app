package xyz.teamgravity.notepad.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_settings.back_b
import xyz.teamgravity.notepad.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        button()
    }

    private fun button() {
        onBack()
        onGithub()
    }

    // back button
    private fun onBack() {
        back_b.setOnClickListener {
            onBackPressed()
        }
    }

    // github button
    private fun onGithub() {
        github_b.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/raheemadam/notepad-simple.git")
                )
            )
        }
    }
}