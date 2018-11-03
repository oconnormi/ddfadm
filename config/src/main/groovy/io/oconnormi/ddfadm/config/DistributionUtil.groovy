package io.oconnormi.ddfadm.config

import java.nio.file.Path
import java.nio.file.Paths

class DistributionUtil {
    static final String BIN_DIR = 'bin'
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
     */
    Path systemPropertiesPath(Path home, Version baseVersion) {
        if (baseVersion.isGreaterThan(LEGACY_PROPS_VERSION)) {
            return home.resolve(ETC_DIR).resolve(SYS_PROPS)
        }

        return home.resolve(ETC_DIR).resolve(LEGACY_SYS_PROPS)
    }

    /**
     * Determines the path to the main system log file based on path to the distributions home directory
     * and the name of the distribution.
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
     */
    Version baseVersion(Path home) {
        Path platformPath = home.resolve(SYSTEM_DIR).resolve(PLATFORM_PATH)
        def versionMatcher = new FileNameByRegexFinder().getFileNames(platformPath.toString(), /\d+\.\d+\.\d+.*/)
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
}
