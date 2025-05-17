package com.edu.encurtador.repository;

import com.edu.encurtador.entities.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {
}
