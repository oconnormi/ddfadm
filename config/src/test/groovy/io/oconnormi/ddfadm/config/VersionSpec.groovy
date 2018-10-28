package io.oconnormi.ddfadm.config

import spock.lang.Specification
import spock.lang.Unroll

class VersionSpec extends Specification {

    def "handling basic version string"() {
        given: "a basic version number containing a major, minor, and patch version"
        def versionString = '1.2.3'

        when: "converting to a Version"
        def version = new Version(versionString)

        then: "the Version should contain each part"
        with(version) {
            major == 1
            minor == 2
            patch == 3
        }
    }

    def "handling version strings with - delimited qualifier"() {
        given: "a version string containing a dash qualifier"
        def versionString = '1.2.3-SNAPSHOT'

        when: "converting to a Version"
        def version = new Version(versionString)

        then: "the version should contain each part, and the qualifier should be ignored"
        with(version) {
            major == 1
            minor == 2
            patch == 3
        }
    }

    def "handling version strings with underscores delimited qualifiers" () {
        given: "a version string containing a underscore qualifier"
        def versionString = '1.2.3_foobar'

        when: "converting to a Version"
        def version = new Version(versionString)

        then: "the version should contain each part and the qualifier should be ignored"
        with(version) {
            major == 1
            minor == 2
            patch == 3
        }
    }

    def "handling version strings with extra digits"() {
        given: "a version string containing more than 3 . delimiters"
        def versionString = '1.2.3.4'

        when: "converting to a Version"
        def version = new Version(versionString)

        then: "an IllegalArgumentException should be thrown"
        thrown IllegalArgumentException
    }

    def "handling version strings with less than 3 digits"() {
        given: "a version string containing less than 3 . delimiters"
        def versionString = '1.2'

        when: "converting to a Version"
        def version = new Version(versionString)

        then: "an IllegalArgumentException should be thrown"
        thrown IllegalArgumentException
    }

    def "handling creating versions from 3 integers"() {
        when: "a version is created from 3 integers"
        def version = new Version(1, 2, 3)

        then: "the Version should contain each part"
        with(version) {
            major == 1
            minor == 2
            patch == 3
        }
    }

    @Unroll
    def "handling version #name with #segment version #target than supplied version"() {
        given:
        def sourceVersion = new Version(1, 2, 3)

        when:
        def result = sourceVersion.invokeMethod(name, testVersion)

        then:
        result == desired

        where:
        name | segment | target | testVersion || desired
        'isGreaterThan' | 'patch' | 'larger' | new Version(1,2, 2) || true
        'isGreaterThan' | 'minor' | 'larger' | new Version(1, 1, 3) || true
        'isGreaterThan' | 'major' | 'larger' | new Version(0, 2, 3) || true
        'isGreaterThan' | 'patch' | 'smaller' | new Version(1, 2, 4) || false
        'isGreaterThan' | 'minor' | 'smaller' | new Version(1, 3, 3) || false
        'isGreaterThan' | 'major' | 'smaller' | new Version(2, 2, 3) || false
        'isLessThan' | 'patch' | 'larger' | new Version(1, 2, 2) || false
        'isLessThan' | 'minor' | 'larger' | new Version(1, 1, 3) || false
        'isLessThan' | 'major' | 'larger' | new Version(0, 2, 3) || false
        'isLessThan' | 'patch' | 'smaller' | new Version(1, 2, 4) || true
        'isLessThan' | 'minor' | 'smaller' | new Version(1, 3, 3) || true
        'isLessThan' | 'major' | 'smaller' | new Version(2, 2, 3) || true
    }
}
