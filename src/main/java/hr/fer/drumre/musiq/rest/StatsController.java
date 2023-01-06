package hr.fer.drumre.musiq.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.drumre.musiq.db.mongo.tracks.TrackService;
import hr.fer.drumre.musiq.db.mongo.users.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@CrossOrigin
public class StatsController {

	@Autowired
	TrackService trackService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/stats")
	public List<Stat> getStats() {
		List<Stat> stats = new ArrayList<>();
		stats.add(new Stat("tracks", trackService.totalTracks()));
		stats.add(new Stat("users", userService.totalUsers()));
		return stats;
	}
	
	@Data
	@AllArgsConstructor
	public class Stat {
		public String name;
		public long num;
	}
}
