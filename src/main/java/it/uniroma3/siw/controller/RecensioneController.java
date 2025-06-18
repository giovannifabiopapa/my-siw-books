package it.uniroma3.siw.controller;

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

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;

@Controller
public class RecensioneController {

	@Autowired
	private RecensioneService recensioneService;
	@Autowired
	private LibroService libroService;
	@Autowired
	private CredentialsService credentialsService;

	@GetMapping("/recensioni/libro/{id}/new")
	public String formNewRecensione(@PathVariable("id") Long libroId, Model model) {
		model.addAttribute("libro", libroService.getLibro(libroId));
		model.addAttribute("recensione", new Recensione());
		return "utente/formNewRecensione.html";
	}

	@PostMapping("/recensioni/libro/{id}")
	public String newRecensione(@PathVariable("id") Long libroId,
			@Valid @ModelAttribute("recensione") Recensione recensione, BindingResult bindingResult, Model model) {

		Libro libro = libroService.getLibro(libroId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user = credentials.getUser();

		if (bindingResult.hasErrors()) {
			model.addAttribute("libro", libro);
			return "utente/formNewRecensione.html";
		}

		if (recensioneService.alreadyReviewed(libro, user)) {
			model.addAttribute("libro", libro);
			model.addAttribute("errore", "recensione.gia.esistente");
			return "utente/formNewRecensione.html";
		}

		recensione.setId(null); // evita merge/sovrascrizione
		recensione.setLibro(libro);
		recensione.setAutore(user);
		recensioneService.saveRecensione(recensione);

		return "redirect:/libri/" + libroId;
	}

	@GetMapping("/recensioni/{id}/delete")
	public String deleteRecensione(@PathVariable("id") Long id) {
	    Recensione recensione = recensioneService.getRecensione(id);

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserDetails userDetails = (UserDetails) auth.getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    User user = credentials.getUser();

	    // Verifica se è l'autore oppure un admin
	    boolean isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);

	    if (recensione != null && (recensione.getAutore().equals(user) || isAdmin)) {
	        Long libroId = recensione.getLibro().getId();
	        recensioneService.deleteRecensione(id);
	        return "redirect:/libri/" + libroId;
	    }

	    return "redirect:/libri";
	}


	@GetMapping("/recensioni/{id}/edit")
	public String editRecensione(@PathVariable("id") Long id, Model model) {
		Recensione recensione = recensioneService.getRecensione(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user = credentials.getUser();

		// Controlla che la recensione appartenga all'utente autenticato
		if (recensione != null && recensione.getAutore().equals(user)) {
			model.addAttribute("recensione", recensione);
			return "utente/formModificaRecensione.html";
		}

		return "redirect:/libri";
	}

	@PostMapping("/recensioni/{id}/update")
	public String updateRecensione(@PathVariable("id") Long id,
			@Valid @ModelAttribute("recensione") Recensione recensione, BindingResult bindingResult, Model model) {

		Recensione originale = recensioneService.getRecensione(id);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User user = credentials.getUser();

		// Controllo autorizzazione
		if (originale == null || !originale.getAutore().equals(user)) {
			return "redirect:/libri";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("recensione", recensione);
			return "utente/formModificaRecensione.html";
		}

		// Aggiorna solo i campi modificabili
		originale.setTitolo(recensione.getTitolo());
		originale.setTesto(recensione.getTesto());
		originale.setVoto(recensione.getVoto());

		recensioneService.saveRecensione(originale);

		return "redirect:/libri/" + originale.getLibro().getId();
	}

}