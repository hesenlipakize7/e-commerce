package eCommerce.serviceLayer.serviceImpl;

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
    public void addToFavorites(Long productId) {
        User user = userService.getAuthenticatedUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        boolean exists = favoriteRepository.existsByUserIdAndProductId(user.getId(), productId);
        if (exists) {
            throw new AlreadyExistsException("Product is already favourite");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFromFavorites(Long productId) {
        User user = userService.getAuthenticatedUser();
        Favorite favorite = favoriteRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new NotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProductResponse> getMyFavorites() {
        User user = userService.getAuthenticatedUser();
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        return favorites.stream()
                .map(Favorite::getProduct)
                .map(productMapper::toDto)
                .toList();
    }
}
