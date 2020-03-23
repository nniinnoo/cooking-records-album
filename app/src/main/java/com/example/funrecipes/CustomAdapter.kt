package com.example.funrecipes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class CustomAdapter(private val context: Context,
                    private val dataRecords: ArrayList<HashMap<String, String>>): BaseAdapter() {

    private val inflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataRecords.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var dataItem = dataRecords[position]

        val itemView = inflater.inflate(R.layout.list_records, parent, false)
        itemView.findViewById<TextView>(R.id.recipe_type).text = dataItem.get("recipe_type")
        itemView.findViewById<TextView>(R.id.comment).text = dataItem.get("comment")
        itemView.findViewById<TextView>(R.id.recorded_at).text = dataItem.get("recorded_at")
        itemView.findViewById<TextView>(R.id.record_no).text = (position+1).toString()

        Picasso.get()
            .load(dataItem.get("image_url"))
            .resize(90,90)
            .centerCrop()
            .into(itemView.findViewById<ImageView>(R.id.food_images))

        itemView.tag = position
        return itemView
    }
}