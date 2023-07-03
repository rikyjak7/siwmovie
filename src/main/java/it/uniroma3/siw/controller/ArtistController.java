package it.uniroma3.siw.controller;

import java.io.IOException;

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

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;

@Controller
public class ArtistController {
	@Autowired
	ArtistService artistService;
	@Autowired
	MovieService movieService;

	@GetMapping("/artists/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.getArtist(id));
		return "artist.html";
	}

	@PostMapping("/artists")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model,
			MultipartFile imageFile) {
		try {
			model.addAttribute("artist", this.artistService.newArtist(artist, bindingResult, imageFile));
			return "artist.html";
		} catch (IOException e) {
			return "admin/formNewArtist.html";
		}
	}

	@GetMapping("admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist.html";
	}

	@GetMapping("/artists")

	public String artists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "artists.html";
	}

	@GetMapping("/admin/artists")

	public String adminArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "admin/artists.html";
	}

	@GetMapping("/formModifyDirector/{id}")
	public String formNewMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.find(id));
		return "formModifyDirector.html";
	}

	@GetMapping("/admin/indexArtist")
	public String indexArtist(Model model) {
		return "admin/indexArtist.html";
	}

	@GetMapping("/admin/updateArtist/{id}")
	public String updateArtist(Model model, @PathVariable("id") Long id) {
		model.addAttribute("artist", this.artistService.getArtist(id));
		return "admin/updateArtist.html";
	}

	@PostMapping("/updateArtist/{id}")
	public String updateArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model,
			MultipartFile imageFile, @PathVariable("id") Long id) {
		try {
			model.addAttribute("artist", this.artistService.updateArtist(artist, bindingResult, imageFile, id));
			return "artist.html";
		} catch (IOException e) {
			return "admin/formNewArtist.html";
		}
	}
}