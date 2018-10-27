package io.oconnormi.ddfadm.cli.commands

import java.util.concurrent.Callable
import picocli.CommandLine.Command
import picocli.CommandLine.Option

/**
* InitCommand is responsible for geneating a ddfadm environment configuration.
* This will only contain the following details:
*   DDF Home Directory
*   DDF Distribution Name
*   Distribution Version
* The primary use case for this would be using ddfadm to manage a permanent 
* installation as it will allow some options to be ommitted from the "ddfadm boot" command
**/
@Command(
  description = "Initializes a ddfadm environment configuration. This is intended for use with permanent ddf installations",
  name = "init",
  mixinStandardHelpOptions = true
)
class InitCommand implements Callable<Void> {
  
  // TODO: decide where ddfadm config should be stored, if it should be any location specified, what options can be manually provided, etc...

  @Override
  public Void call() throws Exception {
    println "Not Implemented Yet!"
    return null
  }
}
