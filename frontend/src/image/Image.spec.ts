import {fromDefault} from './Image';

test('FromData', () => {
    const target = fromDefault();
    expect(target.numberOfNodes).toEqual(7);
    expect(target.numberOfDependencies).toEqual(10);
    expect(target.properties).toEqual({
        collapsePackages: [],
        ignoredPackages: ['java', 'kotlin'],
        name: 'Maven Integration Test',
        splitPackages: ['org'],
        version: '0.0.1-SNAPSHOT'
    });
});

test('getSvgImage', () => {
    const target = fromDefault();
    const result = target.getSvgImage();
    expect(result).toBeDefined();
});

test('toString', () => {
    const target = fromDefault();
    const result = target.toString();
    expect(result).toBeDefined();
});

test('update', () => {
    const target = fromDefault();
    const result = target.update({basePackage: 'dummy'});
    expect(result.properties).toEqual({
        collapsePackages: [],
        basePackage: 'dummy',
        ignoredPackages: ['java', 'kotlin'],
        name: 'Maven Integration Test',
        splitPackages: ['org'],
        version: '0.0.1-SNAPSHOT'
    });
});

test('ignore', () => {
    const target = fromDefault();
    const result = target.toggleIgnored('dummy');
    expect(result.properties).toEqual({
        collapsePackages: [],
        ignoredPackages: ['java', 'kotlin', 'dummy'],
        name: 'Maven Integration Test',
        splitPackages: ['org'],
        version: '0.0.1-SNAPSHOT'
    });
});

test('unignore', () => {
    const target = fromDefault();
    const result = target.toggleIgnored('dummy').toggleIgnored('dummy');
    expect(result.properties).toEqual({
        collapsePackages: [],
        ignoredPackages: ['java', 'kotlin'],
        name: 'Maven Integration Test',
        splitPackages: ['org'],
        version: '0.0.1-SNAPSHOT'
    });
});

test('collapse', () => {
    const target = fromDefault();
    const result = target.toggleCollapsed('dummy');
    expect(result.properties).toEqual({
        collapsePackages: ['dummy'],
        ignoredPackages: ['java', 'kotlin'],
        name: 'Maven Integration Test',
        splitPackages: ['org'],
        version: '0.0.1-SNAPSHOT'
    });
});

test('uncollapse', () => {
    const target = fromDefault();
    const result = target.toggleCollapsed('dummy').toggleCollapsed('dummy');
    expect(result.properties).toEqual({
        collapsePackages: [],
        ignoredPackages: ['java', 'kotlin'],
        name: 'Maven Integration Test',
        splitPackages: ['org'],
        version: '0.0.1-SNAPSHOT'
    });
});

test('findClass', () => {
    const target = fromDefault();
    const result = target.findNode('site.gutschi.dependency.maven.integrationtest.TestA');
    expect(result).toEqual({
        type: 'CLASS',
        fullName: 'site.gutschi.dependency.maven.integrationtest.TestA',
        attributes: [
            {name: 'Access', type: 'TEXT', value: 'Public'},
            {name: 'Final', type: 'BOOLEAN', value: 'True'},
            {name: 'Type', type: 'TEXT', value: 'Class'},
            {name: 'Implemented Interfaces', type: 'TEXT', value: ''},
            {name: 'Base Class', type: 'TEXT', value: 'java.lang.Object'},
        ]
    });
});

test('findPackage', () => {
    const target = fromDefault();
    const result = target.findNode('site.gutschi');
    expect(result).toEqual({type: 'PACKAGE', fullName: 'site.gutschi', attributes: []});
});
