package com.swordgroup.sig.jtelepactrf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * API de transformation telepac.
 *
 * @author mmast Sword/Groupama
 */
public class Jtelepactrf {

    /**
     * Telepac en Json
     */
    private final JsonObject telepac;

    /**
     * Règles
     */
    private final List<Rule> rules;

    /**
     * Rapport
     */
    private final List<String> repport;

    public Jtelepactrf(JsonObject telepac, List<Rule> rules) {
        this.telepac = telepac;
        this.rules = rules;
        this.repport = new ArrayList<>();
    }

    public JsonObject transform() {
        transformGeojson(telepac);
        return telepac;
    }

    private void transformGeojson(JsonElement subgeojson) {
        if (subgeojson == null) {
            repport.add("transformGeojson: Geojson is null");
            return;
        }
        if (!subgeojson.isJsonObject()) {
            repport.add("transformGeojson: Geojson is not JsonObject");
            return;
        }
        JsonObject json = subgeojson.getAsJsonObject();
        JsonElement typeElement = json.get("type");
        if (typeElement == null) {
            repport.add("transformGeojson: type is null");
            return;
        }
        if (!typeElement.isJsonPrimitive()) {
            repport.add("transformGeojson: type is not JsonPrimitive");
            return;
        }
        String type = typeElement.getAsJsonPrimitive().getAsString();
        if ("Feature".equalsIgnoreCase(type)) {
            JsonElement propertiesElement = json.get("properties");
            if (propertiesElement == null) {
                repport.add("transformGeojson: properties is null");
                return;
            }
            if (!propertiesElement.isJsonObject()) {
                repport.add("transformGeojson: properties is not JsonObject");
                return;
            }
            transformProperties(propertiesElement.getAsJsonObject());
        } else if ("FeatureCollection".equalsIgnoreCase(type)) {
            JsonElement featuresElement = json.get("features");
            if (featuresElement == null) {
                repport.add("transformGeojson: features is null");
                return;
            }
            if (!featuresElement.isJsonArray()) {
                repport.add("transformGeojson: features is not JsonArray");
                return;
            }
            for (JsonElement feature : featuresElement.getAsJsonArray()) {
                transformGeojson(feature);
            }
        } else {
            repport.add("transformGeojson: type is not Feature or FeatureCollection");
        }
    }

    private void transformProperties(JsonObject properties) {
        for (Rule rule : rules) {
            // traitement de la règle de transformation "add"
            if (Rule.ADD.equals(rule.getType())) {
                if (!properties.has(rule.getProperty())) {
                    if (rule.getValue() == null) {
                        properties.add(rule.getProperty(), null);
                    } else if (rule.getValue() instanceof CharSequence) {
                        properties.addProperty(rule.getProperty(), rule.getValue().toString());
                    } else if (rule.getValue() instanceof Number) {
                        properties.addProperty(rule.getProperty(), (Number) rule.getValue());
                    } else if (rule.getValue() instanceof Boolean) {
                        properties.addProperty(rule.getProperty(), (Boolean) rule.getValue());
                    } else if (rule.getValue() instanceof Character) {
                        properties.addProperty(rule.getProperty(), (Character) rule.getValue());
                    } else {
                        repport.add("transformProperties: rule " + rule + "is not correctly defined");
                    }
                } else {
                    repport.add("transformProperties: rule " + rule + "is not correctly defined (property already exists)");
                }
            } // traitement de la règle de transformation "remove"
            else if (Rule.REMOVE.equals(rule.getType())) {
                if (properties.has(rule.getProperty())) {
                    properties.remove(rule.getProperty());
                } else {
                    repport.add("transformProperties: rule " + rule + "is not correctly defined (unfound property)");
                }
            } // traitement de la règle de transformation "update"
            else if (Rule.UPDATE.equals(rule.getType())) {
                if (properties.has(rule.getProperty())) {
                    if (rule.getValue() == null) {
                        properties.remove(rule.getProperty());
                        properties.add(rule.getProperty(), null);
                    } else if (rule.getValue() instanceof CharSequence) {
                        properties.remove(rule.getProperty());
                        properties.addProperty(rule.getProperty(), rule.getValue().toString());
                    } else if (rule.getValue() instanceof Number) {
                        properties.remove(rule.getProperty());
                        properties.addProperty(rule.getProperty(), (Number) rule.getValue());
                    } else if (rule.getValue() instanceof Boolean) {
                        properties.remove(rule.getProperty());
                        properties.addProperty(rule.getProperty(), (Boolean) rule.getValue());
                    } else if (rule.getValue() instanceof Character) {
                        properties.remove(rule.getProperty());
                        properties.addProperty(rule.getProperty(), (Character) rule.getValue());
                    } else {
                        repport.add("transformProperties: rule " + rule + "is not correctly defined");
                    }
                } else {
                    repport.add("transformProperties: rule " + rule + "is not correctly defined (unfound property)");
                }
            } // traitement de la règle de transformation "rename"
            else if (Rule.RENAME.equals(rule.getType())) {
                if (properties.has(rule.getProperty()) && !properties.has(rule.getName())) {
                    properties.add(rule.getName(), properties.get(rule.getProperty()));
                    properties.remove(rule.getProperty());
                } else {
                    repport.add("transformProperties: rule " + rule + "is not correctly defined for property (unfound property or renamed property already exists)");
                }
            } else {
                repport.add("transformProperties: rule " + rule.toString() + " is not correctly defined (unknown rule type)");
            }
        }
    }
}
