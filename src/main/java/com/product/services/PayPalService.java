package com.product.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes.Name;
import java.util.HashMap;
import com.paypal.base.rest.OAuthTokenCredential;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Amount;


@Service
public class PayPalService {

	private static final Refund Refund = null;

	private static final String currency = null;

	@Value("${paypal.client.id}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.mode}")
	private String mode;

	private APIContext apiContext;

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
			String Method,
			String intent,
			String description,
			String cancelUrl,
			String successUrl) throws PayPalRESTException {
		Amount amount = new Amount();
		amount.setCurrency(currency);
		amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(Method);

		Payment payment = new Payment();
		payment.setIntent(intent);
		payment.setPayer(payer);
		payment.setTransactions(Arrays.asList(transaction));
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);

		return payment.create(apiContext);
	}

	public Payment excecutePayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}
	public Refund refundPayment(String name) throws PayPalRESTException {
	    Amount refundAmount = new Amount();
	    refundAmount.setCurrency(currency);
	    refundAmount.setTotal(String.format(Locale.US, "%.2f", refundAmount));

	    Sale sale = new Sale();
	    sale.setId(name);

	    return sale.refund(apiContext , Refund);
	}




	public APIContext getAPIContext() {
		return apiContext;
	}

}