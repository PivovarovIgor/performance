package ru.brauer.nativeexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.brauer.nativeexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SimpleViewModel by lazy {
        ViewModelProvider(this@MainActivity)[SimpleViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = viewModel.stringFromJNI("from Android")
    }

    companion object {
        // Used to load the 'nativeexample' library on application startup.
        init {
            System.loadLibrary("nativeexample")
        }
    }
}

class SimpleViewModel : ViewModel() {
    /**
     * A native method that is implemented by the 'nativeexample' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(name: String): String
}