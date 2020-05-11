package com.ciber.foodieshoot.applications.detection.Auxiliar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "form_data",
        "password",
        "username",
        "email"
})
public class RegisterErrorParser {
    @JsonProperty("form_data")
    private RegisterErrorParserFormData formData;
    @JsonProperty("password")
    private List<String> password = null;
    @JsonProperty("username")
    private List<String> username = null;
    @JsonProperty("email")
    private List<String> email = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("form_data")
    public RegisterErrorParserFormData getFormData() {
        return formData;
    }
    @JsonProperty("form_data")
    public void setFormData(RegisterErrorParserFormData formData) {
        this.formData = formData;
    }

    @JsonProperty("password")
    public List<String> getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(List<String> password) {
        this.password = password;
    }

    @JsonProperty("username")
    public List<String> getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(List<String> username) {
        this.username = username;
    }

    @JsonProperty("email")
    public List<String> getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(List<String> email) {
        this.email = email;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
