package hr.fer.drumre.musiq.db.mongo.tracks;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Image {

	private int height;
	private int width;
	private String url;
	
}
