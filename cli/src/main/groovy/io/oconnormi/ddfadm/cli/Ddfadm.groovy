package io.oconnormi.ddfadm.cli

import java.util.concurrent.Callable
import picocli.CommandLine.Command
import picocli.CommandLine.Option

/**
* Base command for ddfadm, wires up all subcommands
**/
@Command(
  description = "Utility for automating setup/configuration for ddf based systems",
  name = "ddfadm",
  mixinStandardHelpOptions = true
)
class Ddfadm implements Callable<Void> {

  @Override
  public Void call() throws Exception {
    println "Nothing to see here..."
  }
}
