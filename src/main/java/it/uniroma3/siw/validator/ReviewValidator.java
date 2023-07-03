package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ArtistRepository;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.CredentialsService;


@Component
public class ReviewValidator implements Validator {
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private CredentialsService credentialsService;
  @Override
  public void validate(Object o, Errors errors) {
    Review review=(Review)o;
    if (reviewRepository.existsByMovieAndUser(review.getMovie(), review.getUser())) {
      errors.reject("review.duplicate");
    }
  }
  
  @Override
    public boolean supports(Class<?> aClass) {
      return Movie.class.equals(aClass);
    }
}
