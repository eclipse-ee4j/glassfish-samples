#!/bin/sh
#
# Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
#

#------------------------------------------------------
#-- BE SURE TO HAVE THE FOLLOWING IN YOUR SETTINGS.XML
#------------------------------------------------------
#
#    <servers>
#        <server>
#            <id>jvnet-nexus-staging</id>
#            <username>jvnet_id</username>
#            <password>password</password>
#        </server>
#    </servers>
#    <profiles>
#      <profile>
#        <id>release</id>
#        <properties>
#          <release.arguments>-Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80 -Dgpg.passphrase=glassfish</release.arguments>
#        </properties>
#        <activation>
#          <activeByDefault>false</activeByDefault>
#        </activation>
#      </profile>
#    </profiles>

# see the following URL for gpg issues
# https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven#HowToGeneratePGPSignaturesWithMaven-GenerateaKeyPair

# login to nexus at maven.java.net and release (Close) the artifact
# https://maven.java.net/index.html#stagingRepositories

# More information:
# https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-8.ReleaseIt
# http://aseng-wiki.us.oracle.com/asengwiki/display/GlassFish/Migrating+Maven+deployment+to+maven.java.net

ARGS=" $*"
# everything supplied as argument will be provided to every maven command.
# e.g to supply -Dmaven.skip.test or -Dmaven.repo.local=/path/to/repo

mvn -B -e release:prepare -DpreparationGoals="package" -DuseDefaultManifestFile=true
