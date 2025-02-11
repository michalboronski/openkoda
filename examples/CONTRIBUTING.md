# Contribution Guidelines - Template apps

## Commit Message

The expected structure for git commit messages is:

```
 [<feature short name>] <Change subject>

Commit message body.
This section explains how things worked before this commit, what has changed, and how things work now.

```

* Start with a unique feature/fix short name, preferable matching with a branch originally worked on
* Keep the commit subject text brief, 72 chars max
* Start subject with a capital letter
* No period at the end of subject line
* Use imperative statements in the subject line ("Fix", "Add", "Change" instead of "Fixed", "Added", "Changed")
* Add an empty line after the subject
* Add line breaks to the description section to make it more readable


## Source Code Management
1.  Create a feature branch ``<major.minor>/<short name>`` from ``master``
    *if multiple devs work on a single feature, each dev should create it's own branch from that main feature branch*
    
2.  Set main ``<version></version>`` of Openkoda core that should be used in a template/application pom. Template/Application ``major`` and ``minor`` version should always match a Openkoda core version used as a baseline. Template's ``build number`` may be freely incremented over development/releasing. When switching template app to a new Openkoda release, update it's ``major`` and ``minor`` version numbers. For instance:
    ```
    <parent>
        <groupId>com.openkoda</groupId>
        <artifactId>openkoda-app</artifactId>
        <version>1.5.2</version> <!-- using 1.5 release with some minor fixes, changes etc. -->
    </parent>
    <groupId>com.openkoda.example</groupId>
    <artifactId>timelog</artifactId>
    <version>1.5.5-SNAPSHOT</version>

   When upgrading to next Openkoda minor release :

    <parent>
        <groupId>com.openkoda</groupId>
        <artifactId>openkoda-app</artifactId>
        <version>1.6.0</version>
    </parent>
    <groupId>com.openkoda.example</groupId>
    <artifactId>timelog</artifactId>
    <version>1.6.0-SNAPSHOT</version>    

3.  Create a PR to ``master`` once changes are ready and particular template app is ready to be released. Template app version in ``pom.xml`` should **NOT** be SNAPSHOT.
    1. assign reviewers
    2. add overall scope of changes in PR's description
    3. merge into master only after reviewers acceptance
    4. Deploy template version to [Artifactory](https://dev.codedose.com:7443/artifactory)
    
    *TODO : create apropriate Jenkins jobs*

4.  If the feature branch should still stay undeleted, increment it's version to next SNAPSHOT version
