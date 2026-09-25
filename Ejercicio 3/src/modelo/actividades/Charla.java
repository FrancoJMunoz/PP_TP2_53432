package modelo.actividades;

public class Charla extends Actividad {

    private String disertante;

    public Charla (int id, String titulo, int cupo, String disertante) {
        super(id, titulo, cupo);
        this.disertante = disertante;
    }

    public String getDisertante() {
        return disertante;
    }

    public void setDisertante (String disertante) {
        if (disertante == null || disertante.isBlank()) {
            return;
        }
        this.disertante = disertante;
    }

    //POLIMORFISMO

    @Override
    public double calcularCostoMateriales() {
        return 0.0;
    }

    @Override
    public String getTipo() {
        return this.getClass().getName();
    }

}
