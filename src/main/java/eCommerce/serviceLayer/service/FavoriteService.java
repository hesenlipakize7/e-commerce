package eCommerce.serviceLayer.service;

import eCommerce.dto.request.FavoriteCreateRequest;
import eCommerce.dto.response.ProductResponse;

import java.util.List;

public interface FavoriteService {
    void addToFavorites(FavoriteCreateRequest favoriteCreateRequest );

    void removeFromFavorites(Long productId);

    List<ProductResponse> getMyFavorites();
}
