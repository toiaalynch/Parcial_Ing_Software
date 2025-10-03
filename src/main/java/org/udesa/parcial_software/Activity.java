package org.udesa.parcial_software;

import java.util.*;

/** Actividad con asientos numerados, tickets vendidos y control de disponibilidad. */
public final class Activity {
    private final String name;
    private int remaining;
    private final List<Integer> seatNumbers;
    private final List<Ticket> soldTickets = new ArrayList<>();

    // Premio: estado de la máquina
    private PrizeState prizeState = new Waiting1();

    public Activity(String name, int capacity, List<Integer> seatNumbers) {
        this.name = name;
        this.seatNumbers = List.copyOf(seatNumbers);

        // Validar consistencia
        Optional.of(capacity)
                .filter(c -> c == this.seatNumbers.size())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Capacity must equal seatNumbers size"));

        this.remaining = capacity;
    }

    /** Vende un ticket, validando asiento y capacidad. */
    public Ticket sellTo(Wristband owner, int seatNumber) {
        // 1. El asiento debe estar en la lista numerada
        seatNumbers.stream()
                .filter(n -> n == seatNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown seat number: " + seatNumber));

        // 2. El asiento no debe estar ya vendido
        soldTickets.stream()
                .filter(t -> t.seatNumber() == seatNumber)
                .findAny()
                .ifPresent(t -> { throw new IllegalStateException("Seat already sold: " + seatNumber); });

        // 3. Debe quedar capacidad disponible
        Optional.of(remaining)
                .filter(r -> r > 0)
                .orElseThrow(() -> new IllegalStateException("No remaining capacity"));

        // 4. Vender y registrar
        remaining = remaining - 1;
        Ticket t = new Ticket(owner, seatNumber);
        soldTickets.add(t);
        return t;
    }

    /** Busca ticket por dueño + asiento. */
    public Ticket findTicket(Wristband owner, int seatNumber) {
        return soldTickets.stream()
                .filter(t -> t.seatNumber() == seatNumber && t.owner().equals(owner))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such ticket"));
    }

    /** Acceso: consume el ticket y decide premio. */
    public void access(Wristband owner, int seatNumber) {
        Ticket t = findTicket(owner, seatNumber);
        t.consume();
        prizeState = prizeState.onAccess(t);
    }

    public List<Ticket> soldTickets() {
        return Collections.unmodifiableList(soldTickets);
    }

    public String name() { return name; }
    public int remaining() { return remaining; }
    public List<Integer> seatNumbers() { return Collections.unmodifiableList(seatNumbers); }

    private abstract class PrizeState { abstract PrizeState onAccess(Ticket t); }
    private final class Waiting1 extends PrizeState { @Override PrizeState onAccess(Ticket t){ return new Waiting2(); } }
    private final class Waiting2 extends PrizeState { @Override PrizeState onAccess(Ticket t){ return new Waiting3(); } }
    private final class Waiting3 extends PrizeState { @Override PrizeState onAccess(Ticket t){ t.assignPrize(); return new Chosen(); } }
    private final class Chosen   extends PrizeState { @Override PrizeState onAccess(Ticket t){ return this; } }

}
