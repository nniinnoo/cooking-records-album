package com.example.funrecipes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    var dataRecords = ArrayList<HashMap<String, String>>()

    // Default value records amount
    var recordsAmount = "5"

    // Default value recipe type
    var chosenRecipeType = "all"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /// SPINNER FEATURE
        val spinner: Spinner = findViewById(R.id.record_amount)

        // Create an arrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.record_amount,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            // Apply the adapter to the spinner
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    val newRecordsAmount = parent?.getItemAtPosition(position).toString()

                    // Compare with the previous amount choice
                    if (recordsAmount != newRecordsAmount) {
                        recordsAmount = newRecordsAmount
                        dataRecords.clear()
                        GetCookingRecordsData().execute()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // not yet implemented
                }
            }
        }


        /// TAB FEATURE
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Soup"))
        tabLayout.addTab(tabLayout.newTab().setText("Main Dish"))
        tabLayout.addTab(tabLayout.newTab().setText("Side Dish"))

        // Define the value of tab accordingly
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    0 -> chosenRecipeType = "all"
                    1 -> chosenRecipeType = "soup"
                    2 -> chosenRecipeType = "main_dish"
                    3 -> chosenRecipeType = "side_dish"
                }
                dataRecords.clear() // To empty the data records ArrayList
                GetCookingRecordsData().execute()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // not yet implemented
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // not yet implemented
            }

        })

        GetCookingRecordsData().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class GetCookingRecordsData : AsyncTask<String, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            // To show loading bar during background process (Set: VIEW.VISIBLE)
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            // Setup the API in background
            return URL(APIConfig(recordsAmount).URL_COOKING_RECORDS).readText(Charsets.UTF_8)
        }

        @SuppressLint("DefaultLocale")
        override fun onPostExecute(result: String) {

            super.onPostExecute(result)

            // To stop loading bar after finishing the background process (Set: VIEW.GONE)
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE

            val jsonObj = JSONObject(result)
            val dataRecordsArr = jsonObj.getJSONArray("cooking_records")

            for (i in 0 until dataRecordsArr.length()){

                val item = dataRecordsArr.getJSONObject(i)

                val map = HashMap<String, String>()

                // To sort the data based on Recipe Type
                // this would be better if the API provide recipe_type param
                if (item.getString("recipe_type") == chosenRecipeType
                    || "all" == chosenRecipeType) {
                    map["image_url"] = item.getString("image_url")
                    map["recipe_type"] = item.getString("recipe_type").replace("_", " ").capitalize()
                    map["comment"] = item.getString("comment")
                    map["recorded_at"] = item.getString("recorded_at")
                    dataRecords.add(map)
                }
            }

            //
            val lv = findViewById<ListView>(R.id.listview)

            lv.adapter = CustomAdapter(this@MainActivity, dataRecords)

            lv.onItemClickListener = AdapterView.OnItemClickListener{
                    _: AdapterView<*>?, _: View?, position: Int, _: Long ->

                val intent = Intent(this@MainActivity, RecipeDetailsActivity::class.java)
                val dr = dataRecords[position]
                intent.putExtra("image_url", dr.getValue("image_url"))
                intent.putExtra("recipe_type", dr.getValue("recipe_type"))
                intent.putExtra("comment", dr.getValue("comment"))
                intent.putExtra("recorded_at", dr.getValue("recorded_at"))

                startActivity(intent)
            }
        }
    }
}
