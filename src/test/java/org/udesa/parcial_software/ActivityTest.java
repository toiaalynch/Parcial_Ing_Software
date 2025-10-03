package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Tests de Activity: control de asientos y capacidad. */
public class ActivityTest {

    @Test
    void test01cannotSellSameSeatTwice() {
        Activity a = new Activity("Talk", 2, List.of(1,2));
        Wristband w1 = new Wristband("W1");
        Wristband w2 = new Wristband("W2");

        a.sellTo(w1, 1);

        assertThrows(IllegalStateException.class, () -> a.sellTo(w2, 1));
    }

    @Test
    void test02cannotSellMoreThanCapacity() {
        Activity a = new Activity("Workshop", 1, List.of(10));
        Wristband w1 = new Wristband("W1");
        Wristband w2 = new Wristband("W2");

        a.sellTo(w1, 10);

        assertThrows(IllegalStateException.class, () -> a.sellTo(w2, 10));
    }

    @Test
    void test03thirdSoldSeatGetsThePrize() {
        Activity a = new Activity("Keynote", 5, List.of(1,2,3,4,5));
        Wristband w1 = new Wristband("W1");
        Wristband w2 = new Wristband("W2");
        Wristband w3 = new Wristband("W3");

        Ticket t1 = a.sellTo(w1, 1); // 1st sold
        Ticket t2 = a.sellTo(w2, 2); // 2nd sold
        Ticket t3 = a.sellTo(w3, 3); // 3rd sold → prize

        assertFalse(t1.hasPrize());
        assertFalse(t2.hasPrize());
        assertTrue(t3.hasPrize());
    }

}

