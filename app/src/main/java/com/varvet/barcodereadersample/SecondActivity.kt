package com.varvet.barcodereadersample
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_grocery_list.*
import java.util.*


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var intent=intent

        var groceryString=intent.getStringExtra("grocery_items")?: "No items provided"
        val groceryList = groceryString.split(",").toTypedArray()
        val arrayList = groceryList.toCollection(ArrayList())
        var boolQrCode:Boolean=intent.getBooleanExtra("QR_code",false)

        setContentView(R.layout.activity_grocery_list)        // Initializing the array lists and the adapter
        lateinit var adapter:ArrayAdapter<String>
        lateinit var itemlist:ArrayList<String>
        itemlist = arrayListOf<String>()
        adapter =ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice
                , itemlist)
        if(boolQrCode) {
            itemlist = arrayList

            adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice
                    , itemlist)
            listView.adapter = adapter
        }

        add.setOnClickListener {
            itemlist.add(editText.text.toString())
            listView.adapter =  adapter
            adapter.notifyDataSetChanged()    // This is because every time when you add the item the input      space or the eidt text space will be cleared
            editText.text.clear()
        }
            // Clearing all the items in the l ist when the clear button is pressed
            clear.setOnClickListener {
                itemlist.clear()
                adapter.notifyDataSetChanged()        }         // Adding the toast message to the list when an item on the list is pressed
            listView.setOnItemClickListener { adapterView, view, i, l ->            Toast.makeText(this, "You Selected the item --> "+itemlist.get(i), Toast.LENGTH_SHORT).show()        }        // Selecting and Deleting the items from the list when the delete button is pressed
            delete.setOnClickListener {            val position: SparseBooleanArray = listView.checkedItemPositions
                val count = listView.count
                var item = count - 1
                while (item >= 0) {                if (position.get(item))
                {
                    adapter.remove(itemlist.get(item))
                }
                    item--
                }
                position.clear()
                adapter.notifyDataSetChanged()
            }
        }
//        var itemlist = arrayListOf<String>()
//        var adapter =ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_multiple_choice
//                , itemlist)

        // Adding the items to the list when the add button is pressed





    }


