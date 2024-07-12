class TrackerViewHelper {
    var shipmentId: String = ""
        private set
    var shipmentNotes: List<String> = listOf()
        private set
    var shipmentUpdateHistory: List<String> = listOf()
        private set
    var expectedShipmentDeliveryDate: String = ""
        private set
    var shipmentStatus: String = ""
        private set

    private val observers = mutableListOf<(Shipment) -> Unit>()

    fun addObserver(observer: (Shipment) -> Unit) {
        observers.add(observer)
    }

    private fun notifyObservers(shipment: Shipment) {
        observers.forEach { it(shipment) }
    }

    fun trackShipment(id: String) {
        val shipment = TrackingSimulator.findShipment(id)
        if (shipment != null) {
            shipmentId = id
            shipmentNotes = shipment.notes
            shipmentUpdateHistory = shipment.updateHistory.map { update ->
                "Shipment went from ${update.previousStatus} to ${update.newStatus} on ${update.updateTimeStamp}"
            }
            expectedShipmentDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString()
            shipmentStatus = shipment.status
            addObserver { updatedShipment ->
                shipmentNotes = updatedShipment.notes
                shipmentUpdateHistory = updatedShipment.updateHistory.map { update ->
                    "Shipment went from ${update.previousStatus} to ${update.newStatus} on ${update.updateTimeStamp}"
                }
                expectedShipmentDeliveryDate = updatedShipment.expectedDeliveryDateTimestamp.toString()
                shipmentStatus = updatedShipment.status
                // Trigger UI update here if needed
            }
            notifyObservers(shipment)
        } else {
            // Handle shipment not found
        }
    }

    fun stopTracking(id: String) {
        // Logic to stop tracking shipment
        // Remove observer or clear state if needed
    }
}
