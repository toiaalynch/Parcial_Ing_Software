package org.udesa.parcial_software;

/** Boleto que vincula pulsera y número de asiento. */
public final class Ticket {
    private final Wristband owner;
    private final int seatNumber;
    private boolean consumed = false;

    public Ticket(Wristband owner, int seatNumber) {
        this.owner = owner;
        this.seatNumber = seatNumber;
    }

    public Wristband owner() { return owner; }
    public int seatNumber() { return seatNumber; }
    private boolean prize = false;

    public boolean hasPrize() { return prize; }

    /** Marca este ticket como ganador de premio. */
    void assignPrize() { this.prize = true; }

    /** Marca el ticket como consumido. Lanza excepción si ya lo estaba. */
    public void consume() {
        if (consumed) {
            throw new IllegalStateException("Ticket already consumed: seat " + seatNumber);
        }
        consumed = true;
    }
}
