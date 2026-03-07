# ACAS Roo Server Build Guide

## Overview

This is the backend Java/Spring Roo server for ACAS. It handles compound registration, experiment data management, and provides REST APIs for the frontend.

## Technology Stack

- **Language**: Java 21
- **Framework**: Spring Roo
- **Build Tool**: Maven 3
- **Container**: Tomcat 10.1
- **Deployment**: Docker

## Building Locally

### Docker Build (Recommended for Testing)

The Dockerfile uses a multistage build that handles Maven compilation internally.

**IMPORTANT**: For local development, use the `indigo` chemistry package to match the docker-compose configuration:

```bash
# From the acas-roo-server directory
docker build --build-arg CHEMISTRY_PACKAGE=indigo -t mcneilco/acas-roo-server-oss:dev -f Dockerfile-multistage .
```

**Build arguments:**
- `CHEMISTRY_PACKAGE`: Chemistry backend to use
  - **`indigo`** - Use for local development (matches docker-compose setup)
  - `bbchem` - Default, but NOT compatible with standard docker-compose setup

**Why indigo?** The docker-compose.yml in the `acas` repo expects `${ACAS_TAG}-indigo` images, so local builds must use the indigo chemistry package.

### Build Process Details

The Dockerfile follows these stages:

1. **Dependency Resolution** (Stage: compile)
   - Uses Maven to download dependencies
   - Cached in `/root/.m2` for faster rebuilds

2. **Compilation** (Stage: compile)
   - Runs `mvn clean`
   - Compiles and packages as WAR: `mvn compile war:war`

3. **Runtime Image** (Stage: final)
   - Based on `tomcat:10.1-jdk21-temurin`
   - Installs Node.js 20.x for config file processing
   - Sets up runner user (UID 1000)

### After Building

After a successful build, you need to:

1. **Update docker-compose** in the `acas` repo to use your local image
   - See [CLAUDE.md in acas repo](https://github.com/mcneilco/acas/blob/master/CLAUDE.md) for details

2. **Start services** via docker-compose
   - The roo service depends on PostgreSQL database

3. **Run tests** via acasclient
   - See [CLAUDE.md in acasclient repo](https://github.com/mcneilco/acasclient/blob/main/CLAUDE.md) for testing workflow

## Development Notes

### Common Build Issues

- **Maven dependency errors**: Check your internet connection and Maven settings
- **Out of memory**: Increase Docker memory limits (build can be memory-intensive)
- **Cache issues**: Use `docker build --no-cache` to force full rebuild

### Code Changes Requiring Rebuild

- Any Java source file changes in `src/`
- POM dependency changes
- Configuration file changes
- Database schema changes (requires DB migration)

## Development Workflow

### Git Branch Naming Convention

When working on Jira tickets, create git branches with names matching the Jira ticket ID:

```bash
# For ticket ACAS-890
git checkout -b ACAS-890

# For ticket SHARED-1234
git checkout -b SHARED-1234
```

This convention makes it easy to:
- Track which branch corresponds to which ticket
- Reference tickets in commit messages
- Link PRs to Jira issues

## Related Documentation

- **Docker Compose Setup**: [CLAUDE.md in acas repo](https://github.com/mcneilco/acas/blob/master/CLAUDE.md)
- **Testing**: [CLAUDE.md in acasclient repo](https://github.com/mcneilco/acasclient/blob/main/CLAUDE.md)
