package io.oconnormi.ddfadm

import io.oconnormi.ddfadm.cli.Ddfadm
import picocli.CommandLine

class Main {
    static void main(String[] args) {
        CommandLine.call(new Ddfadm(), args)
    }
}
