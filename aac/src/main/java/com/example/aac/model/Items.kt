package com.example.aac.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "items", strict = false)
data class Items(
    @field:ElementList(entry = "item", inline = true, required = false)
    var itemList: List<RecommendedBook>? = null
)

