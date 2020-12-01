package coffee.store.entity

data class Address(
        private var id: Long,
        private var country: String?,
        private var subject: String?,
        private var municipalDistrict: String?,
        private var settlement: String?,
        private var humanSettlement: String?,
        private var planningStructure: String?,
        private var street: String?,
        private var landPlot: String?,
        private var building: String?,
        private var room: String?
)