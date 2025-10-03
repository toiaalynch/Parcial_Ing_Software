package org.udesa.parcial_software;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    @Test
    void test01storesOwnerAndSeatCorrectly() {
        Ticket t = new Ticket(new Wristband("WB-9"), 7);
        assertEquals("WB-9", t.owner().code());
        assertEquals(7, t.seatNumber());
    }

    @Test
    void test02ticketHasNoPrizeByDefault() {
        assertFalse(new Ticket(new Wristband("WB-1"), 1).hasPrize());
    }

    @Test
    void test03assignPrizeMarksTicketAsWinner() {
        Ticket t = new Ticket(new Wristband("WB-2"), 2);
        t.assignPrize();
        assertTrue(t.hasPrize());
    }

    @Test
    void test04consumeWorksFirstTime() {
        Ticket t = new Ticket(new Wristband("WB-3"), 3);
        assertDoesNotThrow(t::consume);
    }

    @Test
    void test05doubleConsumeThrowsException() {
        Ticket t = new Ticket(new Wristband("WB-4"), 4);
        t.consume();
        assertThrows(IllegalStateException.class, t::consume);
    }
}
