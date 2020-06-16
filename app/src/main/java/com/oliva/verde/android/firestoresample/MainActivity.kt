package com.oliva.verde.android.firestoresample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private val mDocRef = FirebaseFirestore
                            .getInstance()
                            .document("sampleData/inspiration")

    public var _authorKey = "author"
    public var _quoteKey = "quote"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onSaveButtonClick(view : View?) {
        val etTextQuote = findViewById<EditText>(R.id.evTextQuote)
        val etTextAuthor = findViewById<EditText>(R.id.evTextAuthor)
        val quote = etTextQuote.text.toString()
        val author = etTextAuthor.text.toString()

        if (quote.isEmpty() || author.isEmpty()) {
            Toast.makeText(applicationContext, "Enter text into the blank.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val dataToSave = HashMap<String, Any>()
            dataToSave[_quoteKey] = quote
            dataToSave[_authorKey] = author
            mDocRef.set(dataToSave) // 既存のドキュメントがあれば置き換える
                .addOnSuccessListener {
                    Log.d("FireStoreSample", "Documemt has been saved.")
                }
                .addOnFailureListener {
                    Log.d("FireStoreSample", "Documemt has NOT been saved.")
                }
        }
    }

}