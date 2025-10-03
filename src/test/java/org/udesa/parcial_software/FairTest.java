package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Smoke tests: creación, venta simple, acceso simple, contadores básicos. */
public class FairTest {

    @Test
    void test01createsActivityWithFixedSeats() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Talk", 3, java.util.List.of(1, 2, 3));

        assertEquals("Talk", a.name());
        assertEquals(3, a.remaining());
    }
    @Test
    void test02sellsOneTicketAndLinksOwner() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Talk", 3, List.of(1,2,3));
        Wristband w = new Wristband("WB-1");

        Ticket t = fair.sell(a, w, 2);

        assertEquals("WB-1", t.owner().code());
        assertEquals(2, t.seatNumber());
        assertEquals(2, a.remaining());
    }

    @Test
    void test03accessConsumesOneAndIncrementsGlobalCounter() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Talk", 3, List.of(1,2,3));
        Wristband w = new Wristband("WB-1");

        fair.sell(a, w, 1);
        assertEquals(0, fair.totalConsumptions());

        fair.access(a, w, 1);

        assertEquals(1, fair.totalConsumptions());
    }

    @Test
    void test04cannotAccessTwiceWithSameTicket() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Concert", 2, List.of(1,2));
        Wristband w = new Wristband("WB-9");

        fair.sell(a, w, 2);

        assertEquals(0, fair.totalConsumptions());
        fair.access(a, w, 2);
        assertEquals(1, fair.totalConsumptions());

        // Intentar acceder de nuevo con el mismo asiento debe fallar
        assertThrows(IllegalStateException.class, () -> fair.access(a, w, 2));
    }

}