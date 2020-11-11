package xyz.teamgravity.notepad.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_text.view.*
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.helper.Constants
import xyz.teamgravity.notepad.helper.Constants.Companion.EDIT_NOTE_EXTRA
import xyz.teamgravity.notepad.helper.Constants.Companion.LANGUAGE
import xyz.teamgravity.notepad.helper.Constants.Companion.PARCELABLE_EXTRA
import xyz.teamgravity.notepad.helper.Constants.Companion.PIN_LOCK
import xyz.teamgravity.notepad.helper.Constants.Companion.PREFS
import xyz.teamgravity.notepad.helper.NoteAdapter
import xyz.teamgravity.notepad.model.NoteModel
import xyz.teamgravity.notepad.viewmodel.NoteViewModel
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NoteAdapter.OnNoteListener {
    private lateinit var adapter: NoteAdapter

    private lateinit var viewModel: NoteViewModel

    private lateinit var shp: SharedPreferences

    private var fragment: PFLockScreenFragment? = null

    private var isFirst = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shp = getSharedPreferences(PREFS, MODE_PRIVATE)
        setLanguage()
        setContentView(R.layout.activity_main)

        // check if user logged in
        if (savedInstanceState == null) {
            pinLock()
        } else {
            isFirst = savedInstanceState.getBoolean("is_login")
            if (isFirst)
                pinLock()
        }


        recyclerView()
        viewModel()
        button()
    }

    private fun pinLock() {
        if (shp.getBoolean(PIN_LOCK, false)) {
            loginPin()
        }
    }

    private fun recyclerView() {
        recycler_view.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = NoteAdapter(this)
        recycler_view.adapter = adapter
    }

    private fun viewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        ).get(NoteViewModel::class.java)

        viewModel.getAllNotes().observe(this, {
            adapter.submitList(it)
        })
    }

    // set listeners for buttons
    private fun button() {
        onAdd()
        onMore()
        onDelete()
    }

    // delete selected notes
    private fun deleteSelected() {
        val notes: List<NoteModel> = ArrayList(adapter.selectedNoteIds)
        viewModel.deleteSelected(notes)
        adapter.clearList()
        more_b.visibility = View.VISIBLE
        add_b.visibility = View.VISIBLE
        delete_b.visibility = View.GONE
    }

    // delete all
    private fun deleteAll() {
        viewModel.deleteAll()
        Snackbar.make(main_layout, R.string.deleted_successfully, Snackbar.LENGTH_LONG)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .show()
    }

    // login pin fragment
    private fun loginPin() {
        val pinEncryption = shp.getString(Constants.PIN_HASH, "no_encryption")

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

                if (!isFirst) {
                    deleteAll()
                }
                supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(fragment!!).commit()
                fragment = null
                main_layout.visibility = View.VISIBLE
                add_b.visibility = View.VISIBLE
                isFirst = false
            }

            override fun onFingerprintSuccessful() {
                if (!isFirst) {
                    deleteAll()
                }
                supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .remove(fragment!!).commit()
                fragment = null
                main_layout.visibility = View.VISIBLE
                add_b.visibility = View.VISIBLE
                isFirst = false
            }

            override fun onPinLoginFailed() {
                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.wrong_pin),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFingerprintLoginFailed() {
                Toast.makeText(
                    this@MainActivity,
                    resources.getString(R.string.wrong_fingerprint),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        main_layout.visibility = View.GONE
        add_b.visibility = View.GONE
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment!!)
            .disallowAddToBackStack().commit()
    }

    // set language for app
    @Suppress("DEPRECATION")
    private fun setLanguage() {
        val language = shp.getString(LANGUAGE, "def")!!

        if (!language.equals("def", true)) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }

    // add note button
    private fun onAdd() {
        add_b.setOnClickListener {
            startActivity(Intent(applicationContext, NoteActivity::class.java))
        }
    }

    // more button
    private fun onMore() {
        more_b.setOnClickListener {
            val popup = PopupMenu(this, more_b)
            popup.menuInflater.inflate(R.menu.main_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_all -> {
                        val v = layoutInflater.inflate(R.layout.dialog_text, null)
                        v.title_t.text = resources.getString(R.string.wanna_delete_all)
                        v.body_t.visibility = View.INVISIBLE
                        v.positive_btn.text = resources.getString(R.string.sure)
                        v.negative_b.text = resources.getString(R.string.cancel)

                        val dialog = MaterialAlertDialogBuilder(this)
                            .setView(v)
                            .create()

                        v.positive_btn.setOnClickListener {
                            dialog.dismiss()
                            if (shp.getBoolean(PIN_LOCK, false)) {
                                loginPin()
                            } else {
                                deleteAll()
                            }
                        }

                        v.negative_b.setOnClickListener {
                            dialog.dismiss()
                        }

                        dialog.show()

                        true
                    }
                    R.id.settings -> {
                        startActivity(Intent(applicationContext, SettingsActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    // delet button
    private fun onDelete() {
        delete_b.setOnClickListener {
            val v = layoutInflater.inflate(R.layout.dialog_text, null)
            v.title_t.text = resources.getString(R.string.wanna_delete_selected)
            v.body_t.visibility = View.INVISIBLE
            v.positive_btn.text = resources.getString(R.string.sure)
            v.negative_b.text = resources.getString(R.string.cancel)

            val dialog = MaterialAlertDialogBuilder(this)
                .setView(v)
                .create()

            v.positive_btn.setOnClickListener {
                dialog.dismiss()
                deleteSelected()
            }

            v.negative_b.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    // note click
    override fun onNoteClick(note: NoteModel) {
        val intent = Intent(applicationContext, NoteActivity::class.java)
        intent.putExtra(PARCELABLE_EXTRA, note)
        intent.putExtra(EDIT_NOTE_EXTRA, true)
        startActivity(intent)
    }

    // note long click
    override fun onNoteLongClick(ticking: Boolean) {
        if (ticking) {
            more_b.visibility = View.GONE
            add_b.visibility = View.GONE
            delete_b.visibility = View.VISIBLE
        } else {
            more_b.visibility = View.VISIBLE
            add_b.visibility = View.VISIBLE
            delete_b.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (adapter.ticking) {
            adapter.clearList()
            more_b.visibility = View.VISIBLE
            add_b.visibility = View.VISIBLE
            delete_b.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        // remove fragment before recreation
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(fragment!!).commitAllowingStateLoss()
        }
        super.onSaveInstanceState(outState)
        // store if user already logged in
        outState.putBoolean("is_login", isFirst)
    }
}