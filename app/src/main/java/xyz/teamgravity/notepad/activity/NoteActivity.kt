package xyz.teamgravity.notepad.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.dialog_text.view.*
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.helper.Constants.Companion.EDIT_NOTE_EXTRA
import xyz.teamgravity.notepad.helper.Constants.Companion.PARCELABLE_EXTRA
import xyz.teamgravity.notepad.model.NoteModel
import xyz.teamgravity.notepad.viewmodel.NoteViewModel
import java.util.*

class NoteActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel

    private var isEdit = false

    private var note: NoteModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        getIntentExtra()
        updateUI()
        viewModel()
        button()
    }

    private fun getIntentExtra() {
        isEdit = intent.getBooleanExtra(EDIT_NOTE_EXTRA, false)

        if (isEdit)
            note = intent.getParcelableExtra(PARCELABLE_EXTRA)
    }

    private fun updateUI() {
        if (isEdit) {
            title_field.setText(note?.title)
            body_field.setText(note?.body)
        }
    }

    private fun viewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
        ).get(NoteViewModel::class.java)
    }

    private fun button() {
        onBack()
        onDone()
        onDelete()
        onShare()
    }

    // back button
    private fun onBack() {
        back_b.setOnClickListener {
            onBackPressed()
        }
    }

    // done button
    private fun onDone() {
        done_b.setOnClickListener {
            val title = title_field.text.toString()
            val body = body_field.text.toString()

            if (isEdit) {

                note?.title = title
                note?.body = body
                note?.editedTime = System.currentTimeMillis()

                viewModel.update(note!!)

            } else {

                val note = NoteModel(
                    title = title,
                    body = body,
                    createdTime = System.currentTimeMillis(),
                    editedTime = System.currentTimeMillis()
                )

                viewModel.insert(note)
            }

            finish()
        }
    }

    // delete button
    private fun onDelete() {
        if (isEdit) {
            delete_b.visibility = View.VISIBLE

            delete_b.setOnClickListener {
                val v = layoutInflater.inflate(R.layout.dialog_text, null)
                v.title_t.text = resources.getString(R.string.wanna_delete)
                v.body_t.visibility = View.INVISIBLE
                v.positive_btn.text = resources.getString(R.string.sure)
                v.negative_b.text = resources.getString(R.string.cancel)

                val dialog = MaterialAlertDialogBuilder(this)
                    .setView(v)
                    .create()

                v.positive_btn.setOnClickListener {
                    viewModel.delete(note!!)
                    finish()
                }

                v.negative_b.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }

    // share button
    private fun onShare() {
        if (isEdit) {
            share_b.visibility = View.VISIBLE

            share_b.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"

                val text = String.format(
                    Locale.getDefault(), "%s\n\n%s",
                    title_field.text.toString(),
                    body_field.text.toString()
                )

                intent.putExtra(Intent.EXTRA_TEXT, text)

                startActivity(Intent.createChooser(intent, resources.getString(R.string.choose)))
            }
        }
    }
}