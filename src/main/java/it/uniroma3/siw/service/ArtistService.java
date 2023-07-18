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
import it.uniroma3.siw.validator.ArtistValidator;
import jakarta.validation.Valid;

@Service
public class ArtistService {

@Autowired ArtistRepository artistRepository;
@Autowired MovieRepository movieRepository;
@Autowired ArtistValidator artistValidator;

	public Object getArtist(Long id) {
		return this.artistRepository.findById(id).get();
	}

	public boolean exists(Artist artist) {
	 return	this.artistRepository.existsByNameAndDateOfBirth(artist.getName(), artist.getDateOfBirth());
	}

	public void save(Artist artist) {
		this.artistRepository.save(artist);
	}

	public List<Artist> findAll() {
		return (List<Artist>) this.artistRepository.findAll();
	}

	public Object findNotActors(Long id) {
		Movie movie= this.movieRepository.findById(id).get();
		return this.artistRepository.getArtistByMoviesActNotContains(movie);
	}

	public Object newArtist(@Valid Artist artist, BindingResult bindingResult, MultipartFile image) throws IOException {
		this.artistValidator.validate(artist, bindingResult);
	    if (!bindingResult.hasErrors()) 
	    {
	      try {
	    	String base64Image=  Base64.getEncoder().encodeToString(image.getBytes());
			artist.setImage(base64Image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      this.artistRepository.save(artist);
	      return artist;
	    }
	    else {	
	    	throw new IOException();
	    }
	}

    public Object updateArtist(@Valid Artist artist, BindingResult bindingResult, MultipartFile image, Long id) throws IOException {
        Artist oldArtist = this.artistRepository.findById(id).get();
		this.artistValidator.validate(artist, bindingResult);
	    if (!bindingResult.hasFieldErrors()) 
	    {
		  oldArtist.setDateOfBirth(artist.getDateOfBirth());
		  oldArtist.setDeathDate(artist.getDeathDate());
		  oldArtist.setName(artist.getName());
		  oldArtist.setSurname(artist.getSurname());
	      try {
	    	String base64Image=  Base64.getEncoder().encodeToString(image.getBytes());
			oldArtist.setImage(base64Image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      this.artistRepository.save(oldArtist);
	      return oldArtist;
	    }
	    else {	
			this.artistRepository.save(oldArtist);
	    	throw new IOException();
	    }
    }

}
