# Dependency-Analyzer

A class level dependency analyzer for JVM programs. Can be integrated as a maven plugin and generates an interactive HTML documentation of the class dependencies.

## Usage

Integration as maven plugin

```xml
<build>
    <plugins>
        <plugin>
            <groupId>site.gutschi.dependency</groupId>
            <artifactId>dependency-analyzer</artifactId>
            <version>0.1.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>create-documentation</goal>
                    </goals>
                    <configuration>
                        <!-- see below for config options-->
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

This will generate a documentation under `target/doc`

## Configuration

The following configuration options can be used

| Parameter             | Description                                                                                                                                                                                                             | Example                           | Default               |
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------|-----------------------|
| name                  | The name of the project, shown in the documentation                                                                                                                                                                     | projectName                       | ${project.name}       |
| version               | The version of the project, shown in the documentation                                                                                                                                                                  | 0.0.1-SNAPSHOT                    | ${project.version}    |
| inputs                | Input files to be considered. A list of folders or files, all ".class" and ".jar" files in this and all its sub-folders will be analyzed                                                                                | target/classes,target/project.jar | target/classes        |
| fatJarMatchers        | If a SpringBoot-FatJar is within the input file, libraries in the FatJar are usually not analyzed. You can define a list of regex a fatJarMatchers, all libraries matching on of those regexes will be analyzed as well | guava-.*                          | -                     |
| includeFatJarClasses  | If a SpringBoot-FatJar is within the input file, the SpringBoot-Code to load the Fat-Jar is not analyzed. If you set includeFatJarClasses to true, this code is analyzed as well                                        | true                              | false                 |
| outputFolder          | The output-folder to write the documentation to                                                                                                                                                                         | target/dependency                 | ${basedir}/target/doc |
| basePackage           | A preselected base-package within the generate documentation. If given, the documentation will zoom directly on this package                                                                                            | com.you.application               | -                     |
| collapsePackages      | A list of packages who will be collapsed in the generated documentation. The plugin will try to automatically collapse external libraries, but you can overwrite this setting here.                                     | org.springframework,com.google    | -                     |
| ignoredPackages       | A list of packages who will be ignored in the generated documentation. The plugin will try to automatically ignore JDK-classes, but you can overwrite this setting here.                                                | java,kotlin,sun                   | -                     |
| splitPackages         | A list of packages who will be split in the generated documentation. The plugin will try to automatically split certain widely used prefixes (e.g "org"), but you can overwrite this setting here.                      | org,org.apache,com                | -                     |


## Build
In order to build this plugin from scratch, you have to install
* [Node.js](https://nodejs.org/en), Version >= 18.0.0
* [Yarn](https://yarnpkg.com/), Version >= 3.5.0
* A JDK Version >= 11

You must then build the frontend first using
```bash
cd frontend
yarn install
yarn build
```

and then build the backend using the maven wrapper
```bash
cd dependency-analyzer
./mvnw install
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)






# Foobar

Foobar is a Python library for dealing with word pluralization.

## Installation

Use the package manager [pip](https://pip.pypa.io/en/stable/) to install foobar.

```bash
pip install foobar
```

## Usage

```python
import foobar

# returns 'words'
foobar.pluralize('word')

# returns 'geese'
foobar.pluralize('goose')

# returns 'phenomenon'
foobar.singularize('phenomena')
```




