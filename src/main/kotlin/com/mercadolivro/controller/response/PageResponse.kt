package com.mercadolivro.controller.response

class PageResponse<T>(
    var items: List<T>,
    var totalPages: Int,
    var currentPage: Int,
    var totalItems: Long
) {
}