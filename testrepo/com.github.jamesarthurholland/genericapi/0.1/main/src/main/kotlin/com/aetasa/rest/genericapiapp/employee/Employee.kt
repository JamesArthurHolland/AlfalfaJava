package com.aetasa.rest.genericapiapp.employee


class Employee(
    var id: Int?,
    var firstName: String,
    var lastName: String,
    var department: String,
    var email: String,
    var contracts: List<Contract>
) {
    constructor() : this(null, "", "", "", "", arrayListOf<Contract>())

}
