package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val pageCustomers = PageImpl(fakeCustomers)
        var pageable = PageRequest.of(0, 10)

        every { customerRepository.findAll(pageable) } returns pageCustomers
        val customers = customerService.getAll(null, pageable)

        assertEquals(pageCustomers.content, customers.content)

        //desta maneira verifica se o metodo foi chamado exatamente uma vez
        verify(exactly = 1) { customerRepository.findAll(pageable) }

        //desta maneira eu garanto q nao caiu dentro do if que chama esse metodo
        verify(exactly = 0) { customerRepository.findByNameContaining(any(), any()) }
    }

    @Test
    fun `should return customers when name is informed`() {
        val name = UUID.randomUUID().toString()
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val pageCustomers = PageImpl(fakeCustomers)
        var pageable = PageRequest.of(0, 10)

        every { customerRepository.findByNameContaining(name, pageable) } returns pageCustomers
        val customers = customerService.getAll(name, pageable)

        assertEquals(pageCustomers.content, customers.content)

        //desta maneira verifica se o metodo nao foi chamado
        verify(exactly = 0) { customerRepository.findAll(pageable) }

        //desta maneira eu garanto q nao caiu chamou este metodo uma vez
        verify(exactly = 1) { customerRepository.findByNameContaining(name, pageable) }
    }

    fun buildCustomer(
        id: Int? = null,
        name: String = "customer name",
        email: String = "${UUID.randomUUID()}@email.com",
        password: String = "password"
    ) = CustomerModel(
        id = id,
        name = name,
        email = email,
        status = CustomerStatus.ATIVO,
        password = password,
        roles = setOf(Role.CUSTOMER)
    )
}