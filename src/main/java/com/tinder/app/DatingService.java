package com.tinder.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DatingService {
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Map<Long, Set<Long>> likedBy = new HashMap<>();
    private final Map<Long, Set<Long>> matches = new HashMap<>();

    public DatingService() {
        seedProfiles();
    }

    public void addProfile(Profile profile) {
        profiles.put(profile.getId(), profile);
    }

    public List<Profile> getProfiles() {
        return new ArrayList<>(profiles.values());
    }

    public List<Profile> getProfilesForUser(Long userId) {
        return profiles.values().stream()
                .filter(profile -> !profile.getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Profile getProfile(Long id) {
        return profiles.get(id);
    }

    public SwipeResult swipe(Long userId, Long targetId, boolean liked) {
        if (!profiles.containsKey(userId) || !profiles.containsKey(targetId)) {
            throw new IllegalArgumentException("User or target profile not found.");
        }

        likedBy.computeIfAbsent(userId, key -> new HashSet<>())
                .add(targetId);

        boolean matched = false;
        if (liked) {
            Set<Long> targetLikes = likedBy.getOrDefault(targetId, new HashSet<>());
            if (targetLikes.contains(userId)) {
                matches.computeIfAbsent(userId, key -> new HashSet<>()).add(targetId);
                matches.computeIfAbsent(targetId, key -> new HashSet<>()).add(userId);
                matched = true;
            }
        }

        return new SwipeResult(liked, matched, profiles.get(targetId));
    }

    public List<Match> getMatches(Long userId) {
        Set<Long> matchedIds = matches.getOrDefault(userId, new HashSet<>());
        return matchedIds.stream()
                .map(id -> new Match(id, profiles.get(id).getName()))
                .collect(Collectors.toList());
    }

    private void seedProfiles() {
        addProfile(new Profile(1L, "You", 27, "Coffee, sunsets, and spontaneous weekend plans.", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=500&q=80"));
        addProfile(new Profile(2L, "Ava", 25, "Hiker, baker, and dog lover.", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=500&q=80"));
        addProfile(new Profile(3L, "Noah", 28, "Sunrise runs and travel stories.", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=500&q=80"));
        addProfile(new Profile(4L, "Mila", 24, "Art museum date nights.", "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=500&q=80"));
        addProfile(new Profile(5L, "Ethan", 29, "Trying every new food spot in town.", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=500&q=80"));
    }

    public static class SwipeResult {
        private final boolean liked;
        private final boolean matched;
        private final Profile profile;

        public SwipeResult(boolean liked, boolean matched, Profile profile) {
            this.liked = liked;
            this.matched = matched;
            this.profile = profile;
        }

        public boolean isLiked() {
            return liked;
        }

        public boolean isMatched() {
            return matched;
        }

        public Profile getProfile() {
            return profile;
        }
    }
}
