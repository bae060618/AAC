package com.example.aac.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "body", strict = false)
data class Body(
    @field:Element(name = "items", required = false)
    var items: Items? = null,

    @field:Element(name = "numOfRows", required = false)
    var numOfRows: String? = null,

    @field:Element(name = "pageNo", required = false)
    var pageNo: String? = null,

    @field:Element(name = "totalCount", required = false)
    var totalCount: String? = null
)
