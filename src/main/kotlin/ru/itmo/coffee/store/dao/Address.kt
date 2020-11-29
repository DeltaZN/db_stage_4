package ru.itmo.coffee.store.dao

class Address(var id: Long,
              val country: String?,
              val subject: String?,
              val municipalDistrict: String?,
              val settlement: String?,
              val humanSettlement: String?,
              val planningStructure: String?,
              val street: String?,
              val landPlot: String?,
              val building: String?,
              val room: String?)