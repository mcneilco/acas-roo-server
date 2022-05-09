# README

This repo is the java persistence layer for the [ACAS project](http://github.com/mcneilco/acas) project.  It provides many of the base services that the ACAS project needs to run.

### Docker development installation

These instructions assume you have a basic docker installation of ACAS running on your machine. 

These instruction provide a path to doing development to give you a starting point using a Mac machine, there are many ways to setup this project using your own tool choices.

#### Standard development tools

These tools are required for the below steps to work.

- [Docker](https://www.docker.com/)
- [VSCode](https://code.visualstudio.com/)
- [Brew for java installation](https://brew.sh/)
- [Java installation management with jenv](https://www.jenv.be/)

#### Java

Before running VSCode with this project it's best to have a java installation as the VS Code plugins require java to be installed.

1. Install java 

Install openjdk-8
```
brew tap adoptopenjdk/openjdk
brew install --cask adoptopenjdk8
```

2. Install jenv to manage java installations

Install jenv
```
brew install jenv
```

Install zsh or bash environment
```
# Shell: bash
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.bash_profile
echo 'eval "$(jenv init -)"' >> ~/.bash_profile
# Shell: zsh
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
echo 'eval "$(jenv init -)"' >> ~/.zshrc
```

Add openjdk to jenv
```
jenv add /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home/
```

Set java version

```
jenv global 1.8
```

#### VSCode Plugins

Extension Pack for Java

[Extension Pack for Java](vscode:extension/vscjava.vscode-java-pack)

https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack


#### Docker build

1. Checkout this repository and open VSCode to get started.
2. From a terminal run the following command to build the "indigo" (open source chemistry package) docker image:

> **Note**
>
> We use a specific release/ branch as the default branch for the the ACAS project (e.g. release/1.13.7). 
> So this assumes that you have a release/ branch checked out and that your running ACAS
> Docker stack is using the same release/ builds.

```
docker build -t mcneilco/acas-roo-server-oss:release-1.13.7-indigo -f Dockerfile-multistage --build-arg CHEMISTRY_PACKAGE=indigo .
````

> **Note**
> 
> On first build this can take a while because the docker image is built from scratch pulling down all the dependencies. Subsequent builds will be much faster.


#### Docker run

Update the running ACAS instance to use the latest docker image you just built.

1. Change your working directory to the ACAS project root.

```
cd ../acas
```

2. From a terminal run the following command to update the ACAS docker image:

```
docker-compose up -d roo
```

#### Debugging

1. In VSCode click the Play button
2. Click `create a launch.json file and then click Java
3. Replace the automatically generated configurations with the following:

```

        {
            "type": "java",
            "name": "Debug (Attach) - Remote - localhost",
            "request": "attach",
            "hostName": "localhost",
            "port": "8000",
            "projectName": "acas"
        }

```
4. Click save and you should now be able click `Debug (Attach) - Remote - localhost` and it should connect to the running docker container on port 8000

You should now be able to use java breakpoint debugging in VSCode with the ACAS project.