import {GraphBuilder} from './GraphBuilder';
import {Input, Properties} from './InputFile';

const properties: Properties = {
    collapsePackages: [], ignoredPackages: [], name: '', splitPackages: [], version: ''
};

test('Empty', () => {
    const input = {nodes: [], dependencies: [], properties: properties};
    const result = new GraphBuilder(input);
    expect(result.nodes).toEqual([]);
    expect(result.dependencies).toEqual([]);
    expect(result.attributes).toEqual([]);
});

test('Single Node', () => {
    const input = {nodes: [{fullName: 'a', attributes: []}], dependencies: [], properties: properties};
    const result = new GraphBuilder(input);
    expect(result.nodes).toEqual([{
        name: 'a',
        fullName: 'a',
        type: 'CLASS',
        dependencies: [],
        attributes: [],
        nodes: []
    }]);
    expect(result.dependencies).toEqual([]);
    expect(result.attributes).toEqual([]);
});

test('Attribute Node', () => {
    const input: Input = {nodes: [{fullName: 'a', attributes: [{type: 'LINK', name:'name', value:'value'}]}], dependencies: [], properties: properties};
    const result = new GraphBuilder(input);
    expect(result.nodes).toEqual([{
        name: 'a',
        fullName: 'a',
        type: 'CLASS',
        dependencies: [],
        attributes: [{
            type: 'LINK',
            name: 'name',
            value: 'value'
        }],
        nodes: []
    }]);
    expect(result.dependencies).toEqual([]);
    expect(result.attributes).toEqual([]);
});

test('Dependency', () => {
    const input = {nodes: [{fullName: 'a', attributes: []},{fullName: 'b', attributes: []} ], dependencies: [{from: 'a', to: 'b'}], properties: properties};
    const result = new GraphBuilder(input);
    expect(result.nodes).toEqual([{
        name: 'a',
        fullName: 'a',
        type: 'CLASS',
        dependencies: [],
        attributes: [],
        nodes: []
    },{
        name: 'b',
        fullName: 'b',
        type: 'CLASS',
        dependencies: [],
        attributes: [],
        nodes: []
    }]);
    expect(result.dependencies).toEqual([{from: 'a', to: 'b', dependencies: [{from: 'a', to: 'b'}]}]);
    expect(result.attributes).toEqual([]);
});

test('FullName', () => {
    const input = {nodes: [{fullName: 'a.b.c', attributes: []},{fullName: 'a.c.c', attributes: []} ], dependencies: [{from: 'a.b.c', to: 'a.c.c'}], properties: properties};
    const result = new GraphBuilder(input);
    expect(result.nodes).toEqual([{
        name: 'a',
        fullName: 'a',
        type: 'PACKAGE',
        dependencies: [{from: 'b', to: 'c', dependencies: [{from: 'a.b.c', to: 'a.c.c'}]}],
        attributes: [],
        nodes: [{
            name: 'b',
            fullName: 'a.b',
            type: 'PACKAGE',
            dependencies: [],
            attributes: [],
            nodes: [{
                name: 'c',
                fullName: 'a.b.c',
                type: 'CLASS',
                dependencies: [],
                attributes: [],
                nodes: []
            }]
        },{
            name: 'c',
            fullName: 'a.c',
            type: 'PACKAGE',
            dependencies: [],
            attributes: [],
            nodes: [{
                name: 'c',
                fullName: 'a.c.c',
                type: 'CLASS',
                dependencies: [],
                attributes: [],
                nodes: []
            }]
        }]
    }]);
    expect(result.dependencies).toEqual([]);
    expect(result.attributes).toEqual([]);
});
