package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String newAutore(@Valid @ModelAttribute("autore") Autore autore, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors() && !autoreService.alreadyExists(autore)) {
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
}