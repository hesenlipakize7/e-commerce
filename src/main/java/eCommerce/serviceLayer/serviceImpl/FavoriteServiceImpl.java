package eCommerce.serviceLayer.serviceImpl;

import eCommerce.dto.request.FavoriteCreateRequest;
import eCommerce.dto.response.ProductResponse;
import eCommerce.exception.AlreadyExistsException;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.Favorite;
import eCommerce.model.entity.Product;
import eCommerce.model.entity.User;
import eCommerce.mapper.ProductMapper;
import eCommerce.repository.FavoriteRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.serviceLayer.service.FavoriteService;
import eCommerce.serviceLayer.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserService userService;

    @Override
    public void addToFavorites(FavoriteCreateRequest favoriteCreateRequest) {
        User user = userService.getAuthenticatedUser();
        log.info("Add to favorites requested.");
        Product product = productRepository.findById(favoriteCreateRequest.getProductId())
                .orElseThrow(() -> {
                    log.warn("Product not found while adding to favorites");
                    return new NotFoundException("Product not found");
                });
        boolean exists = favoriteRepository.existsByUserIdAndProductId(user.getId(), favoriteCreateRequest.getProductId());
        if (exists) {
            log.warn("Product already in favorites");
            throw new AlreadyExistsException("Product is already favourite");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
        log.info("Product added to favorites successfully.");
    }

    @Override
    public void removeFromFavorites(Long productId) {
        User user = userService.getAuthenticatedUser();
        log.info("Remove from favorites requested. userId={}, productId={}", user.getId(), productId);
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> {
                    log.warn("Favorite not found while removing. userId={}, productId={}", user.getId(), productId);
                    return new NotFoundException("Favorite not found");
                });
        favoriteRepository.delete(favorite);
        log.info("Product removed from favorites successfully. userId={}, productId={}", user.getId(), productId);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProductResponse> getMyFavorites() {
        User user = userService.getAuthenticatedUser();
        log.info("Fetching favorite products for user. userId={}", user.getId());
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        log.debug("Favorite product count for userId={}: {}", user.getId(), favorites.size());
        return favorites.stream()
                .map(Favorite::getProduct)
                .map(productMapper::toDto)
                .toList();
    }
}
