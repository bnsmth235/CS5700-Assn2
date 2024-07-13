import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class TrackerViewHelper {
    private val _shipmentId = mutableStateOf("")
    val shipmentId: State<String> = _shipmentId

    private val _shipmentLocation = mutableStateOf("")
    val shipmentLocation: State<String> = _shipmentLocation

    private val _shipmentNotes = mutableStateOf(listOf<String>())
    val shipmentNotes: State<List<String>> = _shipmentNotes

    private val _shipmentUpdateHistory = mutableStateOf(listOf<String>())
    val shipmentUpdateHistory: State<List<String>> = _shipmentUpdateHistory

    private val _expectedShipmentDeliveryDate = mutableStateOf("")
    val expectedShipmentDeliveryDate: State<String> = _expectedShipmentDeliveryDate

    private val _shipmentStatus = mutableStateOf("")
    val shipmentStatus: State<String> = _shipmentStatus

    private var currentShipment: Shipment? = null

    fun trackShipment(id: String) {
        // Simulate tracking a shipment
        currentShipment = Shipment(id, "created", "N/A", "N/A")
        currentShipment?.let { shipment ->
            _shipmentId.value = shipment.id
            _shipmentStatus.value = shipment.status
            _expectedShipmentDeliveryDate.value = shipment.expectedDeliveryDate
            _shipmentNotes.value = shipment.notes
            _shipmentUpdateHistory.value = shipment.updateHistory
        }
    }

    fun stopTracking(id: String) {
        // Simulate stopping tracking of a shipment
        if (_shipmentId.value == id) {
            _shipmentId.value = ""
            _shipmentStatus.value = ""
            _expectedShipmentDeliveryDate.value = ""
            _shipmentNotes.value = listOf()
            _shipmentUpdateHistory.value = listOf()
            currentShipment = null
        }
    }

    fun addUpdate(update: ShippingUpdate) {
        currentShipment?.addUpdate(update)
        currentShipment?.let { shipment ->
            _shipmentStatus.value = shipment.status
            _expectedShipmentDeliveryDate.value = shipment.expectedDeliveryDate
            _shipmentNotes.value = shipment.notes
            _shipmentUpdateHistory.value = shipment.updateHistory
        }
    }
}
