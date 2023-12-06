package com.example.einkaufliste1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


import com.example.einkaufliste1.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var lvTodoList: ListView
    private lateinit var fab: FloatingActionButton
    //private lateinit var clo: FloatingActionButton
    private lateinit var shoppingItems: ArrayList<String>
    private lateinit var itemAdapter: ArrayAdapter<String>

    //-----------------------------------------------------
    private lateinit var sharedPreferences: SharedPreferences
    //-----------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lvTodoList = findViewById(R.id.lvTodoListe)
        fab = findViewById(R.id.fabfloatingActionButtonPlus)
        shoppingItems = ArrayList()
        itemAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, shoppingItems)
        lvTodoList.adapter = itemAdapter



        /*
        lvTodoList.setOnItemClickListener { _, _, position, _ ->
            val textView = lvTodoList.getChildAt(position) as TextView
            textView.textSize = 20f // Passe hier die gewünschte Schriftgröße an
        }
        */


        sharedPreferences = getSharedPreferences("NoteData", Context.MODE_PRIVATE)


        // Laden der gespeicherten Einkaufsliste beim Start der App
        val storedShoppingItems = sharedPreferences.getStringSet("shoppingItems", setOf())
        shoppingItems.addAll(storedShoppingItems.orEmpty())
        itemAdapter.notifyDataSetChanged()

        lvTodoList.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, pos, _ ->
                shoppingItems.removeAt(pos)
                saveShoppingItems()
                Toast.makeText(applicationContext, "Element gelöscht", Toast.LENGTH_SHORT).show()
                itemAdapter.notifyDataSetChanged()
                true
            }

        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hinzufügen")

            val input = EditText(this)
            input.hint = " Bitte Text eingeben"
            input.inputType = InputType.TYPE_CLASS_TEXT
            input.gravity = Gravity.LEFT
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ ->
                val newItem = input.text.toString()
                shoppingItems.add(newItem)
                saveShoppingItems()
                itemAdapter.notifyDataSetChanged()
            }

            builder.setNegativeButton("Abbrechen") { _, _ ->
                Toast.makeText(applicationContext, "Abgebrochen", Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }

    }

    private fun saveShoppingItems() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("shoppingItems", shoppingItems.toSet())
        editor.apply()


    }
}

