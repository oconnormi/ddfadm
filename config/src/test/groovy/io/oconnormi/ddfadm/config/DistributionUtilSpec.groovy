package io.oconnormi.ddfadm.config

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class DistributionUtilSpec extends Specification {

    private static final String LEGACY_SYSTEM_PROPERTIES_NAME = "system.properties"
    private static final String SYSTEM_PROPERTIES_NAME = "custom.system.properties"
    private static final String testVersion = "1.2.3"
    private static final String snapshotVersion = "2.3.4-SNAPSHOT"
    private static final String testDistVersion = "5.6.7"
    private static final String testDistName = "foo"

    @Rule
    TemporaryFolder baseDir = new TemporaryFolder()

    DistributionUtil distributionUtil

    File distDir
    File etcDir
    File dataDir
    File logDir
    File systemDir
    File platformFile
    File baseVersionFile
    File sysPropsFile
    File versionFile

    Version version


    def setup() {
        baseDir.create()
        distributionUtil = new DistributionUtil()
        version = Mock(Version)
    }

    @Test
    def "handle legacy system properties location"() {
        given:
        createDistribution(baseDir, testVersion, LEGACY_SYSTEM_PROPERTIES_NAME)

        with(version) {
            isGreaterThan(_) >> false
        }

        when: "the base version is less than ${DistributionUtil.LEGACY_PROPS_VERSION}"
        def systemPropsPath = distributionUtil.systemPropertiesPath(distDir.toPath(), version)

        then: "the system properties file should be located under etc and called ${LEGACY_SYSTEM_PROPERTIES_NAME}"
        systemPropsPath.getParent().toFile() == etcDir
        systemPropsPath.toFile().getName() == LEGACY_SYSTEM_PROPERTIES_NAME
    }

    def "handle non-legacy system properties location"() {
        given: "a distribution with a non-legacy version"
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        with(version) {
            isGreaterThan(_) >> true
        }

        when: "the base version is greater than ${DistributionUtil.LEGACY_PROPS_VERSION}"
        def systemPropsPath = distributionUtil.systemPropertiesPath(distDir.toPath(), version)

        then: "the system properties file should be located under etc and called ${SYSTEM_PROPERTIES_NAME}"
        systemPropsPath.getParent().toFile() == etcDir
        systemPropsPath.toFile().getName() == SYSTEM_PROPERTIES_NAME
    }

    def "handle finding log path"() {
        given:
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when:
        def logPath = distributionUtil.logPath(distDir.toPath(), "foo")

        then:
        logPath.getParent().toFile() == logDir
        logPath.toFile().getName() == "foo.log"
    }

    def "handle finding the base version containing a snapshot qualifier"() {
        given:
        createDistribution(baseDir, snapshotVersion, SYSTEM_PROPERTIES_NAME)

        when:
        def baseVersion = distributionUtil.baseVersion(distDir.toPath())

        then:
        with(baseVersion) {
            major == 2
            minor == 3
            patch == 4
        }
    }

    def "handle finding distribution version"() {
        given:
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when:
        def distVersion = distributionUtil.distroVersion(distDir.toPath())

        then:
        with(distVersion) {
            major == 5
            minor == 6
            patch == 7
        }
    }

    def "handle getting distro name"() {
        given:
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when:
        def distName = distributionUtil.distroName(sysPropsFile.toPath())

        then:
        distName == testDistName
    }


    private void createDistribution(TemporaryFolder baseDir, String version, String systemPropsName) {
        distDir = baseDir.newFolder("dist")
        etcDir = Files.createDirectory(Paths.get(distDir.toString(), "etc")).toFile()
        dataDir = Files.createDirectory(Paths.get(distDir.toString(), "data")).toFile()
        logDir = Files.createDirectory(Paths.get(dataDir.toString(), "log")).toFile()
        systemDir = Files.createDirectory(Paths.get(distDir.toString(), "system")).toFile()
        platformFile = Files.createDirectories(Paths.get(systemDir.toString(), DistributionUtil.PLATFORM_PATH)).toFile()
        baseVersionFile = Files.createDirectory(Paths.get(platformFile.toString(), version)).toFile()
        sysPropsFile = Files.createFile(Paths.get(etcDir.toString(), systemPropsName)).toFile()
        versionFile = Files.createFile(Paths.get(distDir.toString(), "Version.txt")).toFile()
        versionFile.write(testDistVersion)
        sysPropsFile.write("${DistributionUtil.DISTRO_NAME_PROPERTY} = ${testDistName}")
    }
}
