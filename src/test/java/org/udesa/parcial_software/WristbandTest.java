package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WristbandTest {

    @Test
    void test00createWristbandStoresCode() {
        Wristband w = new Wristband("WB-1");
        assertEquals("WB-1", w.code());
    }

    @Test
    void test01returnsCodeCorrectly() {
        assertEquals("WB-123", new Wristband("WB-123").code());
    }

    @Test
    void test02wristbandsWithSameCodeAreEqual() {
        assertEquals(new Wristband("WB-1"), new Wristband("WB-1"));
        assertEquals(new Wristband("WB-1").hashCode(), new Wristband("WB-1").hashCode());
    }

    @Test
    void test03wristbandsWithDifferentCodeAreNotEqual() {
        assertNotEquals(new Wristband("WB-1"), new Wristband("WB-2"));
    }
}
