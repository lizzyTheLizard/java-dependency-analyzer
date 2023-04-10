export const data = `{
    "nodes": [{
        "name": "Intrinsics",
        "fullName": "kotlin.jvm.internal.Intrinsics",
        "attributes": []
    }, {"name": "NotNull", "fullName": "org.jetbrains.annotations.NotNull", "attributes": []}, {
        "name": "Metadata",
        "fullName": "kotlin.Metadata",
        "attributes": []
    }, {
        "name": "TestC",
        "fullName": "site.gutschi.dependency.maven.integrationtest2.TestC",
        "attributes": [{"name": "Access", "value": "Public", "type": "TEXT"}, {
            "name": "Final",
            "value": "True",
            "type": "BOOLEAN"
        }, {"name": "Type", "value": "Class", "type": "TEXT"}, {
            "name": "Implemented Interfaces",
            "value": "",
            "type": "TEXT"
        }, {"name": "Base Class", "value": "java.lang.Object", "type": "TEXT"}]
    }, {
        "name": "TestA",
        "fullName": "site.gutschi.dependency.maven.integrationtest.TestA",
        "attributes": [{"name": "Access", "value": "Public", "type": "TEXT"}, {
            "name": "Final",
            "value": "True",
            "type": "BOOLEAN"
        }, {"name": "Type", "value": "Class", "type": "TEXT"}, {
            "name": "Implemented Interfaces",
            "value": "",
            "type": "TEXT"
        }, {"name": "Base Class", "value": "java.lang.Object", "type": "TEXT"}]
    }, {
        "name": "TestB",
        "fullName": "site.gutschi.dependency.maven.integrationtest.TestB",
        "attributes": [{"name": "Access", "value": "Public", "type": "TEXT"}, {
            "name": "Final",
            "value": "True",
            "type": "BOOLEAN"
        }, {"name": "Type", "value": "Class", "type": "TEXT"}, {
            "name": "Implemented Interfaces",
            "value": "",
            "type": "TEXT"
        }, {"name": "Base Class", "value": "java.lang.Object", "type": "TEXT"}]
    }, {"name": "Object", "fullName": "java.lang.Object", "attributes": []}],
    "dependencies": [{
        "from": "site.gutschi.dependency.maven.integrationtest.TestB",
        "to": "java.lang.Object"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest.TestB",
        "to": "site.gutschi.dependency.maven.integrationtest.TestA"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest.TestB",
        "to": "site.gutschi.dependency.maven.integrationtest2.TestC"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest.TestA",
        "to": "kotlin.Metadata"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest.TestB",
        "to": "kotlin.Metadata"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest.TestB",
        "to": "org.jetbrains.annotations.NotNull"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest2.TestC",
        "to": "java.lang.Object"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest2.TestC",
        "to": "kotlin.Metadata"
    }, {
        "from": "site.gutschi.dependency.maven.integrationtest.TestA",
        "to": "java.lang.Object"
    }, {"from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "kotlin.jvm.internal.Intrinsics"}],
    "properties": {
        "name": "Maven Integration Test",
        "version": "0.0.1-SNAPSHOT",
        "collapsePackages": [],
        "ignoredPackages": ["java", "kotlin"],
        "splitPackages": ["org"]
    }
}`;
