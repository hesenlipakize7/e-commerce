package eCommerce.controller;

import eCommerce.dto.request.FavoriteCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.serviceLayer.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FavoriteController {
    FavoriteService favoriteService;

    @PostMapping("/{productId}")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        favoriteService.addToFavorites(favoriteCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long productId) {
        favoriteService.removeFromFavorites(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getMyFavorites());
    }
}
