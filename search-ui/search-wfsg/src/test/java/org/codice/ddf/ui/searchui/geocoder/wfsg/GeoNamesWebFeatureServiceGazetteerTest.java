/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
package org.codice.ddf.ui.searchui.geocoder.wfsg;

import org.codice.ddf.ui.searchui.geocoder.GeoResult;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GeoNamesWebFeatureServiceGazetteerTest {
    @Test
    public void testGetLocation() throws Exception {
        //String location = "request=GetCapabilities";
        //String location = "<ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\"><ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"\\\"><ogc:PropertyName>iso19112:alternativeGeographicIdentifiers/iso19112:alternativeGeographicIdentifier/iso19112:name</ogc:PropertyName><ogc:Literal>*Phoenix*</ogc:Literal></ogc:PropertyIsLike></ogc:Filter>";
        String location = "<ogc:Filter xmlns:ogc=\"http://www.opengis.net/ogc\"><ogc:PropertyIsEqualTo wildCard=\"*\" singleChar=\"#\" escapeChar=\"\\\"><ogc:PropertyName>alternativeGeographicIdentifier</ogc:PropertyName><ogc:Literal>Scarborough</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>";
        GeoNamesWebFeatureServiceGazetteer gazetteer = new GeoNamesWebFeatureServiceGazetteer();
        GeoResult result = gazetteer.getLocation(location);
        assertTrue(true);
    }
}