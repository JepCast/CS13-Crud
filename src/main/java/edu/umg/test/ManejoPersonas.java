package edu.umg.test;

import edu.umg.datos.Conexion;
import edu.umg.datos.PersonaJDBC;
import edu.umg.domain.Persona;
import java.sql.*;
import java.util.Scanner;

public class ManejoPersonas {

    public static void desplegar(){
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            //el autocommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            PersonaJDBC personaJdbc = new PersonaJDBC(conexion);
            //vamos a listar las personas
            //utilizamos el metodo list de personaJDBC
            //que devuelve un arraylist de objetos persona
            //y lo recorremos con un for each
            for (Persona persona : personaJdbc.select()) {
                System.out.println("persona = " + persona);
            }
            //pausa con sc
            conexion.commit(); //queda permanente en la base de datos
            System.out.println("Se ha hecho commit de la transaccion");

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        }
    }


    public static void main(String[] args) {

        desplegar(); //despliega la información de la base de datos de personas.


        Scanner sc = new Scanner(System.in);
        //definimos la variable conexion
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();
            //el autocommit por default es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            PersonaJDBC personaJdbc = new PersonaJDBC(conexion);

            //se inserta sin telefono ni correo
            Persona nuevaPersona = new Persona();
            nuevaPersona.setNombre("Monica");
            nuevaPersona.setApellido("Veliz");
            personaJdbc.insert(nuevaPersona);

            //pausa para verificar antes del commit
//            System.out.println("presione una tecla para continuar");
//            sc.nextLine();
            conexion.commit(); //queda permanente en la base de datos
            System.out.println("Se ha hecho commit de la transaccion");



//vamos a actalizar sus datos
//            Persona cambioPersona = new Persona();
//            cambioPersona.setId_persona(1);
//            cambioPersona.setNombre("Luis");
//            cambioPersona.setApellido("Garcia");
//            cambioPersona.setEmail("nuevocorreo@gmail.com");
//            cambioPersona.setTelefono("555555555");
//            personaJdbc.update(cambioPersona);
//            //pausa con sc
//            conexion.commit(); //queda permanente en la base de datos
//            System.out.println("Se ha hecho commit de la transaccion");


        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        }
    }
}
