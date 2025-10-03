package org.udesa.parcial_software;

import java.util.List;

/** Punto de entrada del dominio: crea y orquesta actividades de la feria. */
public final class Fair {
    private int consumptions = 0;

    public Activity createActivity(String name, int capacity, List<Integer> seats) {
        return new Activity(name, capacity, seats);
    }

//    /** Vende un ticket delegando en la actividad. */
    public Ticket sell(Activity activity, Wristband owner, int seatNumber) {
        return activity.sellTo(owner, seatNumber);
    }

    /** Registra acceso del visitante a una actividad. Versión mínima sin validaciones. */
    public void access(Activity activity, Wristband owner, int seatNumber) {
        Ticket t = activity.findTicket(owner, seatNumber);
        t.consume();
        consumptions = consumptions + 1;
    }

    /** Cantidad total de accesos consumados en la feria. */
    public int totalConsumptions() { return consumptions; }

}
