package br.com.api.infracoes.shared.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeaderHelper {

    private final HttpServletRequest request;
    private final HttpServletResponse httpServletResponse;

    public Long getUserId() {
        Object userId = request.getAttribute("userId");
        return (userId instanceof Long) ? (Long) userId : null;
    }

    public void setHeader(String name, String value) {
        httpServletResponse.setHeader(name, value);
    }

    public String getHeader(String name) {
        return (String) request.getAttribute(name);
    }

}
