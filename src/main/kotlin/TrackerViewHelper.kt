import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

class TrackerViewHelper(private val trackingSimulator: TrackingSimulator) {
    private val _trackedShipments = mutableStateListOf<Shipment>()
    val trackedShipments: SnapshotStateList<Shipment> = _trackedShipments

    fun trackShipment(id: String) {
        try {
            val shipment = trackingSimulator.findShipment(id)
            if (shipment != null) {
                if (!_trackedShipments.contains(shipment)) {
                    _trackedShipments.add(shipment)
                    // Subscribe to changes in the shipment
                    shipment.addObserver { updatedShipment ->
                        // Update the tracked shipment in the list
                        val index = _trackedShipments.indexOfFirst { it.id == updatedShipment.id }
                        if (index != -1) {
                            _trackedShipments[index] = updatedShipment
                        }
                    }
                }
            } else {
                throw Exception("Shipment not found.")
            }
        } catch (e: Exception) {
            println("Error tracking shipment: ${e.message}")
        }
    }

    fun stopTracking(id: String) {
        val shipmentToRemove = _trackedShipments.find { it.id == id }
        shipmentToRemove?.let {
            it.removeObserver { /* Handle observer removal if needed */ }
            _trackedShipments.remove(it)
        }
    }
}
