package ru.itmo.coffee.store.dao

import java.time.DayOfWeek
import java.time.LocalTime

class ScheduleComponent(val id: Long, name: String?, val schedule: Schedule, val order: Order,
                        val dayOfWeek: DayOfWeek, val time: LocalTime)