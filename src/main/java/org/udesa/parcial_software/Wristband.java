package org.udesa.parcial_software;

/** Pulsera magnética identificada por un código. */
public final class Wristband {
    private final String code;
    public Wristband(String code) { this.code = code; }
    public String code() { return code; }
}
