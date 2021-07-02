package com.sivan.cosplash.datastore.data

data class FilterOptions(
    var query : String?,
    val sort_by : String?,
    val color : String?,
    val orientation : String?,
    val content_filter : String?
) {
}