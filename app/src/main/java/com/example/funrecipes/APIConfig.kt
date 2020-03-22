package com.example.funrecipes

class APIConfig (limit: String) {

    public val URL_COOKING_RECORDS = "https://cooking-records.herokuapp.com/cooking_records?offset=5&limit=$limit"

}