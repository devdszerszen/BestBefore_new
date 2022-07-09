package pl.dszerszen.bestbefore.ui.start

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.dszerszen.bestbefore.ui.theme.BestBeforeTheme

@AndroidEntryPoint
class StartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<StartViewModel>()
            viewModel.loadData()
            BestBeforeTheme {
                StartScreen(viewModel)
            }
        }
    }
}