package com.aetasa.rest.genericapiapp.employee

class Contract(
    var id: Int?,
    var title: String,
    var date: Int,
    @Transient var employee: Employee?
) {
    constructor() : this(null, "", 0, null)
}
