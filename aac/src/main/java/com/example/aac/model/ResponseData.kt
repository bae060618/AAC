package com.example.aac.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "response", strict = false)
data class ResponseData(
    @field:Element(name = "header", required = false)
    var header: Header? = null,

    @field:Element(name = "body", required = false)
    var body: Body? = null
)

