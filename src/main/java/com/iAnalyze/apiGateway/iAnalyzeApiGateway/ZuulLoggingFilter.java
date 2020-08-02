package com.iAnalyze.apiGateway.iAnalyzeApiGateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Component
public class ZuulLoggingFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        String requestURL = context.getRequest().getRequestURL().toString();
        logger.info("request -> {}",
                requestURL);
//        return ((!requestURL.contains("api/users/")) || (!requestURL.contains("api/users/login")));
        return (requestURL.contains("api/users/test") || requestURL.contains("/api/batch"));
    }

    @Override
    public Object run() throws ZuulException {
        // log details of request

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String bearer = request.getHeader("Authorization");
        logger.info("token -> {}", bearer);

        String url = Utils.url;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", bearer);
        requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Boolean> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<Boolean> response = new RestTemplate().exchange(url, HttpMethod.GET,
                requestEntity, Boolean.class);

        if(!response.getBody()) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            context.setResponseBody("Unauthorized");
            context.getResponse().setContentType("application/json");
        }

        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
