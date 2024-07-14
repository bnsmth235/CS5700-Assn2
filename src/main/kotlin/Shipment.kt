import androidx.compose.runtime.mutableStateListOf

class Shipment(
    val id: String,
    var status: String,
    var location: String,
    var expectedDeliveryDate: String,
    var notes: MutableList<String> = mutableStateListOf(),
    var updateHistory: MutableList<String> = mutableStateListOf()
) {

    // Observer pattern implementation
    private val observers = mutableListOf<(Shipment) -> Unit>()

    fun addObserver(observer: (Shipment) -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: (Shipment) -> Unit) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it(this) }
    }

    // Method to add an update and notify observers
    fun addUpdate(update: ShippingUpdate) {
        status = update.newStatus
        updateHistory.add("Shipment went from ${update.previousStatus} to ${update.newStatus} on ${update.updateTimeStamp}")

        when (update) {
            is ShippedUpdate -> {
                expectedDeliveryDate = update.expectedDeliveryDate.toString()
            }
            is DelayedUpdate -> {
                expectedDeliveryDate = update.expectedDeliveryDate.toString()
            }
            is LocationUpdate -> {
                location = update.location
            }
            is NoteAddedUpdate -> {
                notes.add(update.note)
            }
            // Add other update types as needed
        }

        notifyObservers()
    }
}
