package com.example.aac.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "header", strict = false)
data class Header(
    @field:Element(name = "resultCode", required = false)
    var resultCode: String? = null,

    @field:Element(name = "resultMsg", required = false)
    var resultMsg: String? = null
)
