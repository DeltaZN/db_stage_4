package ru.itmo.coffee.store.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.itmo.coffee.store.dao.Address
import ru.itmo.coffee.store.repository.impl.JdbcAddressRepository

@RestController
class TestController(private val addresses: JdbcAddressRepository) {
    @GetMapping("/{id}")
    fun test(@PathVariable id: Long): Address? {
        return addresses.findById(id)
    }

    @GetMapping("/mnogo")
    fun test2(): List<Address> {
        return addresses.findAll()
    }
}