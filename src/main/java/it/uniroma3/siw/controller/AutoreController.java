package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.service.AutoreService;
import jakarta.validation.Valid;

@Controller
public class AutoreController {

    @Autowired
    private AutoreService autoreService;

    @GetMapping("/autori")
    public String listAutori(Model model) {
        model.addAttribute("autori", autoreService.getAllAutori());
        return "autori.html";
    }

    @GetMapping("/autori/{id}")
    public String getAutore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("autore", autoreService.getAutore(id));
        return "autore.html";
    }

    @GetMapping("/admin/autori/new")
    public String formNewAutore(Model model) {
        model.addAttribute("autore", new Autore());
        return "admin/formNewAutore.html";
    }

    @PostMapping("/admin/autori")
    public String newAutore(@Valid @ModelAttribute("autore") Autore autore,
                            BindingResult bindingResult,
                            @RequestParam("fileImage") MultipartFile image,
                            Model model) {
        if (!bindingResult.hasErrors() && !autoreService.alreadyExists(autore)) {
            if (!image.isEmpty()) {
                try {
                    String path = salvaFile(image);
                    autore.setFotografia(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            autoreService.saveAutore(autore);
            return "redirect:/autori";
        }

        return "admin/formNewAutore.html";
    }

    @GetMapping("/admin/autori/{id}/delete")
    public String deleteAutore(@PathVariable("id") Long id) {
        autoreService.deleteAutore(id);
        return "redirect:/autori";
    }
    
    private String salvaFile(MultipartFile file) throws IOException {
        // Crea un nome univoco per l'immagine
        String nomeFile = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Percorso reale nel filesystem (non src/, ma cartella uploads nella root del progetto)
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir); // Crea la cartella se non esiste

        // Percorso completo del file da salvare
        Path pathDestinazione = uploadDir.resolve(nomeFile);
        // Copia sicura e cross-platform
        Files.copy(file.getInputStream(), pathDestinazione, StandardCopyOption.REPLACE_EXISTING);
        // Path relativo da salvare nel database
        return "/uploads/" + nomeFile;
    }

    @GetMapping("/admin/autori/{id}/edit")
    public String formModificaAutore(@PathVariable("id") Long id, Model model) {
        Autore autore = autoreService.getAutore(id);
        if (autore == null)
            return "redirect:/autori"; // fallback

        model.addAttribute("autore", autore);
        return "admin/formModificaAutore.html";
    }

    @PostMapping("/admin/autori/{id}/edit")
    public String modificaAutore(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("autore") Autore autore,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/formModificaAutore.html";
        }

        Autore esistente = autoreService.getAutore(id);
        if (esistente != null) {
            esistente.setNome(autore.getNome());
            esistente.setCognome(autore.getCognome());
            esistente.setNazionalita(autore.getNazionalita());
            esistente.setDataDiNascita(autore.getDataDiNascita());
            esistente.setDataDiMorte(autore.getDataDiMorte());
            autoreService.saveAutore(esistente);
        }

        return "redirect:/autori";
    }



}