package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Base64;

import org.hibernate.dialect.LobMergeStrategy;
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

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.validator.MovieValidator;
import jakarta.validation.Valid;

@Controller
public class MovieController {

	@Autowired
	MovieService movieService;
	@Autowired
	ArtistService artistService;
	@Autowired
	MovieValidator movieValidator;

	@GetMapping("/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}

	@PostMapping("/movies")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult,
			MultipartFile imageFile, Model model) {
		try {
			model.addAttribute("movie", this.movieService.newMovie(movie, bindingResult, imageFile));
			return "movie.html";
		} catch (IOException e) {
			return "admin/formNewMovie.html";
		}
	}

	@PostMapping("/admin/updateMovieData/{id}")
	public String updateMovieData(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult,
			MultipartFile imageFile, Model model, @PathVariable("id") Long id) {
		try {
			model.addAttribute("movie", this.movieService.updateMovie(movie,bindingResult,imageFile,id));
			return "movie.html";
		} catch (IOException e) {
			return "admin/formNewMovie.html";
		}
	}

	@GetMapping("/movies/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.find(id));
		model.addAttribute("reviews", this.movieService.getReviews(id));
		return "movie.html";
	}

	@GetMapping("/movies")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieService.findall());
		return "movies.html";
	}

	@GetMapping("/admin/updateMovie/{id}")
	public String upedateMovie(Model model, @PathVariable("id") Long id) {
		model.addAttribute("movie", this.movieService.find(id));
		return "admin/updateMovie.html";
	}

	@GetMapping("/admin/addDirector/{id}")
	public String addDirector(Model model, @PathVariable("id") Long id) {
		model.addAttribute("artists", this.artistService.findAll());
		model.addAttribute("movie", this.movieService.find(id));
		return "admin/addDirector.html";
	}

	@GetMapping("/admin/setDirector/{idMovie}/{idDirector}")
	public String setDirector(Model model, @PathVariable("idMovie") Long idMovie,
			@PathVariable("idDirector") Long idDirector) {
		model.addAttribute("movie", this.movieService.setDirector(idMovie, idDirector));
		return "admin/updateMovie.html";
	}

	@GetMapping("/admin/addActor/{id}")
	public String addActorToMovie(Model model, @PathVariable("id") Long id) {
		model.addAttribute("artists", this.artistService.findNotActors(id));
		model.addAttribute("movie", this.movieService.find(id));
		return "admin/addActor.html";
	}

	@GetMapping("/searchMovies")
	public String searchMovie(Model model) {
		model.addAttribute("movies", this.movieService.findall());
		return "searchMovies.html";
	}

	@PostMapping("/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer year) {
		model.addAttribute("movies", this.movieService.find(year));
		return "moviesForYear.html";
	}

	@GetMapping("/admin/indexMovie")
	public String indexMovie(Model model) {
		return "admin/indexMovie.html";
	}

	@GetMapping("/admin/movies")
	public String adminMovies(Model model) {
		model.addAttribute("movies", this.movieService.findall());
		return ("admin/movies.html");
	}

	@GetMapping("admin/removeActor/{idMovie}/{idArtist}")
	public String removeActor(Model model, @PathVariable("idMovie") Long idMovie,
			@PathVariable("idArtist") Long idActor) {
		this.movieService.removeActor(idMovie, idActor);
		model.addAttribute("movie", movieService.find(idMovie));
		model.addAttribute("artists", artistService.findNotActors(idMovie));
		return ("admin/addActor.html");
	}

	@GetMapping("/admin/addActor/{idMovie}/{idActor}")
	public String addActor(Model model, @PathVariable("idMovie") Long idMovie, @PathVariable("idActor") Long idActor) {
		Movie movie = movieService.find(idMovie);
		this.movieService.addActor(movie, idActor);
		model.addAttribute("movie", movie);
		model.addAttribute("artists", artistService.findNotActors(idMovie));
		return "admin/addActor.html";
	}

	@GetMapping("/admin/updateMovieData/{id}")
	public String updateMovieData(Model model, @PathVariable("id") Long id) {
		model.addAttribute("movie", this.movieService.find(id));
		return "admin/updateMovieData.html";
	}
}