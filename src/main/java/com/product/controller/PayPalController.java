package com.product.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;
import com.product.entity.OrderTransaction;
import com.product.entity.Product;
import com.product.services.OrderTransactionService;
import com.product.services.PayPalService;
import com.product.services.ProductServices;

@Controller
@RequestMapping("/payment")
public class PayPalController {

	 @Autowired
	 OrderTransactionService orderTransactionService; 
    @Autowired
    ProductServices productServices;

    @Autowired
    PayPalService payPalService;

    @GetMapping("/buy/{id}")
    public String showProductDetail(@PathVariable int id, Model model) {
        try {
            Product product = productServices.getProductById(id);
            if (product == null) {
                return "error";
            }
            model.addAttribute("productName", product.getName());
            model.addAttribute("amount", product.getPrice());
            return "payment";
        } catch (Exception e) {
            System.out.println(e);
            return "error";
        }
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam String name, @RequestParam double price, Model model) {
        System.out.println("Entered in payment");
        model.addAttribute("productName", name);
        model.addAttribute("amount", price);
        return "payment";
    }

    @PostMapping("/payment")
    public RedirectView createPayment(@RequestParam double amount) {
        try {
            String cancelUrl = "http://localhost:8081/payment/cancel";
            String successUrl = "http://localhost:8081/payment/success";
            Payment payment = payPalService.createPayment(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    "Payment for product",
                    cancelUrl,
                    successUrl);
            
            System.out.println("Create: "+payment.toJSON());

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return new RedirectView("/error");
    }

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            Model model) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            System.out.println("Execute: "+payment.toJSON());
            if (payment.getState().equals("approved")) {
                model.addAttribute("message", "Payment successful!");
                return "paymentSucess";
            }
        } catch (PayPalRESTException e) {
            model.addAttribute("message", "Error occurred: " + e.getMessage());
            return "paymentError";
        }
        return "paymentError";
    }


    @GetMapping("/cancel")
    public String paymentCancel(Model model) {
        model.addAttribute("message", "Payment was canceled.");
        return "index";
    }

    @GetMapping("/error")
    public String paymentError(Model model) {
        model.addAttribute("message", "An error occurred during payment.");
        return "error";
    }
    @PostMapping("/refund")
    public String refundPayment(Model model) {
        try {
        	String transactionId = "3AXXXX6XXXXXXXX";
            var transactionOpt = orderTransactionService.findByTransactionId(transactionId);
            if (transactionOpt.isEmpty()) {
                model.addAttribute("message", "Transaction not found");
                return "refundError";
            }

            Refund refund = payPalService.refundPayment(transactionId);
            OrderTransaction transaction = transactionOpt.get();
            transaction.setStatus("REFUNDED");
            orderTransactionService.saveTransaction(transaction);

            model.addAttribute("message", "Refund successful! Refund ID: " + refund.getId());
            return "refundSuccess";
        } catch (PayPalRESTException e) {
            model.addAttribute("message", "Refund failed: " + e.getMessage());
            return "refundError";
        }
    }
    @GetMapping("/refund")
    public String showRefundPage() {
        return "refund"; 
    }
    
    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<OrderTransaction> orders = orderTransactionService.getAllOrders();
        model.addAttribute("orders", orders);
        return "ordered";
    }
}