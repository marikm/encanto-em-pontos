package com.matsumoto.encanto.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile arquivo) throws IOException {
        byte[] bytes = arquivo.getBytes();
        // transformacao para 600x600 com corte centralizado
        var opcoes = ObjectUtils.asMap("eager", "c_fill, w_600, h_600");
        Map resultado = cloudinary.uploader().upload(bytes, opcoes);

        return (String) resultado.get("secure_url");

    }
}
