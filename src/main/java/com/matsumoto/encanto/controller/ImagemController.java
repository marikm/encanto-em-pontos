package com.matsumoto.encanto.controller;

import com.matsumoto.encanto.dto.ProdutoResponse;
import com.matsumoto.encanto.dto.VariacaoResponse;
import com.matsumoto.encanto.service.CloudinaryService;
import com.matsumoto.encanto.service.ProdutoService;
import com.matsumoto.encanto.service.VariacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api")
public class ImagemController {

    private final CloudinaryService cloudinaryService;
    private final ProdutoService produtoService;
    private final VariacaoService variacaoService;

    public ImagemController(CloudinaryService cloudinaryService, ProdutoService produtoService, VariacaoService variacaoService) {
        this.cloudinaryService = cloudinaryService;
        this.produtoService = produtoService;
        this.variacaoService = variacaoService;
    }
    @PostMapping("/produtos/{id}/foto")
    public ResponseEntity<ProdutoResponse> uploadFotoProduto( @PathVariable Integer id,
            @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        String url = cloudinaryService.upload(arquivo);
        ProdutoResponse produtoResponse = produtoService.atualizarFoto(id, url);
        return ResponseEntity.ok().body(produtoResponse);
    }

    @PostMapping("/variacoes/{id}/foto")
    public ResponseEntity<VariacaoResponse> uploadFotoVariacao(@PathVariable Integer id,
            @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        String url = cloudinaryService.upload(arquivo);
        VariacaoResponse variacaoResponse = variacaoService.atualizarFoto(id, url);
        return ResponseEntity.ok().body(variacaoResponse);
    }
}
