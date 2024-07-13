import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App(trackerViewHelper: TrackerViewHelper) {
    // Initialize the shipment updates map using the remember statement
    val shipmentUpdates = remember {
        mapOf(
            "created" to CreatedUpdate::class,
            "shipped" to ShippedUpdate::class,
            "location" to LocationUpdate::class,
            "delivered" to DeliveredUpdate::class,
            "delayed" to DelayedUpdate::class,
            "lost" to LostUpdate::class,
            "canceled" to CanceledUpdate::class,
            "noteadded" to NoteAddedUpdate::class
        )
    }

    MaterialTheme {
        UserInterface(trackerViewHelper, shipmentUpdates).DisplayUI()
    }
}

fun main() = application {
    val trackerViewHelper = TrackerViewHelper()
    Window(onCloseRequest = ::exitApplication) {
        App(trackerViewHelper)
    }
}
