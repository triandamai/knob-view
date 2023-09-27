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

# LICENSE

```text
MIT License

Copyright (c) 2023 trian damai

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
