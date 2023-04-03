import {simpleTestGraph} from './TestData';
import {collapsePackages} from './CollapsePackages';

test('Construct', () => {
    const input = simpleTestGraph;
    const collapse = input.findNode('package1.test1');
    const target = collapsePackages([collapse]);
    const result = input.filter(target);
    expect(result).toBeDefined();

});

//TODO: Implement tests
