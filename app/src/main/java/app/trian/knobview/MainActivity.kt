package app.trian.knobview

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.progressindicator.LinearProgressIndicator

class MainActivity : AppCompatActivity() {
    private lateinit var knobView: KnobView
    private lateinit var tvAngle:TextView
    private lateinit var btnReset:Button
    private lateinit var btnTo:Button
    private lateinit var progress:LinearProgressIndicator

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        knobView = findViewById(R.id.kv_main)
        tvAngle = findViewById(R.id.tv_angle)
        progress = findViewById(R.id.pb)

        btnReset = findViewById(R.id.btn_reset)
        btnTo = findViewById(R.id.btn_animatTo)

        btnTo.setOnClickListener {
            knobView.animateTo(0f)
        }

        btnReset.setOnClickListener {
            knobView.onDoubleTap()
        }
        progress.isIndeterminate = false
        progress.max = 1000
        progress.min = 0

        knobView.config = KnobDefaults.indeterminate()

        knobView.setListener(object : KnobListener {
            override fun onValueChanged(value: Float) {
                tvAngle.text = value.toString()
                progress.progress = value.toInt()
            }
        })
    }
}