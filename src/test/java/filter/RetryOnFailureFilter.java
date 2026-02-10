package filter;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RetryOnFailureFilter implements Filter {

    private final int maxRetries;
    private final int expectedStatus;

    public RetryOnFailureFilter(int maxRetries, int expectedStatus) {
        this.maxRetries = maxRetries;
        this.expectedStatus = expectedStatus;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        Response response = null;
        Exception lastException = null;
        Object originalBody = requestSpec.getBody();

        for (int attempt = 1; attempt <= maxRetries + 1; attempt++) {
            
            try {
                 if (originalBody != null) {
                    requestSpec.body(originalBody);
                }
                response = ctx.next(requestSpec, responseSpec);

                if (response != null) {
                    System.out.println(
                        "Attempt " + attempt +
                        " | Status = " + response.statusCode()
                    );

                    if (response.statusCode() == expectedStatus) {
                        return response; // ✅ success
                    }
                } else {
                    System.out.println(
                        "Attempt " + attempt + " | Response is NULL"
                    );
                }

            } catch (Exception e) {
                lastException = e;
                System.out.println(
                    "Attempt " + attempt +
                    " | Exception occurred: " + e.getClass().getSimpleName()
                );
            }
        }

        // After retries exhausted
        if (response != null) {
            return response;
        }

        // No response ever received → rethrow last exception
        throw new RuntimeException(
            "Request failed after retries. No response received.",
            lastException
        );
    }
}
