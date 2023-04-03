import {simpleTestGraph} from './TestData';
import {basePackage} from './BasePackage';

test('Construct', () => {
    const input = simpleTestGraph;
    const base = input.findNode('package1.test1');
    const target = basePackage(base);
    const result = input.filter([target]);
    expect(result).toBeDefined();

});

//TODO: Implement tests
