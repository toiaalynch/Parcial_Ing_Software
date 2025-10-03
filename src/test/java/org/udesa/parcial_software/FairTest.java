package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FairTest {

    @Test
    void test01createsActivityWithNameCapacityAndSeats() {
        Activity talk = new Fair().createActivity("Talk", 3, List.of(1, 2, 3));

        assertEquals("Talk", talk.name());
        assertEquals(3, talk.remaining());
        assertEquals(List.of(1,2,3), talk.seatNumbers());
    }

    @Test
    void test02sellsOneTicketAndLinksOwner() {
        Activity a = new Fair().createActivity("Talk", 3, List.of(1,2,3));

        assertEquals("WB-1",
                new Fair().sell(a, new Wristband("WB-1"), 2).owner().code());
        assertEquals(2, a.remaining());
    }

    @Test
    void test03accessConsumesOneAndIncrementsGlobalCounter() {
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
    void test04prizeBelongsToThirdBuyerAndCanBeQueriedFromFair() {
        Fair fair = new Fair();
        Activity keynote = fair.createActivity("Keynote", 5, List.of(1,2,3,4,5));

        fair.sell(keynote, new Wristband("W1"), 1);
        fair.sell(keynote, new Wristband("W2"), 2);

        assertTrue(fair.hasPrize(fair.sell(keynote, new Wristband("W3"), 3).owner()));
        assertFalse(fair.hasPrize(new Wristband("W1")));
        assertFalse(fair.hasPrize(new Wristband("W2")));
    }

    @Test
    void test05cannotAccessWithoutMatchingTicket() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Workshop", 2, List.of(5,6));

        fair.sell(a, new Wristband("Buyer"), 5);

        assertThrows(IllegalArgumentException.class,
                () -> fair.access(a, new Wristband("Intruder"), 5));
    }

    @Test
    void test06countTotalConsumptionsAcrossActivities() {
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
    void test07sameWristbandCanBuyInMultipleActivities() {
        Fair fair = new Fair();
        Wristband wb = new Wristband("WB-1");

        Activity a1 = fair.createActivity("Morning", 2, List.of(1,2));
        Activity a2 = fair.createActivity("Evening", 2, List.of(3,4));

        assertEquals("WB-1", fair.sell(a1, wb, 1).owner().code());
        assertEquals("WB-1", fair.sell(a2, wb, 3).owner().code());
    }

    @Test
    void test08wristbandCanWinPrizeInOneActivityButNotAnother() {
        Fair fair = new Fair();
        Wristband wb = new Wristband("Winner");

        Activity a1 = fair.createActivity("Panel", 3, List.of(1,2,3));
        Activity a2 = fair.createActivity("Concert", 3, List.of(4,5,6));

        fair.sell(a1, new Wristband("A"), 1);
        fair.sell(a1, new Wristband("B"), 2);
        fair.sell(a1, wb, 3);  // gana premio en a1

        fair.sell(a2, new Wristband("C"), 4);
        fair.sell(a2, wb, 5);  // no gana premio en a2 (es el 2do)

        assertTrue(fair.hasPrize(wb));  // porque ganó en a1
    }

    @Test
    void test09wristbandCanWinPrizesInMultipleActivities() {
        Fair fair = new Fair();
        Wristband wb = new Wristband("Champion");

        Activity a1 = fair.createActivity("Talk1", 3, List.of(1,2,3));
        Activity a2 = fair.createActivity("Talk2", 3, List.of(4,5,6));

        fair.sell(a1, new Wristband("X"), 1);
        fair.sell(a1, new Wristband("Y"), 2);
        fair.sell(a1, wb, 3);  // premio en a1

        fair.sell(a2, new Wristband("P"), 4);
        fair.sell(a2, new Wristband("Q"), 5);
        fair.sell(a2, wb, 6);  // premio en a2

        assertTrue(fair.hasPrize(wb));  // ganador en ambas
    }

    @Test
    void test10hasPrizeIsFalseWhenNoTicketsSold() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Empty", 3, List.of(1,2,3));

        assertFalse(fair.hasPrize(new Wristband("WB-1")));
    }

    @Test
    void test11hasPrizeIsFalseWhenTicketsSoldButNoThirdYet() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Panel", 3, List.of(1,2,3));

        fair.sell(a, new Wristband("W1"), 1);
        fair.sell(a, new Wristband("W2"), 2);

        assertFalse(fair.hasPrize(new Wristband("W1")));
        assertFalse(fair.hasPrize(new Wristband("W2")));
    }

    @Test
    void test12totalConsumptionsIsZeroWhenNoAccesses() {
        Fair fair = new Fair();
        fair.createActivity("Concert", 2, List.of(1,2));

        assertEquals(0, fair.totalConsumptions());
    }

    @Test
    void test13hasPrizeRemainsFalseIfWinnerNeverSoldThirdTicket() {
        Fair fair = new Fair();
        Activity a = fair.createActivity("Talk", 2, List.of(1,2));

        fair.sell(a, new Wristband("W1"), 1);
        fair.sell(a, new Wristband("W2"), 2);

        // No hubo tercer ticket vendido → nadie gana
        assertFalse(fair.hasPrize(new Wristband("W1")));
        assertFalse(fair.hasPrize(new Wristband("W2")));
    }


}
