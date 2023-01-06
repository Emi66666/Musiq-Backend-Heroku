package hr.fer.drumre.musiq.db.mongo.tracks;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("albums")
public class Album {

	private String id;
	private String name;
	private String href;
	
	@JsonProperty("album_type")
	private String albumType;
	
	@JsonProperty("release_date")
	private String releaseDate;
	
	@JsonProperty("release_date_precision")
	private String releaseDatePrecision;
	
	@JsonProperty("total_tracks")
	private String totalTracks;
	
	@Field("images")
	private List<Image> images;
	
	//@JsonProperty("available_markets")
	//private List<String> availableMarkets;
}
