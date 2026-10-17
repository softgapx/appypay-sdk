package com.softgapx.appypay.dto.charge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.softgapx.appypay.api.PaymentMethod;
import com.softgapx.appypay.exception.InvalidPaymentRequestException;

/**
 * Validador de {@link CreatePaymentRequest}, em Java puro — sem depender
 * de Bean Validation (jakarta.validation) nem de nenhum framework.
 * <p>
 * Isto evita obrigar o consumidor do SDK a ter um provider de validação
 * (ex: Hibernate Validator) no classpath só para poder usar a biblioteca.
 */
public final class PaymentRequestValidator {

    private static final String ORDER_ID_PATTERN = "^[a-zA-Z0-9]+$";
    private static final String DESCRIPTION_PATTERN = "^[a-zA-Z0-9 ]*$";
    private static final String PHONE_PATTERN = "^9[0-9]{8}$";
    private static final int ORDER_ID_MAX_LENGTH = 15;
    private static final int DESCRIPTION_MAX_LENGTH = 100;

    private PaymentRequestValidator() {
    }

    /**
     * Valida o pedido e lança {@link InvalidPaymentRequestException} caso
     * existam violações, com todas as mensagens combinadas.
     */
    public static void validate(CreatePaymentRequest request) {

        List<String> errors = collectErrors(request);

        if (!errors.isEmpty()) {

            throw new InvalidPaymentRequestException(
                    String.join("; ", errors)
            );
        }
    }

    private static List<String> collectErrors(CreatePaymentRequest request) {

        List<String> errors = new ArrayList<>();

        if (request == null) {

            errors.add("O pedido de pagamento não pode ser nulo.");
            return errors;
        }

        validateOrderId(request.orderId(), errors);
        validateAmount(request.amount(), errors);
        validateDescription(request.description(), errors);
        validatePaymentMethod(request, errors);

        return errors;
    }

    private static void validateOrderId(String orderId, List<String> errors) {

        if (orderId == null || orderId.isBlank()) {
            errors.add("orderId é obrigatório");
            return;
        }

        if (orderId.length() > ORDER_ID_MAX_LENGTH) {
            errors.add("orderId deve possuir no máximo " + ORDER_ID_MAX_LENGTH + " caracteres");
        }

        if (!orderId.matches(ORDER_ID_PATTERN)) {
            errors.add("orderId deve conter apenas caracteres alfanuméricos");
        }
    }

    private static void validateAmount(BigDecimal amount, List<String> errors) {

        if (amount == null) {
            errors.add("amount é obrigatório");
            return;
        }

        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            errors.add("amount deve ser maior que zero");
        }
    }

    private static void validateDescription(String description, List<String> errors) {

        if (description == null) {
            return;
        }

        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            errors.add("description deve possuir no máximo " + DESCRIPTION_MAX_LENGTH + " caracteres");
        }

        if (!description.matches(DESCRIPTION_PATTERN)) {
            errors.add("description deve conter apenas letras, números e espaços");
        }
    }

    private static void validatePaymentMethod(CreatePaymentRequest request, List<String> errors) {

        PaymentMethod method = request.paymentMethod();

        if (method == null) {
            errors.add("paymentMethod é obrigatório");
            return;
        }

        if (method == PaymentMethod.GPO) {

            String phoneNumber = request.phoneNumber();

            if (phoneNumber == null || !phoneNumber.matches(PHONE_PATTERN)) {
                errors.add("phoneNumber deve possuir 9 dígitos e começar por 9");
            }

            return;
        }

        if (method == PaymentMethod.REF) {

            if (request.referenceNumber() == null || request.referenceNumber().isBlank()) {
                errors.add("referenceNumber é obrigatório para pagamentos REF");
            }

            if (request.dueDate() == null) {
                errors.add("dueDate é obrigatório para pagamentos REF");
            }
        }
    }
}
