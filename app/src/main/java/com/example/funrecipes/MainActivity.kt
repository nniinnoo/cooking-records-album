package com.example.funrecipes

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import java.net.URL
import com.example.funrecipes.APIConfig
import kotlinx.android.synthetic.*

enum class recipe_types(val kind: String) {
    SOUP("Soup"),
    MAIN_DISH("Main Dish"),
    SIDE_DISH("Side Dish"),
}

class MainActivity : AppCompatActivity() {

    var dataRecords = ArrayList<HashMap<String, String>>()

    var recordsAmount = "5"

    var choosenRecipeType = "all"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.record_amount)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.record_amount,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter

            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val newRecordsAmount = parent?.getItemAtPosition(position).toString()

                    if (recordsAmount != newRecordsAmount) {
                        recordsAmount = newRecordsAmount
                        Log.d("data has changed", newRecordsAmount)
                        dataRecords.clear()
                        getCookingRecordsData().execute()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }


        var tabLayout: TabLayout? = null

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Soup"))
        tabLayout.addTab(tabLayout.newTab().setText("Main Dish"))
        tabLayout.addTab(tabLayout.newTab().setText("Side Dish"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    0 -> choosenRecipeType = "all"
                    1 -> choosenRecipeType = "soup"
                    2 -> choosenRecipeType = "main_dish"
                    3 -> choosenRecipeType = "side_dish"
                }
                dataRecords.clear()
                getCookingRecordsData().execute()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

        getCookingRecordsData().execute()
    }

    inner class getCookingRecordsData : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            return URL(APIConfig(recordsAmount).URL_COOKING_RECORDS).readText(Charsets.UTF_8)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE


            val jsonObj = JSONObject(result)
            val dataRecordsArr = jsonObj.getJSONArray("cooking_records")

            for (i in 0 until dataRecordsArr.length()){

                val item = dataRecordsArr.getJSONObject(i)

                val map = HashMap<String, String>()

                if (item.getString("recipe_type") == choosenRecipeType
                    || "all" == choosenRecipeType) {
                    map["recipe_type"] = item.getString("recipe_type").replace("_", " ").capitalize()
                    map["comment"] = item.getString("comment")
                    map["recorded_at"] = item.getString("recorded_at")
                    map["image_url"] = item.getString("image_url")
                    dataRecords.add(map)
                }
            }

            findViewById<ListView>(R.id.listview).adapter = CustomAdapter(this@MainActivity, dataRecords)
        }
    }
}
