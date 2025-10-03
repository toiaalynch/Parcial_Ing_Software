package org.udesa.parcial_software;

import java.util.*;

/** Punto de entrada del dominio: crea y orquesta actividades de la feria. */
public final class Fair {
    private int consumptions = 0;
    private final List<Activity> activities = new ArrayList<>();

    public Activity createActivity(String name, int capacity, List<Integer> seats) {
        Activity a = new Activity(name, capacity, seats);
        activities.add(a);
        return a;
    }

    public Ticket sell(Activity activity, Wristband owner, int seatNumber) {
        return activity.sellTo(owner, seatNumber);
    }

    public void access(Activity activity, Wristband owner, int seatNumber) {
        activity.access(owner, seatNumber); // Activity decide premio y consumo
        consumptions++;
    }

    public int totalConsumptions() { return consumptions; }

    public boolean hasPrize(Wristband wb) {
        return activities.stream()
                .flatMap(a -> a.soldTickets().stream())
                .anyMatch(t -> t.owner().code().equals(wb.code()) && t.hasPrize());
    }
}
