package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;

@Service
public class cloudinaryService {

    //Definir el tamaño de las imagenes en megabytes
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    //Definir las extensiones permitidas
    private static final  String[] ALLOWED_EXTENSIONS = {".jpg",".jpeg",".png"};
    //Constructor para inyeccion de dependencias cloudinary
    private final Cloudinary cloudinary;

    public cloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);
    }

    private void validateImage(MultipartFile file) {
        //Verificar si el archivo esta vacio
        if(file.isEmpty()){
            throw new IllegalArgumentException("El archivo esta vacío.");
        }

        //Verificar si el tamaño excede el limite
        if(file.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException("El tamaño del archivo no debe de ser mayor a 5MB");
        }

        //Verificar el nombre original del archivo
        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null){
            throw new IllegalArgumentException("Nombre de archivo invalido");
        }

        //Extraer y validar la extensión del archivo
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if(!Arrays.asList(ALLOWED_EXTENSIONS).contains(extension)){
            throw new IllegalArgumentException("Solo se permite archivos JPG, JPEG, PNG");
        }

        //Verificar que el tipo MIME sea una imagen
        if(!file.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("El archivo debe de ser una imagen valida");
        }
    }
}
