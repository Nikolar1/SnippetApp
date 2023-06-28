package com.nikolar.snippetbackend.service;

import org.springframework.web.util.UriUtils;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class SanitazationService {
    public String sanitizeQueryParam(String param) {
        if (param != null) {
            return UriUtils.encodeQueryParam(param, StandardCharsets.UTF_8);
        }
        return null;
    }
}
