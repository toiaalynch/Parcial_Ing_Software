package org.udesa.parcial_software;

import java.util.*;

/** Punto de entrada del dominio: crea y orquesta actividades de la feria. */
public final class Fair {
    private int consumptions = 0;
    private final List<Activity> activities = new ArrayList<>();

    /** Crea una actividad y la registra en la feria. */
    public Activity createActivity(String name, int capacity, List<Integer> seats) {
        Activity a = new Activity(name, capacity, seats);
        activities.add(a);
        return a;
    }

    /** Vende un ticket delegando en la actividad. */
    public Ticket sell(Activity activity, Wristband owner, int seatNumber) {
        return activity.sellTo(owner, seatNumber);
    }

    /** Registra acceso del visitante a una actividad. */
    public void access(Activity activity, Wristband owner, int seatNumber) {
        Ticket t = activity.findTicket(owner, seatNumber);
        t.consume(); // lanza excepción si ya estaba consumido
        consumptions++;
    }

    /** Cantidad total de accesos consumados en la feria. */
    public int totalConsumptions() { return consumptions; }

    /** Consulta si una pulsera ganó algún premio en cualquier actividad de la feria. */
    public boolean hasPrize(Wristband wb) {
        return activities.stream()
                .flatMap(a -> a.soldTickets().stream())
                .anyMatch(t -> t.owner().code().equals(wb.code()) && t.hasPrize());
    }
}
