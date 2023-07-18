package com.amigoscode.fraud;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/fraud-check")
public class FraudController {

    private final FraudCheckHistoryService fraudCheckHistoryService;

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId){
        boolean isFraudulentCustomer = fraudCheckHistoryService.isFraudulentCustomer(customerId);
        log.info("fraud check request for customer {}", customerId);
        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
