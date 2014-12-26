//package com.rajaraman.playerprofile.network;
//
//import com.rajaraman.playerprofile.network.data.provider.OnDataReceivedListener;
//import com.rajaraman.playerprofile.network.data.provider.PlayerProfileApiDataProvider;
//import com.rajaraman.playerprofile.ui.MainActivity;
//import com.rajaraman.playerprofile.utils.ApiResponseEntity;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//@Config(emulateSdk = 18, manifest = "./src/main/AndroidManifest.xml")
//@RunWith(RobolectricTestRunner.class)
//public class PlayerProfileApiDataProviderTest implements OnDataReceivedListener {
//
//    MainActivity activity;
//
//    @Before
//    public void setup()  {
//        // To redirect Robolectric to stdout
//        System.setProperty("robolectric.logging", "stdout");
//
//        activity = Robolectric.buildActivity(MainActivity.class).create().get();
//    }
//
//    @Test
//    public void testGetCountriesApiReturnsAllCountries() throws Exception {
//
//        PlayerProfileApiDataProvider playerProfileApiDataProvider =
//                                         new PlayerProfileApiDataProvider(activity, this);
//
//        assertNotNull(playerProfileApiDataProvider);
//
//        playerProfileApiDataProvider.getCountryList();
//    }
//
//    @Override
//    public void onDataFetched(Object obj) {
//
//        assertNotNull(obj);
//
//        ApiResponseEntity apiResponseEntity = (ApiResponseEntity)obj;
//
//        assertNotNull(apiResponseEntity);
//
//        assertTrue(apiResponseEntity.status.equalsIgnoreCase("success"));
//   }
//}