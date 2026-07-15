package com.db.imagevalidate.service;

import com.db.imagevalidate.entity.PalavraProibida;
import com.db.imagevalidate.repository.PalavraProibidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PalavraProibidaCacheService {

    private static final String CACHE_KEY = "palavras-proibidas";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PalavraProibidaRepository repository;


    public List<String> buscarPalavras() {

        List<String> cache = redisTemplate.opsForList()
                .range(CACHE_KEY, 0, -1);

        if (cache != null && !cache.isEmpty()) {
            return cache;
        }

        List<String> palavras = repository.findAll()
                .stream()
                .map(PalavraProibida::getPalavra)
                .toList();

        if (palavras == null || palavras.isEmpty()) {
            palavras = List.of();
        }else{
            salvarCache(palavras);
        }

        return palavras;
    }


    public void atualizarCache() {

        List<String> palavras = repository.findAll()
                .stream()
                .map(PalavraProibida::getPalavra)
                .toList();

        salvarCache(palavras);
    }


    private void salvarCache(List<String> palavras) {

        redisTemplate.delete(CACHE_KEY);

        redisTemplate.opsForList()
                .rightPushAll(CACHE_KEY, palavras);
    }


    public void limparCache() {
        redisTemplate.delete(CACHE_KEY);
    }
}