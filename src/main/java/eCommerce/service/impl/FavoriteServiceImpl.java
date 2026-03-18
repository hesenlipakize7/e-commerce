package eCommerce.service.impl;

import eCommerce.dto.response.ProductResponse;
import eCommerce.exception.AlreadyExistsException;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Favorite;
import eCommerce.model.entity.Product;
import eCommerce.model.entity.User;
import eCommerce.mapper.ProductMapper;
import eCommerce.repository.FavoriteRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.service.FavoriteService;
import eCommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserService userService;

    @Override
    public void addToFavorites(Long productId) {
        User user = userService.getAuthenticatedUser();
        log.info("Add to favorites request. userId={}, productId={}", user.getId(), productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found while adding to favorites. productId={}", productId);
                    return new NotFoundException("Product not found");
                });
        boolean exists = favoriteRepository.existsByUserIdAndProductId(user.getId(), productId);
        if (exists) {
            log.warn("Add to favorites failed. Product already in favorites. userId={}, productId={}", user.getId(), productId);
            throw new AlreadyExistsException("Product is already favourite");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
        log.info("Product added to favorites successfully. userId={}, productId={}", user.getId(), productId);
    }

    @Override
    public void removeFromFavorites(Long productId) {
        User user = userService.getAuthenticatedUser();
        log.info("Remove from favorites request. userId={}, productId={}", user.getId(), productId);
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> {
                    log.warn("Favorite not found while removing. userId={}, productId={}", user.getId(), productId);
                    return new NotFoundException("Favorite not found");
                });
        favoriteRepository.delete(favorite);
        log.info("Product removed from favorites successfully. userId={}, productId={}", user.getId(), productId);
    }

    @Override
    public List<ProductResponse> getMyFavorites() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching favorite products for userId={}", user.getId());
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        log.info("Favorite products fetched. userId={}, count={}", user.getId(), favorites.size());
        return favorites.stream()
                .map(Favorite::getProduct)
                .map(productMapper::toDto)
                .toList();
    }
}
