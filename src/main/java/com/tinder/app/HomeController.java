package com.tinder.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private final DatingService datingService = new DatingService();

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentUserId", 1L);
        model.addAttribute("profiles", datingService.getProfilesForUser(1L));
        return "index";
    }

    @GetMapping("/api/profiles")
    @ResponseBody
    public List<Profile> getProfiles(@RequestParam Long userId) {
        return datingService.getProfilesForUser(userId);
    }

    @PostMapping("/api/swipe")
    @ResponseBody
    public Map<String, Object> swipe(@RequestParam Long userId,
                                    @RequestParam Long targetId,
                                    @RequestParam boolean liked) {
        DatingService.SwipeResult result = datingService.swipe(userId, targetId, liked);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", result.isLiked());
        response.put("matched", result.isMatched());
        response.put("profile", result.getProfile());
        response.put("message", result.isMatched() ? "It’s a match!" : "Profile passed.");
        return response;
    }

    @GetMapping("/api/matches")
    @ResponseBody
    public List<Match> getMatches(@RequestParam Long userId) {
        return datingService.getMatches(userId);
    }
}
