package eCommerce.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderResponse {
    private Long orderId;
    private Double totalAmount;
    private LocalDate orderDate;

}
