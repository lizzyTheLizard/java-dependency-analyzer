export const data = `
{
  "nodes": [
    {"fullName":"site.gutschi.dependency.maven.integrationtest.TestA", "attributes": [{"name":"TestA", "value":"Test","type":"TEXT"}]},
    {"fullName":"site.gutschi.dependency.maven.integrationtest.TestB", "attributes": [{"name":"TestB", "value":"http://www.google.com","type":"LINK"}]},
    {"fullName":"site.gutschi.dependency.maven.integrationtest2.TestC", "attributes": [{"name":"TestA", "value":"Test","type":"TEXT"}]},
    {"fullName":"java.lang.Object", "attributes": []},
    {"fullName":"kotlin.Metadata", "attributes": []},
    {"fullName":"java.lang.String", "attributes": []},
    {"fullName":"kotlin.jvm.internal.Intrinsics", "attributes": []}
  ],
  "dependencies": [
    { "from": "site.gutschi.dependency.maven.integrationtest.TestA", "to": "java.lang.Object"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestA", "to": "kotlin.Metadata"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "java.lang.Object"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "java.lang.String"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "kotlin.Metadata"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "kotlin.jvm.internal.Intrinsics"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "site.gutschi.dependency.maven.integrationtest.TestA"},
    { "from": "site.gutschi.dependency.maven.integrationtest.TestB", "to": "site.gutschi.dependency.maven.integrationtest2.TestC"},
    { "from": "site.gutschi.dependency.maven.integrationtest2.TestC", "to": "java.lang.Object"},
    { "from": "site.gutschi.dependency.maven.integrationtest2.TestC", "to": "kotlin.Metadata"}
  ]
}  
`;
