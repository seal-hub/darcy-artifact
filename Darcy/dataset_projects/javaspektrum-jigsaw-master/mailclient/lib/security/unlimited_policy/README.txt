
   Unlimited Strength Java(TM) Cryptography Extension Policy Files
   for the Java(TM) Platform, Standard Edition Runtime Environment

                               README

----------------------------------------------------------------------
CONTENTS
----------------------------------------------------------------------

     o Introduction
     o Additional Documentation
     o Installation Instructions


----------------------------------------------------------------------
Introduction
----------------------------------------------------------------------

The Oracle JCE architecture allows for configurable flexible cryptographic
strengths via its jurisdiction policy files.  Due to the import control
restrictions of some countries, the default version of the JCE policy
files in the Java Runtime Environment, or JRE(TM), environment allows for
"strong" but limited cryptography.

The jurisdiction policy files in this directory are drop-in replacements
for the default files, and remove the default restrictions on
cryptographic strengths.  If allowed by local laws, users can replace
the default JCE policy files with these and have unlimited strength
cryptography.

JCE for Java SE has been through the U.S. export review process.  The JCE
framework, along with the various JCE providers that come standard with
it (SunJCE, SunEC, SunPKCS11, SunMSCAPI, etc), are exportable from the
United States.

You are strongly advised to consult your export/import control counsel
or attorney to determine the exact requirements for your environment,
and whether you may use these policy files.


----------------------------------------------------------------------
Additional Documentation
----------------------------------------------------------------------

The following documents may also be of interest to you:

    o  The Java(TM) Cryptography Architecture (JCA) Reference Guide at:

       http://docs.oracle.com/javase/9/docs/technotes/guides/security

    o  The Java SE Security web site has more information about JCE,
       plus additional information about the Java SE Security Model.

       Please see:

       http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136007.html


----------------------------------------------------------------------
Installation Instructions
----------------------------------------------------------------------

This directory contains the following files:

        README.txt                   This file
        local_policy.jar             Unlimited strength local policy file
        US_export_policy.jar         Unlimited strength US export policy file

Simply replace the limited strength policy jar files with the jars in this
directory:

        % cp *.jar ..                      [Unix]
        % copy *.jar ..                    [Windows]

In case you later decide to revert to the original "strong" but limited
policy versions, you should first make a copy of the original JCE policy
files (US_export_policy.jar and local_policy.jar).
