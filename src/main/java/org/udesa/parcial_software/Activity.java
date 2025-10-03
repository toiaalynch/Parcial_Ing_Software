// src/main/java/org/udesa/parcial_software/Activity.java
package org.udesa.parcial_software;

import java.util.*;

/** Actividad con asientos numerados, tickets vendidos y control de disponibilidad. */
public final class Activity {
    private final String name;
    private int remaining;
    private final List<Integer> seatNumbers;
    private final List<Ticket> soldTickets = new ArrayList<>();

    public Activity(String name, int capacity, List<Integer> seatNumbers) {
        this.name = name;
        this.remaining = capacity;
        this.seatNumbers = List.copyOf(seatNumbers);
    }

    /** Vende un ticket para un asiento, lanzando excepción si ya está ocupado. */
    private PrizeState prizeState = new Waiting1();

    public Ticket sellTo(Wristband owner, int seatNumber) {
        soldTickets.stream()
                .filter(t -> t.seatNumber() == seatNumber)
                .findAny()
                .ifPresent(t -> { throw new IllegalStateException("Seat already sold: " + seatNumber); });

        Optional.of(remaining)
                .filter(r -> r > 0)
                .orElseThrow(() -> new IllegalStateException("No remaining capacity"));

        remaining = remaining - 1;
        Ticket t = new Ticket(owner, seatNumber);
        soldTickets.add(t);

        // delega en PrizeState
        prizeState = prizeState.onSell(t);

        return t;
    }

    public Ticket findTicket(Wristband owner, int seatNumber) {
        return soldTickets.stream()
                .filter(t -> t.seatNumber() == seatNumber && t.owner().equals(owner))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such ticket"));
    }

    public String name() { return name; }
    public int remaining() { return remaining; }
    public List<Integer> seatNumbers() { return Collections.unmodifiableList(seatNumbers); }

    abstract class PrizeState {
        abstract PrizeState onSell(Ticket t);
    }

    final class Waiting1 extends PrizeState {
        PrizeState onSell(Ticket t) { return new Waiting2(); }
    }

    final class Waiting2 extends PrizeState {
        PrizeState onSell(Ticket t) { return new Waiting3(); }
    }

    final class Waiting3 extends PrizeState {
        PrizeState onSell(Ticket t) {
            t.assignPrize(); // tercer ticket → premio
            return new Chosen();
        }
    }

    final class Chosen extends PrizeState {
        PrizeState onSell(Ticket t) { return this; } // ya no cambia
    }


}
