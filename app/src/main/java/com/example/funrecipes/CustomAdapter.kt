package com.example.funrecipes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class CustomAdapter(private val context: Context,
                    private val dataRecords: ArrayList<HashMap<String, String>>): BaseAdapter() {

    // to create a new view/layout from the xml layout
    private val inflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // to get the size of data record
    override fun getCount(): Int {
        return dataRecords.size
    }

    // to get the item position in array index
    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // populate data accordingly
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val dataItem = dataRecords[position]

        val itemView = inflater.inflate(R.layout.list_records, parent, false)
        itemView.findViewById<TextView>(R.id.recipe_type).text = dataItem["recipe_type"]
        itemView.findViewById<TextView>(R.id.comment).text = dataItem["comment"]
        itemView.findViewById<TextView>(R.id.recorded_at).text = dataItem["recorded_at"]
        itemView.findViewById<TextView>(R.id.record_no).text = (position+1).toString()

        Picasso.get()
            .load(dataItem["image_url"])
            .resize(90,90)
            .centerCrop()
            .into(itemView.findViewById<ImageView>(R.id.food_images))

        itemView.tag = position

        return itemView
    }
}