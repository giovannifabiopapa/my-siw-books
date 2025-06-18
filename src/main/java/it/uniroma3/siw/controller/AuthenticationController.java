package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.uniroma3.siw.model.Coach;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CoachService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MatchService;
import it.uniroma3.siw.service.PlayerService;
import it.uniroma3.siw.service.TeamService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private LibroService bookService;
	
	@Autowired
	private AutoreService autoreService;
	
	@Autowired
	private RecensioneService recensioneService;
	

	@GetMapping(value = "/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser.html";
	}

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "formLogin.html";
	}

	@GetMapping(value = "/")
	public String index(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    // Se utente non loggato
	    if (authentication instanceof AnonymousAuthenticationToken) {
	        model.addAttribute("teams", this.teamService.getAllTeams());
	        model.addAttribute("matchesFuturi", matchService.findFuturi());
	        model.addAttribute("matchesConclusi", matchService.findConclusi());
	        return "index.html";
	    }

	    // Se loggato
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

	    if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	        return "admin/indexAdmin.html";
	    }

	    model.addAttribute("teams", this.teamService.getAllTeams());
	    model.addAttribute("matchesFuturi", matchService.findFuturi());
	    model.addAttribute("matchesConclusi", matchService.findConclusi());
	    return "index.html";
	}


	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "admin/indexAdmin.html";
		}
		model.addAttribute("teams", this.teamService.getAllTeams());
        model.addAttribute("matchesFuturi", matchService.findFuturi());
        model.addAttribute("matchesConclusi", matchService.findConclusi());
		return "index.html";
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the
		// Credentials nel DB
		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "registrationSuccessful.html";
		}
		return "registerUser";
	}
	
	@GetMapping("/cerca")
	public String cerca(@RequestParam("query") String query, Model model) {
		List<Player> giocatori = playerService.cercaPerNome(query);
		List<Coach> coach = coachService.cercaPerNome(query);
		model.addAttribute("giocatori", giocatori);
		model.addAttribute("coach", coach);
		
		// Reimposta anche i dati per la homepage
		model.addAttribute("teams", teamService.findAll());
		model.addAttribute("matchesFuturi", matchService.findFuturi());
		model.addAttribute("matchesConclusi", matchService.findConclusi());
		
		return "index";
	}

	
	@GetMapping("/autocomplete")
	@ResponseBody
	public List<Map<String, Object>> autocomplete(@RequestParam("query") String query) {
		List<Map<String, Object>> results = new ArrayList<>();

		List<Player> giocatori = playerService.cercaPerNome(query);
		for (Player g : giocatori) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", g.getId());
			map.put("nome", g.getNome());
			map.put("tipo", "player");
			results.add(map);
		}

		List<Coach> coachList = coachService.cercaPerNome(query);
		for (Coach c : coachList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", c.getId());
			map.put("nome", c.getNome());
			map.put("tipo", "coach");
			results.add(map);
		}

		return results;
	}

	
	
	
}