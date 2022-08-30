package xyz.teamgravity.notepad.deprecated.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_note.view.*
import xyz.teamgravity.notepad.R
import xyz.teamgravity.notepad.deprecated.model.NoteModel
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(private val listener: OnNoteListener) :
    ListAdapter<NoteModel, NoteAdapter.NoteViewHolder>(diffCallback) {
    val selectedNoteIds: MutableList<NoteModel> = ArrayList()
    var ticking = false

    init {
        setHasStableIds(true)
    }

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<NoteModel> =
            object : DiffUtil.ItemCallback<NoteModel>() {
                override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
                    return oldItem._id == newItem._id
                }

                override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.body == newItem.body &&
                            oldItem.createdTime == newItem.createdTime &&
                            oldItem.editedTime == newItem.editedTime
                }
            }
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleT: TextView = itemView.title_t
        val bodyT: TextView = itemView.body_t
        val timeT: TextView = itemView.edited_time_t
        val tickI: ImageView = itemView.tick_i
        val layout: LinearLayout = itemView.main_layout

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (ticking) {
                        val model = getItem(position)
                        if (selectedNoteIds.contains(model)) {
                            selectedNoteIds.remove(model)
                            layout.setBackgroundResource(R.drawable.background_card)
                            tickI.visibility = View.GONE
                        } else {
                            selectedNoteIds.add(model)
                            layout.setBackgroundResource(R.drawable.background_ticked_card)
                            tickI.visibility = View.VISIBLE
                        }
                        checkTicking()
                        listener.onNoteLongClick(ticking)
                    } else {
                        listener.onNoteClick(getItem(position))
                    }
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // if id is already added remove id and background
                    val model = getItem(position)
                    if (selectedNoteIds.contains(model)) {
                        selectedNoteIds.remove(model)
                        layout.setBackgroundResource(R.drawable.background_card)
                        tickI.visibility = View.GONE
                    } else {
                        selectedNoteIds.add(model)
                        layout.setBackgroundResource(R.drawable.background_ticked_card)
                        tickI.visibility = View.VISIBLE
                    }

                    checkTicking()
                    listener.onNoteLongClick(ticking)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_note, parent, false)
        return NoteViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val model = getItem(position)
        holder.titleT.text = model.title
        holder.bodyT.text = model.body
        holder.timeT.text = formatTime(model.editedTime)

        if (selectedNoteIds.contains(model)) {
            holder.layout.setBackgroundResource(R.drawable.background_ticked_card)
            holder.tickI.visibility = View.VISIBLE
        } else {
            holder.layout.setBackgroundResource(R.drawable.background_card)
            holder.tickI.visibility = View.GONE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)._id!!
    }

    private fun formatTime(time: Long): String {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(time))
    }

    private fun checkTicking() {
        ticking = selectedNoteIds.size > 0
    }

    fun clearList() {
        selectedNoteIds.clear()
        ticking = false
        notifyDataSetChanged()
    }

    interface OnNoteListener {
        fun onNoteClick(note: NoteModel)
        fun onNoteLongClick(ticking: Boolean)
    }
}