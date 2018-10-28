package io.oconnormi.ddfadm.config
/**
 * Represents a version number of format Major.minor.patch
 *
 */
class Version {
    private final int major
    private final int minor
    private final int patch

    Version(int major, int minor, int patch) {
        this.major = major
        this.minor = minor
        this.patch = patch
    }

    Version(String version) {

        def cleanedVersion = version.split('[-_]')[0]
        def versionList = cleanedVersion.tokenize('.')
        if (versionList.size() != 3) {
            throw new IllegalArgumentException("Version does not match the required format of Major.minor.patch<-OPTONAL_QUALIFIER>")
        }
        this.major = versionList[0] as Integer
        this.minor = versionList[1] as Integer
        this.patch = versionList[2] as Integer
    }

    int getMajor() {
        major
    }

    int getMinor() {
        minor
    }

    int getPatch() {
        patch
    }

    /**
     * Compares with another version and returns true
     * if current version is greater than the supplied version
     * @param version to compare against
     * @return boolean
     */
    boolean isGreaterThan(Version version) {
        if (major < version.major) {
            return false
        }

        if (minor < version.minor) {
            return false
        }

        if (patch < version.patch) {
            return false
        }

        return true
    }

    /**
     * Compares with another version and returns true
     * if current version is less than the supplied version
     * @param version to compare against
     * @return boolean
     */
    boolean isLessThan(Version version) {
        if (major > version.major) {
            return false
        }

        if (minor > version.minor) {
            return false
        }

        if (patch > version.patch) {
            return false
        }

        return true
    }
}
