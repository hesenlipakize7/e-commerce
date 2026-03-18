package eCommerce.service;


import eCommerce.dto.response.ProductResponse;
import eCommerce.exception.AlreadyExistsException;
import eCommerce.exception.NotFoundException;
import eCommerce.model.entity.*;
import eCommerce.repository.FavoriteRepository;
import eCommerce.repository.ProductRepository;
import eCommerce.mapper.ProductMapper;
import eCommerce.service.impl.FavoriteServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private User user;
    private Product product;
    private Favorite favorite;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Phone");
        product.setPrice(BigDecimal.valueOf(1000));

        favorite = new Favorite();
        favorite.setId(1L);
        favorite.setUser(user);
        favorite.setProduct(product);

        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Phone");
    }

    @Test
    void addToFavorites_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(favoriteRepository.existsByUserIdAndProductId(1L,1L)).thenReturn(false);

        favoriteService.addToFavorites(1L);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addToFavorites_productNotFound() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> favoriteService.addToFavorites(1L));
    }

    @Test
    void addToFavorites_alreadyExists() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(favoriteRepository.existsByUserIdAndProductId(1L,1L)).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> favoriteService.addToFavorites(1L));
    }

    @Test
    void removeFromFavorites_success() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(favoriteRepository.findByUserIdAndProductId(1L,1L))
                .thenReturn(Optional.of(favorite));

        favoriteService.removeFromFavorites(1L);

        verify(favoriteRepository).delete(favorite);
    }

    @Test
    void removeFromFavorites_notFound() {

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(favoriteRepository.findByUserIdAndProductId(1L,1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> favoriteService.removeFromFavorites(1L));
    }

    @Test
    void getMyFavorites_success() {

        List<Favorite> favorites = List.of(favorite);
        List<ProductResponse> responses = List.of(productResponse);

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(favoriteRepository.findByUserId(1L)).thenReturn(favorites);
        when(productMapper.toDto(product)).thenReturn(productResponse);

        List<ProductResponse> result = favoriteService.getMyFavorites();

        assertEquals(1, result.size());
        verify(favoriteRepository).findByUserId(1L);
    }
}
