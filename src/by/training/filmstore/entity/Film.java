package by.training.filmstore.entity;

import java.util.List;
import java.io.Serializable;
import java.math.BigDecimal;

public class Film  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2420435173904536159L;
	private short id;
	private String name;
	private String genre;
	private String country;
	private short yearOfRelease;
	private Quality quality;
	private short filmDirector;
	private String description;
	private BigDecimal price;
	private short countFilms;
	private byte[] image;
	private List<Actor> actors;
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public short getYearOfRelease() {
		return yearOfRelease;
	}
	public void setYearOfRelease(short yearOfRelease) {
		this.yearOfRelease = yearOfRelease;
	}
	public Quality getQuality() {
		return quality;
	}
	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	public short getFilmDirector() {
		return filmDirector;
	}
	public void setFilmDirector(short filmDirector) {
		this.filmDirector = filmDirector;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public short getCountFilms() {
		return countFilms;
	}
	public void setCountFilms(short countFilms) {
		this.countFilms = countFilms;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public List<Actor> getActors() {
		return actors;
	}
	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}
	
	public Film() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Film(short id, String name, String genre, String country, short yearOfRelease, Quality quality,
			short filmDirector, String description, BigDecimal price, short countFilms, byte[] image) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.country = country;
		this.yearOfRelease = yearOfRelease;
		this.quality = quality;
		this.filmDirector = filmDirector;
		this.description = description;
		this.price = price;
		this.countFilms = countFilms;
		this.image = image;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + countFilms;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + filmDirector;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((quality == null) ? 0 : quality.hashCode());
		result = prime * result + yearOfRelease;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		if (countFilms != other.countFilms)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (filmDirector != other.filmDirector)
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quality != other.quality)
			return false;
		if (yearOfRelease != other.yearOfRelease)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Film [id=" + id + ", name=" + name + ", genre=" + genre + ", country=" + country + ", yearOfRelease="
				+ yearOfRelease + ", quality=" + quality + ", filmDirector=" + filmDirector + ", description="
				+ description + ", price=" + price + ", countFilms=" + countFilms + ", actors=" + actors + "]";
	}
	
	
}
