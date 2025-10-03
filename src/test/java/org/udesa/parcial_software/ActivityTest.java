package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {
    @Test
    void test00createActivityHasRemainingEqualToCapacity() {
        Activity a = new Activity("Talk", 2, List.of(1,2));
        assertEquals(2, a.remaining());
    }
    @Test
    void test01sellReducesRemainingByOne() {
        Activity a = new Activity("Talk", 2, List.of(1,2));
        a.sellTo(new Wristband("W1"), 1);
        assertEquals(1, a.remaining());
    }

    @Test
    void test02cannotSellSameSeatTwice_inSameActivity() {
        Activity a = new Activity("Talk", 2, List.of(1,2));

        a.sellTo(new Wristband("W1"), 1);

        assertThrows(IllegalStateException.class,
                () -> a.sellTo(new Wristband("W2"), 1));
    }

    @Test
    void test03cannotSellMoreThanCapacity() {
        Activity a = new Activity("Workshop", 1, List.of(10));

        a.sellTo(new Wristband("W1"), 10);

        assertThrows(IllegalStateException.class,
                () -> a.sellTo(new Wristband("W2"), 10));
    }

    @Test
    void test04thirdAccessGetsThePrize() {
        Activity a = new Activity("Keynote", 5, List.of(1,2,3,4,5));

        a.sellTo(new Wristband("W1"), 1);
        a.sellTo(new Wristband("W2"), 2);
        a.sellTo(new Wristband("W3"), 3);

        a.access(new Wristband("W1"), 1);
        a.access(new Wristband("W2"), 2);
        a.access(new Wristband("W3"), 3); // 3er acceso

        assertTrue(a.findTicket(new Wristband("W3"), 3).hasPrize());
    }

    @Test
    void test05remainingGoesToZeroWhenAllSeatsAreSold() {
        Activity a = new Activity("Workshop", 2, List.of(1,2));

        a.sellTo(new Wristband("W1"), 1);
        a.sellTo(new Wristband("W2"), 2);

        assertEquals(0, a.remaining());
    }

    @Test
    void test06cannotSellWhenAllSeatsSoldEvenIfCapacityAndSeatsMatch() {
        Activity a = new Activity("SmallTalk", 2, List.of(1,2));

        a.sellTo(new Wristband("W1"), 1);
        a.sellTo(new Wristband("W2"), 2);

        assertThrows(IllegalStateException.class,
                () -> a.sellTo(new Wristband("W3"), 1)); // ya agotado
    }

    @Test
    void test07seatNumbersListIsImmutable() {
        Activity a = new Activity("ImmutableTest", 1, List.of(10));

        assertThrows(UnsupportedOperationException.class,
                () -> a.seatNumbers().add(99));
    }

    @Test
    void test08cannotSellSeatNotInNumberedList() {
        Activity a = new Activity("Talk", 2, List.of(1,2));

        // Intentar vender asiento inexistente (99) debe fallar
        assertThrows(IllegalArgumentException.class,
                () -> a.sellTo(new Wristband("W1"), 99));
    }
    @Test
    void test09capacityMustMatchSeatNumbersSize() {
        assertThrows(IllegalArgumentException.class,
                () -> new Activity("BadActivity", 3, List.of(1,2)));
    }


}
