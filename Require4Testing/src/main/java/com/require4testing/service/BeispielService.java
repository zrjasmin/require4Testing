package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.BeispielRepository;
import com.require4testing.model.Beispiel;

import java.util.List;

@Service
public class BeispielService {

    @Autowired
    private BeispielRepository repository;

    public List<Beispiel> alleEntities() {
        return repository.findAll();
    }

    public Beispiel speichereEntity(Beispiel entity) {
        return repository.save(entity);
    }
   


    public void neuerArtikelSpeichern() {
        // Neue Entität erstellen
    	Beispiel neuerArtikel = new Beispiel("Tisch");

        // Speichern in der Datenbank
    	repository.save(neuerArtikel);

        System.out.println("Artikel gespeichert mit ID: " + neuerArtikel.getId());
    }
}