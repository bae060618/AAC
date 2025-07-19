package com.example.aac.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class RecommendedBook(
    @field:Element(name = "title", required = false)
    var title: String? = null,

    @field:Element(name = "creator", required = false)
    var creator: String? = null,

    @field:Element(name = "regDate", required = false)
    var regDate: String? = null,

    @field:Element(name = "subjectCategory", required = false)
    var subjectCategory: String? = null,

    @field:Element(name = "description", required = false)
    var description: String? = null
)
