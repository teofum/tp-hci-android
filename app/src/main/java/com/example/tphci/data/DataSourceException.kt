package com.example.tphci.data

class DataSourceException(
    var code: Int,
    message: String,
) : Exception(message)