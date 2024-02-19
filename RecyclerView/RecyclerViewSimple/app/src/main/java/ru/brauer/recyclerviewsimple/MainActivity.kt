package ru.brauer.recyclerviewsimple

import android.os.Build
import android.os.Bundle
import android.view.Choreographer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.brauer.recyclerviewsimple.databinding.ActivityMainBinding
import ru.brauer.recyclerviewsimple.databinding.ViewHolderOneLevelBinding
import ru.brauer.recyclerviewsimple.databinding.ViewHolderTwoLevelBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val numFlow: MutableSharedFlow<Long> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val actor = lifecycleScope.actor<Long> {
        for (num in channel) {
            println("VVV received in actor $num")
        }
    }
    val numChannel = Channel<Long>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var previousTime = 0L
    @Volatile private var interFrameTime = 0L

    private val monitor = Any()


    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (previousTime != 0L) {
                interFrameTime = frameTimeNanos - previousTime
            }
            previousTime = frameTimeNanos
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    private val vsyncCallback = @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object : Choreographer.VsyncCallback {
        override fun onVsync(data: Choreographer.FrameData) {
            val frameTimeNanos = data.frameTimeNanos
            val expectedPresentationTimeNanos = data.preferredFrameTimeline.expectedPresentationTimeNanos
            val deadlineNanos = data.preferredFrameTimeline.deadlineNanos

            println("VVV frameTimeNanos = $frameTimeNanos")
            println("VVV expectedPresentationTimeNanos = $expectedPresentationTimeNanos")
            println("VVV deadlineNanos = $deadlineNanos")
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        scope.launch {
//            while(isActive) {
//                delay(300)
//                println("VVV interFrameTime = $interFrameTime")
//            }
        //}

        Choreographer.getInstance().postVsyncCallback(vsyncCallback)
//        measureNanoTime {
//            syncNum = 1
//        }
//            .also { println("VVV $it") }

        binding.rv1Level.adapter = Adapter1()
            .apply {
                (1..50)
                    .map { Item(it) }
                    .also { submitList(it) }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Choreographer.getInstance().removeFrameCallback(frameCallback)
    }
}

class Adapter1 : ListAdapter<Item, RecyclerView.ViewHolder>(differ) {
    class VH1(private val binding: ViewHolderOneLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.label.text = "VH1 ${Counter.counter}"
            binding.rv2Level.adapter = Adapter2()
                .apply {
                    (1..50)
                        .map { Item(it) }
                        .also { submitList(it) }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        VH1(ViewHolderOneLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }
}

data class Item(val typeView: Int)

class Adapter2 : ListAdapter<Item, RecyclerView.ViewHolder>(differ) {

    class Adapter2_VH1(private val binding: ViewHolderTwoLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.label2.text = "Adapter2_VH1 ${Counter.counter}"
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = Adapter2_VH1(
        ViewHolderTwoLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            it.isSelected = !it.isSelected
            notifyItemChanged(position)
            if (it.isSelected) {
                it.updateLayoutParams<ViewGroup.LayoutParams> {
                    width = 100
                }
            } else {
                it.updateLayoutParams<ViewGroup.LayoutParams> {
                    width = 600
                }
            }
        }


    }
}

object Counter {
    var counter: Int = 0
        get() {
            field++
            return field
        }
}

val differ = object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = false

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = false

}
