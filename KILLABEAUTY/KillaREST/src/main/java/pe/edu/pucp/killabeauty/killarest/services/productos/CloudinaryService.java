package pe.edu.pucp.killabeauty.killarest.services.productos;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary("cloudinary://963741142899685:bVLYTEjhAjH-8-HEgF7I524v_Ig@dlkbckbdm");
        cloudinary.config.secure = true;
    }

    public String subirImagen(File archivo) throws Exception {
        Map resultado = cloudinary.uploader().upload(
                archivo,
                ObjectUtils.asMap("folder", "killa_beauty/productos")
        );

        return resultado.get("secure_url").toString();
    }
}