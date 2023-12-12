package com.springboot.jpa.springbootjpa.models.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final static String UPLOADS_FOLDER = "uploads";

    @Override
    public String copy(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path rutaAbsoluta = getPath(uniqueFilename);
        log.info("rootPath: " + rutaAbsoluta);
        
        Files.copy(file.getInputStream(), rutaAbsoluta);
            
        return uniqueFilename;
    }

    @Override
    public boolean delete(String filename) {
        //Eliminando la foto
        Path rooPath = getPath(filename);
        File archivo = rooPath.toFile();

        //verifico que el archivo exista y que se pueda leer
        if(archivo.exists() && archivo.canRead()) {
            if(archivo.delete()) {
                return true;
            }
        }

        return false;
    }

    public Path getPath(String filename) {
        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }

    //Metodo que elimina el directorio con todo su contenido
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
    }

    //Metodo que crea el directorio
    @Override
    public void init() throws IOException {
        Files.createDirectories(Paths.get(UPLOADS_FOLDER));
    } 
    
}
