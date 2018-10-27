package io.oconnormi.ddfadm.cli.commands

import java.util.concurrent.Callable
import picocli.CommandLine.Command
import picocli.CommandLine.Option

/**
* BootCommand is responsible for wiring up the various tasks required for starting ddf based systems
**/

@Command(
  description = "Bootstrap a ddf-based system",
  name = "boot",
  mixinStandardHelpOptions = true,
)
class BootCommand implements Callable<Void> {
  
  @Option(
    names = ["-s", "--disable-security"],
    description = "Disable security manager"
  )
  private boolean disableSecurityManager = false

  @Option(
    names = ["p", "--https-port"],
    description = "Set https port for ddf instance"
  )
  private int httpsPort = 8993

  @Option(
    names = ["P", "--http-port"],
    description = "Set http port for ddf instance"
  )
  private int httpPort = 8181

  @Option(
    names = ["--ssh-port"],
    description = "Set ssh port for ddf instance"
  )
  private int sshPort = 8101

  @Option(
    names = ["--solr-port"],
    description = "set solr port for ddf instance"
  )
  private int solrPort = 8994

  @Option(
    names = ["--profile"],
    description = "Installation profile"
  )
  private String installProfile

  @Option(
    names = ["-H", "--hostname"],
    description = "Hostname used by the system"
  )
  private String hostname

  @Option(
    names = ["-d", "--debug"],
    description = "Enables karaf debug mode"
  )
  private boolean debugMode = false

  @Option(
    names = ["-i", "--interactive"],
    description = "Start a client session after the system boots"
  )
  private boolean clientMode = false


  @Override
  public Void call() throws Exception {
    println "Not Implemented Yet!"
  }
}
