import {writeNomnoml} from './NomnomlWriter';
import {Graph} from '../transform/Graph';
import {GraphImpl, GraphNodeImpl} from '../transform/GraphImpl';


test('Invalid Line', () => {
    const graph: Graph = new GraphImpl([
        new GraphNodeImpl([
            new GraphNodeImpl([], [], 'test1', 'package.test1', 'CLASS'),
            new GraphNodeImpl([], [], 'test2', 'package.test1', 'CLASS'),
        ], [
            {
                from: 'test1', to: 'test2', dependencies: [
                    {from: 'package.test1', to: 'package.test2'},
                ]
            },
        ], 'package', 'package', 'PACKAGE')
    ], []);
    const output = writeNomnoml(graph);
    expect(output).toEqual('[<package> package | [test1]\n[test2]\n[test1] --> [test2]]');
});
