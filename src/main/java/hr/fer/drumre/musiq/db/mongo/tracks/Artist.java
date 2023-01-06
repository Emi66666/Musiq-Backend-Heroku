package hr.fer.drumre.musiq.db.mongo.tracks;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("artists")
public class Artist {

	private String id;
	private String name;
	private String href;
		
	private int popularity;
	
	private String uri;
	
}
