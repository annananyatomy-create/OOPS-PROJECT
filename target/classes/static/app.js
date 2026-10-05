const profileCard = document.querySelector("#profile-card");
const profilePhoto = document.querySelector("#profile-photo");
const profileName = document.querySelector("#profile-name");
const profileSubtitle = document.querySelector("#profile-subtitle");
const profileDistance = document.querySelector("#profile-distance");
const profileBio = document.querySelector("#profile-bio");
const profileBadge = document.querySelector("#profile-badge");
const interestList = document.querySelector("#interest-list");
const swipeStamp = document.querySelector("#swipe-stamp");
const toast = document.querySelector("#toast");
const matchDialog = document.querySelector("#match-dialog");
const profiles = [];
let profileIndex = 0;
let likeCount = 0;
let lastSwipe = null;
let toastTimer;

async function loadProfiles() {
    try {
        const response = await fetch("/api/profiles");
        if (!response.ok) throw new Error("Could not load profiles");
        profiles.push(...await response.json());
        renderProfile();
    } catch {
        profileName.textContent = "A little quiet here";
        profileSubtitle.textContent = "We couldn't load profiles just now.";
        document.querySelector("#profile-bio").textContent = "Refresh the page to try again.";
        document.querySelector("#pass-button").disabled = true;
        document.querySelector("#like-button").disabled = true;
    }
}

function renderProfile() {
    const profile = profiles[profileIndex];
    if (!profile) {
        profileName.textContent = "That's everyone for now";
        profileSubtitle.textContent = "Your next hello is just around the corner.";
        profilePhoto.style.backgroundImage = "linear-gradient(145deg, #8da896, #455f50)";
        profileBadge.textContent = "YOU'RE ALL CAUGHT UP";
        profileDistance.textContent = "Check back soon";
        profileBio.textContent = "You've seen all the profiles in this little demo. Refresh to start over.";
        interestList.replaceChildren();
        document.querySelector("#pass-button").disabled = true;
        document.querySelector("#like-button").disabled = true;
        return;
    }

    document.querySelector("#pass-button").disabled = false;
    document.querySelector("#like-button").disabled = false;
    profilePhoto.style.backgroundImage = `url("${profile.imageUrl}")`;
    profileName.textContent = `${profile.name}, ${profile.age}`;
    profileSubtitle.textContent = `${profile.job} · ${profile.city}`;
    profileDistance.textContent = profile.distance;
    profileBio.textContent = profile.bio;
    profileBadge.textContent = profile.badge;
    interestList.replaceChildren(...profile.interests.map((interest) => {
        const chip = document.createElement("span");
        chip.className = "interest";
        chip.textContent = interest;
        return chip;
    }));
    profileCard.style.animation = "none";
    void profileCard.offsetWidth;
    profileCard.style.animation = "";
    swipeStamp.className = "swipe-stamp";
}

function showToast(message) {
    toast.textContent = message;
    toast.classList.add("visible");
    window.clearTimeout(toastTimer);
    toastTimer = window.setTimeout(() => toast.classList.remove("visible"), 2200);
}

async function swipe(liked) {
    const profile = profiles[profileIndex];
    if (!profile) return;

    const passButton = document.querySelector("#pass-button");
    const likeButton = document.querySelector("#like-button");
    passButton.disabled = true;
    likeButton.disabled = true;
    swipeStamp.textContent = liked ? "LIKE" : "PASS";
    swipeStamp.classList.add(liked ? "like" : "pass");

    try {
        const response = await fetch("/api/swipes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ profileId: profile.id, liked })
        });
        if (!response.ok) throw new Error("Swipe failed");
        const result = await response.json();
        lastSwipe = { profileIndex, liked };
        if (liked) {
            likeCount += 1;
            document.querySelector("#like-count").textContent = likeCount;
        }
        profileIndex += 1;
        renderProfile();
        if (result.matched) {
            document.querySelector("#match-message").textContent = result.message;
            matchDialog.showModal();
        } else {
            showToast(liked ? "Like sent. Good things take a little time." : "On to the next hello.");
        }
    } catch {
        swipeStamp.className = "swipe-stamp";
        passButton.disabled = false;
        likeButton.disabled = false;
        showToast("That didn't go through. Try again.");
    }
}

document.querySelector("#pass-button").addEventListener("click", () => swipe(false));
document.querySelector("#like-button").addEventListener("click", () => swipe(true));
document.querySelector("#rewind-button").addEventListener("click", () => {
    if (!lastSwipe) return showToast("Nothing to rewind yet.");
    profileIndex = lastSwipe.profileIndex;
    if (lastSwipe.liked) {
        likeCount = Math.max(0, likeCount - 1);
        document.querySelector("#like-count").textContent = likeCount;
    }
    lastSwipe = null;
    renderProfile();
    showToast("One swipe, reconsidered.");
});
document.querySelector("#dialog-close").addEventListener("click", () => matchDialog.close());
document.querySelector("#message-button").addEventListener("click", () => matchDialog.close());
document.querySelector("#filter-button").addEventListener("click", () => showToast("Preferences are coming soon."));
document.querySelector("#likes-link").addEventListener("click", (event) => {
    event.preventDefault();
    showToast(`${likeCount} ${likeCount === 1 ? "like" : "likes"} sent this session.`);
});
document.querySelector("#chats-link").addEventListener("click", (event) => {
    event.preventDefault();
    showToast("Your conversations are just over on the right.");
});
document.querySelector("#rewind-button").addEventListener("keydown", (event) => event.stopPropagation());
document.addEventListener("keydown", (event) => {
    if (event.target instanceof HTMLInputElement || event.target instanceof HTMLTextAreaElement || matchDialog.open) return;
    if (event.key === "ArrowLeft") swipe(false);
    if (event.key === "ArrowRight") swipe(true);
});

loadProfiles();