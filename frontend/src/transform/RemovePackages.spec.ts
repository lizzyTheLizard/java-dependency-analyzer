import {simpleTestGraph} from './TestData';
import {removePackages} from './RemovePackages';

test('Construct', () => {
    const input = simpleTestGraph;
    const remove = input.findNode('package1.test1');
    const target = removePackages([remove]);
    const result = input.filter(target);
    expect(result).toBeDefined();
});

//TODO: Implement tests
