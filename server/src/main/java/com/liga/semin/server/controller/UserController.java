package com.liga.semin.server.controller;

import com.liga.semin.server.message.GetUserProfileResponse;
import com.liga.semin.server.message.PostFavoriteResponse;
import com.liga.semin.server.message.UserDto;
import com.liga.semin.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping(value = "/profile/{id}")
    private ResponseEntity<GetUserProfileResponse> getUserProfile(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @GetMapping(value = "/searching/{id}")
    private ResponseEntity<GetUserProfileResponse> getSearchingUserProfileWithOffset(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getNextUserProfileWithOffset(id));
    }

    // todo: update user ???
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/favorite")
    public ResponseEntity<PostFavoriteResponse> postFavorite(
            @RequestParam(name = "from") Long from,
            @RequestParam(name = "to") Long to
    ) {
        return ResponseEntity.ok(
                new PostFavoriteResponse(userService.postFavorite(from, to))
        );
    }
}
