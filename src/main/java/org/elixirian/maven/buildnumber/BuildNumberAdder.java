/**
 *
 */
package org.elixirian.maven.buildnumber;

import static org.elixirian.kommonlee.util.Objects.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.elixirian.kommonlee.collect.immutable.ImmutableList;
import org.elixirian.kommonlee.io.ByteArrayConsumingContainer;
import org.elixirian.kommonlee.io.DataConsumers;
import org.elixirian.kommonlee.io.DataProducers;
import org.elixirian.kommonlee.io.IoCommonConstants;
import org.elixirian.kommonlee.nio.util.NioUtil;

/**
 * <pre>
 *     ___  _____                                _____
 *    /   \/    /_________  ___ ____ __ ______  /    /   ______  ______
 *   /        / /  ___ \  \/  //___// //     / /    /   /  ___ \/  ___ \
 *  /        \ /  _____/\    //   //   __   / /    /___/  _____/  _____/
 * /____/\____\\_____/   \__//___//___/ /__/ /________/\_____/ \_____/
 * </pre>
 * 
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-13)
 */
@Mojo(
// mvn buildnumber:for-deploy
name = "for-deploy", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class BuildNumberAdder extends AbstractMojo
{
  @Parameter
  private File versionInfoFile;

  @Parameter(defaultValue = "version")
  private String versionKey;

  @Parameter
  private File buildNumberSourceFile;

  @Parameter(defaultValue = "${project.build.outputDirectory}")
  private File outputDirectory;

  @Parameter(defaultValue = "buildNumber.ini")
  private String outputBuildNumberFileName;

  @Parameter(defaultValue = "false")
  private boolean skipIfBuildNumberSourceFileNotFound;

  @Parameter(defaultValue = "true")
  private boolean dontRunIfOutputBuildNumberFileAlreadyExists;

  @Parameter(required = false)
  private List<String> parentsMustContainAnyOf;

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject mavenProject;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    final Log logger = getLog();

    if (!versionInfoFile.exists())
    {
      throw new MojoExecutionException("versionInfoFile does not exist. versionInfoFile: " + versionInfoFile.getPath());
    }
    if (!versionInfoFile.isFile())
    {
      throw new MojoExecutionException("versionInfoFile is not a file! versionInfoFile: " + versionInfoFile.getPath());
    }

    if (null == buildNumberSourceFile)
    {
      if (skipIfBuildNumberSourceFileNotFound)
      {
        return;
      }
      throw new MojoExecutionException(
          "buildNumberSourceFile is not specified. <buildNumberSourceFile>/path/to/build-number-source-file</buildNumberSourceFile>");
    }
    if (!buildNumberSourceFile.exists())
    {
      if (skipIfBuildNumberSourceFileNotFound)
      {
        return;
      }
      throw new MojoExecutionException("buildNumberSourceFile does not exist. buildNumberSourceFile: "
          + buildNumberSourceFile.getPath());
    }
    if (!buildNumberSourceFile.isFile())
    {
      throw new MojoExecutionException("buildNumberSourceFile is not a file! buildNumberSourceFile: "
          + buildNumberSourceFile.getPath());
    }

    if (!outputDirectory.exists())
    {
      throw new MojoExecutionException("outputDirectory does not exist. outputDirectory: " + outputDirectory.getPath());
    }
    if (!outputDirectory.isDirectory())
    {
      throw new MojoExecutionException("outputDirectory is not a directory! outputDirectory: "
          + outputDirectory.getPath());
    }

    final File outputFile = new File(outputDirectory, outputBuildNumberFileName);
    if (outputFile.exists() && dontRunIfOutputBuildNumberFileAlreadyExists)
    {
      logger.info("outputFile (" + outputFile.getPath()
          + ") already exists and dontRunIfOutputBuildNumberFileAlreadyExists parameter value is "
          + dontRunIfOutputBuildNumberFileAlreadyExists);
      return;
    }

    if (null == parentsMustContainAnyOf || parentsMustContainAnyOf.isEmpty())
    {
      logger.info("parentsMustContainAnyOf is null so ignore this option.");
    }
    else
    {
      final String parentFolder = mavenProject.getBasedir()
          .getParent();

      boolean found = false;
      for (final String parentsMustContain : parentsMustContainAnyOf)
      {
        if (parentFolder.contains(parentsMustContain))
        {
          logger.info("parentFolder (" + parentFolder + ") contains one of 'parentsMustContainAnyOf' values ("
              + parentsMustContain + " of " + parentsMustContainAnyOf + ") so keep running!.");
          found = true;
          break;
        }
      }
      if (!found)
      {
        logger.info("parentFolder (" + parentFolder + ") does NOT contain any of 'parentsMustContainAnyOf' values ("
            + parentsMustContainAnyOf + ") so stop running this plugin!.\n"
            + "Running this buildnumber plugin has been cancelled!!!");
        return;
      }
    }

    final Properties versionInfo = new Properties();
    FileInputStream inStream = null;
    try
    {
      inStream = new FileInputStream(versionInfoFile);
      versionInfo.load(inStream);
    }
    catch (final FileNotFoundException e)
    {
      throw new MojoExecutionException("versionInfoFile is not a file! versionInfoFile: " + versionInfoFile.getPath(),
          e);
    }
    catch (final IOException e)
    {
      throw new MojoExecutionException("Failed to read the versionInfoFile. versionInfoFile: "
          + versionInfoFile.getPath(), e);
    }
    finally
    {
      NioUtil.closeQuietly(inStream);
    }

    final ByteArrayConsumingContainer byteArrayConsumingContainer = DataConsumers.newByteArrayConsumingContainer();
    NioUtil.readFile(buildNumberSourceFile, IoCommonConstants.BUFFER_SIZE_8Ki, byteArrayConsumingContainer);

    final String versionNumber = byteArrayConsumingContainer.toString();
    final NumberBasedVersion numberBasedVersion = NumberBasedVersion.newInstance(versionNumber);

    if (numberBasedVersion.isEmpty())
    {
      throw new MojoExecutionException("No version info is found in the file: " + buildNumberSourceFile.getPath());
    }

    final String projectVersionString = (String) versionInfo.get(versionKey);

    logger.info("[BuildNumberAdder] projectVersionString: " + projectVersionString);

    final NumberBasedVersion projectVersion = NumberBasedVersion.newInstance(projectVersionString);

    logger.info("[BuildNumberAdder] projectVersion: " + projectVersion);

    final int takeHowMany = numberBasedVersion.length() - 1;

    final ImmutableList<Integer> projectVersionNumbers = projectVersion.getVersionNumbers()
        .subList(0, takeHowMany);
    final ImmutableList<Integer> projectVersionNumbersFromBuildNumberFile = numberBasedVersion.getVersionNumbers()
        .subList(0, takeHowMany);

    final int lastNumber =
      equal(projectVersionNumbers, projectVersionNumbersFromBuildNumberFile) ? numberBasedVersion.getVersionNumbers()
          .get(takeHowMany)
          .intValue() : -1;

    final int buildNumber = lastNumber + 1;

    logger.info("[BuildNumberAdder] buildNumber: " + buildNumber);

    final byte[] buildNumberBytes = String.valueOf(buildNumber)
        .getBytes();

    NioUtil.writeFile(outputFile, IoCommonConstants.BUFFER_SIZE_8Ki,
        DataProducers.newSimpleByteArrayProducer(buildNumberBytes));

    final StringBuilder stringBuilder = new StringBuilder();
    for (final Integer n : projectVersionNumbers)
    {
      stringBuilder.append(n)
          .append(".");
    }

    final String newVersionForSource = stringBuilder.append(buildNumber)
        .toString();

    logger.info("[BuildNumberAdder] newVersionForSource: " + newVersionForSource);

    NioUtil.writeFile(buildNumberSourceFile, IoCommonConstants.BUFFER_SIZE_8Ki,
        DataProducers.newSimpleByteArrayProducer(newVersionForSource.getBytes()));

  }
}
