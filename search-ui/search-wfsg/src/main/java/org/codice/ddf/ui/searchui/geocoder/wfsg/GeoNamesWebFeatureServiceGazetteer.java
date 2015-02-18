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

import org.codice.ddf.ui.searchui.geocoder.GeoCoder;
import org.codice.ddf.ui.searchui.geocoder.GeoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GeoNamesWebFeatureServiceGazetteer implements GeoCoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoNamesWebFeatureServiceGazetteer.class);

    private static final String WFS_G_URL = "http://geonames.nga.mil/nameswfsg/request.aspx";
    private static final String WFS_SERVICE = "service=WFS";
    private static final String WFS_VERSION = "version=1.1.0";
    private static final String WFS_REQUEST = "request=GetFeature";
    //private static final String WFS_TYPE = "typename=gml:idiso19112:SI_Gazetteer";
    private static final String WFS_TYPE = "typename=iso19112:SI_LocationInstance";
    private static final String WFS_NAMESPACE = "namespace=\"xmlns(iso19112=http://www.isotc211.org/19112)\"";
    private static final String WFS_MAX_FEATURES = "maxFeatures=50";
    private static final String WFS_FILTER = "filter=";
    private static final String URL_PARAMETER_MARKER = "?";
    private static final String URL_PARAMETER_DELIMITER = "&";

    @Override
    public GeoResult getLocation(String location) {
        try {
            location = URLEncoder.encode(location, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Unable to encode location.", e);
        }

        String urlStr = WFS_G_URL + URL_PARAMETER_MARKER +
                WFS_SERVICE + URL_PARAMETER_DELIMITER +
                WFS_VERSION + URL_PARAMETER_DELIMITER +
                WFS_REQUEST + URL_PARAMETER_DELIMITER +
                WFS_TYPE + URL_PARAMETER_DELIMITER +
                WFS_NAMESPACE + URL_PARAMETER_DELIMITER +
                //WFS_MAX_FEATURES +
                WFS_MAX_FEATURES + URL_PARAMETER_DELIMITER +
                WFS_FILTER +
                location;

        URL url = null;
        try {
            System.out.println("Querying: " + urlStr);
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            LOGGER.error("WFS-G URL is invalid: {}", urlStr, e);
        }

        URLConnection urlConnection = null;
        if (url != null) {
            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                LOGGER.error("Unable to open connection to WFS-G service.", e);
            }
        }

        InputStream isResult = null;
        if (urlConnection != null) {
            try {
                urlConnection.connect();
                try {
                    isResult = urlConnection.getInputStream();
                } catch (IOException e) {
                    LOGGER.error("Error retrieving input stream from WFS-G service.", e);
                }
            } catch (IOException e) {
                LOGGER.error("Unable to connect to WFS-G service", e);
            }
        }

        GeoResult geoResult = new GeoResult();
        if (isResult != null) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                br = new BufferedReader(new InputStreamReader(isResult));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                LOGGER.error("Unable to read input stream", e);
            } finally {
                IOUtils.closeQuietly(br);
            }

            System.out.println(sb.toString());
        }

        return geoResult;
    }
}
