package com.csci2020.frontend;

import java.awt.*;
import java.util.Objects;

public class Theme {
    public static Theme LIGHT = new ThemeBuilder()
            .setForegroundPrimary(Color.decode("#333333"))
            .setForegroundSecondary(Color.decode("#000000"))
            .setBackgroundPrimary(Color.decode("#DFF7FD"))
            .setBackgroundSecondary(Color.decode("#FFFFFF"))
            .setBackgroundTertiary(Color.decode("#04CDF8"))
            .setBackgroundQuaternary(Color.DARK_GRAY)
            .setAccentPrimary(Color.decode("#04CDF8"))
            .setAccentSecondary(Color.decode("#0452F8"))
            .setErrorColor(Color.RED)
            .buildDefault();

    public static Theme DARK = new ThemeBuilder()
            .setForegroundPrimary(Color.WHITE)
            .setForegroundSecondary(Color.LIGHT_GRAY)
            .setBackgroundPrimary(Color.BLACK)
            .setBackgroundSecondary(Color.DARK_GRAY)
            .build();

    public static Theme DRACULA = new ThemeBuilder()
            .setForegroundPrimary(Color.decode("#F8F8F2"))
            .setForegroundSecondary(Color.decode("#6272A4"))
            .setBackgroundPrimary(Color.decode("#282A36"))
            .setBackgroundSecondary(Color.decode("#343746"))
            .setBackgroundTertiary(Color.decode("#BD93F9"))
            .setBackgroundQuaternary(Color.decode("#1E1F29"))
            .setAccentPrimary(Color.decode("#BD93F9"))
            .setAccentSecondary(Color.decode("#815CD6"))
            .build();
    private static Theme activeTheme = Theme.LIGHT;
    public static void setActiveTheme(Theme theme){
        activeTheme = theme;
    }
    public static Theme getActiveTheme(){
        return activeTheme;
    }
    private final Color foreground_primary, foreground_secondary;
    private final Color background_primary, background_secondary, background_tertiary, background_quaternary;
    private final Color accent_primary, accent_secondary;
    public Theme(Color foregroundPrimary, Color foregroundSecondary, Color backgroundPrimary, Color backgroundSecondary, Color backgroundTertiary, Color backgroundQuaternary, Color accentPrimary, Color accentSecondary) {
        this.foreground_primary = foregroundPrimary;
        this.foreground_secondary = foregroundSecondary;
        this.background_primary = backgroundPrimary;
        this.background_secondary = backgroundSecondary;
        this.background_tertiary = backgroundTertiary;
        this.background_quaternary = backgroundQuaternary;
        this.accent_primary = accentPrimary;
        this.accent_secondary = accentSecondary;
    }

    public static class ThemeBuilder {
        private Color foreground_primary, foreground_secondary;
        private Color background_primary, background_secondary, background_tertiary, background_quaternary;
        private Color accent_primary, accent_secondary;
        private Color error_color;
        public ThemeBuilder() {

        }

        /**
         * Builds the theme using set values, or values copied from {@link Theme#LIGHT} if unset.
         * @return Built theme
         */
        public Theme build() {
            return new Theme(Objects.requireNonNullElse(foreground_primary, LIGHT.getForegroundPrimary()),
                    Objects.requireNonNullElse(foreground_secondary, LIGHT.getForegroundSecondary()),
                    Objects.requireNonNullElse(background_primary, LIGHT.getBackgroundPrimary()),
                    Objects.requireNonNullElse(background_secondary, LIGHT.getBackgroundSecondary()),
                    Objects.requireNonNullElse(background_tertiary, LIGHT.getBackgroundTertiary()),
                    Objects.requireNonNullElse(background_quaternary, LIGHT.getBackgroundQuaternary()),
                    Objects.requireNonNullElse(accent_primary, LIGHT.getAccentPrimary()),
                    Objects.requireNonNullElse(accent_secondary, LIGHT.getAccentSecondary()));
        }

        /**
         * Builds the theme using set values. Ensure that all fields are set before calling this,
         * and prefer {@link ThemeBuilder#build()} when possible.
         * @return Built theme
         */
        private Theme buildDefault(){
            return new Theme(Objects.requireNonNull(foreground_primary),
                    Objects.requireNonNull(foreground_secondary),
                    Objects.requireNonNull(background_primary),
                    Objects.requireNonNull(background_secondary),
                    Objects.requireNonNull(background_tertiary),
                    Objects.requireNonNull(background_quaternary),
                    Objects.requireNonNull(accent_primary),
                    Objects.requireNonNull(accent_secondary));
        }

        // Setters
        public ThemeBuilder setForegroundPrimary(Color color) {
            this.foreground_primary = color;
            return this;
        }

        public ThemeBuilder setForegroundSecondary(Color color) {
            this.foreground_secondary = color;
            return this;
        }

        public ThemeBuilder setBackgroundPrimary(Color color) {
            this.background_primary = color;
            return this;
        }

        public ThemeBuilder setBackgroundSecondary(Color color) {
            this.background_secondary = color;
            return this;
        }

        public ThemeBuilder setBackgroundTertiary(Color color){
            this.background_tertiary = color;
            return this;
        }

        public ThemeBuilder setBackgroundQuaternary(Color color){
            this.background_quaternary = color;
            return this;
        }

        public ThemeBuilder setAccentPrimary(Color color) {
            this.accent_primary = color;
            return this;
        }

        public ThemeBuilder setAccentSecondary(Color color) {
            this.accent_secondary = color;
            return this;
        }

        public ThemeBuilder setErrorColor(Color color){
            this.error_color = color;
            return this;
        }
    }
    // Getters
    public Color getForegroundPrimary() {
        return foreground_primary;
    }

    public Color getForegroundSecondary() {
        return foreground_secondary;
    }

    public Color getBackgroundPrimary() {
        return background_primary;
    }

    public Color getBackgroundSecondary() {
        return background_secondary;
    }

    public Color getBackgroundTertiary(){
        return background_tertiary;
    }

    public Color getBackgroundQuaternary(){
        return background_quaternary;
    }

    public Color getAccentPrimary() {
        return accent_primary;
    }

    public Color getAccentSecondary() {
        return accent_secondary;
    }
}
