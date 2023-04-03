import {simpleTestGraph} from '../transform/TestData';
import {writeSvg} from './writeSvg';

test('Write Line', () => {
    const output = writeSvg(simpleTestGraph);
    expect(output).toContain('<svg version="1.1"');
    expect(output).toContain('<text x="19.5" y="13.5" stroke="none" text-anchor="middle" data-name="test2">test2</text>');
});
