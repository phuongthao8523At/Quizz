package com.app.quizzservice.security.oauth2;

import com.app.quizzservice.utils.Constants;
import com.app.quizzservice.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        var targetUrl = CookieUtils.get(request, Constants.REDIRECT_URI_PARAM_COOKIE_NAME)
                                   .map(Cookie::getValue)
                                   .orElse((Constants.SLASH));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                                        .queryParam(Constants.ERROR, exception.getLocalizedMessage())
                                        .build().toUriString();
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
