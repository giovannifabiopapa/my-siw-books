package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;
    @Autowired
    private AutoreService autoreService;
    @Autowired
    private CredentialsService credentialsService;


    @GetMapping("/libri")
    public String listLibri(Model model) {
        model.addAttribute("libri", libroService.getAllLibri());
        return "libri.html";
    }

    @GetMapping("/libri/{id}")
    public String getLibro(@PathVariable("id") Long id, Model model) {
        Libro libro = libroService.getLibro(id);

        // Forza il caricamento delle recensioni se sono LAZY
        libro.getRecensioni().size();

        model.addAttribute("libro", libro);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("userDetails", userDetails);
            
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("user", credentials.getUser());
        }

        return "libro.html";
    }


    @GetMapping("/admin/libri/new")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autori", autoreService.getAllAutori());
        return "admin/formNewLibro.html";
    }

    @PostMapping("/admin/libri")
    public String newLibro(@Valid @ModelAttribute("libro") Libro libro,
                           BindingResult bindingResult,
                           @RequestParam("fileImages") MultipartFile[] images,
                           Model model) {
        if (!bindingResult.hasErrors() && !libroService.alreadyExists(libro)) {
            List<String> immaginiPaths = new ArrayList<>();

            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        String path = salvaFile(image);
                        immaginiPaths.add(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            libro.setImmagini(immaginiPaths);
            libroService.saveLibro(libro);
            return "redirect:/libri";
        }

        model.addAttribute("autori", autoreService.getAllAutori());
        return "admin/formNewLibro.html";
    }


    @GetMapping("/admin/libri/{id}/delete")
    public String deleteLibro(@PathVariable("id") Long id) {
        libroService.deleteLibro(id);
        return "redirect:/libri";
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
    @GetMapping("/admin/libri/{id}/edit")
    public String formModificaLibro(@PathVariable("id") Long id, Model model) {
        Libro libro = libroService.getLibro(id);
        model.addAttribute("libro", libro);
        model.addAttribute("autori", autoreService.getAllAutori());
        return "admin/formModificaLibro.html";
    }

    @PostMapping("/admin/libri/{id}/edit")
    public String modificaLibro(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("libro") Libro libro,
                                BindingResult bindingResult,
                                @RequestParam("fileImages") MultipartFile[] images,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("autori", autoreService.getAllAutori());
            return "admin/formModificaLibro.html";
        }

        Libro libroEsistente = libroService.getLibro(id);
        libroEsistente.setTitolo(libro.getTitolo());
        libroEsistente.setAnnoPubblicazione(libro.getAnnoPubblicazione());
        libroEsistente.setAutori(libro.getAutori());

        if (images != null && images.length > 0 && !images[0].isEmpty()) {
            List<String> nuoviPercorsi = new ArrayList<>();
            for (MultipartFile image : images) {
                try {
                    String path = salvaFile(image);
                    nuoviPercorsi.add(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            libroEsistente.setImmagini(nuoviPercorsi);
        }

        libroService.saveLibro(libroEsistente);
        return "redirect:/libri";
    }


}