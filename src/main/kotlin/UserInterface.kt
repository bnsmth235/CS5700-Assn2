import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.reflect.KClass

class UserInterface(
    private val trackerViewHelper: TrackerViewHelper,
    private val shipmentUpdates: Map<String, KClass<out ShippingUpdate>>
) {

    @Composable
    fun DisplayUI() {
        var trackingId by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var shipmentNotes by remember { mutableStateOf(listOf<String>()) }
        var shipmentUpdateHistory by remember { mutableStateOf(listOf<String>()) }
        var expectedShipmentDeliveryDate by remember { mutableStateOf("") }
        var shipmentStatus by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = trackingId,
                onValueChange = { trackingId = it },
                label = { Text("Enter Tracking ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(
                    onClick = {
                        errorMessage = ""
                        try {
                            trackerViewHelper.trackShipment(trackingId)
                            shipmentNotes = trackerViewHelper.shipmentNotes
                            shipmentUpdateHistory = trackerViewHelper.shipmentUpdateHistory
                            expectedShipmentDeliveryDate = trackerViewHelper.expectedShipmentDeliveryDate
                            shipmentStatus = trackerViewHelper.shipmentStatus
                        } catch (e: Exception) {
                            errorMessage = "Shipment not found."
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Track")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        errorMessage = ""
                        try {
                            trackerViewHelper.stopTracking(trackingId)
                        } catch (e: Exception) {
                            errorMessage = "Failed to stop tracking."
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Stop Tracking")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colors.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Shipment Status: $shipmentStatus")
            Text("Expected Delivery Date: $expectedShipmentDeliveryDate")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Notes:")
            shipmentNotes.forEach { note ->
                Text(note)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Update History:")
            shipmentUpdateHistory.forEach { update ->
                Text(update)
            }
        }
    }
}
