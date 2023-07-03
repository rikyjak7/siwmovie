package it.uniroma3.siw.controller;

import java.io.IOException;

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

import it.uniroma3.siw.authentication.AuthConfiguration;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.MovieRepository;
import it.uniroma3.siw.repository.ReviewRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.validator.ReviewValidator;
import jakarta.validation.Valid;

@Controller
public class ReviewController {
@Autowired MovieService movieService;
@Autowired ReviewService reviewService;
@Autowired CredentialsService credentialsService;
@Autowired ReviewValidator reviewValidator;
@GetMapping("/formAddReview/{id}")
public String formAddReview(@PathVariable("id") Long id ,Model model)
{
	Movie movie=this.movieService.find(id);
	model.addAttribute("movie",movie);
	Review review= new Review();
	model.addAttribute("review",review);
	return "formAddReview.html";
}
@PostMapping("/reviews/{id}")
public String addReview(@PathVariable("id") Long id,Model model, @Valid @ModelAttribute("review") Review review,BindingResult bindingResult)
{
	try
	{
		model.addAttribute("review", reviewService.newReview(review, id, bindingResult));
		return "review.html";
	}
	catch(IOException e){
		model.addAttribute("movie", movieService.find(id));
		return "formAddReview.html";
	}
}
@GetMapping("/reviews/{id}")
public String getMovie(@PathVariable("id") Long id, Model model) {
  model.addAttribute("review", this.reviewService.find(id));
  model.addAttribute("user",this.reviewService.getUser(id));
  return "review.html";
}
}	

