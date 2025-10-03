package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ActivityTest {

    @Test
    void test01cannotSellSameSeatTwice_inSameActivity() {
        Activity a = new Activity("Talk", 2, List.of(1,2));

        a.sellTo(new Wristband("W1"), 1);

        assertThrows(IllegalStateException.class,
                () -> a.sellTo(new Wristband("W2"), 1));
    }

    @Test
    void test02cannotSellMoreThanCapacity() {
        Activity a = new Activity("Workshop", 1, List.of(10));

        a.sellTo(new Wristband("W1"), 10);

        assertThrows(IllegalStateException.class,
                () -> a.sellTo(new Wristband("W2"), 10));
    }

    @Test
    void test03thirdSoldSeatGetsThePrize() {
        Activity a = new Activity("Keynote", 5, List.of(1,2,3,4,5));

        a.sellTo(new Wristband("W1"), 1);
        a.sellTo(new Wristband("W2"), 2);

        assertTrue(a.sellTo(new Wristband("W3"), 3).hasPrize());
        assertFalse(a.sellTo(new Wristband("W4"), 4).hasPrize());
    }

    @Test
    void test04onlyThirdTicketGetsPrize() {
        Activity a = new Activity("Panel", 5, List.of(1,2,3,4,5));

        a.sellTo(new Wristband("W1"), 1); // 1st
        a.sellTo(new Wristband("W2"), 2); // 2nd
        Ticket t3 = a.sellTo(new Wristband("W3"), 3); // 3rd → prize
        Ticket t4 = a.sellTo(new Wristband("W4"), 4); // 4th → no prize
        Ticket t5 = a.sellTo(new Wristband("W5"), 5); // 5th → no prize

        assertTrue(t3.hasPrize());
        assertFalse(t4.hasPrize());
        assertFalse(t5.hasPrize());
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


}
