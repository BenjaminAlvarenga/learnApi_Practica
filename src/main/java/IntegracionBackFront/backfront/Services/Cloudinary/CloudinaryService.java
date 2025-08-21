package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
public class CloudinaryService {

    //Constante para definir el tamaño maximo permitido para los archivos(5MB)
    private static final long MAX_FILE_SIZE =  5 * 1024 * 1024;

    //Extensiones de archivos permitidos para subir a Cloudinary
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".png", ".jpeg"};

    //Cliente de Cloudinary inyectado como dependencia
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    //Subir imagenes a la raiz de Cloudinary
    public String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);
        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto",
                "quality", "auto:good"
        ));
        return (String) uploadResult.get("secure_url");
    }

    private void validateImage(MultipartFile file) {
        //Verificar si el archivo esta vacio
        if(file.isEmpty()) throw new IllegalArgumentException("El archivo no puede estar vacio");
        if (file.getSize() > MAX_FILE_SIZE) throw new IllegalArgumentException("El tamaño del archivo no puede exceder los 5MB");
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null) throw new IllegalArgumentException("Nombre de archivo no valido");
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWED_EXTENSIONS).contains(extension)) throw new IllegalArgumentException("Solo se permiten archivos jpg, png o jpeg");
        if(!file.getContentType().startsWith("image/")) throw new IllegalArgumentException("El archivo debe ser una imagen valida");
    }
    //Subir imagenes a la carpeta de Cloudinary
}
