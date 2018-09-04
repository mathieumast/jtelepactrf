package com.swordgroup.sig.jtelepactrf;

import com.google.gson.JsonObject;
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

    public Jtelepactrf(JsonObject telepac, List<Rule> rules) {
        this.telepac = telepac;
        this.rules = rules;
    }

    public JsonObject transform() {
        return telepac;
    }
}
