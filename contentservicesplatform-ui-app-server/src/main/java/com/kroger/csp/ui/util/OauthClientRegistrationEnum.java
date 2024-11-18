package com.kroger.csp.ui.util;

public enum OauthClientRegistrationEnum
{
    KROGER_SERVICE("kroger-service");

    private String registrationId;

    OauthClientRegistrationEnum(String registrationId)
    {
        this.registrationId = registrationId;
    }

    public String getRegistrationId()
    {
        return registrationId;
    }

}