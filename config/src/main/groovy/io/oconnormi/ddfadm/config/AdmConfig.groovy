package io.oconnormi.ddfadm.config

import java.nio.file.Path

class AdmConfig {

    private final DistributionUtil distUtil

    private final Path home
    private String name
    private Version baseVersion
    private Path log
    private Path systemProperties
    private Path start
    private Path stop

     /**
     * Holds configuration information specific to ddfadm. This is primarily related to the version
     * and layout information for the distribution that is being managed by ddfadm
     *
     * @param home path to the distribution home directory
     * @throws MalformedDistributionException when any of the required items in a distribution are missing
     */
    AdmConfig(Path home) throws MalformedDistributionException {
       this(home, new DistributionUtil())
    }

    AdmConfig(Path home, DistributionUtil distributionUtil) throws MalformedDistributionException {
        this.home = home
        this.distUtil = distributionUtil
        init()
    }

    /**
     * Gets the path to the distribution home directory
     *
     * @return path to distribution home
     */
    Path getHome() {
        home
    }

    /**
     * Gets the path to the distribution log
     *
     * @return path to distribution log
     */
    Path getLog() {
        log
    }

    /**
     * Gets the version of DDF platform used in the distribution
     *
     * @return version of ddf platform
     */
    Version getBaseVersion() {
        baseVersion
    }

    /**
     * Gets the name of the distribution
     *
     * @return distribution name
     */
    String getName() {
        name
    }

    /**
     * Gets the path to the distribution system properties file
     *
     * @return path to system properties
     */
    Path getSystemProperties() {
        systemProperties
    }

    /**
     * Gets the path to the distribution start script
     *
     * @return path to start script
     */
    Path getStart() {
        start
    }

    /**
     * Gets the path to the distribution stop script
     *
     * @return path to stop script
     */
    Path getStop() {
        stop
    }

    private void init() throws MalformedDistributionException {
        discoverBaseVersion()
        discoverBinScripts()
        discoverSystemProperties()
        discoverName()
        discoverLog()
    }

    private void discoverName() {
        this.name = distUtil.distroName(systemProperties)
    }

    private void discoverSystemProperties() throws MalformedDistributionException {
        this.systemProperties = distUtil.systemPropertiesPath(home, baseVersion)
    }

    private void discoverBaseVersion() throws MalformedDistributionException {
        this.baseVersion = distUtil.baseVersion(home)
    }

    private void discoverLog() {
        this.log = distUtil.logPath(home, name)
    }

    private void discoverBinScripts() throws MalformedDistributionException {
        this.start = distUtil.startPath(home)
        this.stop = distUtil.stopPath(home)
    }
}
