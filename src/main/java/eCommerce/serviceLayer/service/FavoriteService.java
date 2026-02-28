package eCommerce.serviceLayer.service;

import eCommerce.dto.response.FavoriteResponse;
import eCommerce.dto.response.ProductResponse;

import java.util.List;

public interface FavoriteService {
    void addToFavorites(Long productId);

    void removeFromFavorites(Long productId);

    List<ProductResponse> getMyFavorites();
}
