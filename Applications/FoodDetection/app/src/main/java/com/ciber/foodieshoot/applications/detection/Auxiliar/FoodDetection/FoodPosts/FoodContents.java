package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

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
        "total_calories",
        "processed"
})
public class FoodContents {
    @JsonProperty("total_calories")
    private Double totalCalories;
    @JsonProperty("processed")
    private List<SingleFoodInfo> processed = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("total_calories")
    public Double getTotalCalories() {
        return totalCalories;
    }

    @JsonProperty("total_calories")
    public void setTotalCalories(Double totalCalories) {
        this.totalCalories = totalCalories;
    }

    @JsonProperty("processed")
    public List<SingleFoodInfo> getProcessed() {
        return processed;
    }

    @JsonProperty("processed")
    public void setProcessed(List<SingleFoodInfo> processed) {
        this.processed = processed;
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
