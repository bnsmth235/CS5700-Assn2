class Shipment(val id: String) {
    var status: String = "created"
        private set
    val notes = mutableListOf<String>()
    val updateHistory = mutableListOf<ShippingUpdate>()
    var expectedDeliveryDateTimestamp: Long = 0L
        private set
    var currentLocation: String = ""
        private set

    fun addUpdate(update: ShippingUpdate) {
        status = update.newStatus
        updateHistory.add(update)
        if (update is ShippedUpdate) {
            expectedDeliveryDateTimestamp = update.expectedDeliveryDate
        } else if (update is DelayedUpdate) {
            expectedDeliveryDateTimestamp = update.expectedDeliveryDate
        } else if (update is LocationUpdate) {
            currentLocation = update.location
        }
        // Notify observers about the update
        notifyObservers()
    }

    private fun notifyObservers() {
        // Logic to notify observers about the shipment update
    }
}
