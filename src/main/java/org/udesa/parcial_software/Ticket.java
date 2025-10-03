// src/main/java/org/udesa/parcial_software/Ticket.java
package org.udesa.parcial_software;

public final class Ticket {
    private final Wristband owner;
    private final int seatNumber;
    private boolean prize = false;

    private Consumption state = new Fresh();

    public Ticket(Wristband owner, int seatNumber) {
        this.owner = owner;
        this.seatNumber = seatNumber;
    }

    public Wristband owner() { return owner; }
    public int seatNumber() { return seatNumber; }

    public boolean hasPrize() { return prize; }
    void assignPrize() { this.prize = true; }

    public void consume() { state = state.consume(this); }

    private static abstract class Consumption { abstract Consumption consume(Ticket t); }
    private static final class Fresh extends Consumption {
        @Override Consumption consume(Ticket t) { return new Used(); }
    }
    private static final class Used extends Consumption {
        @Override Consumption consume(Ticket t) {
            throw new IllegalStateException("Ticket already consumed: seat " + t.seatNumber());
        }
    }
}
