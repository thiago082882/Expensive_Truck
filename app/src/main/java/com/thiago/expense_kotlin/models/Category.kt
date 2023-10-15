package com.thiago.expense_kotlin.models

class Category {
    var categoryName: String = ""
    var categoryImage: Int = 0
    var categoryColor: Int = 0

    constructor() {}

    constructor(categoryName: String, categoryImage: Int, categoryColor: Int) {
        this.categoryName = categoryName
        this.categoryImage = categoryImage
        this.categoryColor = categoryColor
    }
}
