package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {
    @Test
    void test00newTicketStoresSeatNumber() {
        Ticket t = new Ticket(new Wristband("WB-1"), 7);
        assertEquals(7, t.seatNumber());
    }

    @Test
    void test01newTicketIsNotConsumedAndHasNoPrize() {
        Ticket t = new Ticket(new Wristband("WB-2"), 1);
        assertFalse(t.hasPrize());
        assertDoesNotThrow(t::consume);
    }


    @Test
    void test02storesOwnerAndSeatCorrectly() {
        Ticket t = new Ticket(new Wristband("WB-9"), 7);
        assertEquals("WB-9", t.owner().code());
        assertEquals(7, t.seatNumber());
    }

    @Test
    void test03ticketHasNoPrizeByDefault() {
        assertFalse(new Ticket(new Wristband("WB-1"), 1).hasPrize());
    }

    @Test
    void test04assignPrizeMarksTicketAsWinner() {
        Ticket t = new Ticket(new Wristband("WB-2"), 2);
        t.assignPrize();
        assertTrue(t.hasPrize());
    }

    @Test
    void test05consumeWorksFirstTime() {
        Ticket t = new Ticket(new Wristband("WB-3"), 3);
        assertDoesNotThrow(t::consume);
    }

    @Test
    void test06doubleConsumeThrowsException() {
        Ticket t = new Ticket(new Wristband("WB-4"), 4);
        t.consume();
        assertThrows(IllegalStateException.class, t::consume);
    }
}
