package org.lineageos.openvfd.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.tv.twopanelsettings.slices.TvSettingsSliceProvider;
import com.android.tv.twopanelsettings.slices.builders.PreferenceSliceBuilder;
import com.android.tv.twopanelsettings.slices.builders.PreferenceSliceBuilder.RowBuilder;

/**
 * Hosts the front panel slices. TvSettings opens them through
 * com.android.tv.twopanelsettings.slices.SliceFragment; the entry row is
 * added to Advanced display settings by TvSettingsOpenVfdOverlay.
 *
 * Brightness is a page of flat radio rows rather than a seekbar: the
 * renderer fires a seekbar row's action without the new value, so a stateless
 * provider cannot learn it (see RkTvSettings' AipqSliceProvider).
 */
public class VfdSliceProvider extends TvSettingsSliceProvider {

    public static final String AUTHORITY = "org.lineageos.openvfd.settings";
    public static final Uri MAIN_URI = Uri.parse("content://" + AUTHORITY + "/main");
    public static final Uri BRIGHTNESS_URI =
            Uri.parse("content://" + AUTHORITY + "/brightness");

    private static final int PAGE_ID = 990101;

    @Override
    public boolean onCreateSliceProvider() {
        return true;
    }

    @Override
    public void onSlicePinned(Uri sliceUri) {
        // Same platform signature as TvSettings, so the slice is readable
        // already; grant explicitly in case the signature policy is tightened.
        try {
            getContext().getSystemService(android.app.slice.SliceManager.class)
                    .grantSlicePermission("com.android.tv.settings", sliceUri);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected boolean createSlice(PreferenceSliceBuilder builder, Uri sliceUri) {
        if (MAIN_URI.equals(sliceUri)) {
            return createMainSlice(builder);
        }
        if (BRIGHTNESS_URI.equals(sliceUri)) {
            return createBrightnessSlice(builder);
        }
        return false;
    }

    private boolean createMainSlice(PreferenceSliceBuilder builder) {
        Context c = getContext();
        builder.addScreenTitle(new RowBuilder()
                .setTitle(c.getString(R.string.vfd_title))
                .setPageId(PAGE_ID));

        boolean enabled = VfdProps.isEnabled();
        addSwitchRow(builder, c, VfdProps.KNOB_ENABLE,
                R.string.vfd_enable_title, R.string.vfd_enable_summary, enabled);
        if (!enabled) {
            return true;
        }
        addSwitchRow(builder, c, VfdProps.KNOB_CLOCK_12H,
                R.string.vfd_clock_12h_title, R.string.vfd_clock_12h_summary,
                VfdProps.is12h());
        addSwitchRow(builder, c, VfdProps.KNOB_ICONS,
                R.string.vfd_icons_title, R.string.vfd_icons_summary,
                VfdProps.iconsOn());
        builder.addPreference(new RowBuilder()
                .setKey("vfd_brightness_page")
                .setTitle(c.getString(R.string.vfd_brightness_title))
                .setSubtitle(brightnessLabel(VfdProps.brightness()))
                .setTargetSliceUri(BRIGHTNESS_URI.toString()));
        return true;
    }

    private boolean createBrightnessSlice(PreferenceSliceBuilder builder) {
        Context c = getContext();
        builder.addScreenTitle(new RowBuilder()
                .setTitle(c.getString(R.string.vfd_brightness_title))
                .setPageId(PAGE_ID + 1));
        int current = VfdProps.brightness();
        for (int value = VfdProps.BRIGHTNESS_MAX; value >= 0; value--) {
            builder.addPreference(new RowBuilder()
                    .setKey("vfd_brightness_" + value)
                    .setTitle(brightnessLabel(value))
                    .setRadioGroup("vfd_brightness")
                    .addRadioButton(
                            knobIntent(c, VfdProps.KNOB_BRIGHTNESS)
                                    .putExtra(VfdBroadcastReceiver.EXTRA_VALUE, value),
                            current == value));
        }
        return true;
    }

    /** The register is 0-7; show it as the 1-8 levels of the datasheet. */
    private static String brightnessLabel(int value) {
        return (value + 1) + " / " + (VfdProps.BRIGHTNESS_MAX + 1);
    }

    private void addSwitchRow(PreferenceSliceBuilder builder, Context c, String knob,
            int titleRes, int summaryRes, boolean checked) {
        String title = c.getString(titleRes);
        builder.addPreference(new RowBuilder()
                .setKey("vfd_" + knob)
                .setTitle(title)
                .setSubtitle(c.getString(summaryRes))
                .addSwitch(knobIntent(c, knob), title, checked));
    }

    private static Intent knobIntent(Context c, String knob) {
        Intent i = new Intent(VfdBroadcastReceiver.ACTION_KNOB);
        i.setClass(c, VfdBroadcastReceiver.class);
        i.putExtra(VfdBroadcastReceiver.EXTRA_KNOB, knob);
        return i;
    }
}
