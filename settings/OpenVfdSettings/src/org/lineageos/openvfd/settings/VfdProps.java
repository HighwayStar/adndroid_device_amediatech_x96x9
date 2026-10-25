package org.lineageos.openvfd.settings;

import android.os.SystemProperties;

/**
 * Front panel settings, kept in persist.sys.openvfd.*. OpenVFDService
 * re-reads them every 0.5 s, so a write takes effect without a restart.
 * The defaults here must match the service's: enabled, 24-hour clock, status
 * icons on (init starts it with -si) and the driver's brightness level 7.
 */
final class VfdProps {
    private VfdProps() {}

    static final String KNOB_ENABLE = "enable";
    static final String KNOB_CLOCK_12H = "clock_12h";
    static final String KNOB_ICONS = "icons";
    static final String KNOB_BRIGHTNESS = "brightness";

    /** FD628 brightness register, 0 (dimmest) to 7. */
    static final int BRIGHTNESS_MAX = 7;

    private static final String P_PREFIX = "persist.sys.openvfd.";

    static boolean isEnabled() {
        return flag(KNOB_ENABLE, true);
    }

    static boolean is12h() {
        return flag(KNOB_CLOCK_12H, false);
    }

    static boolean iconsOn() {
        return flag(KNOB_ICONS, true);
    }

    static int brightness() {
        int value = SystemProperties.getInt(P_PREFIX + KNOB_BRIGHTNESS, BRIGHTNESS_MAX);
        return Math.max(0, Math.min(BRIGHTNESS_MAX, value));
    }

    static void toggle(String knob) {
        set(knob, flag(knob, defaultFlag(knob)) ? "0" : "1");
    }

    static void setBrightness(int value) {
        set(KNOB_BRIGHTNESS, String.valueOf(Math.max(0, Math.min(BRIGHTNESS_MAX, value))));
    }

    private static boolean defaultFlag(String knob) {
        return !KNOB_CLOCK_12H.equals(knob);
    }

    private static boolean flag(String knob, boolean def) {
        return SystemProperties.getBoolean(P_PREFIX + knob, def);
    }

    private static void set(String knob, String value) {
        SystemProperties.set(P_PREFIX + knob, value);
    }
}
