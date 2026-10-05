package com.orbit.date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private final List<Profile> profiles = List.of(
            new Profile("lena-m", "Lena", 27, "Ceramic artist", "Brooklyn", "2 miles away",
                    "Usually making a mess in the studio or finding the best dumplings in the city.",
                    List.of("Art", "Pottery", "Dumplings"),
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=1000&q=85", "YOUR KIND OF CREATIVE"),
            new Profile("marcus-r", "Marcus", 29, "Architect", "Brooklyn", "4 miles away",
                    "Big believer that a long walk and a tiny coffee can fix almost anything.",
                    List.of("Architecture", "Running", "Coffee"),
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=1000&q=85", "WEEKEND EXPLORER"),
            new Profile("jordan-lee", "Jordan", 26, "Chef", "Queens", "5 miles away",
                    "I will cook for you if you pick the playlist. Currently perfecting my Sunday sauce.",
                    List.of("Cooking", "Live music", "Dogs"),
                    "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=1000&q=85", "HERE FOR THE GOOD STUFF"),
            new Profile("noah-p", "Noah", 30, "Music producer", "Manhattan", "3 miles away",
                    "Collecting records, making playlists for people I like, and missing my train stops.",
                    List.of("Music", "Film", "Night walks"),
                    "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=1000&q=85", "SENDS YOU THE AUX"),
            new Profile("maya-s", "Maya", 28, "Marine biologist", "Brooklyn", "6 miles away",
                    "Most at home near the ocean. Will absolutely stop to say hi to every dog.",
                    List.of("Ocean", "Hiking", "Dogs"),
                    "https://images.unsplash.com/photo-1529139574466-a303027c1d8b?auto=format&fit=crop&w=1000&q=85", "OUTSIDE WHENEVER POSSIBLE")
    );

    @GetMapping("/profiles")
    public List<Profile> profiles() {
        return profiles;
    }

    @PostMapping("/swipes")
    public SwipeResult swipe(@RequestBody SwipeRequest request) {
        Profile profile = profiles.stream()
                .filter(candidate -> candidate.id().equals(request.profileId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        boolean matched = request.liked() && profile.id().equals("jordan-lee");
        return new SwipeResult(profile.id(), request.liked(), matched,
                matched ? "It's a match with " + profile.name() + "!" : "Swipe saved");
    }

    public record Profile(String id, String name, int age, String job, String city, String distance,
                          String bio, List<String> interests, String imageUrl, String badge) {}

    public record SwipeRequest(String profileId, boolean liked) {}

    public record SwipeResult(String profileId, boolean liked, boolean matched, String message) {}
}