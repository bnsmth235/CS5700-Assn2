class TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()

    fun findShipment(id: String): Shipment? = shipments.find { it.id == id }

    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }

    fun runSimulation() {
        // Logic to read file and update shipments
        // For each line in the file:
        // val (type, id, timestamp, info) = parseLine(line)
        // val shipment = findShipment(id) ?: Shipment(id).also { addShipment(it) }
        // when (type) {
        //     "created" -> shipment.addUpdate(CreatedUpdate(timestamp.toLong(), shipment.status))
        //     "shipped" -> shipment.addUpdate(ShippedUpdate(timestamp.toLong(), shipment.status, info.toLong()))
        //     "location" -> shipment.addUpdate(LocationUpdate(timestamp.toLong(), shipment.status, info))
        //     "delivered" -> shipment.addUpdate(DeliveredUpdate(timestamp.toLong(), shipment.status))
        //     "delayed" -> shipment.addUpdate(DelayedUpdate(timestamp.toLong(), shipment.status, info.toLong()))
        //     "lost" -> shipment.addUpdate(LostUpdate(timestamp.toLong(), shipment.status))
        //     "canceled" -> shipment.addUpdate(CanceledUpdate(timestamp.toLong(), shipment.status))
        //     "noteadded" -> shipment.addUpdate(NoteAddedUpdate(timestamp.toLong(), shipment.status, info))
        // }
    }
}
