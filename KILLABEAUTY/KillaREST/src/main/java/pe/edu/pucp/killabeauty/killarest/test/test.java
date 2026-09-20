package pe.edu.pucp.killabeauty.killarest.test;

import java.io.IOException;
import pe.edu.pucp.killabeauty.killarest.services.productos.ProductoRS;
public class test {

    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "http://localhost:8080/KillaREST-1.0-SNAPSHOT/services/productos/test";


        ProductoRS producto = new ProductoRS();
        String resultado = producto.test();
//        String resultado = new HttpClientUtils<List<AlumnoDTO>>().get(url, new TypeReference<List<AlumnoDTO>>() {});
//

//
//        url = "http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/alumno";
//
//        AlumnoDTO nuevoAlumno = new AlumnoDTO();
//        nuevoAlumno.setNombre("Alumno java");
//        nuevoAlumno.setApellido("Apellido java");
//
//        AlumnoDTO alumnoRespuesta = new HttpClientUtils<AlumnoDTO>().post(url, nuevoAlumno, new TypeReference<AlumnoDTO>() {});
//        System.out.println(alumnoRespuesta.getCodigo());

    }
}

