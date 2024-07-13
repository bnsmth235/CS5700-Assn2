import java.io.File
import kotlinx.coroutines.*
import java.lang.Exception

class TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()
    private var simulationJob: Job? = null

    fun findShipment(id: String): Shipment? = shipments.find { it.id == id }

    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }

    fun runSimulation(filename: String) {
        // Ensure only one simulation is running at a time
        if (simulationJob?.isActive == true) {
            println("Simulation is already running.")
            return
        }

        simulationJob = GlobalScope.launch {
            try {
                File(filename).useLines { lines ->
                    lines.forEach { line ->
                        processLine(line)
                        delay(1000) // Process one line per second
                    }
                }
                println("Simulation completed.")
            } catch (e: Exception) {
                println("Error in simulation: ${e.message}")
            }
        }
    }

    fun stopSimulation() {
        simulationJob?.cancel()
    }

    private fun processLine(line: String) {
        val (type, id, timestamp, info) = parseLine(line)
        val shipmentUpdate = when (type) {
            "created" -> CreatedUpdate(timestamp.toLong(), id)
            "shipped" -> ShippedUpdate(timestamp.toLong(), id, info.toLong())
            "location" -> LocationUpdate(timestamp.toLong(), id, info)
            "delivered" -> DeliveredUpdate(timestamp.toLong(), id)
            "delayed" -> DelayedUpdate(timestamp.toLong(), id, info.toLong())
            "lost" -> LostUpdate(timestamp.toLong(), id)
            "canceled" -> CanceledUpdate(timestamp.toLong(), id)
            "noteadded" -> NoteAddedUpdate(timestamp.toLong(), id, info)
            else -> throw IllegalArgumentException("Invalid shipment update type: $type")
        }

        val shipment = findShipment(id)
        if (shipment == null) {
            var location = "N/A"
            var deliveryDate = "N/A"
            val notes = mutableListOf<String>()
            val shippingUpdates = mutableListOf<String>()

            when (shipmentUpdate) {
                is LocationUpdate -> location = shipmentUpdate.location
                is ShippedUpdate -> deliveryDate = shipmentUpdate.expectedDeliveryDate.toString()
                is DelayedUpdate -> deliveryDate = shipmentUpdate.expectedDeliveryDate.toString()
                is NoteAddedUpdate -> notes.add(shipmentUpdate.note)
            }

            addShipment(
                Shipment(
                    id,
                    shipmentUpdate.newStatus,
                    location,
                    deliveryDate,
                    notes,
                    shippingUpdates
                )
            )
        } else {
            shipment.addUpdate(shipmentUpdate)
        }
    }

    private fun parseLine(line: String): List<String> {
        return line.split(",")
    }
}
