package com.tinder.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DatingServiceTest {

    @Test
    void shouldCreateMatchWhenBothUsersLikeEachOther() {
        DatingService service = new DatingService();

        Profile user = new Profile(1L, "Ava", 27, "Loves coffee and hikes", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=500&q=80");
        Profile other = new Profile(2L, "Leo", 30, "Travel photographer", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=500&q=80");

        service.addProfile(user);
        service.addProfile(other);

        service.swipe(user.getId(), other.getId(), true);
        service.swipe(other.getId(), user.getId(), true);

        assertEquals(1, service.getMatches(user.getId()).size());
        assertTrue(service.getMatches(user.getId()).stream()
                .anyMatch(match -> match.getMatchedWith().equals("Leo")));
    }
}
