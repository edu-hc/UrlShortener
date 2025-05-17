package com.edu.encurtador.controller;

import com.edu.encurtador.controller.dto.ShortenUrlRequest;
import com.edu.encurtador.controller.dto.ShortenUrlResponse;
import com.edu.encurtador.entities.Url;
import com.edu.encurtador.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class UrlController {

    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping(value = "/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest urlRequest,
                                                         HttpServletRequest httpServletRequest) {

        String id = "";

        do {
            id = RandomStringUtils.randomAlphanumeric(5,10);

        } while (urlRepository.existsById(id));

        urlRepository.save(new Url(id, urlRequest.url(), LocalDateTime.now().plusMinutes(1)));

        var redirectUrl = httpServletRequest.getRequestURL().toString().replace("shorten", id);

        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Url> redirect(@PathVariable String id, HttpServletRequest httpServletRequest) {

        var url = urlRepository.findById(id);

        if (url.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url.get().getFullUrl()));

        url.get().registerAccess(httpServletRequest.getRemoteAddr());

        urlRepository.save(url.get());

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
