package com.example.storageinfo;

import java.io.File;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.widget.TextView;
import android.util.Log;

public class StorageInfoActivity extends Activity {
    private static final String TAG = "StorageInfoActivity";

    private TextView view;
    private StorageManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TextView) findViewById(R.id.text_view_id);
        view.setTextIsSelectable(true);

        manager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        manager.registerStorageVolumeCallback(Executors.newSingleThreadExecutor(),
                                              new VolumeCallback());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateView();
    }

    protected void updateView() {
        Log.i(TAG, "Updating volumes");
        view.setText("Storage volumes:\n");

        for (StorageVolume volume : manager.getStorageVolumes()) {
            Log.v(TAG, "Adding volume " + volume.toString());
            final File directory = volume.getDirectory();
            final String path = directory == null ? "" : directory.toString();

            final StringBuffer buf = new StringBuffer();
            buf.append("\n" + volume.toString() + ":\n");
            buf.append("  Description: " + volume.getDescription(this) + "\n");
            buf.append("  Path: " + path + "\n");
            buf.append("  State: " + volume.getState() + "\n");
            buf.append("  UUID: " + volume.getUuid() + "\n");
            buf.append("  Emulated: " + Boolean.toString(volume.isEmulated()) + "\n");
            buf.append("  Primary: " + Boolean.toString(volume.isPrimary()) + "\n");
            buf.append("  Removable: " + Boolean.toString(volume.isRemovable()) + "\n");

            view.append(buf);
        }
    }

    protected class VolumeCallback extends StorageManager.StorageVolumeCallback {
        public void onStateChanged (StorageVolume volume) {
            Log.i(TAG, "Volume " + volume.toString() + " state changed to " + volume.getState());
            updateView();
        }
    }
}
