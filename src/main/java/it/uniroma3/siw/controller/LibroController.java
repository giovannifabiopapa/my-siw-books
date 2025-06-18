package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;
    @Autowired
    private AutoreService autoreService;

    @GetMapping("/libri")
    public String listLibri(Model model) {
        model.addAttribute("libri", libroService.getAllLibri());
        return "libri.html";
    }

    @GetMapping("/libri/{id}")
    public String getLibro(@PathVariable("id") Long id, Model model) {
        model.addAttribute("libro", libroService.getLibro(id));
        return "libro.html";
    }

    @GetMapping("/admin/libri/new")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autori", autoreService.getAllAutori());
        return "admin/formNewLibro.html";
    }

    @PostMapping("/admin/libri")
    public String newLibro(@Valid @ModelAttribute("libro") Libro libro, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors() && !libroService.alreadyExists(libro)) {
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
}