package com.example.funrecipes

import android.os.Bundle
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.w3c.dom.Text


class RecipeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_details)

        val intent = intent

        val imageURL = intent.getStringExtra("image_url")
        val recipeType = intent.getStringExtra("recipe_type")
        val comment = intent.getStringExtra("comment")
        val recordedAt = intent.getStringExtra("recorded_at")


        val foodImage: ImageView = findViewById(R.id.food_images)
        val foodRecipeType: TextView = findViewById(R.id.recipe_type)
        val foodComment: TextView = findViewById(R.id.comment)
        val foodTimestamp: TextView = findViewById(R.id.recorded_at)

        foodRecipeType.text = recipeType
        foodComment.text = comment
        foodTimestamp.text = recordedAt

        Picasso.get()
            .load(imageURL)
            .resize(360,270)
            .centerCrop()
            .into(foodImage)

    }


}