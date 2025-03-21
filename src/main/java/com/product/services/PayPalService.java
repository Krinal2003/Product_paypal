package com.product.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.*;
import com.product.DAO.OrderTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.util.*;


@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Value("${paypal.currency}")
    private String currency;

    private APIContext apiContext;
    
    @Autowired
    private OrderTransactionRepository orderTransactionRepository;
    @PostConstruct
    public void init() throws PayPalRESTException {
        Map<String, String> config = new HashMap<>();
        config.put("mode", mode);
        apiContext = new APIContext(new OAuthTokenCredential(clientId, clientSecret, config).getAccessToken());
        apiContext.setConfigurationMap(config);
    }

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.US, "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(Collections.singletonList(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }

    public Refund refundPayment(String transactionId) throws PayPalRESTException {
        // Fetch Sale object from PayPal using the transaction ID
        Sale sale = Sale.get(apiContext, transactionId);
        
        Amount refundAmount = new Amount();
        refundAmount.setCurrency(sale.getAmount().getCurrency()); // original currency
        refundAmount.setTotal(sale.getAmount().getTotal()); // Refund full amount
       
        // create refund request in full amount
        Refund refund = new Refund();
        refund.setAmount(sale.getAmount()); // Use original transaction amount

        return sale.refund(apiContext, refund);
    }
}
