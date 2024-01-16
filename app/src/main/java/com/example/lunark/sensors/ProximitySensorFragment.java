package com.example.lunark.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public abstract class ProximitySensorFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor proximitySensor;

    private final int WAVE_TIME = 3000;
    private final int WAVE_NEEDED_MOVEMENTS = 3;
    private long lastWaveTime = 0;
    private float lastSensorValue;
    private float maxSensorValue;
    private float numOfMovements = 0;


    public ProximitySensorFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
    }


    @Override
    public void onResume() {
        super.onResume();
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor s : sensors) {
            Log.i("REZ_TYPE_ALL", s.getName());
        }

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(requireContext(), "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
        } else {
            sensorManager.registerListener(this,
                    proximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            maxSensorValue = proximitySensor.getMaximumRange();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (lastWaveTime + WAVE_TIME < System.currentTimeMillis()) {
                Log.d("SENSOR", "Reset timing");
                lastWaveTime = System.currentTimeMillis();
                numOfMovements = 0;
                lastSensorValue = event.values[0];
            } else if (
                    (event.values[0] != maxSensorValue && lastSensorValue == maxSensorValue) ||
                    (event.values[0] == maxSensorValue && lastSensorValue != maxSensorValue)) {
                numOfMovements++;
                Log.d("SENSOR", "Detected movement");
                if (numOfMovements == WAVE_NEEDED_MOVEMENTS) {
                    Log.d("SENSOR", "Wave event");
                    onWave();
                    lastWaveTime = System.currentTimeMillis();
                    numOfMovements = 0;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.i("REZ_ACCELEROMETER", String.valueOf(accuracy));
        } else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.i("REZ_LINEAR_ACCELERATION", String.valueOf(accuracy));
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.i("REZ_MAGNETIC_FIELD", String.valueOf(accuracy));
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.i("REZ_GYROSCOPE", String.valueOf(accuracy));
        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.i("REZ_TYPE_PROXIMITY", String.valueOf(accuracy));
        } else {
            Log.i("REZ_OTHER_SENSOR", String.valueOf(accuracy));
        }
    }

    public abstract void onWave();
}
