package com.github.linsolas.go2space;


import com.github.amsacode.predict4java.TLE;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest {

    private static final String JSON_CONTENT = "\"catnum\":28375,\"name\":\"AO-51 [+]\",\"setnum\":364,\"year\":9," +
            "\"refepoch\":105.6639197,\"incl\":98.0551,\"raan\":118.9086,\"eccn\":0.0084159,\"argper\":315.8041," +
            "\"meanan\":43.6444,\"meanmo\":14.4063845,\"drag\":3.0E-8,\"nddot6\":0.0,\"bstar\":1.3761000000000001E-5," +
            "\"orbitnum\":25195,\"epoch\":9105.6639197,\"xndt2o\":9.090256520803799E-14,\"xincl\":1.7113843433722922," +
            "\"xnodeo\":2.07534657893693,\"eo\":0.0084159,\"omegao\":5.511821336297426,\"xmo\":0.7617384800574133," +
            "\"xno\":0.06285971070831925,\"deepspace\":false";

    private static final String JSON = "{" + JSON_CONTENT + "}";

    private static final String JSON_WITH_SOURCE = "{" + JSON_CONTENT + ",\"source\":\"LEO\"}";

    @Test
    public void check_json_output_for_TLE() throws IOException {
        InputStream is = JsonUtilsTest.class.getResourceAsStream("/LEO.txt");
        final List<TLE> tles = TLE.importSat(is);
        TLE tle = tles.get(0);
        assertThat(JsonUtils.toJson(tle)).isEqualTo(JSON);
    }

    @Test
    public void check_json_output_for_TLE_including_source() throws IOException {
        InputStream is = JsonUtilsTest.class.getResourceAsStream("/LEO.txt");
        final List<TLE> tles = TLE.importSat(is);
        TLE tle = tles.get(0);
        assertThat(JsonUtils.toJson(tle, "LEO")).isEqualTo(JSON_WITH_SOURCE);
    }


}
