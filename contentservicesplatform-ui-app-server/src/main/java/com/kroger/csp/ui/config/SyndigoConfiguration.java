package com.kroger.csp.ui.config;

import com.kroger.imp.apm.SyndigoAPI;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "kroger.connection.syndigo")
@Slf4j
@Setter
@Getter
@ToString
public class SyndigoConfiguration {
    @NotNull
    private String proxyUserID;
    @NotNull
    private String proxyPwd;
    @NotNull
    private String proxyUrl;
    @NotNull
    private String proxyPort;
    @NotNull
    private String authURL;
    @NotNull
    private String authUsername;
    @NotNull
    private String authSecret;
    @NotNull
    private String assetURL;
    @NotNull
    private String searchURL;
    @NotNull
    private String targetPartyId;
    @NotNull
    private String orderBy;
    @NotNull
    private String attributeId;
    @NotNull
    private String dataOwner;
    @NotNull
    private String viewAngleMapping;
    @NotNull
    private String viewAngleMappingFront;
    @NotNull
    private String viewAngleMappingLeft;
    @NotNull
    private String viewAngleMappingTop;
    @NotNull
    private String viewAngleMappingBack;
    @NotNull
    private String viewAngleMappingRight;
    @NotNull
    private String viewAngleMappingBottom;
    @NotNull
    private String viewAngleMappingSwatch;
    @NotNull
    private String viewAngleMappingLifeStyle;
    @NotNull
    private String viewAngleMappingNutritionPanel;
    @NotNull
    private String viewAngleMappingIngredients;
    @NotNull
    private String viewAngleMappingMobile;
    @NotNull
    private String viewAngleMappingMarketingInfographic;

    @Bean
    public SyndigoAPI syndigoProperties() throws InstantiationException{
        if (StringUtils.isAnyBlank(proxyUserID, proxyPwd, proxyUrl, proxyPort, authURL, authUsername,
                authSecret, assetURL, searchURL)) {
            log.error("[IMPERRORLOG] Syndigo CXH properties cannot be initialized. " +
                    "Required properties proxyUserID, proxyPwd, proxyUrl, proxyPort, authURL, authUsername, " +
                    "uthSecret, assetURL, searchURL or the viewAngleMappings not found");
            throw new InstantiationException("Syndigo CXH  properties cannot be initialized. " +
                    "Required properties proxyUserID, proxyPwd, proxyUrl, proxyPort, authURL, authUsername, " +
                    "uthSecret, assetURL, searchURL or the viewAngleMappings not found");
        }
        Properties syndigoProp = new Properties();
        syndigoProp.setProperty("SYNDIGO.PROXY.UserID", this.proxyUserID);
        syndigoProp.setProperty("SYNDIGO.PROXY.Pwd", this.proxyPwd);
        syndigoProp.setProperty("SYNDIGO.PROXY.URL", this.proxyUrl);
        syndigoProp.setProperty("SYNDIGO.PROXY.PORT", this.proxyPort);
        syndigoProp.setProperty("SYNDIGO.AUTH.URL", this.authURL);
        syndigoProp.setProperty("SYNDIGO.AUTH.USERNAME", this.authUsername);
        syndigoProp.setProperty("SYNDIGO.AUTH.SECRET", this.authSecret);
        syndigoProp.setProperty("SYNDIGO.SEARCH.GTIN.URL", this.searchURL);
        syndigoProp.setProperty("SYNDIGO.DOWNLOAD.ASSET.URL", this.assetURL);
        syndigoProp.setProperty("SYNDIGO.REQUEST.TARGETPARTYID", this.targetPartyId);
        syndigoProp.setProperty("SYNDIGO.REQUEST.ORDERBY", this.orderBy);
        syndigoProp.setProperty("SYNDIGO.REQUEST.ATTRIBUTEID", this.attributeId);
        syndigoProp.setProperty("SYNDIGO.REQUEST.DATAOWNER", this.dataOwner);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING", this.viewAngleMapping);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.FRONT", this.viewAngleMappingFront);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.LEFT", this.viewAngleMappingLeft);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.TOP", this.viewAngleMappingTop);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.BACK", this.viewAngleMappingBack);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.RIGHT", this.viewAngleMappingRight);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.BOTTOM", this.viewAngleMappingBottom);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.SWATCH", this.viewAngleMappingSwatch);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.LIFESTYLE", this.viewAngleMappingLifeStyle);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.NUTRITIONPANEL",this.viewAngleMappingNutritionPanel);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.INGREDIENTS", this.viewAngleMappingIngredients);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.MOBILE", this.viewAngleMappingMobile);
        syndigoProp.setProperty("SYNDIGO.ASSET.VIEWANGLE.MAPPING.MARKETINGINFOGRAPHIC", this.viewAngleMappingMarketingInfographic);
        return new SyndigoAPI(syndigoProp);
    }
}
