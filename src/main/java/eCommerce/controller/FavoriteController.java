package eCommerce.controller;

import eCommerce.dto.response.ProductResponse;
import eCommerce.serviceLayer.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;


    @PostMapping("/{productId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long productId) {
        favoriteService.addToFavorites(productId);
        return ResponseEntity.ok().build();
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
