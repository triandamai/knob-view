# KNOB-VIEW
[![](https://jitpack.io/v/triandamai/knob-view.svg)](https://jitpack.io/#triandamai/knob-view)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Step 1
```kotlin
    allprojects {
		repositories {
			maven(url ="https://jitpack.io")
		}
	}
```
## Step 2
```kotlin

    dependencies { 
        implementation("com.github.triandamai:knob-view:<version>")
	}
```

## Step 3
```xml
  <app.trian.knobview.KnobView
        android:id="@+id/kv_main"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center"
        app:knobSrc="@drawable/knob"
        app:startingAngle="180"
        />
```

## Step 3
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var knobView: KnobView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        knobView = findViewById(R.id.kv_main)
        knobView.config = KnobDefaults.indeterminate()
        knobView.setListener(object : app.trian.knobview.KnobListener {
            override fun onValueChanged(value: Float) {
                //code here
            }
        })
    }
}
```