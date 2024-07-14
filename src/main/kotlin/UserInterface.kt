import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class UserInterface(private val simulator: TrackingSimulator) {
    private val trackerViewHelper = TrackerViewHelper(simulator)

    @Composable
    fun createInterface() {
        var trackingId by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }

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
                            simulator.findShipment(trackingId)
                            trackerViewHelper.trackShipment(trackingId)
                        } catch (e: Exception) {
                            errorMessage = "Shipment not found."
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

            // Display tracked shipments
            DisplayTrackedShipments()
        }
    }

    @Composable
    private fun DisplayTrackedShipments() {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            // Use snapshotStateListOf to ensure proper recomposition
            val shipments = trackerViewHelper.trackedShipments.toList()
            shipments.forEach { shipment ->
                ShipmentCard(shipment, onRemove = { trackerViewHelper.stopTracking(shipment.id) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    @Composable
    private fun ShipmentCard(shipment: Shipment, onRemove: () -> Unit) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Shipment ID: ${shipment.id}")
                Text("Status: ${shipment.status}")
                Text("Location: ${shipment.location}")
                Text("Expected Delivery Date: ${shipment.expectedDeliveryDate}")

                Spacer(modifier = Modifier.height(8.dp))

                Text("Notes:")
                shipment.notes.forEach { note ->
                    Text(note)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Update History:")
                shipment.updateHistory.forEach { update ->
                    Text(update)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onRemove
                ) {
                    Text("Stop Tracking")
                }
            }
        }
    }
}
