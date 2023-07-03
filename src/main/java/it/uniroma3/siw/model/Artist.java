package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Artist{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private Long id;
	
	private String name;
	private String surname;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate DeathDate;
	@Column(length = 100000000)//max 10Mb
	@Size(min=0,max=10000000)
	private String image;
	
	@ManyToMany(mappedBy="actors")
	private List<Movie> moviesAct;
	@OneToMany(mappedBy="director")
	private List<Movie> moviesDir;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public LocalDate getDeathDate() {
		return DeathDate;
	}
	public void setDeathDate(LocalDate deathDate) {
		DeathDate = deathDate;
	}
	public List<Movie> getMoviesAct() {
		return moviesAct;
	}
	public void setMoviesAct(List<Movie> moviesAct) {
		this.moviesAct = moviesAct;
	}
	public List<Movie> getMoviesDir() {
		return moviesDir;
	}
	public void setMoviesDir(List<Movie> moviesDir) {
		this.moviesDir = moviesDir;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist other = (Artist) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
}