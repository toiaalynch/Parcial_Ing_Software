package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FairTest {

    @Test
    void test00fairStartsWithZeroConsumptions() {
        Fair fair = new Fair();
        assertEquals(0, fair.totalConsumptions());
    }

    @Test
    void test01createActivityRegistersItInFair() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Test", 1, List.of(1));
        assertNotNull(a);
        assertEquals("Test", a.name());
    }


    @Test
    void test02createsActivityWithNameCapacityAndSeats() {
        Activity talk = new Fair().createActivity("Talk", 3, List.of(1, 2, 3));

        assertEquals("Talk", talk.name());
        assertEquals(3, talk.remaining());
        assertEquals(List.of(1,2,3), talk.seatNumbers());
    }

    @Test
    void test03sellsOneTicketAndLinksOwner() {
        Activity a = new Fair().createActivity("Talk", 3, List.of(1,2,3));

        assertEquals("WB-1",
                new Fair().sell(a, new Wristband("WB-1"), 2).owner().code());
        assertEquals(2, a.remaining());
    }

    @Test
    void test04accessConsumesOneAndIncrementsGlobalCounter() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Talk", 3, List.of(1,2,3));

        fair.sell(a, new Wristband("WB-1"), 1);
        assertEquals(0, fair.totalConsumptions());

        fair.access(a, new Wristband("WB-1"), 1);
        assertEquals(1, fair.totalConsumptions());

        assertThrows(IllegalStateException.class,
                () -> fair.access(a, new Wristband("WB-1"), 1));
    }

    @Test
    void test05prizeBelongsToThirdPersonAccessingActivity() {
        Fair fair = new Fair();
        Activity keynote = fair.createActivity("Keynote", 5, List.of(1,2,3,4,5));

        fair.sell(keynote, new Wristband("W1"), 1);
        fair.sell(keynote, new Wristband("W2"), 2);
        fair.sell(keynote, new Wristband("W3"), 3);

        fair.access(keynote, new Wristband("W1"), 1);
        fair.access(keynote, new Wristband("W2"), 2);
        fair.access(keynote, new Wristband("W3"), 3);

        assertTrue(fair.hasPrize(new Wristband("W3")));
        assertFalse(fair.hasPrize(new Wristband("W1")));
        assertFalse(fair.hasPrize(new Wristband("W2")));
    }

    @Test
    void test06cannotAccessWithoutMatchingTicket() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Workshop", 2, List.of(5,6));

        fair.sell(a, new Wristband("Buyer"), 5);

        assertThrows(IllegalArgumentException.class,
                () -> fair.access(a, new Wristband("Intruder"), 5));
    }

    @Test
    void test07countTotalConsumptionsAcrossActivities() {
        Fair fair = new Fair();
        Activity morning = fair.createActivity("Morning", 2, List.of(1,2));
        Activity evening = fair.createActivity("Evening", 2, List.of(3,4));

        fair.sell(morning, new Wristband("W1"), 1);
        fair.sell(morning, new Wristband("W1"), 2);
        fair.sell(evening, new Wristband("W2"), 3);

        fair.access(morning, new Wristband("W1"), 1);
        fair.access(evening, new Wristband("W2"), 3);

        assertEquals(2, fair.totalConsumptions());
    }

    @Test
    void test08sameWristbandCanBuyInMultipleActivities() {
        Fair fair = new Fair();
        Wristband wb = new Wristband("WB-1");

        Activity a1 = fair.createActivity("Morning", 2, List.of(1,2));
        Activity a2 = fair.createActivity("Evening", 2, List.of(3,4));

        assertEquals("WB-1", fair.sell(a1, wb, 1).owner().code());
        assertEquals("WB-1", fair.sell(a2, wb, 3).owner().code());
    }

    @Test
    void test09wristbandCanWinPrizeInOneActivityButNotAnother() {
        Fair fair = new Fair();
        Wristband wb = new Wristband("Winner");

        Activity a1 = fair.createActivity("Panel", 3, List.of(1,2,3));
        Activity a2 = fair.createActivity("Concert", 3, List.of(4,5,6));

        // Ventas
        fair.sell(a1, new Wristband("A"), 1);
        fair.sell(a1, new Wristband("B"), 2);
        fair.sell(a1, wb, 3);

        fair.sell(a2, new Wristband("C"), 4);
        fair.sell(a2, wb, 5);
        fair.sell(a2, new Wristband("Q"), 6);

        // Accesos: wb es 3er acceso en a1 → gana
        fair.access(a1, new Wristband("A"), 1);
        fair.access(a1, new Wristband("B"), 2);
        fair.access(a1, wb, 3);

        // Accesos en a2: wb entra 2º, el 3º es Q → wb NO gana
        fair.access(a2, new Wristband("C"), 4);
        fair.access(a2, wb, 5);
        fair.access(a2, new Wristband("Q"), 6);

        assertTrue(fair.hasPrize(wb));
    }

    @Test
    void test10wristbandCanWinPrizesInMultipleActivities() {
        Fair fair = new Fair();
        Wristband wb = new Wristband("Champion");

        Activity a1 = fair.createActivity("Talk1", 3, List.of(1,2,3));
        Activity a2 = fair.createActivity("Talk2", 3, List.of(4,5,6));

        // Ventas
        fair.sell(a1, new Wristband("X"), 1);
        fair.sell(a1, new Wristband("Y"), 2);
        fair.sell(a1, wb, 3);

        fair.sell(a2, new Wristband("P"), 4);
        fair.sell(a2, new Wristband("Q"), 5);
        fair.sell(a2, wb, 6);

        // Accesos: wb entra 3º en ambas actividades → gana en las dos
        fair.access(a1, new Wristband("X"), 1);
        fair.access(a1, new Wristband("Y"), 2);
        fair.access(a1, wb, 3);

        fair.access(a2, new Wristband("P"), 4);
        fair.access(a2, new Wristband("Q"), 5);
        fair.access(a2, wb, 6);

        assertTrue(fair.hasPrize(wb));
    }

    @Test
    void test11hasPrizeIsFalseWhenNoTicketsSold() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Empty", 3, List.of(1,2,3));

        assertFalse(fair.hasPrize(new Wristband("WB-1")));
    }

    @Test
    void test12hasPrizeIsFalseWhenTicketsSoldButNoThirdYet() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Panel", 3, List.of(1,2,3));

        fair.sell(a, new Wristband("W1"), 1);
        fair.sell(a, new Wristband("W2"), 2);

        assertFalse(fair.hasPrize(new Wristband("W1")));
        assertFalse(fair.hasPrize(new Wristband("W2")));
    }

    @Test
    void test13totalConsumptionsIsZeroWhenNoAccesses() {
        Fair fair = new Fair();
        fair.createActivity("Concert", 2, List.of(1,2));

        assertEquals(0, fair.totalConsumptions());
    }

    @Test
    void test14hasPrizeRemainsFalseIfWinnerNeverSoldThirdTicket() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Talk", 2, List.of(1,2));

        fair.sell(a, new Wristband("W1"), 1);
        fair.sell(a, new Wristband("W2"), 2);

        // No hubo tercer ticket vendido → nadie gana
        assertFalse(fair.hasPrize(new Wristband("W1")));
        assertFalse(fair.hasPrize(new Wristband("W2")));
    }

    @Test
    void test15prizeGoesToThirdAccessRegardlessOfPurchaseOrder() {
        Fair fair = new Fair();
        Activity keynote = fair.createActivity("Keynote", 5, List.of(1,2,3,4,5));

        // Compran en orden A, B, C
        fair.sell(keynote, new Wristband("A"), 1);
        fair.sell(keynote, new Wristband("B"), 2);
        fair.sell(keynote, new Wristband("C"), 3);

        // Acceden en orden B, C, A → el 3er acceso es A
        fair.access(keynote, new Wristband("B"), 2);
        fair.access(keynote, new Wristband("C"), 3);
        fair.access(keynote, new Wristband("A"), 1);

        assertTrue(fair.hasPrize(new Wristband("A")));   // gana A por ser 3er acceso
        assertFalse(fair.hasPrize(new Wristband("B")));
        assertFalse(fair.hasPrize(new Wristband("C")));
    }


}
