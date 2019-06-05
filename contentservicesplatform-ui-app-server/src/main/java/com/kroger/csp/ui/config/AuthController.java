package com.kroger.csp.ui.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kroger.commons.security.spoofing.SpoofingConfigurationProperties;

/**
 * Controller for auth related endpoints
 */
@Controller
@EnableConfigurationProperties({SpoofingConfigurationProperties.class})
public class AuthController
{
    @Autowired
    private Environment environment;
    
    @Autowired
    private SpoofingConfigurationProperties spoofingConfigurationProperties;

    @ResponseBody
    @RequestMapping(value = "/api/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object me(Authentication authentication)
    {
        if (null != authentication)
        {
            return authentication.getPrincipal();
        }
        HashMap<String, Boolean> anonymous = new HashMap<>();
        anonymous.put("anonymous", true);
        return anonymous;
    }

    @ResponseBody
    @RequestMapping(value = "/api/spoofing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object spoofing()
    {
        return spoofingConfigurationProperties.spoofingResponse(environment);
    }
}
