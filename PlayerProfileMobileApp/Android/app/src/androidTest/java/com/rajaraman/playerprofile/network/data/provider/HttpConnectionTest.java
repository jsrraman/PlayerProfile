package com.rajaraman.playerprofile.network.data.provider;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rajaraman.playerprofile.R;
import com.rajaraman.playerprofile.network.data.provider.HttpConnection;
import com.rajaraman.playerprofile.network.data.provider.PlayerProfileApiDataProvider;
import com.rajaraman.playerprofile.ui.MainActivity;

import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.FragmentTestUtil;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18, manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class HttpConnectionTest {
    @Before
    public void setup()  {
        // To redirect Robolectric to stdout
        System.setProperty("robolectric.logging", "stdout");
    }

    @Test
    public void testWhetherGetCountryListWebServiceIsWorking() throws Exception {

        String url = PlayerProfileApiDataProvider.profilePlayerWebServicesBaseUrl;
        url += PlayerProfileApiDataProvider.countryListUrl;

        HttpConnection httpConn = new HttpConnection();
        InputStream inputStream = httpConn.getData(url);

        assertNotNull(inputStream);
    }

    @Test
    public void testWhetherPlayerListForEnglandWebServiceIsWorking() throws Exception {

        String url = PlayerProfileApiDataProvider.profilePlayerWebServicesBaseUrl;
        url += PlayerProfileApiDataProvider.playerListUrl;
        url += "1"; // England

        HttpConnection httpConn = new HttpConnection();
        InputStream inputStream = httpConn.getData(url);

        assertNotNull(inputStream);
    }

    @Test
    public void testWhetherPlayerProfileForPlayerWebServiceIsWorking() throws Exception {

        String url = PlayerProfileApiDataProvider.profilePlayerWebServicesBaseUrl;
        url += PlayerProfileApiDataProvider.playerProfileUrl;
        url += "8917"; // Moeen Ali

        HttpConnection httpConn = new HttpConnection();
        InputStream inputStream = httpConn.getData(url);

        assertNotNull(inputStream);
    }
}