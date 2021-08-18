package com.mercadolivro.repository

import com.mercadolivro.model.BookModel
import org.springframework.data.repository.CrudRepository

interface BookRepository: CrudRepository<BookModel, Int> {
}