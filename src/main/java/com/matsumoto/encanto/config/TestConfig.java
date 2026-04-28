package com.matsumoto.encanto.config;

import com.matsumoto.encanto.domain.Cor;
import com.matsumoto.encanto.repository.CorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

// Classe para popular o banco de dados

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    private final CorRepository cor;

    public TestConfig(CorRepository cor) {
        this.cor = cor;
    }
    @Override
    public void run(String... args) throws Exception {
        Cor cor1 = new Cor(null, "azul");
        Cor cor2 = new Cor(null, "vermelho");

        cor.saveAll(Arrays.asList(cor1, cor2));
    }
}
