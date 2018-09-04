package com.swordgroup.sig.jtelepactrf;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
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
        return jsonObject.toString();
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
