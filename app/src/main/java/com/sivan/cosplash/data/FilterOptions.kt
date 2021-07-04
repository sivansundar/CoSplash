package com.sivan.cosplash.data

import kotlinx.serialization.Serializable

@Serializable
data class FilterOptions(
    var query : String? = null,
    var sort_by : String? = null,
    var color : String? = null,
    var orientation : String? = null,
    var content_filter : String? = null
) {

}