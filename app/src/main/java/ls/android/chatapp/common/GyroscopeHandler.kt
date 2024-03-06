package ls.android.chatapp.common

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ls.android.chatapp.presentation.chat.BOTTOM_BAR_HEIGHT

private val deltaRotationVectorRaw = FloatArray(3) { 0f }

class GyroscopeHelper(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    fun getGyroscopeData(): Flow<FloatArray> = callbackFlow {
        val sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    deltaRotationVectorRaw[0] = event.values[0]
                    deltaRotationVectorRaw[1] = event.values[1]
                    deltaRotationVectorRaw[2] = event.values[2]

                    // Calculate the angular speed of the sample
                    event.let {
                        trySend(
                            deltaRotationVectorRaw
                        ).isSuccess
                    }
                }
            }
        }
        sensorManager.registerListener(
            sensorEventListener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose { sensorManager.unregisterListener(sensorEventListener) }
    }

    companion object {
        fun calculateWeight(
            deltaRotationMatrixRaw: FloatArray,
            maxXValue: Float,
            maxYValue: Float,
            currentOffset: Offset,
            density: Float
        ): Offset {
            var sumX = currentOffset.x
            var sumY = currentOffset.y
            deltaRotationMatrixRaw.forEachIndexed { index, float ->
                sumX += if (1 == index) {
                    float * density
                } else {
                    0f
                }
                sumY += if (0 == index) {
                    float * density * 2
                } else {
                    0f
                }
            }

            // TODO not tested - should work
            return Offset(calculateXOffset(sumX, maxXValue), calculateYOffset(sumY, maxYValue))
        }

        private fun calculateXOffset(offset: Float, max: Float): Float {
            val ret = if (offset <= 0f) {
                0f
            } else {
                if (offset >= max) {
                    max
                } else {
                    offset
                }
            }
            return ret
        }

        private fun calculateYOffset(offset: Float, max: Float): Float {
            val newMax = max - (BOTTOM_BAR_HEIGHT + 1)
            val ret = if (offset <= 50f) {
                50f
            } else {
                if (offset >= newMax) {
                    newMax
                } else {
                    offset
                }
            }
            return ret
        }
    }
}
