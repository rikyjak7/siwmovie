package it.uniroma3.siw.service;

import java.io.IOException;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.validator.ReviewValidator;

@Service
public class ReviewService {

	@Autowired
	MovieRepository movieRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	CredentialsRepository credentialsRepository;
	@Autowired
	ReviewValidator reviewValidator;

	public void save(Review newReview) {
		this.reviewRepository.save(newReview);
	}

	public Review find(Long id) {
		return this.reviewRepository.findById(id).get();
	}

	public User getUser(Long id) {
		return this.reviewRepository.findById(id).get().getUser();
	}

	public Review newReview(Review review, Long idMovie, BindingResult bindingResult) throws IOException {
		Review newReview = new Review(review.getTitolo(), review.getValutazione(), review.getTesto());
		Movie movie = this.movieRepository.findById(idMovie).get();
		newReview.setMovie(movie);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsRepository.findByUsername(userDetails.getUsername()).get();
		User user = credentials.getUser();
		newReview.setUser(user);
		this.reviewValidator.validate(newReview, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.movieRepository.save(movie);
			this.reviewRepository.save(newReview);
			movie.getReviews().add(newReview);
			return newReview;
		} else {
			throw new IOException();
		}
	}
}
