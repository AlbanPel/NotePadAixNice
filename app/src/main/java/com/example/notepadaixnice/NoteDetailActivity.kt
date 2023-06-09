package com.example.notepadaixnice


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        val REQUEST_EDIT_NOTE = 1
        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE_NOTE = "com.example.notepadaixnice.actions.ACTION_SAVE_NOTE"
        val ACTION_DELETE_NOTE = "com.example.notepadaixnice.actions.ACTION_DELETE_NOTE"
    }

    lateinit var note: Note
    var noteIndex: Int = -1

    lateinit var titleView: TextView
    lateinit var textView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        note = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          intent.getParcelableExtra(EXTRA_NOTE, Note::class.java)!!
        }
        else{
            intent.getParcelableExtra<Note>(EXTRA_NOTE)!!
        }

        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById<TextView>(R.id.title_view_detail)
        textView = findViewById<TextView>(R.id.text_view_detail)

        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("ITEMID", "${item}")
            when (item.itemId) {
                R.id.action_save -> {
                    saveNote()
                    return true
                }
                R.id.action_delete -> {
                    showConfirmDeleteNoteDialog()
                    return true
                }
                else -> return super.onOptionsItemSelected(item)
            }
    }
    private fun showConfirmDeleteNoteDialog() {
        Log.i("DELETE note INDEX", "${noteIndex}")
        val confirmFragment = note.title?.let { ConfirmDeleteNoteFragment(it) }
        confirmFragment?.listner = object : ConfirmDeleteNoteFragment.ConfirmDeleteDialogueListner{
            override fun onDialoguePositiveClick() {
                    deleteNote()
            }

            override fun onDialogueNegativeClick() {}
        }
        if (noteIndex != -1) {
            confirmFragment?.show(supportFragmentManager, "confirmDeleteDialog")
        }
    }

    fun saveNote() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        intent = Intent(ACTION_SAVE_NOTE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun deleteNote() {
        intent = Intent(ACTION_DELETE_NOTE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}