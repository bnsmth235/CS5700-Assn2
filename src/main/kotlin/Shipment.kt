import androidx.compose.runtime.mutableStateListOf

class Shipment(
    val id: String,
    var status: String,
    var location: String,
    var expectedDeliveryDate: String,
    var notes: MutableList<String> = mutableStateListOf(),
    var updateHistory: MutableList<String> = mutableStateListOf()
) {

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
                // Handle location update if needed
            }
            is NoteAddedUpdate -> {
                notes.add(update.note)
            }
            // Add other update types as needed
        }

        notifyObservers()
    }

    private val observers = mutableListOf<(Shipment) -> Unit>()

    private fun notifyObservers() {
        observers.forEach { it(this) }
    }

    fun addObserver(observer: (Shipment) -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: (Shipment) -> Unit) {
        observers.remove(observer)
    }
}