package com.ciber.foodieshoot.applications.detection.Auxiliar.FoodDetection.FoodPosts;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "Serving quantity",
        "Serving unit",
        "Serving weight (grams)",
        "Total calories",
        "Total fat",
        "Total saturated fat",
        "Cholestrol",
        "Sodium",
        "Total carbs",
        "Fiber",
        "Sugar",
        "Protein",
        "Potassium",
        "Ocurrences"
})
public class SingleFoodInfo {
    @JsonProperty("name")
    private String name;
    @JsonProperty("Serving quantity")
    private Integer servingQuantity;
    @JsonProperty("Serving unit")
    private String servingUnit;
    @JsonProperty("Serving weight (grams)")
    private Integer servingWeightGrams;
    @JsonProperty("Total calories")
    private Double totalCalories;
    @JsonProperty("Total fat")
    private Integer totalFat;
    @JsonProperty("Total saturated fat")
    private Object totalSaturatedFat;
    @JsonProperty("Cholestrol")
    private Object cholestrol;
    @JsonProperty("Sodium")
    private Object sodium;
    @JsonProperty("Total carbs")
    private Double totalCarbs;
    @JsonProperty("Fiber")
    private Object fiber;
    @JsonProperty("Sugar")
    private Object sugar;
    @JsonProperty("Protein")
    private Double protein;
    @JsonProperty("Potassium")
    private Object potassium;
    @JsonProperty("Ocurrences")
    private Integer ocurrences;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Serving quantity")
    public Integer getServingQuantity() {
        return servingQuantity;
    }

    @JsonProperty("Serving quantity")
    public void setServingQuantity(Integer servingQuantity) {
        this.servingQuantity = servingQuantity;
    }

    @JsonProperty("Serving unit")
    public String getServingUnit() {
        return servingUnit;
    }

    @JsonProperty("Serving unit")
    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    @JsonProperty("Serving weight (grams)")
    public Integer getServingWeightGrams() {
        return servingWeightGrams;
    }

    @JsonProperty("Serving weight (grams)")
    public void setServingWeightGrams(Integer servingWeightGrams) {
        this.servingWeightGrams = servingWeightGrams;
    }

    @JsonProperty("Total calories")
    public Double getTotalCalories() {
        return totalCalories;
    }

    @JsonProperty("Total calories")
    public void setTotalCalories(Double totalCalories) {
        this.totalCalories = totalCalories;
    }

    @JsonProperty("Total fat")
    public Integer getTotalFat() {
        return totalFat;
    }

    @JsonProperty("Total fat")
    public void setTotalFat(Integer totalFat) {
        this.totalFat = totalFat;
    }

    @JsonProperty("Total saturated fat")
    public Object getTotalSaturatedFat() {
        return totalSaturatedFat;
    }

    @JsonProperty("Total saturated fat")
    public void setTotalSaturatedFat(Object totalSaturatedFat) {
        this.totalSaturatedFat = totalSaturatedFat;
    }

    @JsonProperty("Cholestrol")
    public Object getCholestrol() {
        return cholestrol;
    }

    @JsonProperty("Cholestrol")
    public void setCholestrol(Object cholestrol) {
        this.cholestrol = cholestrol;
    }

    @JsonProperty("Sodium")
    public Object getSodium() {
        return sodium;
    }

    @JsonProperty("Sodium")
    public void setSodium(Object sodium) {
        this.sodium = sodium;
    }

    @JsonProperty("Total carbs")
    public Double getTotalCarbs() {
        return totalCarbs;
    }

    @JsonProperty("Total carbs")
    public void setTotalCarbs(Double totalCarbs) {
        this.totalCarbs = totalCarbs;
    }

    @JsonProperty("Fiber")
    public Object getFiber() {
        return fiber;
    }

    @JsonProperty("Fiber")
    public void setFiber(Object fiber) {
        this.fiber = fiber;
    }

    @JsonProperty("Sugar")
    public Object getSugar() {
        return sugar;
    }

    @JsonProperty("Sugar")
    public void setSugar(Object sugar) {
        this.sugar = sugar;
    }

    @JsonProperty("Protein")
    public Double getProtein() {
        return protein;
    }

    @JsonProperty("Protein")
    public void setProtein(Double protein) {
        this.protein = protein;
    }

    @JsonProperty("Potassium")
    public Object getPotassium() {
        return potassium;
    }

    @JsonProperty("Potassium")
    public void setPotassium(Object potassium) {
        this.potassium = potassium;
    }

    @JsonProperty("Ocurrences")
    public Integer getOcurrences() {
        return ocurrences;
    }

    @JsonProperty("Ocurrences")
    public void setOcurrences(Integer ocurrences) {
        this.ocurrences = ocurrences;
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
