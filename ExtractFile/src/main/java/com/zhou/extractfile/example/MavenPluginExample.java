package com.zhou.extractfile.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Created by Zhou Yibing on 2015/12/11.
 * @goal print
 * @phase process-sources
 */
public class MavenPluginExample extends AbstractMojo{

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("This is first maven plugin example.");
    }
}
