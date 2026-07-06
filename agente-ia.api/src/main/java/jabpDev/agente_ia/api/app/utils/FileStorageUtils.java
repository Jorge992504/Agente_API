package jabpDev.agente_ia.api.app.utils;


import jabpDev.agente_ia.api.exception.ErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;
import java.nio.file.*;

@Service
public class FileStorageUtils {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;


    public String salvarBase64(String base64, String prefixo){
        if (base64 == null || base64.isEmpty()) return null;

        try {
            String[] partes = base64.split(",");
            String meta = partes[0];
            byte[] bytes = Base64.getDecoder().decode(partes.length > 1 ? partes[1] : partes[0]);
            String extensao = meta.contains("/") ? meta.split("/")[1].split(";")[0] : "bin";

            String nomeArquivo = prefixo + "_" + UUID.randomUUID() + "." + extensao;
            Path caminhoCompleto = Paths.get(uploadDir, nomeArquivo);
            Files.createDirectories(caminhoCompleto.getParent());
            Files.write(caminhoCompleto, bytes);
            return "/uploads/" + nomeArquivo;
        }catch (Exception e){
            throw new ErrorException("Erro ao salvar o arquivo enviado", 500);
        }
    }
}
