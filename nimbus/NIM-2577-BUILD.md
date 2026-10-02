# NIM-2577: JChem build of acas-roo-server 2026.3.0-dev8

These are instructions for building, smoke-testing and reporting on this branch. If you are a Claude Code session that was asked to "follow nimbus/NIM-2577-BUILD.md", carry out the steps below in order. This file is branch-local working notes: delete it before opening any pull request from this branch.

## What this branch is

- **Base:** tag `2026.3.0-dev8` (commit `5f589f20`, also the tip of `release/2026.3.x` when branched).
- **Purpose:** a roo-only upgrade with no JChem change. JChem stays at **23.9.0**, the version currently deployed.
- **The only code change:** `pom.xml` sets `jchem.version` 23.4.0 → 23.9.0. dev8 already pins commons-lang3 3.18.0, so the older commons-lang3 3.12.0 jchem patch is not needed.
- **Image tag to produce:** `mcneilco/acas-roo-server:2026.3.0-dev8-jchem`.

## Rules

- **Build on linux/amd64.** Stop if `uname -m` is not `x86_64`.
- **Leave other containers alone.** Don't stop, restart or remove any running container on the build host. Don't bind ports 8080, 3000 or 1080. Don't edit files outside this checkout and a scratch directory.
- **Don't push** the image to any registry, and don't push git changes. Stop and ask the operator first.
- **Never print, log or commit the ChemAxon credentials.** When you report on credentials, say only whether they're present and the file path.
- **Ask before** deleting anything, running `docker system prune`, or anything else you can't undo.
- **If the build fails, report it.** Don't change source code to fix it. Report and wait.

## Steps

1. **Pre-flight.** Report:
   - `uname -m` and `docker version`.
   - BuildKit availability. The Dockerfile uses `RUN --mount=type=cache`, so the build needs `DOCKER_BUILDKIT=1`.
   - Free disk space for the Docker root and the checkout.
   - `docker ps`, as a before-snapshot.
   - `git log --oneline -3`. The branch should be one or two commits on top of `5f589f20`.
2. **ChemAxon Maven credentials.** The JChem artifacts come from `hub.chemaxon.com`, which needs a login. The Dockerfile copies the repo's `settings.xml` into the builder stage, and the committed one is a placeholder.
   - **Finding a real one:** the operator supplies a `settings.xml` containing a `<server>` with id `Chemaxon Public Repository`. If none was supplied, look for one left over from earlier JChem builds on this host, such as `~/.m2/settings.xml` or other `acas-roo-server` checkouts.
   - **Checking it:** the server id must match, and the username must not be the upstream placeholder. Compare only the first three characters.
   - **Installing it:** copy it over `./settings.xml`, then run `git update-index --skip-worktree settings.xml` so it can't be committed.
   - If you can't find one, stop and ask.
3. **Build.** Run from the repo root and keep the log:
   ```
   DOCKER_BUILDKIT=1 docker build -t mcneilco/acas-roo-server:2026.3.0-dev8-jchem \
     -f Dockerfile-multistage --build-arg CHEMISTRY_PACKAGE=jchem . 2>&1 | tee ../NIM-2577-build.log
   ```
   - Upstream publishes no JChem image at dev8, so the `jchem` profile may not have been compiled against Java 21 / Spring 6 / Hibernate 6.
   - **If it fails,** report the first real error with 40 lines of context. It is most likely one of two things: a dependency-resolution failure, which points at credentials, or a compile error in `chemclasses/jchem`.
4. **Smoke tests.** These run without starting Tomcat and without a database:
   ```
   IMG=mcneilco/acas-roo-server:2026.3.0-dev8-jchem
   docker image inspect $IMG --format '{{.Id}} {{.Architecture}} {{.Size}}'
   docker run --rm --entrypoint sh $IMG -c 'ls /usr/local/tomcat/webapps/acas/WEB-INF/lib | grep -i jchem'
   docker run --rm --entrypoint sh $IMG -c 'java -version 2>&1; echo JAVA_HOME=$JAVA_HOME; ls -l $JAVA_HOME/lib/security/cacerts; ls -l /usr/lib/jvm/java/jre/lib/security/cacerts 2>&1; which openssl keytool; head -2 /etc/os-release'
   docker run --rm --entrypoint sh $IMG -c 'ls /usr/local/tomcat/webapps/acas/WEB-INF/lib | grep -i -E "commons-lang|guava|spring-security-core|hibernate-core"'
   docker run --rm --entrypoint sh $IMG -c 'find / -name settings.xml -not -path "/proc/*" 2>/dev/null'
   ```
   - **Expected JChem jar set** (the currently deployed 23.9.0 build). Report an exact diff against this list:
     ```
     jchem-cachereg-common-22.14.1.jar
     jchem-connection-23.9.0.jar
     jchem-constants-23.9.0.jar
     jchem-main-23.9.0.jar
     jchem-molecule-persister-23.9.0.jar
     jchem-persist-common-22.14.1.jar
     jchem-sql-utils-23.9.0.jar
     jchem-utils-23.9.0.jar
     ```
   - **cacerts and openssl:** the downstream compose file imports an LDAP certificate with `keytool` into `/usr/lib/jvm/java/jre/lib/security/cacerts`, the old image's path. Report whether that path is missing and `$JAVA_HOME/lib/security/cacerts` is present, and whether `openssl` and `keytool` exist.
   - **settings.xml must not appear** anywhere in the final image.
   - **Don't start the container** with Tomcat or point it at a database. Full startup testing happens in the integration environment.
5. **After-check.** Run `docker ps` again and confirm nothing else changed.

## Report

Write `../NIM-2577-report.md`, next to the checkout and not inside it, and print it. Include:
- Arch, Docker version, and disk space before and after.
- `git log --oneline -3`.
- The `settings.xml` source path.
- Build result, duration, and image ID and size. On failure, the first error with 40 lines of context.
- Smoke-test output: the JChem jar diff against the expected set, Java version, cacerts paths, and `openssl`/`keytool` presence.
- Anything surprising.

End with: "Image built locally and not pushed. Waiting for the go-ahead to push `mcneilco/acas-roo-server:2026.3.0-dev8-jchem`."
