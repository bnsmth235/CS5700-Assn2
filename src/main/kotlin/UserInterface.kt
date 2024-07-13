import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class UserInterface {
    private val trackerViewHelper = TrackerViewHelper()

    @Composable
    fun createInterface() {
        var trackingId by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var shipments by remember { mutableStateOf(mutableListOf<Shipment>()) }

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
                            trackerViewHelper.trackShipment(trackingId).let { shipment ->
                                shipments.add(shipment)
                                trackingId = "" // Clear tracking ID after successful track
                            } ?: run {
                                errorMessage = "Shipment not found."
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error tracking shipment."
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Track")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colors.error)
            }

            Spacer(modifier = Modifier.height(8.dp))

            shipments.forEach { shipment ->
                displayShipment(shipment) {
                    shipments.remove(shipment)
                    trackerViewHelper.stopTracking(shipment.id)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Composable
    fun displayShipment(shipment: Shipment, onStopTracking: () -> Unit) {
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Display shipment details
                    Text("Shipment ID: ${shipment.id}")
                    IconButton(
                        onClick = {
                            onStopTracking()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Stop Tracking"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Status: ${trackerViewHelper.shipmentStatus.value}")
                Text("Location: ${trackerViewHelper.shipmentLocation.value}")
                Text("Expected Delivery Date: ${trackerViewHelper.expectedShipmentDeliveryDate.value}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Notes:")
                trackerViewHelper.shipmentNotes.value.forEach { note ->
                    Text(note)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Update History:")
                trackerViewHelper.shipmentUpdateHistory.value.forEach { update ->
                    Text(update)
                }
            }
        }
    }
}

private fun <E> MutableList<E>.add(element: Unit) {

}
