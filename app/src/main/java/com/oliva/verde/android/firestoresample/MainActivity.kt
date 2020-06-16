package com.oliva.verde.android.firestoresample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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

    override fun onStart() {
        super.onStart()
        // 第一引数にコンテキストを指定し、Activityがストップしたらリスナをdetachする
        mDocRef.addSnapshotListener(this@MainActivity, object : EventListener<DocumentSnapshot> {
            override fun onEvent(p0: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
                Log.d("FireStoreSample", "Successed to get quote from Documemt.")
                // it : DocumentSnapshotオブジェクト
                // DocumentReferenceから得られるスナップショットで、単一のドキュメントのデータを保持している
                val quote = p0?.get(_quoteKey).toString()
                val author = p0?.get(_authorKey).toString()
                val tvQuote = findViewById<TextView>(R.id.tvTextQuote)
                val tvAuthor = findViewById<TextView>(R.id.tvTextAuthor)
                tvQuote.text = quote
                tvAuthor.text = author
            }
        })
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

    fun onFetchButtonClick(view: View) {
        val tvQuote = findViewById<TextView>(R.id.tvTextQuote)
        val tvAuthor = findViewById<TextView>(R.id.tvTextAuthor)
        mDocRef.get()
            .addOnSuccessListener {
                Log.d("FireStoreSample", "Successed to get quote from Documemt.")
                // it : DocumentSnapshotオブジェクト
                // DocumentReferenceから得られるスナップショットで、単一のドキュメントのデータを保持している
                val quote = it[_quoteKey]?.toString()
                val author = it[_authorKey]?.toString()
                tvQuote.text = quote
                tvAuthor.text = author
            }
            .addOnFailureListener {
                Log.d("FireStoreSample", "Failed to get quote from Documemt.")
            }
    }

}