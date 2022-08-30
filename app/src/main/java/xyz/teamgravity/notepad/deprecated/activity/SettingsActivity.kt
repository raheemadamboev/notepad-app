package xyz.teamgravity.notepad.deprecated.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment
import kotlinx.android.synthetic.main.activity_settings.*
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.deprecated.helper.Constants.Companion.LANGUAGE
import xyz.teamgravity.notepad.deprecated.helper.Constants.Companion.PIN_HASH
import xyz.teamgravity.notepad.deprecated.helper.Constants.Companion.PIN_LOCK
import xyz.teamgravity.notepad.deprecated.helper.Constants.Companion.PREFS
import xyz.teamgravity.notepad.deprecated.helper.Constants.Companion.SUPPORT_MAIL

class SettingsActivity : AppCompatActivity() {
    private lateinit var shp: SharedPreferences

    private var fragment: PFLockScreenFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        shp = getSharedPreferences(PREFS, MODE_PRIVATE)
        button()
        language()
        pinSwitch()
    }

    private fun button() {
        onBack()
        onAbout()
        onSuggestion()
        onShare()
        onReview()
    }

    private fun language() {
        when (shp.getString(LANGUAGE, "def")) {
            "en" -> english.isChecked = true
            "ru" -> russian.isChecked = true
            "ko" -> korean.isChecked = true
            "uz" -> uzbek.isChecked = true
            "ba" -> uzbek_kiril.isChecked = true
            "ho" -> khorezm.isChecked = true
        }

        radio_group.setOnCheckedChangeListener { _, _, _, checkedId ->
            when (checkedId) {
                R.id.english -> setLanguage("en")
                R.id.russian -> setLanguage("ru")
                R.id.korean -> setLanguage("ko")
                R.id.uzbek -> setLanguage("uz")
                R.id.uzbek_kiril -> setLanguage("ba")
                R.id.khorezm -> setLanguage("ho")
            }
        }
    }

    private fun pinSwitch() {
        lock_s.isChecked = shp.getBoolean(PIN_LOCK, false)

        lock_s.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                createPin()
            } else {
                loginPin()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setLanguage(language: String) {
        val editor = shp.edit()
        editor.putString(LANGUAGE, language)
        editor.apply()

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun createPin() {
        fragment = PFLockScreenFragment()
        val builder = PFFLockScreenConfiguration.Builder(this)
            .setTitle(resources.getString(R.string.create_pin))
            .setMode(PFFLockScreenConfiguration.MODE_CREATE)
            .setCodeLength(4)
            .setNextButton(resources.getString(R.string.pin_next))

        fragment!!.setConfiguration(builder.build())

        fragment!!.setCodeCreateListener(object :
            PFLockScreenFragment.OnPFLockScreenCodeCreateListener {
            override fun onCodeCreated(encodedCode: String?) {
                val editor = shp.edit()
                editor.putString(PIN_HASH, encodedCode)
                editor.putBoolean(PIN_LOCK, true)
                editor.apply()
                supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(fragment!!).commit()
                fragment = null
                main_layout.visibility = View.VISIBLE
            }

            override fun onNewCodeValidationFailed() {
                Toast.makeText(
                    this@SettingsActivity,
                    resources.getString(R.string.pin_error),
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        main_layout.visibility = View.GONE
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment!!)
            .disallowAddToBackStack().commit()
    }

    private fun loginPin() {
        val pinEncryption = shp.getString(PIN_HASH, "no_encryption")

        fragment = PFLockScreenFragment()
        fragment!!.setEncodedPinCode(pinEncryption)
        val builder = PFFLockScreenConfiguration.Builder(this)
            .setTitle(resources.getString(R.string.pin_header))
            .setUseFingerprint(true)
            .setMode(PFFLockScreenConfiguration.MODE_AUTH)
            .setCodeLength(4)
            .setClearCodeOnError(true)
            .setErrorAnimation(true)
            .setErrorVibration(true)

        fragment!!.setConfiguration(builder.build())

        fragment!!.setLoginListener(object : PFLockScreenFragment.OnPFLockScreenLoginListener {
            override fun onCodeInputSuccessful() {
                shp.edit().putBoolean(PIN_LOCK, false).apply()
                supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(fragment!!).commit()
                fragment = null
                main_layout.visibility = View.VISIBLE
            }

            override fun onFingerprintSuccessful() {
                shp.edit().putBoolean(PIN_LOCK, false).apply()
                supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(fragment!!).commit()
                fragment = null
                main_layout.visibility = View.VISIBLE
            }

            override fun onPinLoginFailed() {
                Toast.makeText(
                    this@SettingsActivity,
                    resources.getString(R.string.wrong_pin),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFingerprintLoginFailed() {
                Toast.makeText(
                    this@SettingsActivity,
                    resources.getString(R.string.wrong_fingerprint),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        main_layout.visibility = View.GONE
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment!!)
            .disallowAddToBackStack().commit()
    }

    // back button
    private fun onBack() {
        back_b.setOnClickListener {
            onBackPressed()
        }
    }

    // about me button
    private fun onAbout() {
        about_b.setOnClickListener {
            startActivity(Intent(applicationContext, AboutActivity::class.java))
        }
    }

    // suggestion button
    private fun onSuggestion() {
        suggestion_b.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", SUPPORT_MAIL, null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.improvement))
            startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.choose)))
        }
    }

    // share button
    private fun onShare() {
        share_b.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_app))
            startActivity(Intent.createChooser(intent, resources.getString(R.string.choose)))
        }
    }

    // review button
    private fun onReview() {
        review_b.setOnClickListener {
            val packageName = packageName
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
    }

    // remove fragment before recreation
    override fun onSaveInstanceState(outState: Bundle) {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(fragment!!).commitAllowingStateLoss()
        }
        super.onSaveInstanceState(outState)
    }
}