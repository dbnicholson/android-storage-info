package org.endlessos.storageinfo;

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

            final StringBuffer buf = new StringBuffer();
            buf.append(String.format("%n%1$s%n", volume));
            buf.append(String.format("  Description: %1$s%n", volume.getDescription(this)));
            buf.append(String.format("  Path: %1$s%n", volume.getDirectory()));
            buf.append(String.format("  State: %1$s%n", volume.getState()));
            buf.append(String.format("  UUID: %1$s%n", volume.getUuid()));
            buf.append(String.format("  Emulated: %1$b%n", volume.isEmulated()));
            buf.append(String.format("  Primary: %1$b%n", volume.isPrimary()));
            buf.append(String.format("  Removable: %1$b%n", volume.isRemovable()));

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
