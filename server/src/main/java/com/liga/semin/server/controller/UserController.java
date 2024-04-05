package com.liga.semin.server.controller;

import com.liga.semin.server.message.GetProfilesResponse;
import com.liga.semin.server.message.GetUserProfileResponse;
import com.liga.semin.server.message.PostFavoriteResponse;
import com.liga.semin.server.message.UserDto;
import com.liga.semin.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        logger.info("createUser with id {} and name {}", user.getId(), user.getUsername());
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        logger.info("getUser by user {}", id);
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping(value = "/profile/{id}")
    private ResponseEntity<GetUserProfileResponse> getUserProfile(@PathVariable("id") Long id) {
        logger.info("getUserProfile by user {}", id);
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @GetMapping(value = "/searching/{id}")
    private ResponseEntity<GetUserProfileResponse> getSearchingUserProfileWithOffset(@PathVariable("id") Long id) {
        logger.info("getSearchingUserProfileWithOffset by user {}", id);
        return ResponseEntity.ok(userService.getNextUserProfileWithOffset(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        logger.info("deleteUser by user {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorite")
    public ResponseEntity<PostFavoriteResponse> postFavorite(
            @RequestParam(name = "from") Long from,
            @RequestParam(name = "to") Long to
    ) {
        logger.info("postFavorite from user {} to user {}", from, to);
        return ResponseEntity.ok(userService.postFavorite(from, to));
    }

    @GetMapping("/favorite/{id}")
    public ResponseEntity<GetProfilesResponse> getFavoriteProfiles(@PathVariable("id") Long id) {
        logger.info("getFavoriteProfiles by user {}", id);
        return ResponseEntity.ok(userService.getFavoriteProfiles(id));
    }

    @GetMapping("/follower/{id}")
    public ResponseEntity<GetProfilesResponse> getFollowerProfiles(@PathVariable("id") Long id) {
        logger.info("getFollowerProfiles by user {}", id);
        return ResponseEntity.ok(userService.getFollowerProfiles(id));
    }

    @GetMapping("/mutual/{id}")
    public ResponseEntity<GetProfilesResponse> getMutualFollowingProfiles(@PathVariable("id") Long id) {
        logger.info("getMutualFollowingProfiles by user {}", id);
        return ResponseEntity.ok(userService.getMutualFollowingProfiles(id));
    }
}
