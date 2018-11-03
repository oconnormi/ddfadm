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
    File binDir
    File startScript
    File stopScript
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
        given: "a distribution"
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when: "getting the log file path for a distribution named 'foo'"
        def logPath = distributionUtil.logPath(distDir.toPath(), "foo")

        then: "the log should be in the log directory and be named after the distribution"
        logPath.getParent().toFile() == logDir
        logPath.toFile().getName() == "foo.log"
    }

    def "handle finding the base version containing a snapshot qualifier"() {
        given: "a distribution with a SNAPSHOT DDF platform version"
        createDistribution(baseDir, snapshotVersion, SYSTEM_PROPERTIES_NAME)

        when: "getting the base version"
        def baseVersion = distributionUtil.baseVersion(distDir.toPath())

        then: "the base version should be the snapshot version with the SNAPSHOT qualifier omitted"
        with(baseVersion) {
            major == 2
            minor == 3
            patch == 4
        }
    }

    def "handle finding distribution version"() {
        given: "a distribution"
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when: "getting the version of the distribution"
        def distVersion = distributionUtil.distroVersion(distDir.toPath())

        then: "the version should be read from the Version.txt file"
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

    def "handle missing system properties file"() {
        given: "a distribution without a system properties file"
        createDistribution(baseDir, testVersion,  "foo.properties")

        with(version) {
            isGreaterThan(_) >> false
        }

        when: "getting the path to the system properties file"
        distributionUtil.systemPropertiesPath(distDir.toPath(), version)

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    def "handle missing ddf platform version"() {
        given: "a distribution without a version of DDF platform"
        createDistribution(baseDir, "foo", SYSTEM_PROPERTIES_NAME)

        when: "getting the base version"
        distributionUtil.baseVersion(distDir.toPath())

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    def "handle finding path to distribution start script"() {
        given: "a distribution"
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when: "getting the path to the start script"
        def script = distributionUtil.startPath(distDir.toPath())

        then:
        script.toFile() == startScript
    }

    def "handle missing start script"() {
        given: "a distribution with a missing start script"
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)
        startScript.delete()

        when: "getting the path to the start script"
        distributionUtil.startPath(distDir.toPath())

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    def "handle finding path to distribution stop script"() {
        given:
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)

        when:
        def script = distributionUtil.stopPath(distDir.toPath())

        then:
        script.toFile() == stopScript
    }

    def "handle missing stop script"() {
        given: "a distribution with a missing stop script"
        createDistribution(baseDir, testVersion, SYSTEM_PROPERTIES_NAME)
        stopScript.delete()

        when: "getting the path to the stop script"
        distributionUtil.stopPath(distDir.toPath())

        then: "an exception should be thrown"
        thrown MalformedDistributionException
    }

    private void createDistribution(TemporaryFolder baseDir, String version, String systemPropsName) {
        distDir = baseDir.newFolder("dist")
        binDir = Files.createDirectory(Paths.get(distDir.toString(), "bin")).toFile()
        startScript = Files.createFile(Paths.get(binDir.toString(), "start")).toFile()
        stopScript = Files.createFile(Paths.get(binDir.toString(), "stop")).toFile()
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
