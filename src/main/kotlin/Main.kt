import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ray.practice.ComposePractice


fun main(args: Array<String>) =  application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 400.dp, height = 300.dp),
        title = "Ray Tracer"
    ) {
        ComposePractice()
    }
}