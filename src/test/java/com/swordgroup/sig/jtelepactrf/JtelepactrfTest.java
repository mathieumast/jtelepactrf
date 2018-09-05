package com.swordgroup.sig.jtelepactrf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.junit.Test;

/**
 * Test de la transformation.
 *
 * @author mmast Sword/Groupama
 */
public class JtelepactrfTest {

    private static final String COORDINATES = "coordinates";

    private static final String POLYGON = "Polygon";

    private static final String TYPE = "type";

    private static final String GEOMETRY = "geometry";

    private static final String FEATURES = "features";

    @Test
    public void testTransformation() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("telepac_1_2018.shp").getFile());
        FeatureCollection featureCollection = ShapeUtils.shape2features(file);
        String featureJson = multiPolyToPolyJson(new FeatureJSON().toString(featureCollection));
        System.out.println(featureJson);
    }
    
    public List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.add("COMMERC", 0));
        rules.add(Rule.add("AIDEBIO", ""));
        rules.add(Rule.add("MAEC1_CODE", ""));
        rules.add(Rule.add("MAEC1CIBLE", 0));
        rules.add(Rule.add("MAEC2_CODE", ""));
        rules.add(Rule.add("MAEC2CIBLE", 0));
        rules.add(Rule.add("MAEC3_CODE", ""));
        rules.add(Rule.add("MAEC3CIBLE", 0));
        rules.add(Rule.remove("SURF"));
        rules.add(Rule.remove("RECONV_PP"));
        rules.add(Rule.remove("CIBLE_SHP"));
        rules.add(Rule.remove("MAEC_PRV"));
        rules.add(Rule.update("NUMERO_PA", 0));
        rules.add(Rule.rename("NUMERO_I", "NUMERO_SI"));
        rules.add(Rule.rename("NUMERO_P", "NUMERO"));     
        return rules;
    }

    public String multiPolyToPolyJson(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray features = jsonObject.get(FEATURES).getAsJsonArray();
        for (int i = 0; i < features.size(); i++) {
            JsonObject feature = features.get(i).getAsJsonObject();
            JsonObject geometry = feature.get(GEOMETRY).getAsJsonObject();
            geometry.remove(TYPE);
            geometry.addProperty(TYPE, POLYGON);

            JsonArray coordinates = geometry.get(COORDINATES).getAsJsonArray();
            JsonArray subLevel = coordinates.get(0).getAsJsonArray();
            geometry.remove(COORDINATES);
            geometry.add(COORDINATES, subLevel);
        }
        
        Jtelepactrf jtelepactrf = new Jtelepactrf(jsonObject, createRules());
        
        return jtelepactrf.transform().toString();
    }

    private static class ShapeUtils {

        public static FeatureCollection shape2features(File file) throws IOException {
            DataStore dataStore = new ShapefileDataStore(file.toURI().toURL());
            try {
                SimpleFeatureSource featureSource = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
                return featureSource.getFeatures();
            } finally {
                dataStore.dispose();
            }
        }
    }
}
