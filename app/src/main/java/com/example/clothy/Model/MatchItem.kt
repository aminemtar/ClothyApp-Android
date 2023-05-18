package com.example.clothy.Model

class MatchItem {
    var id: String? = null
    var idR: String? = null
    var name: String? = null
    var content: String? = null
    var count = 0
    var picture: String? = null


    constructor() {}
    constructor(
        id: String?,
        idR: String?,
        name: String?,
        content: String?,
        count: Int,
        picture: String?
    ) {
        this.id = id
        this.idR = idR
        this.name = name
        this.content = content
        this.count = count
        this.picture = picture
    }
}