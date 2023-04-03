import {simpleTestGraph} from './TestData';
import {removeClasses} from './RemoveClasses';

test('Construct', () => {
    const input = simpleTestGraph;
    const target = removeClasses('HIDE_ALL');
    const result = input.filter([target]);
    expect(result).toBeDefined();

});

//TODO: Implement tests
