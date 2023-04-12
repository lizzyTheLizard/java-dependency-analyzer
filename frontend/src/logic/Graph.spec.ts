import {Graph} from './Graph';

test('Empty', () => {
    const target = Graph.createFrom([]);
    expect(target).toEqual({fullName: '', nodes: []});
});

test('Add individual nodes', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar', attributes: []},
        {fullName: 'bar.bar', attributes: []}
    ]);
    expect(target).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodeDefinition: {fullName: 'foo.bar', attributes: []}, nodes: []},
        {fullName: 'bar.bar', nodeDefinition: {fullName: 'bar.bar', attributes: []}, nodes: []},
    ]});
});

test('Add individual nodes some prefix', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar', attributes: []},
        {fullName: 'foo2.bar', attributes: []}
    ]);
    expect(target).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodeDefinition: {fullName: 'foo.bar', attributes: []}, nodes: []},
        {fullName: 'foo2.bar', nodeDefinition: {fullName: 'foo2.bar', attributes: []}, nodes: []},
    ]});
});

test('Add child nodes', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar', attributes: []},
        {fullName: 'foo.bar.zip', attributes: []}
    ]);
    expect(target).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodeDefinition: {fullName: 'foo.bar', attributes: []}, nodes: [
            {fullName: 'foo.bar.zip', nodeDefinition: {fullName: 'foo.bar.zip', attributes: []}, nodes: []},
        ]},
    ]});
});

test('Add sibling nodes', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar.zip', attributes: []},
        {fullName: 'foo.bar.zap', attributes: []}
    ]);
    expect(target).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: [
            {fullName: 'foo.bar.zip', nodeDefinition: {fullName: 'foo.bar.zip', attributes: []}, nodes: []},
            {fullName: 'foo.bar.zap', nodeDefinition: {fullName: 'foo.bar.zap', attributes: []}, nodes: []},
        ]},
    ]});
});

test('Add sibling nodes inner classes', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar.zip', attributes: []},
        {fullName: 'foo.bar.zip$inner', attributes: []}
    ]);
    expect(target).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: [
            {fullName: 'foo.bar.zip', nodeDefinition: {fullName: 'foo.bar.zip', attributes: []}, nodes: []},
            {fullName: 'foo.bar.zip$inner', nodeDefinition: {fullName: 'foo.bar.zip$inner', attributes: []}, nodes: []},
        ]},
    ]});
});

test('Show All Classses', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar.zip', attributes: []},
        {fullName: 'foo.bar.zap', attributes: []},
        {fullName: 'foo.bar.zap$inner', attributes: []}
    ]);
    const result = target.removeClasses('SHOW_ALL');
    expect(result).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: [
            {fullName: 'foo.bar.zip', nodeDefinition: {fullName: 'foo.bar.zip', attributes: []}, nodes: []},
            {fullName: 'foo.bar.zap', nodeDefinition: {fullName: 'foo.bar.zap', attributes: []}, nodes: []},
            {fullName: 'foo.bar.zap$inner', nodeDefinition: {fullName: 'foo.bar.zap$inner', attributes: []}, nodes: []},
        ]},
    ]});
});

test('Hide Inner Classses', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar.zap', attributes: []},
        {fullName: 'foo.bar.zap$inner', attributes: []}
    ]);
    const result = target.removeClasses('HIDE_INNER');
    //TODO: When code improved, check that there is only one node left
    expect(result).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: [
            {fullName: 'foo.bar.zap', nodeDefinition: {fullName: 'foo.bar.zap', attributes: []}, nodes: []}
        ]},
    ]});
});

test('Hide All Classses', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar.zip', attributes: []},
        {fullName: 'foo.bar.zap', attributes: []},
        {fullName: 'foo.bar.zap$inner', attributes: []}
    ]);
    const result = target.removeClasses('HIDE_ALL');
    expect(result).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: []},
    ]});
});

test('Remove Ignored', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.zip', attributes: []},
        {fullName: 'foo.bar.zip', attributes: []},
        {fullName: 'foo.bar.zap', attributes: []},
        {fullName: 'foo.bar.zap$inner', attributes: []}
    ]);
    const result = target.removeIgnored(['foo.bar']);
    //TODO: When code improved, check that there is only one node left
    expect(result).toEqual({fullName: '', nodes: [
        {fullName: 'foo', nodes: [
            {fullName: 'foo.zip', nodeDefinition: {fullName: 'foo.zip', attributes: []}, nodes: []},
        ]},
    ]});
});

test('Collapse Collapsed', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.bar.zip', attributes: []},
        {fullName: 'foo.bar.zap', attributes: []},
        {fullName: 'foo.bar.zap$inner', attributes: []}
    ]);
    const result = target.collapseCollapsed(['foo.bar']);
    expect(result).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: []}
    ]});
});

test('Take Base', () => {
    const target = Graph.createFrom([
        {fullName: 'foo.zip', attributes: []},
        {fullName: 'foo.bar.zap', attributes: []},
        {fullName: 'foo.bar.zap$inner', attributes: []}
    ]);
    const result = target.takeBase('foo.bar');
    expect(result).toEqual({fullName: '', nodes: [
        {fullName: 'foo.bar', nodes: [
            {fullName: 'foo.bar.zap', nodeDefinition: {fullName: 'foo.bar.zap', attributes: []}, nodes: []},
            {fullName: 'foo.bar.zap$inner', nodeDefinition: {fullName: 'foo.bar.zap$inner', attributes: []}, nodes: []},
        ]},
    ]});
});
