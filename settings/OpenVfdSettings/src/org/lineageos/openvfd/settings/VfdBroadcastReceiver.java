package org.lineageos.openvfd.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.tv.twopanelsettings.slices.TvSettingsSliceProvider;

/**
 * Applies the knob changes fired by the slice rows and invalidates the slices
 * so TvSettings re-renders with the new state. Switch rows carry no state, so
 * the current value is flipped.
 */
public class VfdBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION_KNOB = "org.lineageos.openvfd.settings.action.KNOB";
    static final String EXTRA_KNOB = "knob";
    static final String EXTRA_VALUE = "value";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ACTION_KNOB.equals(intent.getAction())) {
            return;
        }
        String knob = intent.getStringExtra(EXTRA_KNOB);
        if (knob == null) {
            return;
        }
        switch (knob) {
            case VfdProps.KNOB_ENABLE:
            case VfdProps.KNOB_CLOCK_12H:
            case VfdProps.KNOB_ICONS:
                VfdProps.toggle(knob);
                break;
            case VfdProps.KNOB_BRIGHTNESS:
                VfdProps.setBrightness(intent.getIntExtra(EXTRA_VALUE,
                        VfdProps.BRIGHTNESS_MAX));
                TvSettingsSliceProvider.invalidateSlice(context,
                        VfdSliceProvider.BRIGHTNESS_URI);
                break;
            default:
                return;
        }
        TvSettingsSliceProvider.invalidateSlice(context, VfdSliceProvider.MAIN_URI);
    }
}
