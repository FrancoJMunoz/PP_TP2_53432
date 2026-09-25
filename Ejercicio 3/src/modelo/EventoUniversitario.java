package modelo;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

public class EventoUniversitario implements Serializable{ //IMPORTANTE "implements Serializable", ejercicio 2 TP 2
    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;

    private Sala sala;
    private List<Actividad> actividades;

    private static int cantidadEventos;
    static {
        cantidadEventos = 0;
        System.out.println("Inicializador estático: se cargo la clase EventoUniversitario.");
    }

    public EventoUniversitario (String id, String titulo, double costoBase, boolean gratuito) {
        cantidadEventos++;
        this.id = "EVT-"+cantidadEventos;
        if (gratuito) {
            this.costoBase = 0;
        } else {
            this.costoBase = costoBase;
        }
        setTitulo(titulo);

        this.actividades = new ArrayList<>(); //Relación de composición
    }

    public EventoUniversitario (EventoUniversitario otro) {
        this (otro.id + "-COPIA", otro.titulo, otro.costoBase, otro.gratuito);
    }

    public String getId () {
        return id;
    }

    public String getTitulo () {
        return titulo;
    }

    public void setTitulo (String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            this.titulo = titulo;
        }
    }

    public double calcularCostoEstimado () {
        if (gratuito) {
            return 0;
        }
        //Añadimos el costo de todas las actividades
        double costoTotal = costoBase;
        for(Actividad actividad : actividades) {
            costoTotal += actividad.calcularCostoMateriales();
        }
        return costoTotal * 1.21;
    }

    public static int getCantidadEventos () {
        return cantidadEventos;
    }

    public void mostrarDatos () {
        System.out.println("==============================================================");
        System.out.println("Evento Codigo: "+id);
        System.out.println("Titulo: "+titulo);
        System.out.println("Costo: "+this.calcularCostoEstimado());
        //Agregado en ejercicio 2:
        System.out.print("Sala: "); //Notar como no tiene "ln" en print, es porque no hace un salto de línea
        if (sala != null) {
            System.out.println(sala.getNombre());
        } else {
            System.out.println("Sin sala");
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("Actividades: ");
        for (Actividad actividad : actividades) {
            actividad.mostrarIdentificacion();
            actividad.mostrarInscripciones();
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("==============================================================");
    }

//Métodos del Ejercicio 2

    public Sala getSala() {
        return sala;
    }

    public void asignarSala (Sala sala) {
        this.sala = sala;
    }

    public List<Actividad> getActividades() {
        return Collections.unmodifiableList(actividades);
    }

//Métodos del ejercicio 3

    public void crearActividad(int id, String titulo, int cupo, String tipoActividad) {
        Scanner leer = new Scanner(System.in);
        switch (tipoActividad) {
            case "charla":
                System.out.println("Ingrese el nombre del disertante para la charla");
                String disertante = leer.nextLine();
                Actividad charla = new Charla(id, titulo, cupo, disertante); //Notar como es "Actividad charla" y no "Charla charla"
                this.actividades.add(charla);
                break;
            case "taller":
                System.out.println("Requiere uso de Notebooks? S/N");
                String respuesta = leer.nextLine().trim().toLowerCase();
                boolean notebook = false;
                if(respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                    notebook = true;
                }
                Actividad taller = new Taller (id, titulo, cupo, notebook);
                this.actividades.add(taller);
                break;
            case "curso": //Añadido en TP2, ejercicio 2
                System.out.println("Ingrese el nivel del curso, entre 1 y 4");
                int nivel = leer.nextInt();
                leer.nextLine();
                Actividad curso = new Curso(id, titulo, cupo, nivel);
                this.actividades.add(curso);
                break;
            default:
                System.out.println("No se encontró el tipo de actividad");
        }

    }

    //Métodos TP 2 Ejercicio 1

    public boolean persistirEvento() throws IOException {
        String nombreArchivo = "evento_" + this.id + ".dat";
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(this);
            return true;
        }
    }

    public EventoUniversitario recuperarEvento (String id) throws IOException, ClassNotFoundException {
        String nombreArchivo = "evento_" + this.id + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return(EventoUniversitario) ois.readObject();
        }
    }

    //Métodos TP 2 Ejercicio 3

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (tipo.isInstance(actividad)) {
                resultado.add(tipo.cast(actividad));
            }
        }
        return resultado;
    }

    public double calcularCostoMateriales (List<? extends Actividad> actividades) {
        double total = 0.0;
        for(Actividad actividad : actividades) {
            total+= actividad.calcularCostoMateriales();
        }
        return total;
    }
}
