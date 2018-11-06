package io.oconnormi.ddfadm.config

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class AdmConfigSpec extends Specification {

    Path base = Paths.get("base")

    def "handle a valid distribution"() {
        given: "a valid distribution"
        DistributionUtil distributionUtil = Mock(DistributionUtil) {
            systemPropertiesPath(_, _) >> Paths.get("base", "etc", "system.properties")
            startPath(_) >> Paths.get("base", "bin", "start")
            stopPath(_) >> Paths.get("base", "bin", "stop")
            distroName(_) >> "foo"
            logPath(_, _) >> Paths.get("base", "data", "log", "foo.log")
        }

        when: "the configuration is initialized"
        AdmConfig admConfig = new AdmConfig(base, distributionUtil)

        then: "the configuration should be populated with the correct information from the distribution"
        admConfig.stop == Paths.get("base", "bin", "stop")
        admConfig.start == Paths.get("base", "bin", "start")
        admConfig.name == "foo"
        admConfig.log == Paths.get("base", "data", "log", "foo.log")
        admConfig.systemProperties == Paths.get("base", "etc", "system.properties")
        admConfig.home == base
    }

    def "handle a distributon with a missing start script"() {
        given: "a distribution without a start script"
        DistributionUtil distUtil = Mock(DistributionUtil) {
            startPath(_) >> { throw new MalformedDistributionException() }
        }

        when: "the configuration is initialized"
        new AdmConfig(base, distUtil)

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    def "handle a distribution with a missing stop script"() {
        given: "a distribution without a stop script"
        DistributionUtil distUtil = Mock(DistributionUtil) {
            stopPath(_) >> { throw new MalformedDistributionException() }
        }

        when: "the configuration is initialized"
        new AdmConfig(base, distUtil)

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    def "handle a distribution with no base version"() {
        given: "a distribution with no base version"
        DistributionUtil distUtil = Mock(DistributionUtil) {
            baseVersion(_) >> { throw new MalformedDistributionException() }
        }

        when: "the configuration is initialized"
        new AdmConfig(base, distUtil)

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    def "handle a distribution with no system properties"() {
        given: "a distribution with no system properties"
        DistributionUtil distUtil = Mock(DistributionUtil) {
            systemPropertiesPath(_, _) >> { throw new MalformedDistributionException() }
        }

        when: "the configuration is initialized"
        new AdmConfig(base, distUtil)

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }
}
