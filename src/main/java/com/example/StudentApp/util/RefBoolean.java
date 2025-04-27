package com.example.StudentApp.util;

public class RefBoolean {
    // for REF boolean, to pass by ref and not value.
    // for flags, help me decide which error message to write
    private boolean value;

    public RefBoolean(boolean value) {
        this.value = value;
    }

    public RefBoolean() {
        this.value = false; // default
    }

    public boolean get() {
        return value;
    }

    public void set(boolean value) {
        this.value = value;
    }
}
