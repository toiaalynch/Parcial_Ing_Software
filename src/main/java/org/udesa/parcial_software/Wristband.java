package org.udesa.parcial_software;

import java.util.Objects;

/** Pulsera magnética identificada por un código. */
public final class Wristband {
    private final String code;
    public Wristband(String code) { this.code = code; }
    public String code() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wristband)) return false;
        Wristband that = (Wristband) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
