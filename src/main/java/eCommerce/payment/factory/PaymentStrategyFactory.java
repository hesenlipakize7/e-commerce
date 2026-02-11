package eCommerce.payment.factory;

import eCommerce.exception.BadRequestException;
import eCommerce.model.enums.PaymentMethod;
import eCommerce.payment.strategi.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentStrategyFactory {
    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentStrategyFactory(List<PaymentStrategy> strategyList) {
        strategies = new HashMap<>();
        for (PaymentStrategy strategy : strategyList) {
            strategies.put(resolveMethod(strategy), strategy);
        }
    }

    public PaymentStrategy getStrategy(PaymentMethod paymentMethod) {
        PaymentStrategy strategy = strategies.get(paymentMethod);
        if (strategy == null) {
            throw new BadRequestException("Unsupported payment method");
        }
        return strategy;
    }

    private PaymentMethod resolveMethod(PaymentStrategy strategy) {
        return strategy.getPaymentMethod();
    }
}
