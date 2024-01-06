import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import ray.practice.ComposePractice


fun main(args: Array<String>) =  application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(),
        title = "Ray Tracer"
    ) {
        ComposePractice()
    }
}