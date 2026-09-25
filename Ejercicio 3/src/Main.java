
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Sala;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import modelo.certificacion.Certificable;

public class Main {
    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        String titulo;
        double costoBase;
        boolean gratuito = true;
        String id = "";
        int idsala = 0;
        String respuesta;
        boolean continuar = false;

        List<Estudiante> estudiantes = new ArrayList<>();
        System.out.println("REGISTRO DE ESTUDIANTES");
        System.out.println("=====================");
        do {
            System.out.println("Ingrese legajo del estudiante: ");
            String legajo = leer.nextLine();
            System.out.println("Ingrese nombre y apellido del estudiante: ");
            String nomyape = leer.nextLine();
            Estudiante estudiante = new Estudiante(legajo, nomyape);
            estudiantes.add(estudiante);
            System.out.println("Desea crear otro estudiante S/N?");
            respuesta = leer.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")){
                continuar = true;
            } else {
                continuar = false;
            }
        } while (continuar);

        System.out.println("REGISTRO DE EVENTOS");
        System.out.println("=====================");
        do {
            System.out.println("Ingrese un título para el evento: ");
            titulo = leer.nextLine();
            System.out.println("Ingrese el costo base: ");
            costoBase = leer.nextDouble();
            leer.nextLine();
            System.out.println("El evento tendra costo para los participantes S/N?");
            respuesta = leer.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")){
                gratuito = false;
            } else {
                gratuito = true;
            }

            EventoUniversitario evento = new EventoUniversitario (id, titulo, costoBase,gratuito);

            //Agrego sala, actividades e inscripciones del ejercicio 2:

            System.out.println("Ingrese el nombre de la sala: ");
            String nombresala = leer.nextLine();
            Sala sala = new Sala (idsala , nombresala);
            evento.asignarSala(sala);

            idsala++;

            System.out.println("REGISTRO DE ACTIVIDADES");
            System.out.println("=====================");
            int idActividad = 1;
            do {
                System.out.println("Ingrese el titulo de la actividad: ");
                String tituloActividad = leer.nextLine();
                System.out.println("Ingrese el cupo máximo de la actividad: ");
                int cupoMaximo = leer.nextInt();
                leer.nextLine();

                //Añadimos si es taller charla, ejercicio 3
                System.out.println("La actividad es una charla, un taller o un curso?"); //Se añade curso en TP2, ejercicio 2
                String tipo = leer.nextLine().trim().toLowerCase();;

                //Añadimos tipo como parámetro
                evento.crearActividad(idActividad,tituloActividad,cupoMaximo, tipo);
                //Tuve que eliminar el "eventoCopia" porque me pedía de vuelta la validación de tipo
                System.out.println("Desea crear otra actividad S/N?");
                respuesta = leer.nextLine().trim().toLowerCase();
                if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")){
                    continuar = true;
                } else {
                    continuar = false;
                }
                idActividad++;
            } while(continuar);

            // Se añade el try-catch para TP2 Ejercicio 1, excepciones
            try {
                System.out.println("INSCRIPCIÓN DE ESTUDIANTES");
                System.out.println("=====================");
                do {
                    System.out.println("Ingrese legajo del estudiante a inscribir: ");
                    String legajo = leer.nextLine();
                    System.out.println("Ingrese id de la actividad: ");
                    idActividad = leer.nextInt();
                    leer.nextLine();

                    for (Estudiante estudiante : estudiantes) {
                        if (estudiante.getLegajo().equals(legajo)) {
                            evento.getActividades().get(--idActividad).inscribir(estudiante); //Notar que moví el "--" al parámetro y se coloca ANTES DE ID ACTIVIDAD

                        }
                    }
                    System.out.println("Desea generar otra inscripción? S/N");
                    respuesta = leer.nextLine().trim().toLowerCase();
                    if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                        continuar = true;
                    } else {
                        continuar = false;
                    }
                } while (continuar);
            } catch (CupoExcedidoException e) {
                System.out.println("Error al inscribirse: "+ e.getMessage());
            }

            // Serialización y captura de errores (De lo específico a lo general)
            try {
                evento.persistirEvento();
                EventoUniversitario copiaDesdeArchivo = evento.recuperarEvento(evento.getId());
                evento.mostrarDatos();
                copiaDesdeArchivo.mostrarDatos();

                //Implementamos generics para las actividades del ejercicio 3
                List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
                List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
                List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

                System.out.println("Actividades filtradas por tipo usando método parametrizado acotado");
                System.out.println("Talleres encontrados: "+talleres.size());
                System.out.println("Charlas encontradas: "+charlas.size());
                System.out.println("Cursos encontrados: "+cursos.size());

                //Uso de wildcards
                System.out.println("Costo de materiales de talleres: "+evento.calcularCostoMateriales(talleres));
                //Charlas no tiene costo
                System.out.println("Costo de materiales de cursos: "+evento.calcularCostoMateriales(cursos));
                System.out.println("Costo de materiales de todas las actividades: "+evento.calcularCostoMateriales(evento.getActividades()));

            } catch (FileNotFoundException e) {
                System.out.println("Error 01: No se encontró el archivo del evento: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("No fue posible reconstruir el objeto almacenado: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Se produjo un error de entrada/salida: " + e.getMessage());
            }

            //Añadimos la certificación del TP2, Ejercicio 2
            for (Actividad actividad : evento.getActividades()) {
                if (actividad instanceof Certificable certificable) { //También es posible preguntar si es instancia de taller o curso
                    System.out.println("Certificados emitidos para la actividad "+ actividad.getTitulo());
                    for (Inscripcion inscripcion : actividad.getInscripciones()) {
                        String certificado = certificable.generarCertificado(inscripcion.getEstudiante());
                        System.out.println(certificado);
                    }
                }
            }

            System.out.println("Desea crear otro evento S/N?");
            respuesta = leer.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")){
                continuar = true;
            } else {
                continuar = false;
            }
        } while (continuar);
        System.out.println("TOTAL DE EVENTOS CREADOS: "+ EventoUniversitario.getCantidadEventos());
    }
}