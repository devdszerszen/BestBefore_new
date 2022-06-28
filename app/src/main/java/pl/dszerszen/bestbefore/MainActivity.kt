package pl.dszerszen.bestbefore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme
import pl.dszerszen.bestbefore.util.DispatchersProvider
import pl.dszerszen.bestbefore.util.Logger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<MainViewModel>()
            BestBeforeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting(viewModel.title)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = name)
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logger: Logger,
    private val initializeUseCase: InitializeUseCase,
) : ViewModel() {

    var title by mutableStateOf("Hello project")
        private set

    var isInitialized by mutableStateOf(false)

    init {
        viewModelScope.launch {
            initialize()
        }
    }

    suspend fun initialize() {
        isInitialized = initializeUseCase()
        title = "Initialized successfully"
    }
}

class InitializeUseCase @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(): Boolean = withContext(dispatchersProvider.ioDispatcher()) {
        delay(2000)
        true
    }
}