package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.validator.MovieValidator;
import it.uniroma3.siw.model.Review;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class MovieService {
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	ArtistRepository artistRepository;
	@Autowired
	MovieValidator movieValidator;

	@Transactional
	public Movie find(Long id) {
		return this.movieRepository.findById(id).get();
	}

	@Transactional
	public void save(@Valid Movie movie) {
		this.movieRepository.save(movie);
	}

	@Transactional
	public List<Review> getReviews(Long id) {
		return this.movieRepository.findById(id).get().getReviews();
	}

	@Transactional
	public List<Movie> findall() {
		return (List<Movie>) this.movieRepository.findAll();
	}

	@Transactional
	public Movie setDirector(Long idMovie, Long idDirector) {
		Movie movie = movieRepository.findById(idMovie).get();
		movie.setDirector(artistRepository.findById(idDirector).get());
		return this.movieRepository.save(movie);
	}

	@Transactional
	public void addActor(Movie movie, Long idActor) {
		if (!movie.hasActor(idActor)) {
			movie.getActors().add(artistRepository.findById(idActor).get());
			this.movieRepository.save(movie);
		}
	}

	@Transactional
	public List<Movie> find(Integer year) {
		return this.movieRepository.findByYear(year);
	}

	@Transactional
	public Movie newMovie(Movie movie, BindingResult bindingResult, MultipartFile image) throws IOException {

		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				movie.setImage(base64Image);
			} catch (IOException e) {
			}
			this.movieRepository.save(movie);
			return movie;
		} else {
			throw new IOException();
		}
	}
	@Transactional
	public Movie updateMovie(Movie movie, BindingResult bindingResult, MultipartFile image, Long id) throws IOException {
		movieValidator.validate(movie, bindingResult);
		Movie oldMovie=this.movieRepository.findById(id).get();
		if (!bindingResult.hasFieldErrors()) {
			oldMovie.setTitle(movie.getTitle());
			oldMovie.setYear(movie.getYear());
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				oldMovie.setImage(base64Image);
			} catch (IOException e) {
			}
			this.movieRepository.save(oldMovie);
			return oldMovie;
		} else {
			this.movieRepository.save(oldMovie);
			throw new IOException();
		}
	}
	@Transactional
	public void removeActor(Long idMovie, Long idActor) {
		Movie movie = this.movieRepository.findById(idMovie).get();
		Artist actor = this.artistRepository.findById(idActor).get();
		movie.getActors().remove(actor);
	}

}
