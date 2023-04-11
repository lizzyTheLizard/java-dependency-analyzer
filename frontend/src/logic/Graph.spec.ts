import {Graph} from './Graph';

test('Empty', () => {
    const target = new Graph('', [], undefined);
    expect(target).toEqual({
        _nodes: [],
        fullName: '',
    });
});

test('Add individual nodes', () => {
    const target = new Graph('', [], undefined);
    target.addNode({fullName: 'foo.bar', attributes: []});
    target.addNode({fullName: 'bar.bar', attributes: []});
    expect(target).toEqual(new Graph('', [
        new Graph('foo.bar', [], {fullName: 'foo.bar', attributes: []}),
        new Graph('bar.bar', [], {fullName: 'bar.bar', attributes: []}),
    ], undefined));
});

test('Add child nodes', () => {
    const target = new Graph('', [], undefined);
    target.addNode({fullName: 'foo.bar', attributes: []});
    target.addNode({fullName: 'foo.bar.zip', attributes: []});
    expect(target).toEqual(new Graph('', [
        new Graph('foo.bar', [
            new Graph('foo.bar.zip', [], {fullName: 'foo.bar.zip', attributes: []}),
        ], {fullName: 'foo.bar', attributes: []}),
    ], undefined));
});

test('Add sibling nodes', () => {
    const target = new Graph('', [], undefined);
    target.addNode({fullName: 'foo.bar.zip', attributes: []});
    target.addNode({fullName: 'foo.bar.zap', attributes: []});
    expect(target).toEqual(new Graph('', [
        new Graph('foo.bar', [
            new Graph('foo.bar.zip', [], {fullName: 'foo.bar.zip', attributes: []}),
            new Graph('foo.bar.zap', [], {fullName: 'foo.bar.zap', attributes: []}),
        ],undefined),
    ], undefined));
});

//TODO: Tests
