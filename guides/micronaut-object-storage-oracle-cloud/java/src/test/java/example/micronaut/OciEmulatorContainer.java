package example.micronaut;

import com.oracle.bmc.Region;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Supplier;

import static io.micronaut.objectstorage.oraclecloud.OracleCloudStorageConfiguration.PREFIX;

public class OciEmulatorContainer extends GenericContainer<OciEmulatorContainer> {

    public static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("cameritelabs/oci-emulator");
    public static final int DEFAULT_PORT = 12000;
    public static final String DEFAULT_NAMESPACE = "testtenancy";

    public OciEmulatorContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
    }

    @Override
    protected void configure() {
        super.configure();

        addExposedPort(DEFAULT_PORT);
    }

    public String getEndpoint() {
        return "http://127.0.0.1:" + getMappedPort(DEFAULT_PORT);
    }

    public String getCompartmentId() {
        return "ocid1.compartment.oc1..testcompartment";
    }

    public String getTenantId() {
        return "ocid1.tenancy.oc1..testtenancy";
    }

    public String getUserId() {
        return "ocid1.user.oc1..testuser";
    }

    public String getFingerprint() {
        return "50:a6:c1:a1:da:71:57:dc:87:ae:90:af:9c:38:99:67";
    }

    public char[] getPassphraseCharacters() {
        return new char[0];
    }

    public Supplier<InputStream> getPrivateKeySupplier() {
        return () -> {
            try {
                return Files.newInputStream(Paths.get("src/test/resources/key.pem"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public Region getRegion() {
        return Region.SA_SAOPAULO_1;
    }
}
