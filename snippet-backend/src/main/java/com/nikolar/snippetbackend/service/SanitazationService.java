package com.nikolar.snippetbackend.service;

import org.springframework.web.util.UriUtils;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
public class SanitazationService {
    public String sanitizeQueryParam(String param) {
        if (param != null) {
            String rez = UriUtils.encodeQueryParam(param, StandardCharsets.UTF_8);
            rez = rez.replace("%20", " ");
            rez = rez.replace("%22", "\"");
            return rez;
        }
        return null;
    }
}
