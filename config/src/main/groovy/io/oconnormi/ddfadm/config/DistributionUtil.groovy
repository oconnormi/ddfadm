package io.oconnormi.ddfadm.config

import java.nio.file.Path
import java.nio.file.Paths

class DistributionUtil {
    static final String BIN_DIR = 'bin'
    static final String START_SCRIPT = 'start'
    static final String STOP_SCRIPT = 'stop'
    static final String ETC_DIR = 'etc'
    static final String SYSTEM_DIR = 'system'
    static final String DATA_DIR = 'data'
    static final String LOG_DIR = "${DATA_DIR}/log"
    static final String LEGACY_SYS_PROPS = 'system.properties'
    static final String SYS_PROPS = 'custom.system.properties'
    static final String VERSION_FILE = 'Version.txt'
    static final String PLATFORM_PATH = 'ddf/platform/api/platform-api'
    static final String DISTRO_NAME_PROPERTY = "org.codice.ddf.system.branding"

    static final Version LEGACY_PROPS_VERSION = new Version(2, 3, 2)

    /**
     * Determines the path to the system properties file based on the distribution home directory
     * and the base version (base version is the version of ddf the distribution includes).
     * Depending on the version of ddf that the distribution is built on top of, the system properties file
     * has a different name. This method will ensure that the correct path is found
     *
     * @param home directory of the distribution
     * @param baseVersion of the distribution
     * @return path to system properties file
     * @throws MalformedDistributionException when the system properties file does not exist
     */
    Path systemPropertiesPath(Path home, Version baseVersion) throws MalformedDistributionException {
        Path sysProps
        if (baseVersion.isGreaterThan(LEGACY_PROPS_VERSION)) {
            sysProps = home.resolve(ETC_DIR).resolve(SYS_PROPS)
        } else {
            sysProps = home.resolve(ETC_DIR).resolve(LEGACY_SYS_PROPS)
        }

        if (!sysProps.toFile().exists()) {
            throw new MalformedDistributionException("System Properties file: ${sysProps.toString()} does not exist")
        }

        return sysProps
    }

    /**
     * Determines the path to the main system log file based on path to the distributions home directory
     * and the name of the distribution. Does nothing to ensure the existence of the log since it may not exist
     * until the system has been started
     *
     * @param home directory of the distribution
     * @param name of the distribution
     * @return path to the system log
     */
    Path logPath(Path home, String name) {
        return Paths.get(home.toString(), LOG_DIR, "${name}.log")
    }

    /**
     * Determines the base version of the system. More specifically this is the version of DDF that is
     * included in the system
     *
     * @param home directory of the distribution
     * @return Version of DDF included in the distribution
     * @throws MalformedDistributionException when the base version can not be determined
     */
    Version baseVersion(Path home) throws MalformedDistributionException {
        Path platformPath = home.resolve(SYSTEM_DIR).resolve(PLATFORM_PATH)
        def versionMatcher = new FileNameByRegexFinder().getFileNames(platformPath.toString(), /\d+\.\d+\.\d+.*/)
        if (versionMatcher.size() == 0) {
            throw new MalformedDistributionException("Unable to find a version of DDF platform located under: ${platformPath}")
        }
        String  versionFile = new File(versionMatcher.get(0)).getName()
        return new Version(versionFile)
    }

    /**
     * Determines the version of the distribution.
     *
     * @param home directory of the system
     * @return Version of the distribution
     */
    Version distroVersion(Path home) {
        String versionText = home.resolve(VERSION_FILE).toFile().readLines().get(0)
        return new Version(versionText)
    }

    /**
     * Determines the name of the distribution.
     *
     * @param systemProperties
     * @return
     */
    String distroName(Path systemProperties) {
        Properties properties = new Properties()
        properties.load(new BufferedInputStream(new FileInputStream(systemProperties.toFile())))
        return properties.get(DISTRO_NAME_PROPERTY)
    }

    /**
     * Determines the path to the start script for the distribution
     *
     * @param home directory of the distribution
     * @return path to the distribution start script
     * @throws MalformedDistributionException when start script is not found
     */
    Path startPath(Path home) throws MalformedDistributionException {
        Path startPath = home.resolve(BIN_DIR).resolve(START_SCRIPT)
        if (!startPath.toFile().exists()) {
            throw new MalformedDistributionException("No start script found at: ${startPath.toString()}")
        }
        return startPath
    }

    /**
     * Determines the path to the stop script for the distribution
     *
     * @param home directory of the distribution
     * @return the path to the distribution's stop script
     * @throws MalformedDistributionException when the stop script is not found
     */
    Path stopPath(Path home) throws MalformedDistributionException {
        Path stopPath = home.resolve(BIN_DIR).resolve(STOP_SCRIPT)
        if (!stopPath.toFile().exists()) {
            throw new MalformedDistributionException("No stop script found at: ${stopPath.toString()}")
        }
        return stopPath
    }
}
