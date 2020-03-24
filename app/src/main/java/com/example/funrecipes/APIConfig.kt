package com.example.funrecipes

class APIConfig (limit: String) {

    // Any API configuration should put here
    public val URL_COOKING_RECORDS = "https://cooking-records.herokuapp.com/cooking_records?offset=0&limit=$limit"

}