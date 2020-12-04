package coffee.store.entity

import javax.persistence.*

@Table(name = "адрес")
@Entity
data class Address(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @Column(name = "страна")
        val country: String? = null,
        @Column(name = "субъект")
        val subject: String? = null,
        @Column(name = "муниципальный_район")
        val municipalDistrict: String? = null,
        @Column(name = "поселение")
        val settlement: String? = null,
        @Column(name = "населенный_пункт")
        val humanSettlement: String? = null,
        @Column(name = "планировочная_структура")
        val planningStructure: String? = null,
        @Column(name = "улица")
        val street: String? = null,
        @Column(name = "номер_земельного_участка")
        val landPlot: String? = null,
        @Column(name = "номер_здания")
        val building: String? = null,
        @Column(name = "номер_помещения")
        val room: String? = null
)