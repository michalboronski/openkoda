# Contribution Guidelines - Openkoda Core

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
When working on a new release

1.  Create a feature branch ``<major.minor>`` from ``master``, for example `1.5`
    *if multiple devs work on a single feature, each dev should create it's own branch from that main feature branch*
    
2.  Set main ``<version></version>`` of Openkoda core to next SNAPSHOT minor version, for example 1.5.0-SNAPSHOT
3.  When developing features for this release branch, create separate feature branches from `<major.minor>`
4.  Create a PR to `<major.minor>` once changes are released. 
    1. Version in ``pom.xml`` should **NOT** be SNAPSHOT., for example 1.5.0
    2. assign reviewers
    3. add overall scope of changes in PR's description
    4. merge into master only after reviewers acceptance
    5. Deploy template version to [Artifactory](https://dev.codedose.com:7443/artifactory)
    6. version should increment to next SNAPSHOT build number, for example 1.5.1-SNAPSHOT
    *TODO : create appropriate Jenkins jobs*

5.  If the feature branch should still stay undeleted, increment it's version to next SNAPSHOT version
6.  Commiting bugfixes, minor change sets of a feature should increment build number version
7.  If a change set is considered ready or new major features needs to be developed that should not be part of `major.minor.x` (for example `1.5.x`)
    1.  increment `master` to next minor version. 
    2.  Create new feature branch As described in p. 1 on top, for example `feature/1.6`.
6.  Previous Openkoda release may still get patches, changes and should keep incrementing `build number` as in p. 4
